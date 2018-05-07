#!/bin/bash
test=$1

hdfs dfs -mkdir /${test}/count

hdfs dfs -mkdir /${test}/sort

c1=`hdfs dfs -put /${test}/countfile.txt /${test}/count`

c2=`hdfs dfs -put /${test}/sortfile.txt /${test}/sort`

c3=`hadoop jar /${test}/count.jar com.hadoop.mr01.WordCount /${test}/count/countfile.txt /${test}/countoutput`&&c4=`hadoop jar /${test}/sort.jar com.hadoop.mr.Sort /${test}/sort/sortfile.txt /${test}/sortoutput`

