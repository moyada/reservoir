package com.moyada.reservoir;

import com.moyada.reservoir.domain.ReservoirInfoDO;
import com.moyada.reservoir.enums.DataType;
import com.moyada.reservoir.repository.ReservoirInfoRepository;
import com.moyada.reservoir.task.FujianTask;
import com.moyada.reservoir.utils.DateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.Instant;

@SpringBootTest
class ReservoirApplicationTests {

    @Autowired
    private ReservoirInfoRepository reservoirInfoRepository;

    @Autowired
    private FujianTask fujianTask;

    @Test
    public void testCrudRepositorySave() {
        ReservoirInfoDO infoDO = new ReservoirInfoDO();
        infoDO.setName("test");
        infoDO.setDeleted(DataType.OK.getValue());
        infoDO.setCreateTime(DateUtil.now());
        infoDO.setUpdateTime(DateUtil.now());
        this.reservoirInfoRepository.save(infoDO);
    }

    @Test
    public void testTask() {
        fujianTask.run();
    }
}
