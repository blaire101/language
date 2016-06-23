package com.qunar.fresh.librarysystem.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qunar.fresh.librarysystem.model.Library;
import com.qunar.fresh.librarysystem.model.User;
import com.qunar.fresh.librarysystem.model.enums.AdminUserStatus;
import com.qunar.fresh.librarysystem.model.enums.OperationType;
import com.qunar.fresh.librarysystem.service.UserService;
import com.qunar.fresh.librarysystem.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.qunar.fresh.librarysystem.dao.ManagerDao;
import com.qunar.fresh.librarysystem.model.Manager;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA. User: he.chen Date: 14-3-28 Time: 下午5:12
 * 
 * @author he.chen
 */
@Service
public class ManagerService {
    private static final Logger logger = LoggerFactory.getLogger("servicelogger");
    @Resource
    private ManagerDao managerDao;
    @Resource
    private LibraryService libraryService;

    @Resource
    private UserService userService;

    @Resource
    private LogService logService;

    /**
     * 得到一个图书馆的所有管理员
     * 
     * @param libId
     * @return
     */
    public List<Manager> fetchAllManager(int libId) {
        logger.info("得到一个图书馆的所有管理员，图书馆id：{}", libId);
        List<Manager> managerList = managerDao.fetchAllManager(libId);
        return managerList;
    }

    public List<Map<String, Object>> fetchAllManagerJson(int libId) {
        logger.info("得到一个图书馆的所有管理员的json格式，图书馆id：{}", libId);
        List<Manager> managerList = managerDao.fetchAllManager(libId);
        List<Map<String, Object>> managerJson = Lists.transform(managerList,
                new Function<Manager, Map<String, Object>>() {
                    @Override
                    public Map<String, Object> apply(Manager input) {
                        return toJsonMap(input);
                    }
                });
        return managerJson;
    }

    /**
     * 给一个图书馆增加一个管理员
     * 
     * @param manager
     * @param
     * @return
     */
    @Transactional
    public boolean addManager(Manager manager, String operatorRtx) {
        logger.info("增加一个管理员：{}", manager);
        Manager existManager = managerDao.fetchManager(manager.getUserRtx(), null);
        if (existManager == null) {
            managerDao.addManager(manager);
            StringBuilder logInfo = new StringBuilder();
            Library library = libraryService.fetchLibraryByLibId(manager.getLibId());

            logInfo.append(operatorRtx).append(" 给图书馆：").append(library.getLibName()).append(" 增加了一个管理员：")
                    .append(manager.getUserRtx());
            logService.insertLog(OperationType.ADDADMIN, new Date(), operatorRtx, manager.getLibId(),
                    logInfo.toString());
            return true;
        } else if (existManager.getStatus() == AdminUserStatus.NOTVALID.code()) {
            manager.setStatus(AdminUserStatus.ISVALID.code());
            managerDao.updateManager(manager);
            StringBuilder logInfo = new StringBuilder();
            Library library = libraryService.fetchLibraryByLibId(manager.getLibId());


            logInfo.append(" 给图书馆：").append(library.getLibName()).append(" 增加了一个管理员：")
                    .append(manager.getUserRtx());
            logService.insertLog(OperationType.ADDADMIN, new Date(), operatorRtx, manager.getLibId(),
                    logInfo.toString());
            return true;
        } else if (existManager.getStatus() == AdminUserStatus.ISVALID.code()) {
            return false;
        }
        return false;
    }

    /**
     * 删除一个图书馆下的若干个管理员
     * 
     * @param userRtxs
     * @param libId
     * @param request
     */
    @Transactional
    public void deleteManager(List<String> userRtxs, int libId, HttpServletRequest request) {
        if (userRtxs.isEmpty()) {
            return;
        }
        List<Manager> managerList = Lists.newArrayList();
        for (String userRtx : userRtxs) {
            managerList.add(new Manager(userRtx, libId));
        }
        logger.info("删除管理员：{}", managerList);
        managerDao.deleteManager(libId, managerList);
        Library library = libraryService.fetchLibraryByLibId(libId);
        String operatorRtx = UserUtils.getUserRtx(request);
        for (Manager manager : managerList) {
            StringBuilder logInfo = new StringBuilder();
            logInfo.append(" 删除图书馆： ").append(library.getLibName()).append(" 的管理员：")
                    .append(manager.getUserRtx());
            logService.insertLog(OperationType.DELETEADMIN, new Date(), operatorRtx, libId, logInfo.toString());
        }

    }

    public ManagerDao getManagerDao() {
        return managerDao;
    }

    public void setManagerDao(ManagerDao managerDao) {
        this.managerDao = managerDao;
    }

    private Map<String, Object> toJsonMap(Manager manager) {
        Map<String, Object> managerMap = Maps.newHashMap();
        String userRtx = manager.getUserRtx();
        User user = userService.getUserByRtx(userRtx);
        managerMap.put("userRtx", user.getUserRtx());
        managerMap.put("userName", user.getUserName());
        managerMap.put("userDept", user.getUserDept());
        managerMap.put("libId", manager.getLibId());
        return managerMap;
    }

    public LibraryService getLibraryService() {
        return libraryService;
    }

    public void setLibraryService(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }
}
