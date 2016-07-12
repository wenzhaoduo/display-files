#!/bin/sh

#main function
CURRENT_DIR=`pwd -P`
SCRIPT_DIR=`cd $(dirname $0); pwd -P`

source $SCRIPT_DIR/clean_config.sh

#清理各种日志文件

if [ -n "$LOG_DIR_BASE" ]
then
	cd ${LOG_DIR_BASE}
	echo "Deleteing old app log..."
	find . -regextype posix-extended -regex '.*\.log\.(([0-9]{8})|([0-9]{10}))(\.[0-9]+)*(\.gz)?' -type f -mtime +${DELETE_DAY_BEYOND} -exec rm {} \;
	find . -regextype posix-extended -regex '.*\.log\.wf\.(([0-9]{8})|([0-9]{10}))(\.[0-9]+)*(\.gz)?' -type f -mtime +${DELETE_DAY_BEYOND} -exec rm {} \;
	find . -regextype posix-extended -regex '.*\.log\.[0-9]{4}(-[0-9]{2}){2,3}(\.gz)?' -type f -mtime +${DELETE_DAY_BEYOND} -exec rm {} \;
	find . -regextype posix-extended -regex '.*\.log\.wf\.[0-9]{4}(-[0-9]{2}){2,3}(\.gz)?' -type f -mtime +${DELETE_DAY_BEYOND} -exec rm {} \;
	echo "Compressing old app log..."
        find . -regextype posix-extended -regex '.*\.log\.(([0-9]{8})|([0-9]{10}))(\.[0-9]+)*' -type f -mmin +${COMPRESS_MINUTE_BEYOND} -mtime -${DELETE_DAY_BEYOND} -exec gzip -f {} \;
        find . -regextype posix-extended -regex '.*\.log\.wf\.(([0-9]{8})|([0-9]{10}))(\.[0-9]+)*' -type f -mmin +${COMPRESS_MINUTE_BEYOND} -mtime -${DELETE_DAY_BEYOND} -exec gzip -f {} \;
        find . -regextype posix-extended -regex '.*\.log\.[0-9]{4}(-[0-9]{2}){2,3}' -type f -mmin +${COMPRESS_MINUTE_BEYOND} -mtime -${DELETE_DAY_BEYOND} -exec gzip -f {} \;
        find . -regextype posix-extended -regex '.*\.log\.wf\.[0-9]{4}(-[0-9]{2}){2,3}' -type f -mmin +${COMPRESS_MINUTE_BEYOND} -mtime -${DELETE_DAY_BEYOND} -exec gzip -f {} \;
	cd ${CURRENT_DIR}
fi

