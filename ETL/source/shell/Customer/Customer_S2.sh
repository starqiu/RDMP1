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
# clear temporary data in DB and load interface files into DB.
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

etl_s2 'Customer'
if [ $? -ne 0 ]; then
  return 1
fi

return 0
