package com.x.demo.controller;

import com.google.common.collect.Lists;
import com.x.demo.model.UserInfo;
import com.x.demo.service.UserInfoService;
import com.x.demo.util.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserInfoController {

    private static Logger logger = LoggerFactory.getLogger(UserInfoController.class);

    @Resource
    private UserInfoService userInfoService;

    @RequestMapping(value = "/addusers", method = RequestMethod.GET)
    //@ResponseBody
    public ModelAndView addUserList() { // about these users' info, you should get them from the page. This is only example.

        List<UserInfo> userInfoList = Lists.newArrayList();

        UserInfo userInfo1 = new UserInfo("Jay", "Chou", 32);
        UserInfo userInfo2 = new UserInfo("Andy", "Wong", 31);
        UserInfo userInfo3 = new UserInfo("Blair", "Chan", 26);

        userInfoList.add(userInfo1);
        userInfoList.add(userInfo2);
        userInfoList.add(userInfo3);

        userInfoService.addUserInfoList(userInfoList);

        ModelAndView modelAndView = new ModelAndView("user/addSuccess");
        return modelAndView;
    }

    @RequestMapping(value = "/getuser/{uid}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getUserById(@PathVariable("uid") String uid) {
        logger.info("getUserById uid : {}", uid);
        Long id = null;
        try {
            id = Long.parseLong(uid);
        } catch (Exception e) {
            logger.error("String uid {} trans to Long exist error : {}", uid, e.getMessage());
        }
        if (null == id) {
            return JsonResult.statusJson(1, "failed", null);

        }
        UserInfo userInfo = userInfoService.getUserInfoById(id);
        return JsonResult.statusJson(0, "success", userInfo);
    }

    @RequestMapping(value = "/getusers", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getAllUserList() {
        List<UserInfo> userInfoList = userInfoService.getAllUserList();
        return JsonResult.statusJson(0, "success", userInfoList);
    }

}

