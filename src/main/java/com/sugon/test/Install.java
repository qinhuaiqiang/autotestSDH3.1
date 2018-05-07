package com.sugon.test;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.IOException;

public class Install {
    public static void main(String[] args) throws JSchException, IOException, SftpException, InterruptedException {
        Install ins=new Install();
        ins.install("172.16.0.72","Sugon123");
    }
    public  void install(String ip,String passWord) throws JSchException, IOException, SftpException, InterruptedException {
        Shell shell=new Shell();
        Session session=shell.connectHost(ip,passWord);
        shell.upLoadFile(session,"loadshell/install.exp","/root/");
        shell.execCmd(session,"chmod +x /root/install.exp");
        shell.execExpCmd(session, "./install.exp");
        shell.disconnectHost();
    }
}
