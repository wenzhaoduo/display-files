#!/bin/sh

set -e
set -x

JOB_ENV=$1
CLUSTER=`python ./deploy/find_cluster.py ${JOB_ENV}`
echo $CLUSTER

SCRIPT_DIR=`cd $(dirname $0); pwd -P`
cd $SCRIPT_DIR

#install resin to $SCRIPT_DIR
export SCRIPT_DIR
python ./deploy/installer.py ./deploy/resin-installer.yaml

source /home/work/build_script/env/java/jdk_1.7.env
mvn -U clean package -D${CLUSTER}=true -Dlog-monitor=true

mkdir -p release/webapp/
cp -r target/log-monitor-1.0.0-SNAPSHOT/* release/webapp/
cp -r deploy/ release/
cp -r libexec/ release/

mvn clean

#build jmxmonitor
pushd jmxmonitor
sh build.sh
popd

cp -r jmxmonitor/release/libexec release/

