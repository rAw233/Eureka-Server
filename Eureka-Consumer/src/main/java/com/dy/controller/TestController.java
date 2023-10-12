package com.dy.controller;


import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.loadbalancer.IRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/test")
public class TestController {

    /*springCloud定义的client抽象接口*/
    @Autowired
    DiscoveryClient discoveryClient;
    @Autowired
    EurekaClient eurekaClient;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    IRule iRule;

    @RequestMapping("/services")
    @ResponseBody
    public Object services() {
        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            System.out.println(service);

        }
        return "success";
    }

    @RequestMapping("/instance")
    @ResponseBody
    public Object instance() {
        //获取客户端的生产者，进行消费
        List<InstanceInfo> instanceInfos = eurekaClient.getInstancesByVipAddress("provider", false);
        for (InstanceInfo instanceInfo : instanceInfos) {
            //生产者的状态为活着
            if (instanceInfo.getStatus() == InstanceInfo.InstanceStatus.UP) {

                String url = "http://" + instanceInfo.getHostName() + ":" + instanceInfo.getPort();

                RestTemplate restTemplate = new RestTemplate();

                String resp = restTemplate.getForObject(url, String.class);

                System.out.println("resp" + resp);

            }
        }
        return "success";
    }


    @RequestMapping("/ribbon")
    @ResponseBody
    public String ribbon() {

        /*通过负载均衡器来获取指定的生产者客户端*/
        ServiceInstance providerInstance = loadBalancerClient.choose("provider");

        String url = "http://" + providerInstance.getHost() + ":" + providerInstance.getPort();

        String resp = restTemplate.getForObject(url, String.class);

        System.out.println("resp" + resp);

        return "success";
    }

    @RequestMapping("/personal")
    @ResponseBody
    public String personal() {

        /*通过负载均衡器来获取指定的生产者客户端*/
//        ServiceInstance providerInstance = loadBalancerClient.choose("provider");


        //拿到所有服务列表
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances("provider");
        //生成一个随机数
        int i = new Random(serviceInstances.size()).nextInt();
        //从列表中随机取服务
        ServiceInstance providerInstance = serviceInstances.get(i);

        String url = "http://" + providerInstance.getHost() + ":" + providerInstance.getPort();

        String resp = restTemplate.getForObject(url, String.class);

        System.out.println("resp" + resp);

        return "success";
    }


    @RequestMapping("/restTemplate")
    @ResponseBody
    public Object restTemplate() {

        //可以根据rest格式的请求路由去调用生产者客户端的接口
        String url = "http://provider/hi";
        //第二个参数的类型是根据调用方法的返回值而定的
        String resp = restTemplate.getForObject(url, String.class);

        System.out.println("resp" + resp);

        return "success";
    }
}
