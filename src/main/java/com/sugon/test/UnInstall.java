package com.sugon.test;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.IOException;

public class UnInstall {
    public static void main(String[] args) throws JSchException, IOException, SftpException, InterruptedException {
        UnInstall ins=new UnInstall();
        ins.unInstall("172.16.0.72","Sugon123");
    }
    public void unInstall(String ip,String passWord) throws JSchException, IOException, SftpException, InterruptedException {
        Shell shell=new Shell();
        Session session=shell.connectHost(ip,passWord);
        shell.upLoadFile(session,"loadshell/uninstall.exp","/root/");
        shell.execCmd(session,"chmod +x /root/uninstall.exp");
        shell.execExpCmd(session, "./uninstall.exp");
        shell.disconnectHost();
    }
}
