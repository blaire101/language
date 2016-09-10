package com.qunar.fresh.librarysystem.service.impl;

import com.qunar.fresh.librarysystem.model.enums.LibraryStatus;
import com.qunar.fresh.librarysystem.model.enums.OperationType;
import com.qunar.fresh.librarysystem.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.Integer;
import javax.annotation.Resource;
import com.qunar.fresh.librarysystem.model.SystemParam;
import com.qunar.fresh.librarysystem.dao.SystemServiceDao;
import java.util.*;

/**
 * Created with IntelliJ IDEA. User: yingnan.zhang Date: 14-4-1 Time: 下午1:48 To change this template use File | Settings
 * | File Templates.
 */
public class SystemServiceImp implements SystemService {
    @Resource
    private SystemServiceDao systemServiceDao;
    @Resource
    private LogService logService;
    private static final Integer NOT_SUCH_LIBRARY_CODE = -1;
    private int SYSTEM_PARAM_NUM;
    private String[] arrayParamInsert;
    private String[] arrayParamString;
    private String[] arrayParamUpdate;
    private Integer[] constrain;
    private String differenceContext;
    private static Logger logger = LoggerFactory.getLogger("servicelogger");

    /*
     * 根据libid从数据库系统设置表中读取：可借阅的本数、可借阅的天数、可续借的次数、续借一次的天数限制 邮件提醒催还书籍的天数信息，并封装成SystemParam对象。
     */
    public SystemParam fetchSystemService(Integer libid) {

        return systemServiceDao.fetchSystemData(libid);
    }

    /*
     * 根据libid跟新数据库系统设置表中可借阅的本数、可借阅的天数、可续借的次数、续借一次的天数限制、 邮件提醒催还书籍的天数信息。
     */
    public int updateSystemData(int libid, int borrowPeriod, int borrowTotalNum, int redecoratePeriod,
                                int redecorateNum, int remindDay) {

        return systemServiceDao.updateSystemData(libid, borrowTotalNum, borrowPeriod, redecoratePeriod, redecorateNum,
                remindDay);
    }

    /*
     * 在数据库系统设置表中查看字段lib_id等于libid的条数。
     */
    public Integer fetchSystemCount(int libId) {
        return systemServiceDao.fetchSystemCount(libId);
    }

    /*
     * 向数据库系统设置表中插入可借阅的本数、可借阅的天数、可续借的次数、续借一次的天数限制、 邮件提醒催还书籍的天数信息。
     */
    public int insertSystemData(int libId, int borrowPeriod, int borrowTotalNum, int redecoratePeriod,
                                int redecorateNum, int remindDay) {
        return systemServiceDao.insertSystemData(libId, borrowTotalNum, borrowPeriod, redecoratePeriod, redecorateNum,
                remindDay, 1);
    }

    /*
     * 根据user_rtx从数据库管理员表中查阅对应的lib_id信息
     */

    public Integer fetchAdminls(String user_rtx) {
        List<Integer> CheckResult;
        CheckResult = systemServiceDao.fetchAdminls(user_rtx);
        if (CheckResult == null || CheckResult.size() != 1) {
            logger.error("数据库出现异常：{}查询失败", user_rtx);
            return null;
        } else {
            return (Integer) CheckResult.iterator().next();
        }
    }

    /*
     * 向数据库日志信息表中插入用户操作信息（操作类型为：系统设置）。
     */
    private void insertLog(int libid, String user_rtx, String info) {
        logService.insertLog(OperationType.SYSTEMSETTING, new Date(), user_rtx, libid, info);
    }

