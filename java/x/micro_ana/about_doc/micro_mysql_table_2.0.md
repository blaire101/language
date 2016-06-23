# micro_mysql_table

===

> stat_date 改为 date 类型， 去掉一些不必要的字段

## 1. 表设计

### dw\_micro\_shop\_analysis\_d

**商户分析表**

字段 | 类型 | 示例
------- | ------- | -------
id | bigint(20) | 1
shop\_id | bigint(20) | 1231
shop\_name | varchar(255) | -
shop\_full\_name | varchar(255) | -
shop_pv | bigint(20) | 190000
shop_uv | bigint(20) | 5000
stat_date | date | -
create_date | timestamp | -

### dw\_micro\_shop\_product\_analysis\_d

**商品分析表**

字段 | 类型 | 示例
------- | ------- | -------
id | bigint(20) | 1
shop\_id | bigint(20) | 123109
product\_id | bigint(20) | 6328126
product\_name | varchar(255) | -
product_pv | bigint(20) | 10
product_uv | bigint(20) | 5
stat_date | date | 00:00:00
create_date | timestamp | 00:00:00


## 2. 建表语句

===

### 2.1 商户分析表

```sql
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
```

> 以上建表语句已经验证，没有问题

**建立索引**

```
ALTER TABLE `dw_micro_shop_analysis_d` ADD INDEX shop_analysis_stat_date (`stat_date`)

ALTER TABLE `dw_micro_shop_analysis_d` ADD INDEX shop_analysis_shop_id (`shop_id`)

ALTER TABLE `dw_micro_shop_analysis_d` ADD INDEX shop_analysis_stat_date_shop_id (`stat_date`, `shop_id`)
```

### 2.2 商品分析表

```sql
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
```

> 以上建表语句已经验证，没有问题

**建立索引**

```
ALTER TABLE `dw_micro_shop_product_analysis_d` ADD INDEX shop_product_analysis_stat_date (`stat_date`)

ALTER TABLE `dw_micro_shop_product_analysis_d` ADD INDEX shop_product_analysis_shop_id (`shop_id`)

ALTER TABLE `dw_micro_shop_product_analysis_d` ADD INDEX shop_product_analysis_stat_date_shop_id (`stat_date`, `shop_id`)
```
