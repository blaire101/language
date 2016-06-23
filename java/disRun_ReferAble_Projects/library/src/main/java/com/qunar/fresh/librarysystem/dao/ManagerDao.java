package com.qunar.fresh.librarysystem.dao;

import com.qunar.fresh.librarysystem.model.Manager;
import com.qunar.fresh.librarysystem.model.enums.AdminUserStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA. User: he.chen Date: 14-3-27 Time: 下午9:10
 * 
 * @author hang.gao
 * @author he.chen
 */
@Repository
public interface ManagerDao {

    public List<Manager> fetchAllManager(@Param("libId") int libId);

    public void addManager(Manager manager);

    public void deleteManager(@Param("libId") int libId, @Param("list") List<Manager> managerList);

    public Manager fetchManager(@Param("userRtx") String userRtx, @Param("status") AdminUserStatus status);

    public void updateManager(@Param("manager") Manager manager);
}