    /*
     * 记录systemParam0,SystemParamD两个对象属性的差异，并将差异用字符串的形式返回
     */
    public String differContext(SystemParam systemParam0, SystemParam systemParamD) {
        StringBuilder stringBuilder = new StringBuilder("");
        String differenceDetial;
        boolean change = false;
        if (systemParamD == null) {
            change = true;
            List<Integer> paramInteger = paramListPacking(systemParam0);
            for (int begin = 0; begin < SYSTEM_PARAM_NUM; begin++) {
                stringBuilder.append(arrayParamInsert[begin]);
                stringBuilder.append(paramInteger.get(begin));
            }
        } else {
            List<Integer> paramIntegerO = paramListPacking(systemParam0);
            List<Integer> paramIntegerD = paramListPacking(systemParamD);
            for (int begin = 0; begin < SYSTEM_PARAM_NUM; begin++) {
                if (!(paramIntegerO.get(begin).equals(paramIntegerD.get(begin)))) {
                    stringBuilder.append(arrayParamUpdate[begin]);
                    stringBuilder.append(paramIntegerO.get(begin));
                    stringBuilder.append("->");
                    stringBuilder.append(paramIntegerD.get(begin));
                    change = true;
                }
            }
        }
        if (change == true)
            differenceDetial = stringBuilder.toString();
        else
            differenceDetial = differenceContext;
        return differenceDetial;
    }

    /*
     * 根据传入参数设置SystemParam对象的属性并将对象返回。
     */
    public SystemParam setSystemParam(int borrowPeriod, int borrowTotalNum, int redecoratePeriod, int redecorateNum,
                                      int remindDay) {
        SystemParam systemParam = new SystemParam();
        systemParam.setBorrowTotalNum(borrowTotalNum);
        systemParam.setBorrowPeriod(borrowPeriod);
        systemParam.setRedecorateNum(redecorateNum);
        systemParam.setRedecoratePeriod(redecoratePeriod);
        systemParam.setRemindDay(remindDay);
        return systemParam;
    }

