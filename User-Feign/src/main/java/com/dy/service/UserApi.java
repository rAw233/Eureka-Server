package com.dy.service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-provider")
public interface UserApi {

    /**
     * 这里的@GetMapping内的地址是给feign看的，让feign知道应该调用服务提供方的哪个接口
     * 地址需要与provider提供的地址一致
     * @param id
     * @return
     */
    @GetMapping("/alive")
    public String alive(@RequestParam Integer id);

    @GetMapping("register")
    public String register();
}
