package com.dy.controller;


import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@RequestMapping("/test")
public class TestController {

    /*springCloud定义的client抽象接口*/
    @Autowired
    DiscoveryClient discoveryClient;
    @Autowired
    EurekaClient eurekaClient;


    @Autowired
    LoadBalancerClient loadBalancerClient;

    @RequestMapping("/services")
    @ResponseBody
    public Object services(){
        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            System.out.println(service);

        }
        return "success";
    }

    @RequestMapping("/instance")
    @ResponseBody
    public Object instance(){
        //获取客户端的生产者，进行消费
        List<InstanceInfo> instanceInfos = eurekaClient.getInstancesByVipAddress("provider", false);
        for (InstanceInfo instanceInfo : instanceInfos) {
            //生产者的状态为活着
            if(instanceInfo.getStatus() == InstanceInfo.InstanceStatus.UP){

                String url = "http://" + instanceInfo.getHostName() + ":" +instanceInfo.getPort();

                RestTemplate restTemplate = new RestTemplate();

                String resp = restTemplate.getForObject(url, String.class);

                System.out.println("resp"+resp);

            }
        }
        return "success";
    }


    @RequestMapping("/ribbon")
    @ResponseBody
    public String ribbon(){

        /*通过负载均衡器来获取指定的生产者客户端*/
        ServiceInstance providerInstance = loadBalancerClient.choose("provider");

        String url = "http://" + providerInstance.getHost() + ":" +providerInstance.getPort();

        RestTemplate restTemplate = new RestTemplate();

        String resp = restTemplate.getForObject(url, String.class);

        System.out.println("resp"+resp);

        return "success";
    }
}
