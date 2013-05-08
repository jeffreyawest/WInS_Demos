#!/bin/bash

pid_list=`ps aux|grep nodem|grep -v "weblogic.Name"|awk '{print $2}'`

for pid in $pid_list
do
  echo "Killing NodeManager pid=${pid}"
  kill -9 $pid
done