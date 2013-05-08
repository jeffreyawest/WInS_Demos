#!/bin/bash

pid_list=`ps aux|grep "weblogic.Name"|awk '{print $2}'`

for pid in $pid_list
do
  echo "Killing WebLogic pid=${pid}"
  kill -9 $pid
done