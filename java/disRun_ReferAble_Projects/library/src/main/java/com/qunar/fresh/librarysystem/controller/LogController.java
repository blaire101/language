package com.qunar.fresh.librarysystem.controller;

import com.google.common.base.Strings;
import com.qunar.fresh.librarysystem.resulttype.JsonResult;
import com.qunar.fresh.librarysystem.model.LogInfo;
import com.qunar.fresh.librarysystem.model.enums.OperationType;
import com.qunar.fresh.librarysystem.service.UserService;
import com.qunar.fresh.librarysystem.service.impl.LogService;
import com.qunar.fresh.librarysystem.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.commons.lang.time.DateUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: he.chen Date: 14-3-28 Time: 下午2:31 To change this template use File | Settings |
 * File Templates.
 */
@Controller
public class LogController {

    private static final Logger logger = LoggerFactory.getLogger(LogController.class);
    @Resource
    private LogService logService;
    @Resource
    private UserService userService;

    private static final String[] dateFormate = new String[] { "yyyy-MM-dd", "yyyy/MM/dd" };

    /**
     * 
     * @param libId
     * @param type
     * @param startTimeStr
     * @param endTimeStr
     * @param goPageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("superAdmin/log.do")
    @ResponseBody
    public Object superAdminFetchLogInfo(@RequestParam("libId") int libId, @RequestParam("operationType") int type,
            @RequestParam("startTime") String startTimeStr, @RequestParam("endTime") String endTimeStr,
            @RequestParam("goPageNum") int goPageNum, @RequestParam("pageSize") int pageSize) throws ParseException {
        if (Strings.isNullOrEmpty(startTimeStr) || Strings.isNullOrEmpty(endTimeStr)) {
            return JsonResult.errInfo("无效的时间");
        }
        if (goPageNum <= 0 || pageSize < 15 || pageSize > 60) {
            goPageNum = 1;
            pageSize = 15;
        }
        return fetchLogInfo(libId,type,startTimeStr,endTimeStr,goPageNum,pageSize);
    }

    @RequestMapping("admin/log.do")
    @ResponseBody
    public Object adminFetchLogInfo(@RequestParam("operationType") int type,
            @RequestParam("startTime") String startTimeStr, @RequestParam("endTime") String endTimeStr,
            @RequestParam("goPageNum") int goPageNum, @RequestParam("pageSize") int pageSize, HttpServletRequest request)
            throws ParseException {
        if (Strings.isNullOrEmpty(startTimeStr) || Strings.isNullOrEmpty(endTimeStr)) {
            return JsonResult.errInfo("无效的时间");
        }
        if (goPageNum <= 0 || pageSize < 15 || pageSize > 60) {
            goPageNum = 1;
            pageSize = 15;
        }
        String userRtx = UserUtils.getUserRtx(request);
        int libId = userService.getUserLibId(userRtx);
        return fetchLogInfo(libId,type,startTimeStr,endTimeStr,goPageNum,pageSize);
    }

    private Map<String, Object> fetchLogInfo(int libId, int type, String startTimeStr, String endTimeStr,
            int goPageNum, int pageSize) throws ParseException {
        Date startTime, endTime;
        try {
            startTime = DateUtils.parseDate(startTimeStr, dateFormate);
            endTime = DateUtils.parseDate(endTimeStr, dateFormate);
        } catch (ParseException e) {
            logger.error("查询日志时，对传入的时间解析出异常:{}", e);
            throw e;
        }
        LogInfo logInfo = logService.getLogInfo(OperationType.codeOf(type), startTime, libId);
        endTime = DateUtils.addDays(endTime, 1);
        Map<String, Object> logInfoList = logService.fetchLogInfoJson(logInfo, endTime, goPageNum, pageSize);
        return JsonResult.statusJson(0, "", logInfoList);

    }

}
