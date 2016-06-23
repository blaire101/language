CREATE TABLE `admin` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `lib_id` int(10) unsigned NOT NULL COMMENT '数据库id',
  `user_rtx` varchar(32) NOT NULL COMMENT '用户rtx名称',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '管理员状态',
  `is_super` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '超级管理员状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_user_rtx` (`user_rtx`)
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8 COMMENT='管理员信息表' ;

CREATE TABLE `book` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `book_id` varchar(32) NOT NULL COMMENT '书籍的编号10位',
  `book_info_id` int(10) unsigned NOT NULL COMMENT'书籍信息表相应的编号',
  `status` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '表示图书的状态 0未借，1借出，2删除',
  `lib_id` int(10) unsigned NOT NULL COMMENT '所属图书馆id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `book_id` (`book_id`),
  KEY `ind_lib_id_hot` (`lib_id`)
) ENGINE=InnoDB AUTO_INCREMENT=278 DEFAULT CHARSET=utf8 COMMENT '图书表';

CREATE TABLE `book_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '书籍信息id',
  `book_name` varchar(64) NOT NULL COMMENT '书籍名称',
  `author` varchar(64) NOT NULL COMMENT '作者名称',
  `press` varchar(64) NOT NULL COMMENT '出版社',
  `intro` varchar(1024) NOT NULL COMMENT '书籍简介',
  `image_url` varchar(128) NOT NULL COMMENT '书图片的地址',
  `hot` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '热度',
  `nav_id` int(10) unsigned NOT NULL COMMENT ' navigation表的id,表示分类',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_book_name_author_nav_id` (`book_name`,`author`,`nav_id`),
  KEY `ind_lib_name_hot` (`book_name`,`hot`)
) ENGINE=InnoDB AUTO_INCREMENT=206 DEFAULT CHARSET=utf8 COMMENT='书籍信息表'


CREATE TABLE `borrowed` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `book_id` varchar(32) NOT NULL COMMENT '书的条码',
  `user_rtx` varchar(16) NOT NULL COMMENT '借阅者,rtx的登陆名',
  `borrow_date` date NOT NULL COMMENT '借阅日期',
  `return_date` date NOT NULL COMMENT '还书日期',
  `redecorate_num` tinyint(4) NOT NULL DEFAULT '2' COMMENT '还可以续借的次数',
  `is_return` tinyint(4) NOT NULL COMMENT '是否已经归还',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=279 DEFAULT CHARSET=utf8 COMMENT='图书借阅i表'


CREATE TABLE `library` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `lib_name` varchar(64) NOT NULL COMMENT '图书馆名字',
  `lib_dept` varchar(256) NOT NULL COMMENT '员工所属部门',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '图书馆创建时间',
  `status` tinyint(3) unsigned NOT NULL COMMENT '图书馆状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8 COMMENT='图书馆表'

CREATE TABLE `log` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `operation_type` tinyint(3) unsigned NOT NULL COMMENT '日志类型，0:借书，1：还书，2：增书，3：删书，4：系统设置，5：其他',
  `create_time` datetime NOT NULL COMMENT '日志生成时间',
  `operator_rtx` varchar(32) NOT NULL COMMENT '操作管理员rtx',
  `lib_id` int(10) unsigned NOT NULL COMMENT '操作的图书馆id',
  `info` varchar(512) NOT NULL COMMENT '日志信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1385 DEFAULT CHARSET=utf8 COMMENT='日志信息表'


