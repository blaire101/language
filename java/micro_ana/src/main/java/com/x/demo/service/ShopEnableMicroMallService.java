package com.x.demo.service;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Lists;
import com.x.demo.model.ShopOpenMicroInfo;
import com.x.demo.util.BaseConvert;
import com.x.demo.util.JDBCUtil;
import com.x.demo.util.spring.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Date : 2016-06-12
 */

/** Singleton pattern **/
public class ShopEnableMicroMallService {

    private static final ShopEnableMicroMallService instance = new ShopEnableMicroMallService();

    public static final ShopEnableMicroMallService getInstance() {
        return instance;
    }

    private static Logger logger = LoggerFactory.getLogger(ShopEnableMicroMallService.class);

    public ShopEnableMicroMallService() {
    }

    public List<ShopOpenMicroInfo> getShopOpenMicroInfoList() {
        // current day
        DruidDataSource dataSourceFrom = SpringContextUtil.getBean("dataSourceFrom");

        Connection connectionStat = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<ShopOpenMicroInfo> shopOpenMicroInfoList = Lists.newArrayList();

        try {
            connectionStat = dataSourceFrom.getConnection();
            shopOpenMicroInfoList = getShopMicroInfo(connectionStat, pstmt, rs);
        } catch (SQLException e) {
            logger.info("SQLException : {}", e);
        } finally {
            JDBCUtil.closeConnection(rs, pstmt, connectionStat);
        }
        return shopOpenMicroInfoList;
    }

    private List<ShopOpenMicroInfo> getShopMicroInfo(Connection connection, PreparedStatement pstmt, ResultSet rs) throws SQLException {
        String sql = ResourceBundle.getBundle("props.processSql").getString("shop_open_micro_sql");
        logger.info("exec sql : {}", sql);
        pstmt = (PreparedStatement) connection.prepareStatement(sql);
        rs = pstmt.executeQuery();
        return getShopMicroInfoBatch(rs);
    }

    private List<ShopOpenMicroInfo> getShopMicroInfoBatch(ResultSet rs) {
        List<ShopOpenMicroInfo> shopOpenMicroInfoList = Lists.newArrayList();
        try {
            while (rs.next()) {
                Map<String, Object> map = BaseConvert.ConvertResultMap(rs);
                ShopOpenMicroInfo shopOpenMicroInfo = null;
                try {
                    shopOpenMicroInfo = getShopShortInfo(map);
                } catch (Exception e) {
                    logger.error("getShopShortInfo by {} and appear error {} !",map, e);
                }
                if (null == shopOpenMicroInfo) {
                    continue;
                }
                shopOpenMicroInfoList.add(shopOpenMicroInfo);
            }
        } catch (SQLException e) {
            logger.error("Traverse rs : {} appear error!", rs, e);
        }
        return shopOpenMicroInfoList;
    }

    private ShopOpenMicroInfo getShopShortInfo(Map<String, Object> map) {
        Long shopId = (Long) map.get("shopId");
        String shopName = (String)map.get("shopName");
        String shopFullName = (String) map.get("shopFullName");
        return new ShopOpenMicroInfo(shopId, shopName, shopFullName);
    }
}
