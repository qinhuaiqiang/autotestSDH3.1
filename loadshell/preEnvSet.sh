#!/bin/bash
#获取当前主机的ip
local_ip=`/sbin/ifconfig -a|grep inet|grep -v 127.0.0.1|grep -v inet6|awk '{print $2}'|tr -d "addr:"`
string="$local_ip"
#分割当前主机ip
array=(${string//./ })
#集群中三台主机的ip以参数的形式获得
node1_ip=$1
node2_ip=$2
node3_ip=$3
#获得集群中三台主机的域名
array_node1=(${node1_ip//./ })
array_node2=(${node2_ip//./ })
array_node3=(${node3_ip//./ })
new_domin_node1="node${array_node1[3]}.xdata"
new_domin_node2="node${array_node2[3]}.xdata"
new_domin_node3="node${array_node3[3]}.xdata"
#要修改的文件名
filepath_network="/etc/sysconfig/network"
filepath_hosts="/etc/hosts"
#获取当前主机的域名
old_domin=`awk -F= '$1 ~ /HOSTNAME/ {print $2}'  $filepath_network`
#组合得到当前主机新的主机域名
new_domin="node${array[3]}.xdata"
#修改当前主机域名
old_domin=`awk -F= '$1 ~ /HOSTNAME/ {print $2}'  $filepath_network`
sed -i "s/HOSTNAME=${old_domin}/HOSTNAME=${new_domin}/g" $filepath_network
source $filepath_network

#修改当前主机hosts文件，添加当前主机节点的映射
ip_domin_node1="$node1_ip  $new_domin_node1"
echo "$ip_domin_node1" >>  $filepath_hosts

ip_domin_node2="$node2_ip  $new_domin_node2"
echo "$ip_domin_node2" >>  $filepath_hosts

ip_domin_node3="$node3_ip  $new_domin_node3"
echo "$ip_domin_node3" >>  $filepath_hosts
source $filepath_hosts


