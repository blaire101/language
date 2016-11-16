package com.x.demo.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.x.demo.dao.ProductAnaInfoDAO;
import com.x.demo.dao.ShopAnaInfoDAO;
import com.x.demo.model.*;
import com.x.demo.util.CommonFunction;
import com.x.demo.util.Constant;
import com.x.demo.util.EsHandler;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 * Date : 2016-06-13
 */

public class MicroPointAnaService {

    public static MicroPointAnaService instance = new MicroPointAnaService();

    public static MicroPointAnaService getInstance() {
        return instance;
    }

    private static Logger logger = LoggerFactory.getLogger(MicroPointAnaService.class);


    @Resource
    private ShopAnaInfoDAO shopAnaInfoDAO;

    @Resource
    private ProductAnaInfoDAO productAnaInfoDAO;

    private void addShopAnaInfoList(List<ShopAnaInfo> shopAnaInfos) {
        if (checkListNotNullAndEmpty(shopAnaInfos)) return;
        try {
            this.shopAnaInfoDAO.addShopAnaInfoList(shopAnaInfos);
        } catch (Exception e) {
            logger.error("dao shopAnaInfoDAO insert mysql exist Exception {}", e);
        }
    }

    private void addProductAnaInfoList(List<ProductAnaInfo> productAnaInfos) {
        if (checkListNotNullAndEmpty(productAnaInfos)) return;
        try {
            this.productAnaInfoDAO.addProductAnaInfoList(productAnaInfos);
        } catch (Exception e) {
            logger.error("dao productAnaInfoDAO insert mysql exist Exception {}", e);
        }
    }

    private boolean checkListNotNullAndEmpty(Collection shopAnaInfos) {
        if (null == shopAnaInfos || shopAnaInfos.isEmpty()) {
            return true;
        }
        return false;
    }

    public void run() {
        List<ShopAnaInfo> shopAnaInfos = Lists.newArrayList();

        /** first : get all shopInfo for Open Micro **/
        List<ShopOpenMicroInfo> shopOpenMicroInfoList = ShopEnableMicroMallService.getInstance().getShopOpenMicroInfoList();
        logger.info("open micromall's shop size is {}", shopOpenMicroInfoList.size());

        // online
        String yesCreateDt = CommonFunction.getStringDate(new Date(System.currentTimeMillis() - Constant.DAY_MS));
        // test
//        yesCreateDt= "20160612"; // for test
        // 1000953, 1000100 for test
//        shopOpenMicroInfoList.clear();
//        shopOpenMicroInfoList.add(new ShopOpenMicroInfo(1000953L, "朱佑独立商户测试8", "朱佑独立商户测试8"));
//        shopOpenMicroInfoList.add(new ShopOpenMicroInfo(1000100L, "XX商户测试", "XX商户测试full"));


        for (ShopOpenMicroInfo shopOpenMicroInfo : shopOpenMicroInfoList) {

            /** 1. construct condition **/
            Map<String, Object> conditionMap = getStringObjectMap(yesCreateDt, shopOpenMicroInfo);

            /** 2. search document by condition **/
            SearchHit[] searchHits = getSearchHits(conditionMap);

            /** 3. searchHits convert to model(object) list --> MicroPointInfo **/
            List<MicroPointInfo> microPointInfoList = getMicroPointInfoList(searchHits);

            shopAnaInfos = getShopAnaInfoList(shopAnaInfos, shopOpenMicroInfo, yesCreateDt, microPointInfoList);

            // insert productAnaInfos
            List<ProductAnaInfo> productAnaInfos = getProductAnaInfoList(microPointInfoList, shopOpenMicroInfo, yesCreateDt);
            this.addProductAnaInfoList(productAnaInfos);
            logger.info("CreateDt : {}, shopId : {}, shopName : {}, productCount : {}", yesCreateDt, shopOpenMicroInfo.getShopId(), shopOpenMicroInfo.getShopName(), productAnaInfos.size());
        }

        // insert shopAnaInfos
        this.addShopAnaInfoList(shopAnaInfos);
        logger.info("CreateDt : {}, shopCount : {}", yesCreateDt, shopAnaInfos.size());
        return;
    }

    private Map<String, Object> getStringObjectMap(String yesCreateDt, ShopOpenMicroInfo shopOpenMicroInfo) {
        Map<String, Object> conditionMap = Maps.newHashMap();

        conditionMap.put(Constant.SHOP_ID, shopOpenMicroInfo.getShopId()); // 1000560
        conditionMap.put(Constant.CREATE_DT, yesCreateDt);
        return conditionMap;
    }


    private SearchHit[] getSearchHits(Map<String, Object> conditionMap) {
        SearchHit[] searchHits = null;

        String _index = ResourceBundle.getBundle("props.es_config").getString("micro_point_index");
        String _type = ResourceBundle.getBundle("props.es_config").getString("micro_point_type");
        String maxEsSearchCntStr = ResourceBundle.getBundle("props.es_config").getString("MaxEsSearchCnt");

        int maxEsSearchCnt = Integer.parseInt(maxEsSearchCntStr);

        try {
            searchHits = EsHandler.getInstance().getElasticSearchHandler().searcherMoreCondition(getQueryBuild(conditionMap), _index, _type, maxEsSearchCnt);
        } catch (Exception e) {
            logger.error("ElasticSearchHandler is Exception {} conditionMap : {} ", e, conditionMap);
        }
        if (0 == searchHits.length) {
            logger.info("searchHits.length is 0, {}", conditionMap);
        }
        return searchHits;
    }

