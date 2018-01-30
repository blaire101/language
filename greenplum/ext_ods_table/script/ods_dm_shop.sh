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
