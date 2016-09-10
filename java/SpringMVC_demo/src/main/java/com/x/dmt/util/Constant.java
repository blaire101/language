package com.x.dmt.util;

public interface Constant {

    Long DAY_MS = 24*60*60*1000L;

    Long HOUR_MS = 60*60*1000L;

    String DATE_FOMMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss"; // "yyyy-MM-dd HH:mm:ss"

    String DATE_FOMMAT_YMDH = "yyyy-MM-dd HH";

    String DATE_FOMMAT_YMD = "yyyy-MM-dd";

    Long TIME_DIFF = 100000L;

    /** Mail Content ssemble **/

    String SPLIT_LINE = "------------------------------------------------------------------------------------------------------------";

    String MAIL_DEFAULT_SUBJECT = "爱客仕数据异常检测报告";

    String VIP_SHOP_MAIL_SUBJECT = "爱客仕VIP商户数据异常检测报告";

    String ALL_SHOP_MAIL_SUBJECT = "爱客仕ALL商户数据异常检测报告";

    String EXCEPT_SHOP_COUNT_DESC = "异常商户数量 :  ";
    String EXCEPT_SHOP_ID_DESC = "异常商户编号 :  ";
    String CHECK_TIME_RANGE_DESC = "检测时间区间 :  ";

    String SHOP_ID_DESC = "商户编号 :";
    String SHOP_NAME_DESC = "商户名称 : ";

    String ORDER_NUMBER_DESC = "订单编号 : ";
    String EXCEPT_SHOW_DESC = "异常描述 : ";

    // exceptType
    String EXCEPT_TYPE_SHOW_1 = "订单延迟超过1小时";
    String EXCEPT_TYPE_SHOW_2 = "终端系统时间设定异常";
    String EXCEPT_TYPE_SHOW_3 = "应付金额与实付金额不一致";
    String EXCEPT_TYPE_SHOW_4 = "订单状态与流水状态不一致";
    String EXCEPT_TYPE_SHOW_5 = "商品总价与订单实付不一致";

    String MAIL_NOT_NEED_REPLY = "该邮件为系统自动发出，请勿回复，谢谢.";
    String MAIL_FOOTER = "\n\nAll Rights Reserved xkeshi Corp. System (XCS)\nAny problem, Please Contact Xkeshi-Administrator\n";

    /** ---------  ShopSummary ------------ **/
    Integer STATUS_TYPE_WARNING  = 1;
    Integer STATUS_TYPE_ERROR  = 2;

    Integer ALERT_TYPE_NO  = 0;
    Integer ALERT_TYPE_YES  = 1;

    Integer CHECK_TYPE_VIP = 1;
    Integer CHECK_TYPE_ALL = 2;

    /** ---------- ETL ------- **/

    Integer INSERT_ES_SIZE = 500;

    /** -----   **/

    Integer START_ADD_DAYS = 1;

    Integer RECOVERY_ADD_DAYS = 1;
}
