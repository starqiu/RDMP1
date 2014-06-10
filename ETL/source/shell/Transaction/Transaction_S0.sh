#!/bin/ksh
rundir=$PWD
cmd=$0
case $cmd in
/*)  pathname=$cmd;;
*)  pathname="$rundir/$cmd";;
esac
echo $(dirname $(dirname $(dirname $(dirname "$pathname")))) | read ETL_PROFILE_PATH
if [ -f "$ETL_PROFILE_PATH/config/env_setup.ini" ]; then
	 . "$ETL_PROFILE_PATH/config/env_setup.ini" 
else
	echo "env_setup.ini not found at $ETL_PROFILE_PATH/config"
	return 1
fi
. "$ETL_SHELL_PATH"/common/etl_shell_common_function.sh

##############################
# Function:
# Checking if the file is exist or not,if all the input files are available,the
# batch will go on,otherwise it will return status "1"
#
# Created By : Eric Li
# Created On : 16 April 2014
#
# Amendment History:
# Amended By     Amended On         Remark
# ---------      ----------         ------------------------------------------
#
##################################################################

etl_s0 'Transaction'
if [ $? -ne 0 ]; then
  return 1
fi

return 0
