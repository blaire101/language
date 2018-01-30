# Automation Programmed pull data

date |version| need report | dev | describe
--------|-------|--------|-------|-------
2017-01-20 | 1.0 | someone | Libin Chan | mysql -> pg ods table

## Preface

```bash
运行本模块之前 :

cd ext_ods_table
mkdir log flag
mkdir -p log/crontab
mkdir data

构造 datax json file， put it to **data** 目录
```

## 1. Needs

&nbsp;&nbsp;&nbsp;&nbsp;程序自动化拉取数据进入原始层 ODS
 
## 2. Data Flow

![ods flow][0]

> 具体请以实际线上代码 为准

## 3. Involved technology

1. [Shell][1]
2. [Datax][2]
3. [PostgreSQL][3]
4. [Greenplum][4]
5. [Pentaho kettle][6]

> [gpfdist protocol][5]

## 4. Main Code Flow

(1). linux crontab

```bash
*/5 00-19 * * * source /etc/bashrc; sh /data0/dm/online/ext_ods_table/crontab_job/crontab_job_ods_shop.sh
```

> source /etc/bashrc; 环境设置

(2). crontab\_job\_ods\_shop.sh

```bash
...
sh ods_dm_shop.sh ${d1}
...
```

(3). ods\_dm\_shop.sh

```bash
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

## 5. Code Deploy

> /data0/dm/online/ext_ods_table  
>
> 注意： 版本控制中，涉及的敏感 库地址，用户名，密码 等 不上传。

## 6. Frnd Link

1. [pull data detail flow][11]
2. [scheduling-framework][12]

[0]: /greenplum/ext_ods_table/doc/greenplum-ods-flow.png
[1]: https://zh.wikipedia.org/zh-hans/Unix_shell
[2]: https://github.com/alibaba/DataX/
[3]: https://en.wikipedia.org/wiki/PostgreSQL
[4]: http://dbaplus.cn/news-21-341-1.html
[5]: http://www.greenplumdba.com/gpfdist
[6]: http://www.pentaho.com/
[11]: /greenplum/ext_ods_table/doc/README.md
[12]: /greenplum/ext_ods_table/doc/scheduling-framework.md
