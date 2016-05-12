create table if not exists user
(
	id int unsigned not null primary key auto_increment,
	username varchar(32) not null default 'pass',
	password varchar(32) not null default 'pass',
	age varchar(32) not null default 10,
	address varchar(32) not null default 'beijing'
) engine=innodb default charset=utf8;

INSERT INTO users VALUES (1,'lilei','3752a328cf125dc589f01a1c1ff9e322','10','北京'),
(2,'hanmei','b1ec58dadaadf8dc078cc6f7d6a52a00','11','上海'),
