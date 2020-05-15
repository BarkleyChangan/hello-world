#!/bin/bash
# 遇到不存在的变量就会报错并停止执行
set -u

work_path=`pwd`

# 如果目录部位空,才能执行删除
if [ ${work_path} != ""];then
    rm -rf ${work_path}/*
fi