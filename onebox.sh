#!/bin/bash

#ORIG_DIR=`pwd -P`
#SCRIPT_DIR=`cd $(dirname $0); pwd -P`
#cd $SCRIPT_DIR

#
#oneboxhost=10.237.93.22
#mvn -U clean package #-Dstaging=true
mvn -U clean package

echo "start scp"
scp -qr target/log-monitor-1.0.0-SNAPSHOT.war root@oneboxhost:/opt/resin/webapps/log-monitor.war
echo "done"
