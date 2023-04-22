package com.panghu.controller;

import com.panghu.mode.User;
import com.panghu.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/user")
@RestController
public class UserServiceController {

    @Resource
    private UserService userService;


    @PostMapping("/save")
    public String saveUser(@RequestBody User user){
        int result = userService.save(user);
        return result==1?"添加成功":"添加失败";
    }
}
