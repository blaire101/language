package com.x.demo.util;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/** Common Function **/
public class CF {

    private static Logger logger = LoggerFactory.getLogger(CF.class);

    public static String getStrYMDDateByTodayAddDays(int cnt) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FOMMAT_YMD);
        Date yesterday = DateUtils.addDays(new Date(), cnt);
        String strDate = sdf.format(yesterday);
        return strDate;
    }

}
