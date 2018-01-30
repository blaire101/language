# pull data detail flow

date |version| need report | dev | describe
--------|-------|--------|-------|-------
2017-01-20 | 1.0 | someone | Libin Chan | mysql -> pg ods table

> 以 shop table 为例，说明详细流程, shop 为全量导入
> 
> orders 则为增量导入，ods_orders.kjb 不清空表

## 1. datax\_home and kettle

```bash
/data0/dm/datax_home
```

```bash
/data0/dm/data-integration
```

## 2. startup gpfdist

```bash
gpfdist -d /data0/dm/datax_home/myextdata -p 8900 > /tmp/gpfdist.log 2>&1 &
``` 
> ps -ef | grep gpf

```bash
[hdfs@node190 gpextdata]$ pwd
/data0/dm/datax_home/myextdata/gpextdata
[hdfs@node190 gpextdata]$ ll
total 4
drwxrwxr-x 2 hdfs hdfs 4096 Jan 19 17:51 your_company_name_com.ext
drwxrwxr-x 2 hdfs hdfs   59 Jan 11 14:57 your_company_name_member.ext
```

## 3. conf/default.conf

配置 datax、kettle、gpextdata 路径等，方便 调度框架 调用

```bash
#user
user="hdfs"

#kettle
data_integration="/data0/dm/data-integration"

#datax
datax_home="/data0/dm/datax_home"

#gpfdist
gpextdata="/data0/dm/datax_home/myextdata/gpextdata"
```

## 4. create ext table

```
DROP EXTERNAL TABLE ext.ext_dm_shop;
CREATE EXTERNAL TABLE ext.ext_dm_shop (
    id bigint,
    category_id bigint,
    region_id bigint,
    cityCode varchar(6),
    merchant_id bigint,
    balance_id bigint, 
    balance decimal(12,2),
    address varchar(255),
    contact varchar(255),
    domain varchar(255),
    shopHours varchar(500),
    banner_id bigint,
    avatar_id bigint,
    name varchar(255),
    fullName varchar(255),
    visible boolean,
    brand_id bigint,
    wechat varchar(255),
    weibo varchar(255),
    stars double precision,
    tag varchar(255),
    position_id bigint,
    printer_enable boolean,
    printer_ip varchar(100),
    printer_port varchar(50),
    enable_shift smallint,
    visible_shift_receivable_data smallint, 
    enable_multiple_payment smallint,
    statisticsDate varchar(50),
    createDate timestamp,
    modifyDate timestamp,
    deleted boolean,
    need_sy boolean,
    sy_start_time timestamp,
    sy_end_time timestamp,
    slogans varchar,
    distribution varchar,
    signboards_id bigint
) LOCATION (
    'gpfdist://192.168.xx.xx:8900/gpextdata/your_company_name_com.ext/shop.txt*'
) format 'text' (DELIMITER '\t' NULL AS 'null' escape 'OFF')
Encoding 'UTF-8' Log errors into ext.ext_dm_shop_err segment reject limit 10 rows;

SELECT * FROM ext.ext_dm_shop limit 20;
```

## 5. datax import data

### 5.1 mysql2textfile-shop.json

```
{
    "job": {
        "content": [
        {
            "reader": {
                "name": "mysqlreader",
                    "parameter": {
                        "username": "$username",
                        "password": "$password",
                        "connection": [
                        {
                            "jdbcUrl": [
                                "$jdbcUrl"
                                ],
                            "table": [
                                "your_company_name_com.shop"
                                ]
                        }
                        ],
                        "column":[
                            "id",
                            "category_id",
                            "region_id",
                            "cityCode",
                            "merchant_id",
                            "balance_id",
                            "balance",
                            "REPLACE(address, '\t', '')",
                            "contact",
                            "domain",
                            "shopHours",
                            "banner_id",
                            "avatar_id",
                            "REPLACE(name, '\t', '')",
                            "REPLACE(fullName, '\t', '')",
                            "visible",
                            "brand_id",
                            "wechat",
                            "weibo",
                            "stars",
                            "tag",
                            "position_id",
                            "printer_enable",
                            "printer_ip",
                            "printer_port",
                            "enable_shift",
                            "visible_shift_receivable_data",
                            "enable_multiple_payment",
                            "statisticsDate",
                            "createDate",
                            "modifyDate",
                            "deleted",
                            "need_sy",
                            "sy_start_time",
                            "sy_end_time",
                            "slogans",
                            "'distribution' as distribution",
                            "signboards_id"
                        ],
                        "where": "createDate BETWEEN '$begin_time 00:00:00' AND '$end_time 23:59:59'"
                    }
            },
                "writer": {
                    "name": "txtfilewriter",
                    "parameter": {
                        "path": "/$gpextdata/your_company_name_com.ext",
                        "fileName": "shop.txt",
                        "writeMode": "truncate",
                        "dateFormat": "yyyy-MM-dd",
                        "fieldDelimiter": '\t'
                    }
                }
        }
        ],
        "setting": {
            "speed": {
                "channel": 1
            }
        }
    }
}
```

