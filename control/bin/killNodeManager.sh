#!/bin/bash

ps -ef |grep nodem
pid_list=`ps aux|grep nodem|grep -v "weblogic.Name"|awk '{print $2}'`

for pid in $pid_list
do
  ps ${pid}
  echo "Killing pid=${pid}"
  kill -9 $pid
done