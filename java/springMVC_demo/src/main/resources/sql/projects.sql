drop table user;

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) DEFAULT NULL COMMENT '名',
  `last_name` varchar(255) DEFAULT NULL COMMENT '姓',
  `age` INT DEFAULT 0 COMMENT 'age',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户表';
