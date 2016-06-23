package com.qunar.fresh.librarysystem.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.qunar.fresh.librarysystem.dao.UserDao;
import com.qunar.fresh.librarysystem.model.User;
import com.qunar.fresh.librarysystem.utils.HttpUtil;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA. User: he.chen Date: 14-4-15 Time: 下午12:20 To change this template use File | Settings |
 * File Templates.
 */
public class UserTaskService {
    @Resource
    private UserDao userDao;

    private static final Logger logger = LoggerFactory.getLogger("servicelogger");
    private static String STAFF_URL = "http://qunar.it/api/employees/?require=all";

    public void insertUser(List<User> userList) {
        userDao.insertUser(userList);
    }

    /**
     * 更新所有员工信息
     * 
     * @throws IOException
     */
    public void updateStaff() throws IOException {
        logger.info("updateStaff task starting");
        String jsonStr = HttpUtil.getContent(STAFF_URL, null);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(jsonStr);
        JsonNode data = jsonNode.findValue("data");
        List<JsonNode> staffs = Lists.newArrayList(data.getElements());
        List<User> staffList = jsonNode2User(staffs);
        updateUserList(staffList);
        logger.info("updateStaff task ending");
    }

    /**
     * 对已经在数据中的更新数据，未在数据库中的插入新信息
     * 
     * @param userList
     */
    private void updateUserList(List<User> userList) {
        Preconditions.checkNotNull(userList);
        Map<String, User> allStaffs;
        List<User> dbStaffList = userDao.fetchAllStaffs();
        Map<String, User> dbStaffs;
        allStaffs = userList2Map(userList);
        dbStaffs = userList2Map(dbStaffList);
        MapDifference<String, User> diffMap = Maps.difference(allStaffs, dbStaffs);
        Map<String, User> onlyLeft = diffMap.entriesOnlyOnLeft();
        if (!onlyLeft.isEmpty()) {
            userDao.insertUser(Lists.newArrayList(onlyLeft.values()));
        }
        Map<String, MapDifference.ValueDifference<User>> valDiffMap = diffMap.entriesDiffering();
        Set<Map.Entry<String, MapDifference.ValueDifference<User>>> entries = valDiffMap.entrySet();
        for (Map.Entry<String, MapDifference.ValueDifference<User>> entry : entries) {
            userDao.updateUser(entry.getValue().leftValue());
        }
    }

    /**
     * 把得到的json数据转成user
     * 
     * @param jsonNodeList
     * @return
     * @throws IOException
     */
    private List<User> jsonNode2User(List<JsonNode> jsonNodeList) throws IOException {
        Preconditions.checkNotNull(jsonNodeList);
        List<User> userList = Lists.newArrayList();
        for (JsonNode item : jsonNodeList) {
            List<JsonNode> elements = Lists.newArrayList(item.getElements());
            String rtx = item.findValue("rtx_id").asText();
            User user = new User();
            user.setUserRtx(rtx);
            user.setUserName(item.findValue("cn").asText());
            StringBuilder userDept = new StringBuilder();
            userDept.append(item.findValue("dep1").asText()).append("->").append(item.findValue("dep2").asText());
            user.setUserDept(userDept.toString());
            userList.add(user);
        }
        return userList;
    }

    /**
     * List转成map
     * 
     * @param userList
     * @return
     */
    private Map<String, User> userList2Map(List<User> userList) {
        Map<String, User> staffMap = Maps.newHashMap();
        for (User user : userList) {
            staffMap.put(user.getUserRtx(), user);
        }
        return staffMap;
    }

    /**
     * 获取已经在数据中的需要更新信息的员工
     * 
     * @param differenceMap
     * @return
     */
    private List<User> getSameKeyValInRight(Map<String, MapDifference.ValueDifference<User>> differenceMap) {
        Preconditions.checkNotNull(differenceMap);
        StringBuilder sb = new StringBuilder();
        List<Map.Entry<String, MapDifference.ValueDifference<User>>> entryList = Lists.newArrayList(differenceMap
                .entrySet());
        List<User> userInRightList = Lists.newArrayList();
        for (Map.Entry<String, MapDifference.ValueDifference<User>> entry : entryList) {
            userInRightList.add(entry.getValue().leftValue());
        }
        return userInRightList;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
