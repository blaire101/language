package com.qunar.fresh.librarysystem.service;

import java.util.List;
import java.util.Map;

import com.qunar.fresh.librarysystem.dao.SystemServiceDao;
import com.qunar.fresh.librarysystem.model.SystemParam;

/**
 * Created with IntelliJ IDEA. User: yingnan.zhang Date: 14-4-9 Time: 下午1:48 To change this template use File | Settings
 * | File Templates.
 */
public interface SystemService {
    /*
     * 根据libid从数据库系统设置表中读取：可借阅的本数、可借阅的天数、可续借的次数、续借一次的天数限制 邮件提醒催还书籍的天数信息，并封装成SystemParam对象。
     */
    public SystemParam fetchSystemService(Integer libId);

    /*
     * 根据user_rtx从数据库管理员表中查阅对应的lib_id信息
     */
    public Integer fetchAdminls(String user_rtx);

    /**
     * 获取默认的系统设置表信息
     * 
     * @return 返回默认的系统设置表信息
     */
    public SystemParam getDefaultSystemParam();

    /*
     * 根据输入参数判读前端是读数据请求或更新数据请求。 如果borrowPeriod,borrowTotalNum,redecoratePeriod,redecorateNum,remindDay都为空，
     * 则是读数据请求，否则是更新数据请求。
     */
    public boolean getCheckOut(String borrowPeriod, String borrowTotalNum, String redecoratePeriod,
            String redecorateNum, String remindDay);

    /*
     * 判断输入参数是否合法，如果不合法将输入参数对应的Map字段段置为-1，将Map("check")=false,返回Map。如果合法,
     * 如果是管理员第一次设置，则将参数信息插入到系统设置表中，同时跟新日志信息表。如果不是，则将参数信息跟新到 系统设置表中，并判断原信息与跟新后信息的差异，将该信息同时跟新到日志信息表。
     */
    public Map<String,Object> parameterParse(String borrowPeriod, String borrowTotalNum, String redecoratePeriod,
            String redecorateNum, String remindDay, Integer libId, String checkRtx);

    public void setSystemParamDao(SystemServiceDao systemParamDao);

    /*
     * 向数据库系统设置表中插入可借阅的本数、可借阅的天数、可续借的次数、续借一次的天数限制、 邮件提醒催还书籍的天数信息。
     */
    public int insertSystemData(int libId, int borrowPeriod, int borrowTotalNum, int redecoratePeriod,
            int redecorateNum, int remindDay);

    public void deleteLibSystemParam(int libId);

    /*
     * 记录systemParam0,SystemParamD两个对象属性的差异，并将差异用字符串的形式返回
     */
    public String differContext(SystemParam systemParam0, SystemParam systemParamD);

    /**
     * 获取可以借阅的本数
     * 
     * @param libId 图书馆的id
     * @return 图书馆中的书可以被借阅的本数
     */
    public Integer getBorrowNumber(Integer libId);

    /**
     * 获取可以借阅的天数
     * 
     * @param libId 图书馆的id
     * @return 图书馆中的书可以被借阅的天数
     */
    public Integer getBorrowPeriod(Integer libId);

    /**
     * 获取可以续借的次数
     * 
     * @param libId 图书馆的id
     * @return 图书馆中的书可以被续借的次数
     */
    public Integer getRedecBorrowNumber(Integer libId);

    /**
     * 获取可以续借的天数
     * 
     * @param libId 图书馆的id
     * @return 图书馆中的书可以被续借的天数
     */
    public Integer getRedecBorrowPeriod(Integer libId);

}
