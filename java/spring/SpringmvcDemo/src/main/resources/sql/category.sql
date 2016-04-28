use xkeshi_stat3;
CREATE TABLE `category` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) unsigned NOT NULL COMMENT '父类目ID',
  `name` varchar(10) NOT NULL COMMENT '类目名称',
  `createDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `modifyDate` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `visible` bit(1) DEFAULT NULL COMMENT '是否启用',
  `deleted` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1350 DEFAULT CHARSET=utf8 COMMENT='商户所属的行业类目模板。由后台超级管理员负责维护。';
