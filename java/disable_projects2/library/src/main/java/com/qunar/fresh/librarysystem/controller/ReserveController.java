package com.qunar.fresh.librarysystem.controller;

import com.google.common.primitives.Ints;
import com.qunar.fresh.librarysystem.resulttype.JsonResult;
import com.qunar.fresh.librarysystem.service.ReserveService;
import com.qunar.fresh.librarysystem.utils.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA. User: libin.chen Date: 14-04-02 Time: 13:33 To change this template use File | Settings |
 * File Templates.
 */
@Controller
public class ReserveController {
    @Resource
    private ReserveService reserveService;

    @RequestMapping(value = "/user/reserve.do")
    @ResponseBody
    public Object reserve(HttpServletRequest request) {

        String userRtx = UserUtils.getUserRtx(request);

        Integer bookInfoId = Ints.tryParse(request.getParameter("bookInfoId"));
        Integer bookLib = Ints.tryParse(request.getParameter("bookLib"));

        /**
         * 参数检验与防御性编程
         * */
        String errInfo = new String();

        if (null == bookInfoId || null == bookLib || null == userRtx || "" == userRtx) {
            return JsonResult.statusJson(0, "", JsonResult.dataJson(false, "errInfo", "参数错误"));
        }

        int flag = reserveService.dealReserveInfo(bookInfoId.intValue(), userRtx, bookLib.intValue());

        if (flag == ReserveService.reserveSuccess) {
            return JsonResult.statusJson(0, "", JsonResult.dataJson(true, "errInfo", ""));
        } else if (flag == ReserveService.existInLib) {
            errInfo = "该书已经在图书馆中存在，可以直接借阅，预约失败";
        } else if (flag == ReserveService.isReserved) {
            errInfo = "该书您近期已经预约过，预约失败";
        } else if (flag == ReserveService.parameterError) {
            errInfo = "参数错误";
        } else {
            errInfo = "未知错误";
        }
        return JsonResult.statusJson(0, "", JsonResult.dataJson(false, "errInfo", errInfo));
    }
}
