package com.qunar.fresh.librarysystem.controller;

import com.qunar.fresh.librarysystem.service.SystemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.qunar.fresh.librarysystem.utils.UserUtils;

import java.lang.Integer;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import com.qunar.fresh.librarysystem.model.SystemParam;
import com.qunar.fresh.librarysystem.resulttype.JsonResult;


/**
 * Created with IntelliJ IDEA.
 * User: yingnan.zhang
 * Date: 14-4-1
 * Time: 下午2:09
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class SystemServiceController {
    @Resource
    public SystemService sysemService;

    public SystemServiceController(SystemService sysemService) {
        this.sysemService = sysemService;
    }

    public SystemServiceController() {

    }

    /*
    该Controller完成两项功能
    1.前端请求系统设置页所需的数据，该方法会首先判断管理员是否已经登录，如果未登录，返回出错状态，
      如果管理员已经登录，将该管理员设置的系统参数返回给前端。
    2.接收前端返回管理员设置的系统设置页数据，如果返回的数据被判断非法，则返回出错信息，如果数据合法，
    则将数据库中系统设置表中的数据更新，返回更新成功信息。
    @param browPeriod
    @param redecCount
    @param redecPeriod
    @param browTotalNum
    @param remindTime
    @return 返回跟新成功或失败信息
    */
    @RequestMapping(value = "admin/syssetup.do", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> updateIdResult(HttpServletRequest httpServletRequest) {
        String checkRtx = UserUtils.getUserRtx(httpServletRequest);
        String browPeriod, browTotalNum, redecCount, remindTime, redecPeriod;
        Integer checkResult;
        Map<String,Object> checkOut = null;

        browPeriod = httpServletRequest.getParameter("browPeriod");
        browTotalNum = httpServletRequest.getParameter("browTotalNum");
        redecCount = httpServletRequest.getParameter("redecCount");
        redecPeriod = httpServletRequest.getParameter("redecPeriod");
        remindTime = httpServletRequest.getParameter("remindTime");

        JsonResult jsonResult = new JsonResult(true);
        if (checkRtx == null || checkRtx.equals("")) {
            jsonResult.put("ret", false).put("errInfo", "用户未登录");
            return JsonResult.statusJson(0, "", jsonResult.getJsonDataMap());
        }
        checkResult = sysemService.fetchAdminls(checkRtx.trim());
        if (checkResult == null) {
            jsonResult.put("ret", false).put("errInfo", "用户ID不存在");
            return JsonResult.statusJson(0, "", jsonResult.getJsonDataMap());
        }
        if (sysemService.getCheckOut(browPeriod, browTotalNum, redecPeriod, redecCount, remindTime) == false) {
            return viewResult(checkResult);
        }
        checkOut = sysemService.parameterParse(browPeriod, browTotalNum,
                redecPeriod, redecCount,
                remindTime, checkResult, checkRtx);
        jsonResult.put("browPeriod", checkOut.get("browPeriod"))
                .put("browTotalNum", checkOut.get("browTotalNum"))
                .put("redecPeriod", checkOut.get("redecPeriod"))
                .put("redecCount", checkOut.get("redecCount"))
                .put("remindTime", checkOut.get("remindTime"))
                .put("ret",checkOut.get("ret"))
                .put("errInfo",checkOut.get("errInfo"));
        return JsonResult.statusJson(0, "", jsonResult.getJsonDataMap());
    }

    private Map<String, Object> viewResult(Integer id) {
        SystemParam systemParam = sysemService.fetchSystemService(id);
        JsonResult jsonResult = new JsonResult(true);
        if (systemParam == null) {
            systemParam = sysemService.getDefaultSystemParam();
        }
        jsonResult.put("ret", true)
                .put("browPeriod", systemParam.getBorrowPeriod())
                .put("redecCount", systemParam.getRedecorateNum())
                .put("redecPeriod", systemParam.getRedecoratePeriod())
                .put("browTotalNum", systemParam.getBorrowTotalNum())
                .put("remindTime", systemParam.getRemindDay());
        return JsonResult.statusJson(0, "", jsonResult.getJsonDataMap());
    }
}