package com.dy.service;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

public class HealthStatusService implements HealthIndicator {

    private Boolean status = true;

    public void setStatus(Boolean status){
        this.status = status;
    }

    @Override
    public Health health() {
        //根据自定义状态来控制结点health的状态
        if (status)
        return new Health.Builder().up().build();
        return new Health.Builder().down().build();
    }


    public String getStatus() {
        return this.status.toString();
    }
}