    private QueryBuilder getQueryBuild(Map<String, Object> conditionMap) {

        BoolQueryBuilder boolQueryBuilder = boolQuery();

        for (String key : conditionMap.keySet()) {
            boolQueryBuilder = boolQueryBuilder.must(termQuery(key, conditionMap.get(key)));
        }
        return boolQueryBuilder;
    }

    private List<MicroPointInfo> getMicroPointInfoList(SearchHit[] searchHits) {
        List<MicroPointInfo> microPointInfoList = Lists.newArrayList(); // init value is empty (not null), this is important
        for (SearchHit hit : searchHits) {
            Map<String, Object> map = hit.getSource();
            MicroPointInfo microPointInfo = null;
            try {
                microPointInfo = CommonFunction.mapToMicroPointInfo(map);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("map transTo MicroPointInfo error!  map : {} ", map);
            }
            microPointInfoList.add(microPointInfo);
        }
        return microPointInfoList;
    }

    /** ----------------- get ShopAnaInfos by MicroPointInfos for insert mysql -------------------- **/

    private List<ShopAnaInfo> getShopAnaInfoList(List<ShopAnaInfo> shopAnaInfoList, ShopOpenMicroInfo shopOpenMicroInfo, String createDt, List<MicroPointInfo> microPointInfoList) {
        Preconditions.checkArgument(null != microPointInfoList);

        Long shopId = shopOpenMicroInfo.getShopId();
        String shopName = shopOpenMicroInfo.getShopName();
        String shopFullName = shopOpenMicroInfo.getShopFullName();

        Set<String> sessIdsets = Sets.newHashSet();

        for (MicroPointInfo microPointInfo : microPointInfoList) {
            if (null == microPointInfo) {
                continue;
            }
            String sessionId = microPointInfo.getSessionId();
            if (StringUtils.isEmpty(sessionId)) {
                logger.error("exist sessionId is null or empty, {}", microPointInfo.toString());
            }
            sessIdsets.add(sessionId);
        }
        Long shopPv = Long.parseLong(String.valueOf(microPointInfoList.size()));
        Long shopUv = Long.parseLong(String.valueOf(sessIdsets.size()));
        Date statDate = null;
        try {
            statDate = Constant.SDF.parse(createDt);
        } catch (ParseException e) {
            logger.error("parse date ParseException occurred {}");
        }
        shopAnaInfoList.add(new ShopAnaInfo(shopId, shopName, shopFullName, shopPv, shopUv,statDate, new Date()));
        return shopAnaInfoList;
    }

    /** ----------------- get every ProductAnaInfos by MicroPointInfos for insert mysql -------------------- **/

    private List<ProductAnaInfo> getProductAnaInfoList(List<MicroPointInfo> microPointInfoList, ShopOpenMicroInfo shopOpenMicroInfo, String createDt) {

        List<ProductAnaInfo> productAnaInfos = Lists.newArrayList();

        Long shopId = shopOpenMicroInfo.getShopId();

        Map<Long, TmpProductAnaInfo> longListMap = getTmpProductAnalongListMap(microPointInfoList);

        for (Map.Entry<Long, TmpProductAnaInfo> entry : longListMap.entrySet()) {

            Long productId = entry.getKey();
            TmpProductAnaInfo tmpProductAnaInfo = entry.getValue();

            String productName = tmpProductAnaInfo.getProductName();
            Long productPv = tmpProductAnaInfo.getProductPv();
            Long productUv = Long.parseLong(String.valueOf(tmpProductAnaInfo.getSessionIdSet().size()));
            Date statDate = null;
            try {
                statDate = Constant.SDF.parse(createDt);
            } catch (ParseException e) {
                logger.error("parse createDt {} ParseException {} ", createDt, e);
            }

            productAnaInfos.add(new ProductAnaInfo(shopId, productId, productName, productPv, productUv, statDate, new Date()));
        }
        return productAnaInfos;
    }

    private Map<Long, TmpProductAnaInfo>  getTmpProductAnalongListMap(List<MicroPointInfo> microPointInfoList) {
        Map<Long, TmpProductAnaInfo> longListMap = Maps.newHashMap();

        for (MicroPointInfo microPointInfo : microPointInfoList) {
            if (null == microPointInfo || null == microPointInfo.getProductId()) {
                continue;
            }
            Long productId = microPointInfo.getProductId();
            String productName = microPointInfo.getProductName();
            String sessionId = microPointInfo.getSessionId();

            if (longListMap.isEmpty() || null == longListMap.get(productId)) {
                Set<String> sessSet = Sets.newHashSet();
                sessSet.add(sessionId);
                longListMap.put(productId, new TmpProductAnaInfo(productName, Constant.LONG_ONE, sessSet));
            } else {
                TmpProductAnaInfo tmpProductAnaInfo = longListMap.get(productId);
                Set<String> sessionIdSet = tmpProductAnaInfo.getSessionIdSet();

                sessionIdSet.add(sessionId);

                tmpProductAnaInfo.setProductPv(tmpProductAnaInfo.getProductPv() + Constant.LONG_ONE);
                tmpProductAnaInfo.setSessionIdSet(tmpProductAnaInfo.getSessionIdSet());

                longListMap.put(productId, tmpProductAnaInfo);
            }
        }
        return longListMap;
    }

    public static void main(String[] args) {
        MicroPointAnaService.getInstance().run();
    }

}