CREATE TABLE `navigation` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(64) NOT NULL COMMENT '图书的大分类',
  `book_type` varchar(32) NOT NULL COMMENT '图书的小分类',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_title_book_type` (`title`,`book_type`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8 COMMENT='图书分类表'

 CREATE TABLE `reserve` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `book_info_id` int(10) unsigned NOT NULL COMMENT '书的信息ID',
  `user_rtx` varchar(32) NOT NULL COMMENT '借阅者，rtx的登陆名',
  `status` tinyint(3) unsigned NOT NULL COMMENT '状态，表示预约是否已经被系统处>理,0表示没有处理，1表示已经被处理',
  `lib_id` int(10) unsigned NOT NULL COMMENT '图书馆id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8 COMMENT='图书借阅表'


CREATE TABLE `system_param` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `borrow_total_num` int(10) unsigned NOT NULL DEFAULT '2' COMMENT '可借阅的本数',
  `borrow_period` int(10) unsigned NOT NULL DEFAULT '30' COMMENT '可借阅的天数',
  `redecorate_num` int(10) unsigned NOT NULL DEFAULT '1' COMMENT '可续借的次数',
  `redecorate_period` int(10) unsigned NOT NULL DEFAULT '15' COMMENT '续借一次的天数限制',
  `remind_day` int(10) unsigned NOT NULL DEFAULT '5' COMMENT '邮件提醒催还书籍的天数',
  `lib_id` int(10) unsigned NOT NULL COMMENT '所属图书馆ID',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '标识是否有效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COMMENT='系统参数设置表'


 CREATE TABLE `user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_rtx` varchar(32) NOT NULL COMMENT '用户RTX',
  `borrow_num` int(11) NOT NULL DEFAULT '0' COMMENT '读者借阅数量',
  `remain_brow_num` int(11) NOT NULL DEFAULT '2' COMMENT '还可以续借的次数',
  `lib_id` int(10) unsigned NOT NULL DEFAULT '1' COMMENT '图书馆id',
  `is_valid` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0代表用户无效，1代表用户有效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15835 DEFAULT CHARSET=utf8 COMMENT='已经登陆过的用户表'

CREATE TABLE `user_login` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_rtx` varchar(32) NOT NULL COMMENT '用户RTX',
  `token` varchar(64) DEFAULT '0' COMMENT '用户cookie',
  `last_login_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '用户最后登录时间',
  `user_dept` varchar(128) NOT NULL DEFAULT '' COMMENT '员工部门',
  `user_name` varchar(32) NOT NULL DEFAULT '' COMMENT '员工名字',
  PRIMARY KEY (`id`)
  UNIQUE KEY `uniq_user_rtx` (`user_rtx`)
) ENGINE=InnoDB AUTO_INCREMENT=12844 DEFAULT CHARSET=utf8 COMMENT='员工登录表'




索引：
library 表：
	alter table library add index idx_lib_id(id);
	alter table library add index idx_lib_name(lib_name(8));

log 表：
	alter table log add index idx_operation_type_create_time_lib_id(operation_type,create_time,lib_id);

admin 表：
	alter table admin add index idx_lib_id(lib_id);
	alter table admin add unique idx_user_rtx(user_rtx(8));
user_login 表：
	alter table user_login add unique uniq_user_rtx(user_rtx(8));
	alter table user_login add index idx_token(token(8));
book 表：
	alter table book add index idx_lib_id (lib_id);
	alter table book add unique uniq_book_id(book_id(8));
	CREATE INDEX idx_book_info_id ON book(book_info_id);

book_info 表：
	alter table book_info add index idx_lib_name_hot (book_name(8), hot);
	alter table book_info add index idx_book_name_author(book_name(8),author(6));
	CREATE INDEX idx_nav_id ON book_info(nav_id);
navigation 表：
	alter table navigation add unique uniq_title_book_type(title(8),book_type(8));

borrowed 表
	alter table borrowed add index idx_book_id(book_id(8));
	CREATE INDEX idx_user_rtx ON borrowed ( user_rtx);

system_param 表：
	CREATE UNIQUE INDEX uniq_lib_id   ON system_param(lib_id);
user 表：
	CREATE  INDEX idx_user_rtx_borrow_num ON user(user_rtx(8),borrow_num);
	CREATE        INDEX idx_lib_id               ON user(lib_id);
reserve 表：
	CREATE        INDEX idx_book_info_id         ON reserve(book_info_id);
	CREATE        INDEX idx_lib_id               ON reserve(lib_id);
	CREATE        INDEX idx_user_rtx             ON reserve(user_rtx(8));