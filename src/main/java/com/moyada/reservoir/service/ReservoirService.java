package com.moyada.reservoir.service;

import com.moyada.reservoir.domain.ReservoirInfoDO;
import com.moyada.reservoir.enums.DataType;
import com.moyada.reservoir.enums.LevelType;
import com.moyada.reservoir.model.QueryReq;
import com.moyada.reservoir.model.ReservoirVO;
import com.moyada.reservoir.repository.ReservoirInfoRepository;
import com.moyada.reservoir.repository.ReservoirInfoSpecification;
import com.moyada.reservoir.utils.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Service
public class ReservoirService {

    @Autowired
    private ReservoirInfoRepository reservoirInfoRepository;

    public List<ReservoirVO> get(QueryReq req) {
        ReservoirInfoDO infoDO = new ReservoirInfoDO();
        infoDO.setProvinceCode(req.getProvince());
        infoDO.setCityCode(req.getCity());
        infoDO.setDeleted(DataType.OK.getValue());
        ReservoirInfoSpecification condition = new ReservoirInfoSpecification(infoDO);

        List<ReservoirInfoDO> reservoirList = reservoirInfoRepository.findAll(condition, Sort.by("id"));
        if (CollectionUtils.isEmpty(reservoirList)) {
            return Collections.emptyList();
        }
        return reservoirList.stream().map(this::build).collect(Collectors.toList());
    }

    private ReservoirVO build(ReservoirInfoDO infoDO) {
        ReservoirVO vo = new ReservoirVO();
        BeanUtils.copyProperties(infoDO, vo);
        vo.setType(LevelType.get(infoDO.getLevel()));
        vo.setUpdateTime(DateUtil.format(infoDO.getUpdateTime(), "yyyy年MM月dd日 HH:mm:ss"));
        return vo;
    }
}