### 5.2 json file path

```bash
[hdfs@node190 your_company_name_com.ext]$ pwd
/data0/dm/online/ext_ods_table/data/your_company_name_com.ext
[hdfs@node190 your_company_name_com.ext]$ ll
total 16
-rwxr-xr-x 1 hdfs hdfs 2849 Jan 19 16:51 mysql2textfile-orders.json
-rwxr-xr-x 1 hdfs hdfs 3010 Jan 19 10:26 mysql2textfile-shop.json
[hdfs@node190 your_company_name_com.ext]$
```

### 5.3 run import data by datax.py

```
python ${datax_home}/bin/datax.py -p "-Dbegin_time='2010-01-01' -Dend_time='${d1}' -Dgpextdata='${gpextdata}'" ${data_dir}/your_company_name_com.ext/mysql2textfile-shop.json
```

## 6. create ods table

ods.ods\_dm\_shop

```sql
DROP TABLE ods.ods_dm_shop;

CREATE TABLE "ods"."ods_dm_shop" (
    id bigint PRIMARY KEY,
    category_id bigint,
    region_id bigint,
    cityCode varchar(6),
    merchant_id bigint,
    balance_id bigint, 
    balance decimal(12,2),
    address varchar(255),
    contact varchar(255),
    domain varchar(255),
    shopHours varchar(500),
    banner_id bigint,
    avatar_id bigint,
    name varchar(255),
    fullName varchar(255),
    visible boolean,
    brand_id bigint,
    wechat varchar(255),
    weibo varchar(255),
    stars double precision,
    tag varchar(255),
    position_id bigint,
    printer_enable boolean,
    printer_ip varchar(100),
    printer_port varchar(50),
    enable_shift smallint,
    visible_shift_receivable_data smallint, 
    enable_multiple_payment smallint,
    statisticsDate varchar(50),
    createDate timestamp,
    modifyDate timestamp,
    deleted boolean,
    need_sy boolean,
    sy_start_time timestamp,
    sy_end_time timestamp,
    slogans varchar,
    distribution varchar,
    signboards_id bigint
)
WITH (OIDS=FALSE);
ALTER TABLE "ods"."ods_dm_shop" OWNER TO "your_company_name_gp";

SELECT count(*) FROM ods.ods_dm_shop limit 20;
```

> 注意 id 指定 PRIMARY KEY 约束

## 7. kettle kjb

### 7.1 kjb location

```bash
[hdfs@node190 ktrs]$ pwd
/data0/dm/online/ext_ods_table/ktrs
[hdfs@node190 ktrs]$ ll
total 32
-rwxr--r-- 1 hdfs hdfs 7455 Jan 19 16:34 ods_e_coupon.kjb
-rwxr--r-- 1 hdfs hdfs 7389 Jan 19 16:34 ods_orders.kjb
-rwxr--r-- 1 hdfs hdfs 7447 Jan 19 16:34 ods_region.kjb
-rwxr--r-- 1 hdfs hdfs 7439 Jan 19 16:33 ods_shop.kjb
[hdfs@node190 ktrs]$
```

### 7.2 kjb content

```sql
TRUNCATE TABLE ods.ods_dm_shop;

INSERT INTO ods.ods_dm_shop SELECT
    *
FROM
    ext.ext_dm_shop;
```

### 7.3 run kjb

```bash
${data_integration}/kitchen.sh -file=${ktrs_dir}/ods_shop.kjb
```

## 8. main code flow

```
###############################################################################
#
# @date:   2017.01.20
# @desc:   mysql data -> ext shop -> ods shop
#
###############################################################################

cd `dirname $0`/.. && wk_dir=`pwd` && cd -
source ${wk_dir}/util/env
source ${util_dir}/my_functions

begin_time="2010-01-01"
end_time=${d1}

import_gpdata_from_rds1 ${begin_time} ${end_time} ${data_dir}/x_com.ext/mysql2textfile-shop.json
check_success

echo_ex "${data_integration}/kitchen.sh -file=${ktrs_dir}/ods_shop.kjb"

${data_integration}/kitchen.sh -file=${ktrs_dir}/ods_shop.kjb
check_success

echo_ex "import shop end"

exit 0
```

[0]: /doc/greenplum-ods-flow.png
[1]: https://zh.wikipedia.org/zh-hans/Unix_shell
[2]: https://github.com/alibaba/DataX/
[3]: https://en.wikipedia.org/wiki/PostgreSQL
[4]: http://dbaplus.cn/news-21-341-1.html
[5]: http://www.greenplumdba.com/gpfdist
[6]: http://www.pentaho.com/
