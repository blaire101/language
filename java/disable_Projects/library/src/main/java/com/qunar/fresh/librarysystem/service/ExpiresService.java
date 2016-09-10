package com.qunar.fresh.librarysystem.service;

import com.google.common.collect.Lists;
import com.qunar.fresh.librarysystem.dao.ExpiresDao;
import com.qunar.fresh.librarysystem.model.Library;
import com.qunar.fresh.librarysystem.model.ReminderInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: libin.chen Date: 14-4-2 Time: 下午1:40 To change this template use File | Settings |
 * File Templates.
 */
@Service
public class ExpiresService {
    @Resource
    private ExpiresDao expiresDao;

    public ExpiresDao getExpiresDao() {
        return expiresDao;
    }

    public void setExpiresDao(ExpiresDao expiresDao) {
        this.expiresDao = expiresDao;
    }

    public List<ReminderInfo> getReminderExpiresInfo() {
        List<ReminderInfo> reminderExpiresInfoList = Lists.newArrayList();

        List<Library> libraries = expiresDao.selectValidLibrary();

        for (Library library : libraries) {
            int libId = library.getLibId();
            List<ReminderInfo> list = expiresDao.getReminderExpiresInfo(libId);

            for (ReminderInfo reminderInfo : list) {
                reminderInfo.setLibDept(library.getLibDept());
                reminderInfo.setLibName(library.getLibName());
            }

            reminderExpiresInfoList.addAll(list);
        }
        return reminderExpiresInfoList;
    }

}
