package com.dy.controller;

import com.dy.service.HealthStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    HealthStatusService healthStatusService;

    @GetMapping("/health")
    public String health(@RequestParam("status")Boolean status){
        //通过前端可以改变自定义状态值
        healthStatusService.setStatus(status);
        return healthStatusService.getStatus();
    }

}
