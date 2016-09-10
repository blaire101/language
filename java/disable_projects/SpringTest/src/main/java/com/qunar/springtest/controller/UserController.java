package com.qunar.springtest.controller;

import com.qunar.springtest.model.User;
import com.qunar.springtest.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Author: libin.chen@qunar.com  Date: 14-5-28 11:45
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    //@ResponseBody
    public String login() { // 这只是一个函数名字，叫做什么无所谓
        return "user/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request) {
        if (userService.isValid(username, password, request)) {
            return "welcome";
        } else {
            return "login";
        }
    }

    @RequestMapping("/register")
    public String register() {
        return "user/register";
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView createUser(User user) {
        userService.createUser(user);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user/createSuccess");
        modelAndView.addObject("user", user);
        return modelAndView;
    }
}

