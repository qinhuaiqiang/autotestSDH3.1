#!/bin/bash
#后期可以将安装包所在的文件夹写成参数
node1_ip="$1"
node2_ip="$2"
node3_ip="$3"
password="$4"
cd /root/SDH3.1/
#rpm包所在的地方也可写成另外一个参数
tar  xzf ./XData_V3.1.0_b2_24377.tar.gz
mv   ./rpm  ./sdhpkg/
echo "$node1_ip" >  ./sdhpkg/sugon/conf/nodeslist
echo "$node2_ip" >> ./sdhpkg/sugon/conf/nodeslist
echo "$node3_ip" >> ./sdhpkg/sugon/conf/nodeslist
sed -i '$d' ./sdhpkg/sugon/conf/var.cnf
echo "password=\"$password\"" >> ./sdhpkg/sugon/conf/var.cnf

