package com.qunar.fresh.librarysystem.service;

import com.qunar.fresh.librarysystem.dao.ReserveDao;
import com.qunar.fresh.librarysystem.email.TaskManager;
import com.qunar.fresh.librarysystem.model.ReminderInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: libin.chen Date: 14-4-3 Time: 下午4:48 To change this template use File | Settings |
 * File Templates.
 */
@Service
public class ReserveService {

    @Resource
    private ReserveDao reserveDao;

    @Resource
    private TaskManager taskManager;

    public ReserveDao getReserveDao() {
        return reserveDao;
    }

    public void setReserveDao(ReserveDao reserveDao) {
        this.reserveDao = reserveDao;
    }

    private final static int reserveFlag = 0;

    public final static int reserveSuccess = 1;

    public final static int existInLib = 2;

    public final static int isReserved = 3;

    public final static int unknownError = 4;

    public final static int parameterError = 5;

    private final int notExistRemindUserFlag = 0;

    private int existRemindUserFlag = 1;

    public int dealReserveInfo(int bookInfoId, String userRtx, int bookLib) {
        if (reserveDao.isConExistBookInfoIdAndLibId(bookInfoId, bookLib).intValue() == 0) {
            return parameterError;
            /** 返回 5 , 参数错误，预约失败 */
        } else if (reserveDao.isStillExistInLib(bookInfoId, bookLib).intValue() != 0) {
            return existInLib;
            /** 返回 2 , 代表该书在库中存在，预约失败 */
        } else if (reserveDao.isReserved(userRtx, bookInfoId, bookLib).intValue() != 0) {
            return isReserved;
            /** 返回 3 , 代表该书用户已经预约过，预约失败 */
        } else {
            int count = 0;
            count = reserveDao.addReserveInfo(bookInfoId, userRtx, bookLib);
            if (count > 0) {
                return reserveSuccess;
                /** 返回 1, 代表预约成功 */
            }
            return unknownError;
        }
    }

    public List<ReminderInfo> selectReminderReserveInfo() {

        return reserveDao.selectReminderReserveInfo();
    }

    public int dealReminderReserveUser() {
        List<ReminderInfo> reminderReserveInfos = selectReminderReserveInfo();

        if (null == reminderReserveInfos || reminderReserveInfos.isEmpty())
            return notExistRemindUserFlag;

        taskManager.integratingPersonalEmailContent(reminderReserveInfos, TaskManager.EmailType.Reserve); // 标志位 0 代表是
                                                                                                          // 预约提醒

        for (ReminderInfo reminderInfo : reminderReserveInfos) {
            updateReserve(reminderInfo.getReserveId());
        }
        return existRemindUserFlag;
    }

    public void updateReserve(int id) {
        reserveDao.updateReserve(id);
    }

}
