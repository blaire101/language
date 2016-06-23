package com.qunar.fresh.librarysystem.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.math.IntMath;
import com.qunar.fresh.librarysystem.resulttype.JsonResult;
import com.qunar.fresh.librarysystem.dao.LogDao;
import com.qunar.fresh.librarysystem.model.Library;
import com.qunar.fresh.librarysystem.model.LogInfo;
import com.qunar.fresh.librarysystem.model.enums.OperationType;
import com.qunar.fresh.librarysystem.model.PageTurningInfo;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: he.chen Date: 14-3-28 Time: 下午12:13 To change this template use File | Settings |
 * File Templates.
 */
@Service
public class LogService {
    private static final Logger logger = LoggerFactory.getLogger("servicelogger");

    @Resource
    private LogDao logDao;

    @Resource
    private LibraryService libraryService;

    /**
     * 向数据库中插入一天操作日志
     * 
     * @param operationType
     * @param createTime
     * @param operatorRtx
     * @param libId
     * @param info
     */
    public void insertLog(OperationType operationType, Date createTime, String operatorRtx, int libId, String info) {
        LogInfo logInfo = new LogInfo(operationType, createTime, operatorRtx, libId, info);
        logDao.insertLog(logInfo);
    }

    /**
     * 向数据库中插入一天操作日志
     * 
     * @param logInfo
     */
    public void insertLog(LogInfo logInfo) {
        logDao.insertLog(logInfo);
    }

    /**
     * 获取日志页面的翻夜信息
     * 
     * @param logInfoList
     * @param endTime
     * @param goPageNum
     * @param pageSize
     * @return
     */
    public PageTurningInfo getPageTurningInof(List<LogInfo> logInfoList, Date endTime, int goPageNum, int pageSize) {
        PageTurningInfo totalInfo = new PageTurningInfo(0, 0, 0, 0);
        PageTurningInfo pageTurningInfo = new PageTurningInfo();
        OperationType operationType;
        for (LogInfo logInfo : logInfoList) {
            operationType = logInfo.getOperationType();
            if (operationType.code().equals(5) || operationType.code().equals(4)) {
                logInfo.setLibId(0);
            }
            pageTurningInfo.setTotalCount(countLogInfo(logInfo, endTime));
            pageTurningInfo.setPageSize(pageSize);
            pageTurningInfo.setCurrentPageNum(goPageNum);
            pageTurningInfo.setTotalPage(IntMath.divide(pageTurningInfo.getTotalCount(), pageTurningInfo.getPageSize(),
                    RoundingMode.CEILING));

            totalInfo.setTotalCount(totalInfo.getTotalCount() + pageTurningInfo.getTotalCount());
            totalInfo.setPageSize(pageTurningInfo.getPageSize());
            totalInfo.setCurrentPageNum(pageTurningInfo.getCurrentPageNum());
        }
        totalInfo
                .setTotalPage(IntMath.divide(totalInfo.getTotalCount(), totalInfo.getPageSize(), RoundingMode.CEILING));
        return totalInfo;
    }

    /**
     * 根据指定日志类型，时间日期，翻页数获得操作日志信息
     * 
     * @param logInfos
     * @param endTime
     * @param goPageNum
     * @param pageSize
     * @return
     */
    public List<Map<String, Object>> fetchLogInfoList(List<LogInfo> logInfos, Date endTime, int goPageNum, int pageSize) {
        Preconditions.checkNotNull(logInfos);
        RowBounds rowBounds = new RowBounds((goPageNum - 1) * pageSize, pageSize);
        List<OperationType> operationTypeList = Lists.newArrayList();
        for (LogInfo logInfo : logInfos) {
            operationTypeList.add(logInfo.getOperationType());
        }
        LogInfo logInfo = logInfos.get(0);
        OperationType operationType = operationTypeList.get(0);
        if (operationType.code().equals(5) || operationType.code().equals(4)) {
            logInfo.setLibId(0);
        }
        List<LogInfo> logInfoList = logDao.fetchLogInfoList(logInfo, operationTypeList, endTime, rowBounds);
        List<Map<String, Object>> logMapList = Lists.transform(logInfoList,
                new Function<LogInfo, Map<String, Object>>() {
                    @Override
                    public Map<String, Object> apply(LogInfo input) {
                        return toJsonMap(input);
                    }
                });
        return logMapList;
    }

    /**
     * 获得日志信息转换成json
     * 
     * @param logInfo
     * @param endTime
     * @param goPageNum
     * @param pageSize
     * @return
     */
    public Map<String, Object> fetchLogInfoJson(LogInfo logInfo, Date endTime, int goPageNum, int pageSize) {
        Preconditions.checkNotNull(logInfo);
        List<LogInfo> logInfoList = Lists.newArrayList(logInfo);
        LogInfo preLogInfo = new LogInfo(logInfo);
        preLogInfo.setOperationType(OperationType.values()[preLogInfo.getOperationType().code() - 1]);
        logInfoList.add(preLogInfo);
        List<Map<String, Object>> logMapList = fetchLogInfoList(logInfoList, endTime, goPageNum, pageSize);
        if (logMapList == null) {
            return JsonResult.errInfo("获取日志信息失败");
        }
        JsonResult jsonResult = new JsonResult(true);
        PageTurningInfo pageTurningInfo = getPageTurningInof(logInfoList, endTime, goPageNum, pageSize);
        jsonResult.put("currentPageNum", pageTurningInfo.getCurrentPageNum());
        jsonResult.put("totalCount", pageTurningInfo.getTotalCount());
        jsonResult.put("pageSize", pageTurningInfo.getPageSize());
        jsonResult.put("totalPage", pageTurningInfo.getTotalPage());
        jsonResult.put("logList", logMapList);
        return jsonResult.getJsonDataMap();
    }

    public int countLogInfo(LogInfo logInfo, Date endTime) {
        return (Integer) logDao.countLogInfo(logInfo, endTime);
    }

    public LogDao getLogDao() {
        return logDao;
    }

    public void setLogDao(LogDao logDao) {
        this.logDao = logDao;
    }

    public LogInfo getLogInfo(OperationType operationType, Date startTime, int libId) {
        LogInfo logInfo = new LogInfo(operationType, startTime, libId);
        return logInfo;
    }

    /**
     * 转换结果到需要的json格式
     * 
     * @param logInfo
     * @return
     */
    public Map<String, Object> toJsonMap(LogInfo logInfo) {
        Library library = libraryService.fetchLibraryByLibId(logInfo.getLibId());
        if (library == null) {
            library = new Library();
            library.setLibName("无");
        }
        Map<String, Object> map = Maps.newHashMap();
        map.put("createTime", logInfo.getCreateTime());
        map.put("libName", library.getLibName());
        map.put("operationType", logInfo.getOperationType().text());
        map.put("userRtx",logInfo.getOperatorRtx());
        map.put("logInfo", logInfo.getInfo());
        return map;
    }
}
