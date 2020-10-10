package com.moyada.reservoir.task;

import com.moyada.reservoir.client.HttpClient;
import com.moyada.reservoir.domain.ReservoirInfoDO;
import com.moyada.reservoir.entity.FujianTrend;
import com.moyada.reservoir.enums.DataType;
import com.moyada.reservoir.manager.TrendManager;
import com.moyada.reservoir.repository.ReservoirInfoRepository;
import com.moyada.reservoir.repository.ReservoirInfoSpecification;
import com.moyada.reservoir.repository.ReservoirTrendRepository;
import com.moyada.reservoir.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;


/**
 * @author xueyikang
 * @since 1.0
 **/
@Slf4j
@Component
public class FujianTask implements Task {

    private static final String CUR_PROVINCE = "福建省";
    private static final String CUR_PROVINCE_CODE = "350000";

    @Autowired
    @Qualifier("taskExecutor")
    private Executor executor;

    @Autowired
    private ReservoirInfoRepository reservoirInfoRepository;

    @Autowired
    private ReservoirTrendRepository reservoirTrendRepository;

    @Autowired
    private TrendManager trendManager;

    @PostConstruct
//    @Scheduled(cron = "0 0/5 * * * ?")
    @Scheduled(cron = "0 0/30 * * * ?")
    @Override
    public void run() {
        log.info("run fujian task");
        recordTrend();
        log.info("end fujian task");
    }

    private void recordTrend() {
        Instant instant = Instant.now();
        Timestamp now = Timestamp.from(instant);

        String end = DateUtil.format(now, "yyyy-MM-dd'T'HH:mm:00");

        if (now.getHours() < 12) {
            instant = instant.minus(1L, ChronoUnit.DAYS);
            now = Timestamp.from(instant);
        }
        String start = DateUtil.format(now, "yyyy-MM-dd'T'08:00:00");

        FujianTrend data = HttpClient.get("http://58.22.3.131:9003/water?no_data_visible=false&time=[" + start + "," + end + "]", FujianTrend.class);
        if (data == null) {
            log.error("fetch water info error");
            return;
        }

        ReservoirInfoDO infoDO = new ReservoirInfoDO();
        infoDO.setProvinceCode(CUR_PROVINCE_CODE);
        ReservoirInfoSpecification specification = new ReservoirInfoSpecification(infoDO);
        List<ReservoirInfoDO> all = reservoirInfoRepository.findAll(specification);
        Map<String, ReservoirInfoDO> reservoirMap = all.stream()
                .collect(Collectors.toMap(s -> s.getCityCode() + "_" + s.getOriginName(), s -> s));

        data.getData()
                .stream()
                .filter(s -> s.getVal() != null)
                .filter(s -> s.getVal() > 10)
//                .forEach(o -> executor.execute(() -> record(reservoirMap, o)));
                .forEach(o -> record(reservoirMap, o));
    }

    private void record(Map<String, ReservoirInfoDO> reservoirMap, FujianTrend.Data data) {
        ReservoirInfoDO infoDO = getExist(reservoirMap, data);
        if (infoDO == null) {
            save(data);
        } else {
            update(infoDO, data);
        }
    }

    private ReservoirInfoDO getExist(Map<String, ReservoirInfoDO> reservoirMap, FujianTrend.Data data) {
        String key = data.getCity_code() + "_" + data.getName();
        ReservoirInfoDO infoDO = reservoirMap.get(key);
        return infoDO;
    }

    private void save(FujianTrend.Data data) {
        ReservoirInfoDO infoDO = new ReservoirInfoDO();
        infoDO.setName(data.getName());
        infoDO.setOriginName(data.getName());

        infoDO.setProvince(CUR_PROVINCE);
        infoDO.setProvinceCode(CUR_PROVINCE_CODE);
        infoDO.setCity(data.getCity_name());
        infoDO.setCityCode(data.getCity_code());
        infoDO.setDistrict(data.getArea_name());
        infoDO.setDistrictCode(data.getArea_id());
        infoDO.setTown(data.getTown_name());
        infoDO.setTownCode(data.getTown_code());

        infoDO.setAddress(data.getAddress());
        infoDO.setRiverName(data.getRiver_name());
        infoDO.setWaterSource(data.getWater_system());

        infoDO.setWaterLine(data.getVal().doubleValue());
        infoDO.setHighWater(data.getEnsure_val() == null ? null : data.getEnsure_val().doubleValue());

        infoDO.setLatitude(data.getLat());
        infoDO.setLongitude(data.getLng());

        infoDO.setDeleted(DataType.OK.getValue());
        infoDO.setCreateTime(DateUtil.now());
        infoDO.setUpdateTime(DateUtil.now());
        reservoirInfoRepository.save(infoDO);

        trendManager.record(infoDO.getId(), data.getVal().doubleValue());
    }

    private void update(ReservoirInfoDO infoDO, FujianTrend.Data data) {
        infoDO.setWaterLine(data.getVal().doubleValue());

        double val = trendManager.getFirstRecord(infoDO.getId());
        if (val != 0D) {
            infoDO.setWaterTrend(infoDO.getWaterLine() - val);
        } else {
            infoDO.setWaterTrend(0D);
        }

        infoDO.setUpdateTime(DateUtil.now());

        try {
            reservoirInfoRepository.save(infoDO);
        } catch (DuplicateKeyException e) {
            log.error("save error", e);
            return;
        }
        trendManager.record(infoDO.getId(), data.getVal().doubleValue());
    }
}
