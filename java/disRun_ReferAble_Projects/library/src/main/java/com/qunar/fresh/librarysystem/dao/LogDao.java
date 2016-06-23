package com.qunar.fresh.librarysystem.dao;

import com.qunar.fresh.librarysystem.model.LogInfo;
import com.qunar.fresh.librarysystem.model.enums.OperationType;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: he.chen Date: 14-3-28 Time: 下午12:15 To change this template use File | Settings |
 * File Templates.
 */
@Repository
public interface LogDao {
    void insertLog(LogInfo logInfo);

    List<LogInfo> fetchLogInfoList(@Param("logInfo") LogInfo logInfo,
            @Param("list") List<OperationType> operationTypeList, @Param("endTime") Date endTime, RowBounds rowBounds);

    int countLogInfo(@Param("logInfo") LogInfo logInfo, @Param("endTime") Date endTime);
}
