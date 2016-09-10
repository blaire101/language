package com.qunar.fresh.librarysystem.dao;

import com.qunar.fresh.librarysystem.model.Library;
import com.qunar.fresh.librarysystem.model.ReminderInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA. User: libin.chen Date: 14-04-02 Time: 下午4:18 To change this template use File | Settings
 * | File Templates.
 */
@Repository
public interface ExpiresDao {

    public List<Library> selectValidLibrary();

    public List<ReminderInfo> getReminderExpiresInfo(int libId);
}
