create database spring_test;
use spring_test;

create table if not exists user
(
	id int unsigned not null primary key auto_increment comment '主键',
	username varchar(32) not null default 'dd'  comment '用户名',
	password varchar(32) not null default 'pass' comment '密码',
	age varchar(32) not null default 10 comment '年龄',
	address varchar(32) not null default 'beijing' comment '地址'
) engine=innodb default charset=utf8 comment='用户表';
----
insert into user values(null, 'lilei', md5('lilei'), 10, '北京');
insert into user values(null, 'hanmei', md5('hanmei'), 11, '上海');

