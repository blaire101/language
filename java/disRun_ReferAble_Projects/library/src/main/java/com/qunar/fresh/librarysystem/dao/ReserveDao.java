package com.qunar.fresh.librarysystem.dao;

import com.qunar.fresh.librarysystem.model.ReminderInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA. User: libin.chen Date: 14-04-03 Time: 下午4:18 To change this template use File | Settings
 * | File Templates.
 */
@Repository
public interface ReserveDao {

    public Integer addReserveInfo(@Param("book_info_id") int bookInfoId, @Param("user_rtx") String userRtx,
            @Param("lib_id") int bookLib);

    public Integer isConExistBookInfoIdAndLibId(@Param("book_info_id") int bookInfoId, @Param("lib_id") int bookLib);

    public Integer isStillExistInLib(@Param("book_info_id") int bookInfoId, @Param("lib_id") int bookLib);

    public Integer isReserved(@Param("user_rtx") String userRtx, @Param("book_info_id") int bookInfoId,
            @Param("lib_id") int bookLib);

    public List<ReminderInfo> selectReminderReserveInfo();

    public Integer updateReserve(int id);

}
