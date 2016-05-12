all: judge_client.cc
	g++ -w -c -I/usr/local/mysql/include/mysql -I/usr/include/mysql judge_client.cc
	g++ -w -o judge_client judge_client.o -L/usr/local/mysql/lib/mysql -L/usr/lib/mysql -lmysqlclient
