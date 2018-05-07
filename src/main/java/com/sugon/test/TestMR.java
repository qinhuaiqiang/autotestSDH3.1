package com.sugon.test;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.File;
import java.io.IOException;

public class TestMR {
    //linux机根目录下文件名
    private static String desFile;
    //windows机下路径
    private static String localFilePath;
    public static void main(String[] args) throws IOException, JSchException, SftpException, InterruptedException {
        TestMR testmr=new TestMR();
        TestMR.desFile="MRTest";
        TestMR.localFilePath="D:/mydata";
          testmr.loadjarFile("172.16.0.72","Sugon123",desFile,localFilePath);
          testmr.putdataExc("172.16.0.72","Sugon123",desFile,localFilePath);
    }
    /*
    ip为要连接的Linux机的ip，这里为主节点ip
    passWord为要连接的Linux机的密码，这里为主节点的密码
    desFile为HDFS上用来接收要处理的数据和输出MR程序处理结果的文件夹,该文件夹应该是根目录下第一层文件夹。
    localFilePath为本地windows机接收MR程序处理结果以及原始处理数据的路径
     */
    public  void putdataExc(String ip,String passWord,String desFile,String localFilePath) throws JSchException, IOException, InterruptedException, SftpException {
        Shell shell=new Shell();
        Session session=shell.connectHost(ip,passWord);
        shell.execCmd(session,"su - hdfs -c \"hdfs dfs -mkdir /"+desFile+"\"");
        shell.execCmd(session,"su - hdfs -c \" hdfs dfs -put /"+desFile+"/testMR.sh /"+desFile+"\"");
        shell.execCmd(session,"su - hdfs -s /bin/bash /"+desFile+"/testMR.sh "+desFile);
        shell.execCmd(session,"hdfs dfs -get /"+desFile+"/countoutput/part-r-00000 /"+desFile+"/countout");
        shell.execCmd(session,"hdfs dfs -get /"+desFile+"/sortoutput/part-r-00000 /"+desFile+"/sortout");
        File fileCount=new File(localFilePath+"/countout");
        File fileSort=new File(localFilePath+"/sortout");
        fileCount.mkdir();
        fileSort.mkdir();
        shell.download(session,"/"+desFile+"/countout/part-r-00000",localFilePath+"/countout");
        shell.download(session,"/"+desFile+"/sortout/part-r-00000",localFilePath+"/sortout");
        System.out.println("success");
        shell.disconnectHost();
    }
    //desFile为Linux主节点上用来接收jar包，上传jar包到HDFS上和处理数据以及下载MR程序执行结果到Linux机上的文件夹，该文件夹在原Linux机上不存在。是在根目录下创建的第一层文件夹如Test
    //localFilePath为本地windows机保存原始数据的路径
    public  void loadjarFile(String ip,String passWord,String desFile,String localFilePath) throws JSchException, IOException, SftpException {
        Shell shell=new Shell();
        Session session=shell.connectHost(ip,passWord);
        shell.execCmd(session,"mkdir /"+desFile);
        shell.execCmd(session,"chmod 777 /"+desFile);
        shell.execCmd(session,"mkdir /"+desFile+"/sortout");
        shell.execCmd(session,"mkdir /"+desFile+"/countout");
        shell.upLoadFile(session,"loadjar/count.jar","/"+desFile);
        shell.upLoadFile(session,"loadjar/sort.jar","/"+desFile);
        shell.upLoadFile(session,"loaddata/countfile.txt","/"+desFile);
        shell.upLoadFile(session,"loaddata/sortfile.txt","/"+desFile);
        shell.upLoadFile(session,"loadshell/testMR.sh","/"+desFile);
        File file=new File(localFilePath);
        file.mkdirs();
        shell.download(session,"/"+desFile+"/sortfile.txt",localFilePath);
        shell.download(session,"/"+desFile+"/countfile.txt",localFilePath);
        shell.disconnectHost();
    }
}
