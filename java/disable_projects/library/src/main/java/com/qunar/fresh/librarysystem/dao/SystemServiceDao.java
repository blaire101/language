package com.qunar.fresh.librarysystem.dao;

import com.qunar.fresh.librarysystem.model.SystemParam;
import com.qunar.fresh.librarysystem.model.enums.LibraryStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA. User: yingnan.zhang Date: 14-4-8 Time: 下午5:35 To change this template use File | Settings
 * | File Templates.
 */
@Repository
public interface SystemServiceDao {
    /*
     * 查询系统设置表中的数据并将结果封装成SystemParam对象.
     */
    public SystemParam fetchSystemData(@Param("libId") int libId);

    /*
     * 根据libId更新系统设置表中的数据
     */
    public int updateSystemData(@Param("libId") int libId, @Param("borrowTotalNum") int borrowTotalNum,
            @Param("borrowPeriod") int borrowPeriod, @Param("redecoratePeriod") int redecoratePeriod,
            @Param("redecorateNum") int redecorateNum, @Param("remindDay") int remindDay);

    /*
     * 统计系统设置表中字段lib_id为libId的数据条数
     */
    public Integer fetchSystemCount(@Param("libId") int libId);

    /*
     * 向系统设置表中插入数据
     */
    public int insertSystemData(@Param("libId") int libId, @Param("borrowTotalNum") int borrowTotalNum,
            @Param("borrowPeriod") int borrowPeriod, @Param("redecoratePeriod") int redecoratePeriod,
            @Param("redecorateNum") int redecorateNum, @Param("remindDay") int remindDay, @Param("status") int status);

    /*
     * 根据user_rtx从管理员表中查出对应的lib_id和stauts字段。
     */
    public List<Integer> fetchAdminls(@Param("user_rtx") String user_rtx);

    /*
     * 向日志信息表中插入系统设置更新信息。
     */
    public int insertLog(@Param("operationType") int operationType, @Param("createTime") Timestamp createTime,
            @Param("operatorRtx") String operationRtx, @Param("libId") int lidId, @Param("info") String info);

    public void setSysParamValid(@Param("libId") int libId, @Param("status") LibraryStatus status);

    public Integer getBorrowNumber(@Param("libId") int libId);

    public Integer getBorrowPeriod(@Param("libId") int libId);

    public Integer getRedecBorrowNum(@Param("libId") int libId);

    public Integer getRedecBorrowPeriod(@Param("libId") int libId);

}
