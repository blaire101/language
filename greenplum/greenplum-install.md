About greenplum install, please refer to the relevant books or official documents

> About install steps, this isnot all doc 

## 1. 配置 /etc/hosts

vim /etc/hosts

```bash
####  add by libin @ 2016-12-28 start ####
#cat /etc/hosts
#BEGIN_GROUP_CUSTOMER
127.0.0.1   localhost localhost.localdomain
192.168.xx.190   dw-greenplum-1 mdw
192.168.xx.196   dw-greenplum-2 sdw1
192.168.xx.198   dw-greenplum-3 sdw2
#END_GROUP_CUSTOMER
####  add by libin @ 2016-12-28 end ####
```

## 2. 增加 gpadmin group、user 

```bash
groupdel gpadmin
userdel gpadmin

groupadd -g 530 gpadmin
useradd -g 530 -u 530 -m -d /home/gpadmin -s /bin/bash gpadmin

chown -R gpadmin:gpadmin /home/gpadmin/
passwd gpadmin
```

## 3. 建立数据存储位置目录

```bash
mkdir -p  /gp/data
chown -R gpadmin.gpadmin /gp/data
chown -R gpadmin.gpadmin /gp/
```

## 4. Create Role And testdb

```sql
CREATE ROLE "libin" CREATEDB LOGIN PASSWORD 'libin';
GRANT Connect, Temporary, Create ON DATABASE "testdb" TO "libin"
ALTER USER yourusername WITH PASSWORD 'yourpassword';
```
