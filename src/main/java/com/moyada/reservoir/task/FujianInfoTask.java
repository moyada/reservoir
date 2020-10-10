package com.moyada.reservoir.task;

import com.moyada.reservoir.client.HttpClient;
import com.moyada.reservoir.domain.ReservoirInfoDO;
import com.moyada.reservoir.entity.FujianInfo;
import com.moyada.reservoir.enums.LevelType;
import com.moyada.reservoir.repository.ReservoirInfoRepository;
import com.moyada.reservoir.repository.ReservoirInfoSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * @author xueyikang
 * @since 1.0
 **/
@Slf4j
@Component
public class FujianInfoTask implements Task {

    private static final String CUR_PROVINCE_CODE = "350000";

    @Autowired
    private ReservoirInfoRepository reservoirInfoRepository;

//    @PostConstruct
//    @Scheduled(cron = "0 40 11 * * ? ")
    @Override
    public void run() {
        log.info("run fujian info task");
        syncData();
    }

    private void syncData() {
        FujianInfo data = HttpClient.get("http://58.22.3.131:9003/basic/data?key=select_reservoir_dispatch_info", FujianInfo.class);
        if (data == null) {
            return;
        }

        ReservoirInfoDO infoDO = new ReservoirInfoDO();
        infoDO.setProvinceCode(CUR_PROVINCE_CODE);
        ReservoirInfoSpecification specification = new ReservoirInfoSpecification(infoDO);
        List<ReservoirInfoDO> all = reservoirInfoRepository.findAll(specification);

        for (FujianInfo.Data item : data.getData()) {
            ReservoirInfoDO selectMatch = selectMatch(item, all);
            if (selectMatch == null) {
                continue;
            }
            update(item, selectMatch);
        }
    }

    private ReservoirInfoDO selectMatch(FujianInfo.Data data, List<ReservoirInfoDO> all) {
        String city = data.getCity() + "市";
        String name = data.getName();

        List<ReservoirInfoDO> select = new ArrayList<>();
        for (ReservoirInfoDO infoDO : all) {
            if (!infoDO.getCity().equals(city)) {
                continue;
            }
            if (infoDO.getOriginName().contains(name)) {
                select.add(infoDO);
            }
        }

        if (select.isEmpty()) {
            return null;
        }
        if (select.size() == 1) {
            return select.get(0);
        }

        name = name + "水库";
        for (ReservoirInfoDO infoDO : select) {
            if (infoDO.getName().contains(name)) {
                return infoDO;
            }
        }
        return select.stream()
                .min(Comparator.comparing(ReservoirInfoDO::getWaterLine))
                .orElse(null);
    }

    private void update(FujianInfo.Data data, ReservoirInfoDO infoDO) {
        infoDO.setName(data.getName() + "水库");
        infoDO.setHighWater(data.getEnsure_val().doubleValue());
        infoDO.setArea(data.getCatchment_area().doubleValue());

        LevelType level = getLevel(data.getEncl());
        if (level != null) {
            infoDO.setLevel(level.getCode());
        }

        try {
            reservoirInfoRepository.save(infoDO);
        } catch (Exception e) {
            log.error("update info error", e);
        }
    }

    private LevelType getLevel(String val) {
        if (val == null) {
            return null;
        }

        if (val.contains("Ⅰ")) {
            return LevelType.BIG;
        }
        if (val.contains("Ⅱ")) {
            return LevelType.MIDDLE;
        }
        if (val.contains("Ⅲ")) {
            return LevelType.SMALL;
        }
        return null;
    }
}
