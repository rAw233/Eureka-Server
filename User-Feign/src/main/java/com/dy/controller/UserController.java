package com.dy.controller;

import com.dy.service.UserApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserApi userApi;

    @RequestMapping("/getMap")
    public void getMap(){
        userApi.alive(1);
    }

}
