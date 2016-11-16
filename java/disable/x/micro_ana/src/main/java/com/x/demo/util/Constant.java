package com.x.demo.util;

import java.text.SimpleDateFormat;

public interface Constant {

    String CREATE_DT = "create_dt";

    String SHOP_ID = "shop_id";

    String SHOP_NAME = "shop_name";

    String SHOP_FULL_NAME = "shop_full_name";

    String SESSION_ID = "session_id";

    String PRODUCT_ID = "product_id";

    String PRODUCT_NAME = "product_name";

    Long DAY_MS = 24*60*60*1000L;

    Long LONG_ONE = 1L;


    SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");
}
