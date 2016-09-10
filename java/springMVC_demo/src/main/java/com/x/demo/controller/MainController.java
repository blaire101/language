package com.x.demo.controller;

import com.x.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

@Controller
@RequestMapping("/")
public class MainController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
//    @ResponseBody
    public String login() { // 这只是一个函数名字，叫做什么无所谓
        return "main/welcome";
    }

}