    /*
     * 判断输入参数是否合法，如果不合法将输入参数对应的Map字段段置为-1，将Map("check")=false,返回Map。如果合法,
     * 如果是管理员第一次设置，则将参数信息插入到系统设置表中，同时跟新日志信息表。如果不是，则将参数信息跟新到 系统设置表中，并判断原信息与跟新后信息的差异，将该信息同时跟新到日志信息表。
     */
    public Map<String,Object> parameterParse(String borrowPeriod, String borrowTotalNum, String redecoratePeriod,
                                        String redecorateNum, String remindDay, Integer libId, String checkRtx) {
        boolean check = true,checkstauts=true;
        Integer paramInteger = 0, getParamUpdate =0;
        SystemParam paramOrigial = null, paramDest = null;
        List<String> packParamString;
        List<Integer> packParamInteger;
        Map<String,Object>resultParam = new HashMap<String, Object>();
        String getParamOrigial = null;
        StringBuilder errorbuilderString=new StringBuilder("");
        resultParam.put("errInfo","");//默认值
        Integer SelectCount = fetchSystemCount(libId);
        packParamString = paramListPacking(borrowPeriod, borrowTotalNum, redecoratePeriod, redecorateNum, remindDay);
        if (SelectCount == 0) {
            logger.info("新增图书馆ID：{}：管理员{}:数据库进行插入操作", libId, checkRtx);
            packParamInteger = paramListPacking(getDefaultSystemParam());

        } else {
            paramOrigial = fetchSystemService(libId);
            packParamInteger = paramListPacking(paramOrigial);
        }
        for (int begin = 0; begin < SYSTEM_PARAM_NUM; begin++) {
            getParamOrigial = packParamString.get(begin);
            getParamUpdate = packParamInteger.get(begin);
            if (null == getParamOrigial || "".equals(getParamOrigial)) {
                resultParam.put(arrayParamString[begin],getParamUpdate);
            } else {
                try {
                    paramInteger = Integer.parseInt(getParamOrigial);
                    if (paramInteger > 0 && paramInteger <= constrain[begin]) {
                        resultParam.put(arrayParamString[begin],paramInteger);
                    } else {
                        resultParam.put(arrayParamString[begin],getParamUpdate);
                       if(checkstauts==true){
                       resultParam.put("errInfo",errorMessage(begin,getParamOrigial));
                       checkstauts=false;    }
                       check = false;
                    }

                } catch (Exception e) {
                    resultParam.put(arrayParamString[begin],getParamUpdate);
                    if(checkstauts==true){
                        resultParam.put("errInfo",errorMessage(begin,getParamOrigial));
                        checkstauts=false;    }
                         check = false;
                }
            }
        }

        if (check == false) {
            resultParam.put("ret",false);
            return resultParam;
        } else if ((Integer)resultParam.get(arrayParamString[2]) >(Integer) resultParam.get(arrayParamString[0])) {
            resultParam.put(arrayParamString[0], packParamInteger.get(0));
            resultParam.put(arrayParamString[2], packParamInteger.get(2));
            resultParam.put("ret",false);
            if(checkstauts==true){
                resultParam.put("errInfo",errorMessage(-1,getParamOrigial));
                }
            return resultParam;
        }

        if (SelectCount == 0) {
            insertSystemData(libId, (Integer)resultParam.get(arrayParamString[0]),
                    (Integer)resultParam.get(arrayParamString[1]),
                    (Integer)resultParam.get(arrayParamString[2]),
                    (Integer)resultParam.get(arrayParamString[3]),
                    (Integer)resultParam.get(arrayParamString[4]));
            paramOrigial = setSystemParam((Integer)resultParam.get(arrayParamString[0]),
                    (Integer)resultParam.get(arrayParamString[1]),
                    (Integer)resultParam.get(arrayParamString[2]),
                    (Integer)resultParam.get(arrayParamString[3]),
                    (Integer)resultParam.get(arrayParamString[4]));
            insertLog(libId, checkRtx, differContext(paramOrigial, null));
            resultParam.put("ret",true);
        } else {
            paramDest = setSystemParam((Integer)resultParam.get(arrayParamString[0]),
                    (Integer)resultParam.get(arrayParamString[1]),
                    (Integer)resultParam.get(arrayParamString[2]),
                    (Integer)resultParam.get(arrayParamString[3]),
                    (Integer)resultParam.get(arrayParamString[4]));
            SelectCount=updateSystemData(libId,(Integer)resultParam.get(arrayParamString[0]),
                    (Integer)resultParam.get(arrayParamString[1]),
                    (Integer)resultParam.get(arrayParamString[2]),
                    (Integer)resultParam.get(arrayParamString[3]),
                    (Integer)resultParam.get(arrayParamString[4]));
            if (updateSystemData(libId,(Integer)resultParam.get(arrayParamString[0]),
                    (Integer)resultParam.get(arrayParamString[1]),
                    (Integer)resultParam.get(arrayParamString[2]),
                    (Integer)resultParam.get(arrayParamString[3]),
                    (Integer)resultParam.get(arrayParamString[4])) != 1) {
                logger.error("图书馆ID:{}更新错误:", libId);
                resultParam.put("ret",false);
            } else {
                insertLog(libId, checkRtx, differContext(paramOrigial, paramDest));
                resultParam.put("ret",true);
            }
        }
        return resultParam;
    }
    public void setSystemParamDao(SystemServiceDao systemParamDao) {
        this.systemServiceDao = systemParamDao;
    }

    /*
     * 根据输入参数判读前端是读数据请求或更新数据请求。 如果borrowPeriod,borrowTotalNum,redecoratePeriod,redecorateNum,remindDay都为空，
     * 则是读数据请求，否则是更新数据请求。
     */
    public boolean getCheckOut(String borrowPeriod, String borrowTotalNum, String redecoratePeriod,
                               String redecorateNum, String remindDay) {
        if ((borrowPeriod == null || borrowPeriod.equals("")) && (borrowTotalNum == null || borrowTotalNum.equals(""))
                && (redecorateNum == null || redecorateNum.equals(""))
                && (redecoratePeriod == null || redecoratePeriod.equals(""))
                && (remindDay == null || remindDay.equals(""))) {
            return false;
        }
        return true;
    }

    @Override
    public void deleteLibSystemParam(int libId) {
        systemServiceDao.setSysParamValid(libId, LibraryStatus.INVALID);
    }

    public Integer getBorrowNumber(Integer libId) {
        Integer borrowNumber = systemServiceDao.getBorrowNumber(libId);
        if (null == borrowNumber || 0 == borrowNumber) {
            logger.error("图书馆系统设置图书借阅数量：未查到该libId:{}", libId);
            return NOT_SUCH_LIBRARY_CODE;
        } else {
            return borrowNumber;
        }
    }

