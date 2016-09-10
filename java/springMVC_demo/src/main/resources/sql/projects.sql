drop table monitor_shop;


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
