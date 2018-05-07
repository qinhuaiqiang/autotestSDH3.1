package com.sugon.test;


import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class PreEnvSet {
     //声明集群中的三个节点
     private static  Shell   shell01;
     private static  Shell   shell02;
     private static  Shell   shell03;

    public static void main(String[] args) throws JSchException, IOException, SftpException, InterruptedException {
 PreEnvSet pre=new PreEnvSet();
 String ip01="172.16.0.72";
 String ip02="172.16.0.75";
 String ip03="172.16.0.90";
 String passWord="Sugon123";
 pre.uploadAndExc(ip01,ip02,ip03,passWord);
    }

    //上传并执行脚本，实现linux集群环境的预配置.
     public void uploadAndExc(String ip01,String ip02,String ip03,String passWord) throws JSchException, IOException, SftpException, InterruptedException {
        //实例化对象
        shell01=new Shell();
        shell02=new Shell();
        shell03=new Shell();
        //创建与三台主机节点的连接，并上传预置环境修改的脚本
        Session session01=shell01.connectHost(ip01,passWord);
        Session session02=shell02.connectHost(ip02,passWord);
        Session session03=shell03.connectHost(ip03,passWord);
        //上传环境配置脚本
         shell01.execCmd(session01,"mkdir /root/preEnv");
        shell01.upLoadFile(session01,"loadshell/preEnvSet.sh","/root/preEnv");
         shell02.execCmd(session02,"mkdir /root/preEnv");
        shell02.upLoadFile(session02,"loadshell/preEnvSet.sh","/root/preEnv");
         shell03.execCmd(session03,"mkdir /root/preEnv");
        shell03.upLoadFile(session03,"loadshell/preEnvSet.sh","/root/preEnv");
        //执行环境配置脚本
        shell01.execCmd(session01,"sh  /root/preEnv/preEnvSet.sh"+" "+ip01+" "+ip02+" "+ip03);
        shell02.execCmd(session02,"sh  /root/preEnv/preEnvSet.sh"+" "+ip01+" "+ip02+" "+ip03);
        shell03.execCmd(session03,"sh  /root/preEnv/preEnvSet.sh"+" "+ip01+" "+ip02+" "+ip03);
        //上传修改配置文件的脚本
        shell01.upLoadFile(session01,"loadshell/untar.sh","/root/preEnv");
        //执行修改配置文件的脚本
         shell01.execCmd(session01,"sh /root/preEnv/untar.sh"+" "+ip01+" "+ip02+" "+ip03+" "+passWord);
        //重启三台Linux机
         shell01.execCmd(session01,"reboot");
         shell02.execCmd(session02,"reboot");
         shell03.execCmd(session03,"reboot");
         shell01.disconnectHost();
         shell02.disconnectHost();
         shell03.disconnectHost();
         System.out.println("PreEnvironment Set is Success");

    }
}