    public Integer getBorrowPeriod(Integer libId) {
        Integer borrowPeriod = systemServiceDao.getBorrowPeriod(libId);
        if (null == borrowPeriod || 0 == borrowPeriod) {
            logger.error("图书馆系统设置图书可借阅天数：未查到该libId:{}", libId);
            return NOT_SUCH_LIBRARY_CODE;
        } else {
            return borrowPeriod;
        }
    }

    public Integer getRedecBorrowNumber(Integer libId) {
        Integer redecBorrowNum = systemServiceDao.getRedecBorrowNum(libId);
        if (null == redecBorrowNum || 0 == redecBorrowNum) {
            logger.error("图书馆系统设置图书可续借本数：未查到该libId:{}", libId);
            return NOT_SUCH_LIBRARY_CODE;
        } else {
            return redecBorrowNum;
        }
    }

    public Integer getRedecBorrowPeriod(Integer libId) {
        Integer redecBorrowPeriod = systemServiceDao.getRedecBorrowPeriod(libId);
        if (null == redecBorrowPeriod || 0 == redecBorrowPeriod) {
            logger.error("图书馆系统设置图书可续借本数：未查到该libId:{}", libId);
            return NOT_SUCH_LIBRARY_CODE;
        } else {
            return redecBorrowPeriod;
        }
    }

    /**
     * 获取系统设置默认参数
     */
    public SystemParam getDefaultSystemParam() {
        SystemParam param = new SystemParam();
        param.setBorrowTotalNum(5);
        param.setBorrowPeriod(30);
        param.setRedecorateNum(2);
        param.setRedecoratePeriod(15);
        param.setRemindDay(5);
        return param;
    }
    private String errorMessage(int index,String originalInfo){
        StringBuilder errmsg =new StringBuilder("");
        if(index==-1){
          errmsg.append("亲~可借阅时间要要大于等于可续借时间奥");
        }
        else {
         errmsg.append(arrayParamUpdate[index]);
         errmsg.append("设定值为：").append(originalInfo);
         errmsg.append("--但该参数范围为：1-").append(constrain[index]).append("请修正");
        }
        return errmsg.toString();
    }
    /**
     * 获取系统设置默认参数
     */
    public List<String> paramListPacking(String borrowPeriod, String borrowTotalNum, String redecoratePeriod,
                                         String redecorateNum, String remindDay) {
        List<String> paramPacking = new ArrayList<String>();
        paramPacking.add(borrowPeriod);
        paramPacking.add(borrowTotalNum);
        paramPacking.add(redecoratePeriod);
        paramPacking.add(redecorateNum);
        paramPacking.add(remindDay);
        return paramPacking;
    }

    /**
     * 获取系统设置默认参数
     */
    public List<Integer> paramListPacking(SystemParam systemPacking) {
        List<Integer> paramPacking = new ArrayList<Integer>();
        paramPacking.add(systemPacking.getBorrowPeriod());
        paramPacking.add(systemPacking.getBorrowTotalNum());
        paramPacking.add(systemPacking.getRedecoratePeriod());
        paramPacking.add(systemPacking.getRedecorateNum());
        paramPacking.add(systemPacking.getRemindDay());
        return paramPacking;
    }
    public void setArrayParamInsert(String[] arrayParamInsert) {
        this.arrayParamInsert = arrayParamInsert;
    }

    public void setArrayParamString(String[] arrayParamString) {
        this.arrayParamString = arrayParamString;
    }
    public void setArrayParamUpdate(String[] arrayParamUpdate){
        this.arrayParamUpdate=arrayParamUpdate;
    }

    public void setSYSTEM_PARAM_NUM(Integer infonum) {
        this.SYSTEM_PARAM_NUM = infonum;
    }

    public void setDifferenceContext(String differenceContext) {
        this.differenceContext = differenceContext;
    }

    public void setConstrain(Integer[] constrain) {
        this.constrain = constrain;
    }
}