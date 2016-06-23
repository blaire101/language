package com.qunar.fresh.librarysystem.controller;

import com.google.common.collect.Lists;
import com.qunar.fresh.librarysystem.resulttype.JsonResult;
import com.qunar.fresh.librarysystem.model.Manager;
import com.qunar.fresh.librarysystem.service.impl.ManagerService;
import com.qunar.fresh.librarysystem.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: he.chen Date: 14-3-31 Time: 下午6:25 To change this template use File | Settings |
 * File Templates.
 */
@Controller
@RequestMapping("superAdmin")
public class ManagerController {
    private static final Logger logger = LoggerFactory.getLogger(ManagerController.class);

    @Resource
    private ManagerService managerService;

    /**
     * @param userRtx
     * @param libId
     * @param request
     * @return
     */
    @RequestMapping("addadmin.do")
    @ResponseBody
    public Map<String, Object> addManger(@RequestParam("userRtx") String userRtx, @RequestParam("libId") int libId,
            HttpServletRequest request) {
        try {
            if (!UserUtils.checkUser(userRtx)) {
                return JsonResult.errInfo("无效的用户");
            }
        } catch (IOException e) {
            logger.error("无效的用户 {}", e);
            return JsonResult.errInfo("获取userRtx失败");
        }
        String operatorRtx = UserUtils.getUserRtx(request);
        boolean isAddSucceed = managerService.addManager(new Manager(userRtx, libId), operatorRtx);
        if (!isAddSucceed) {
            return JsonResult.errInfo(userRtx + "：此管理员已经存在");
        }
        JsonResult jsonResult = new JsonResult(true);
        jsonResult.put("adminList", managerService.fetchAllManagerJson(libId));
        return JsonResult.statusJson(0, "", jsonResult.getJsonDataMap());
    }

    /**
     * @param
     * @param libId
     * @param request
     * @return
     */
    @RequestMapping("deladmin.do")
    @ResponseBody
    public Object deleteManager(@RequestParam(value = "userRtxs") String[] userRtxs, @RequestParam("libId") int libId,
            HttpServletRequest request) {

        managerService.deleteManager(Lists.newArrayList(userRtxs), libId, request);

        JsonResult jsonResult = new JsonResult(true);
        jsonResult.put("adminList", managerService.fetchAllManagerJson(libId));
        return JsonResult.statusJson(0, "", jsonResult.getJsonDataMap());
    }
}
