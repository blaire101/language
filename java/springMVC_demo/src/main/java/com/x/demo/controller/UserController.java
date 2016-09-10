package com.x.demo.controller;

import com.x.demo.model.UserInfo;
import com.x.demo.service.UserService;
import com.x.demo.util.JsonResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping("/register")
    public String register() {
        return "user/register";
    }

    @RequestMapping(value = "/{id}/", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getUserById(@PathVariable("id") String id) {
        UserInfo userInfo = userService.getUserInfoById(id);
        return JsonResult.statusJson(0, "success", userInfo);
    }


    @RequestMapping(value = "/addUser/", method = RequestMethod.POST)
    public ModelAndView createUser(UserInfo userInfo) {
        userService.addUserInfo(userInfo);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user/createSuccess");
        modelAndView.addObject("user", userInfo);
        return modelAndView;
    }

}

