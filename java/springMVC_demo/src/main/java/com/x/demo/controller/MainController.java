package com.x.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class MainController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
//    @ResponseBody
    public String welcome() { // 这只是一个函数名字，叫做什么无所谓
        return "main/welcome";
    }

}

