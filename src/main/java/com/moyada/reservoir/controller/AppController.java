package com.moyada.reservoir.controller;

import com.moyada.reservoir.model.QueryReq;
import com.moyada.reservoir.model.ReservoirVO;
import com.moyada.reservoir.service.ReservoirService;
import com.moyada.reservoir.task.Task;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
@RestController
public class AppController implements BeanFactoryAware {

    @Autowired
    private ReservoirService reservoirService;

    @Autowired
    private BeanFactory beanFactory;

    @GetMapping("now")
    public List<ReservoirVO> get(QueryReq req) {
        if (req.getProvince() == null) {
            return Collections.emptyList();
        }
        if (req.getCity() == null) {
            return Collections.emptyList();
        }
        return reservoirService.get(req);
    }

    @GetMapping("run")
    public String run(@RequestParam("task") String task) {
        Task taskJob = beanFactory.getBean(task, Task.class);
        taskJob.run();
        return "ok";
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
