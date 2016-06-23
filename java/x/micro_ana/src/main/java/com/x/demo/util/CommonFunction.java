package com.x.demo.util;

import com.x.demo.model.MicroPointInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Date : 2016-06-08
 */
public class CommonFunction {

    /**
     * Map转换层Bean，使用泛型免去了类型转换的麻烦。但是必须是严格限制的setter函数,不支持别名转化
     * commons-beanutils.jar
     */

    public static MicroPointInfo mapToMicroPointInfo(Map<String, Object> map) {
        MicroPointInfo microPointInfo = null;

        Long shopId = ((Integer)map.get(Constant.SHOP_ID)).longValue();

        String shopName = (String) map.get(Constant.SHOP_NAME);

        String shopFullName = (String) map.get(Constant.SHOP_FULL_NAME);

        String sessionId = (String) map.get(Constant.SESSION_ID);

        Long productId = null;

        if (null != map.get(Constant.PRODUCT_ID)) {
            productId = ((Integer)map.get(Constant.PRODUCT_ID)).longValue();
        }

        String productName = (String) map.get(Constant.PRODUCT_NAME);

        String createDt = (String) map.get(Constant.CREATE_DT);

        microPointInfo = new MicroPointInfo(shopId, shopName, shopFullName, sessionId, productId, productName, createDt);

        return microPointInfo;
    }

    public static String getStringDate(Date date) {
        DateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        String yesStringDt = fmt.format(date);
        return yesStringDt;
    }

    public static void byList(List<MicroPointInfo> list) {
        if (null == list) return;
        for (MicroPointInfo microPointInfo : list) {
            System.out.println(microPointInfo);
        }
    }
}
