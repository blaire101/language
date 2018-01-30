#!/bin/bash
###############################################################################
#                                                                             
# @date:   2017.01.19
#                                                                            
############################################################################### 

cd `dirname $0`/.. && wk_dir=`pwd` && cd -
source ${wk_dir}/util/crontab_job_env

log_uri=${log_dir}/${job_name}.log.${d1}
echo "$log_uri"
exec 1>>${log_uri} 2>&1

echo "##################################################"
echo "[INFO] date:${d1}"
echo "[INFO] time:" `date`
echo "[INFO] job_name: ${job_name}"
flag_dir=${flag_dir}/ods_orders

#check crontab_label whether exist
if check_local_crontab_label ${flag_dir} ${d1}
  then
    echo "[INFO] script already run!"
else
  echo "[INFO] check dependention"
  echo "[INFO] script run!"

# generate crontab_label
  touch_local_crontab_label ${flag_dir} ${d1}
#sleep 
  sleep 10
# run main script
  echo "[INFO] start run..."
  
  sh ods_dm_orders.sh ${d1}

  if [ $? -eq 0 ] ; then
    touch_local_all_done ${flag_dir} ${d1}
    alert "$job_name" "successed-$d1" "${log_uri}"
  else
    alert "$job_name" "failure-$d1" "${log_uri}"
  fi
fi
echo_ex "run $0 end!"
