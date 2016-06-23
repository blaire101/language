CREATE TABLE `dw_micro_shop_analysis_d` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `shop_id` bigint(20) NOT NULL COMMENT '商户id',
  `shop_name` varchar(255) DEFAULT NULL COMMENT '商户名字',
  `shop_full_name` varchar(255) DEFAULT NULL COMMENT '商户全称',
  `shop_pv` bigint(20) NOT NULL COMMENT 'pv',
  `shop_uv` bigint(20) NOT NULL COMMENT 'uv',
  `stat_date` date DEFAULT NULL COMMENT '统计日期',
  `create_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=114062 DEFAULT CHARSET=utf8 COMMENT='商户分析表';

CREATE TABLE `dw_micro_shop_product_analysis_d` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `shop_id` bigint(20) NOT NULL COMMENT '商户id',
  `product_id` bigint(20) NOT NULL COMMENT '商品id',
  `product_name` varchar(255) DEFAULT NULL COMMENT '商品名字',
  `product_pv` bigint(20) NOT NULL COMMENT 'pv',
  `product_uv` bigint(20) NOT NULL COMMENT 'uv',
  `stat_date` date DEFAULT NULL COMMENT '统计日期',
  `create_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=114062 DEFAULT CHARSET=utf8 COMMENT='商户商品分析表';
