drop table monitor_shop;
drop table shop_exception_order;
drop table shop_data_exception_intime;
drop table shop_average_data;

CREATE TABLE `monitor_shop` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `shop_id` bigint(20) NOT NULL COMMENT '商户id',
  `shop_name` varchar(255) DEFAULT NULL COMMENT '商户名字',
  `shop_full_name` varchar(255) DEFAULT NULL COMMENT '商户全称',
  `merchant_id` bigint(20) DEFAULT NULL COMMENT '集团ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_shopid` (`shop_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='监控商户表';

CREATE TABLE `shop_average_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `shop_id` bigint(20) NOT NULL COMMENT '商户id',
  `shop_name` varchar(255) DEFAULT NULL COMMENT '商户名字',
  `shop_full_name` varchar(255) DEFAULT NULL COMMENT '商户全称',
  `order_count` bigint(20) NOT NULL COMMENT '正常订单数量',
  `order_amount` decimal(12,2) NOT NULL COMMENT '正常订单总金额',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_shopid_intime` (`shop_id`, `start_time`, `end_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='商户正常数据表';

CREATE TABLE `shop_exception_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `shop_id` bigint(20) NOT NULL COMMENT '商户id',
  `shop_name` varchar(255) DEFAULT NULL COMMENT '商户名字',
  `shop_full_name` varchar(255) DEFAULT NULL COMMENT '商户全称',
  `except_order_number` varchar(32) DEFAULT NULL COMMENT 'except_order_number',
  `except_type` int(11) NOT NULL COMMENT '异常类型',
  `except_desc` varchar(500) DEFAULT NULL COMMENT '异常描述',
  `trade_time` datetime DEFAULT NULL COMMENT '交易时间',
  `sync_time` datetime DEFAULT NULL COMMENT '同步时间',
  `alert_type` int(11) NOT NULL DEFAULT '0' COMMENT '报警类型 0 未报警 1 报警',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_eo_et` (`except_order_number`,`except_type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='商户异常订单表';


CREATE TABLE `shop_data_exception_intime` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `shop_id` bigint(20) NOT NULL COMMENT '商户id',
  `shop_name` varchar(255) DEFAULT NULL COMMENT '商户名字',
  `shop_full_name` varchar(255) DEFAULT NULL COMMENT '商户全称',
  `except_type` int NOT NULL COMMENT '异常类型',
  `except_desc` varchar(500) DEFAULT NULL COMMENT '异常描述',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_shopid_intime` (`shop_id`, `start_time`, `end_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='异常商户表, 精确到时间段';


CREATE TABLE `alarm_mail_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `shop_id` bigint(20) NOT NULL COMMENT '商户id',
  `shop_name` varchar(255) DEFAULT NULL COMMENT '商户名字',
  `shop_full_name` varchar(255) DEFAULT NULL COMMENT '商户全称',
  `order_number` varchar(32) DEFAULT NULL COMMENT 'order_number',
  `send_type` int NOT NULL COMMENT '发送类型编号',
  `send_desc` varchar(500) DEFAULT NULL COMMENT '发送类型描述, 如 1 具体订单异常，2 商户级别异常',
  `sender` varchar(500) DEFAULT NULL COMMENT '发送者',
  `receivers` varchar(1000) DEFAULT NULL COMMENT '接收者们',
  `send_content` varchar(2000) DEFAULT NULL COMMENT '发送内容',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='报警邮件记录表';

