package com.sugon.test;

import com.jcraft.jsch.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.util.Vector;

public class Shell {
   //声明一个用来创建连接的jsch对象
    private static JSch jsch;
    //声明连接的对象
    private static Session session;
    //设置远程连接的主机端口
    private static final int HOST_PORT=22;
    //设置远程连接的主机用户
    private static final String HOST_USER="root";
    //保存输出内容的容器
    private static ArrayList<String> stdout;
    //与一个linux主机root用户建立连接
    public Session connectHost(String hostIp,String hostPasswd) throws JSchException {
        jsch = new JSch();// 创建JSch对象
        session = jsch.getSession(Shell.HOST_USER, hostIp, Shell.HOST_PORT);// 根据用户名、主机ip、端口号获取一个Session对象
        session.setPassword(hostPasswd);// 设置密码
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);// 为Session对象设置properties
        session.setTimeout(1500);// 设置超时
        session.connect();// 通过Session建立连接
        return session;
             }
    //关闭与一个Linux主机建立的连接
    public void disconnectHost() {
        session.disconnect();
          }

   //在连接的Linux主机上执行shell命令和运行sh脚本
    public void execCmd(Session session,String command) throws JSchException, IOException {
        int returnCode=0;
        BufferedReader reader = null;
        Channel channel = null;
         try {
            if (command != null) {
                channel = session.openChannel("exec");//打开一个执行命令的通道
                ChannelExec channelExec=(ChannelExec) channel;
                channelExec.setCommand(command);

                InputStream in = channelExec.getInputStream();
                reader = new BufferedReader(new InputStreamReader(in));

                channelExec.connect();

                System.out.println("the command is:"+command);
                //接收远程服务器执行的结果。
                String buf = null;
                while ((buf = reader.readLine()) != null) {
                    System.out.println(buf);
                    stdout.add(buf);
                    }
            }
                 } catch (IOException e) {
                       e.printStackTrace();
                     } catch (JSchException e) {
                         e.printStackTrace();
                     } finally {
                 try {
                           reader.close();
                        } catch (IOException e) {
                           e.printStackTrace();
                        }
                    channel.disconnect();

                  }
         }

         //执行expect程序时需要显示控制台的输出结果
    public void execExpCmd(Session session,String command) throws JSchException, IOException {
        int returnCode=0;
        BufferedReader reader = null;
        Channel channel = null;
        try {
            if (command != null) {
                channel = session.openChannel("exec");//打开一个执行命令的通道
                ChannelExec channelExec=(ChannelExec) channel;
                channelExec.setCommand(command);

                InputStream in = channelExec.getInputStream();
                reader = new BufferedReader(new InputStreamReader(in));

                channelExec.connect();

                System.out.println("the command is:"+command);
                //接收远程服务器执行的结果。
                String buf = null;
                while ((buf = reader.readLine()) != null) {
                    System.out.println(buf);
                    //stdout.add(buf);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSchException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            channel.disconnect();
        }
    }




    public  void upLoadFile(Session session,String sPath, String dPath) throws JSchException, SftpException {
            Channel channel = null;
            channel = (Channel) session.openChannel("sftp");
            channel.connect(10000000);
            ChannelSftp sftp = (ChannelSftp) channel;
            sftp.cd(dPath);
            File file = new File(sPath);
            copyFile(sftp, file, sftp.pwd());

    }
         /*
    public  void upLoadFile(Session session,String sPath, String dPath) {

        Channel channel = null;
        try {
            channel = (Channel) session.openChannel("sftp");
            channel.connect(10000000);
            ChannelSftp sftp = (ChannelSftp) channel;
            try {
                sftp.cd(dPath);
                Scanner scanner = new Scanner(System.in);
                System.out.println(dPath + ":此目录已存在,文件可能会被覆盖!是否继续y/n?");
                String next = scanner.next();
                if (!next.toLowerCase().equals("y")) {
                    return;
                }

            } catch (SftpException e) {

                sftp.mkdir(dPath);
                sftp.cd(dPath);

            }
            File file = new File(sPath);
            copyFile(sftp, file, sftp.pwd());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */
    public static void copyFile(ChannelSftp sftp, File file, String pwd) {

        if (file.isDirectory()) {
            File[] list = file.listFiles();
            try {
                try {
                    String fileName = file.getName();
                    sftp.cd(pwd);
                    System.out.println("正在创建目录:" + sftp.pwd() + "/" + fileName);
                    sftp.mkdir(fileName);
                    System.out.println("目录创建成功:" + sftp.pwd() + "/" + fileName);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                pwd = pwd + "/" + file.getName();
                try {

                    sftp.cd(file.getName());
                } catch (SftpException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            for (int i = 0; i < list.length; i++) {
                copyFile(sftp, list[i], pwd);
            }
        } else {

            try {
                sftp.cd(pwd);

            } catch (SftpException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            System.out.println("正在复制文件:" + file.getAbsolutePath());
            InputStream instream = null;
            OutputStream outstream = null;
            try {
                outstream = sftp.put(file.getName());
                instream = new FileInputStream(file);

                byte b[] = new byte[1024];
                int n;
                try {
                    while ((n = instream.read(b)) != -1) {
                        outstream.write(b, 0, n);
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } catch (SftpException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    outstream.flush();
                    outstream.close();
                    instream.close();

                } catch (Exception e2) {
                    // TODO: handle exception
                    e2.printStackTrace();
                }
            }
        }
    }
    /**
      * 下载文件
     *
     * @param src
      * @param dst
     * @throws JSchException
      * @throws SftpException
      */
            public void download(Session session,String src, String dst) throws JSchException, SftpException {
                // src linux服务器文件地址，dst 本地存放地址
                ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
                channelSftp.connect();
                channelSftp.get(src, dst);
                channelSftp.quit();
            }

             /**
      * 删除文件
     *
      * @param directory\
              *                 *            要删除文件所在目录
      * @param deleteFile
     *            要删除的文件
      * @param sftp
      * @throws SftpException
       * @throws JSchException
      */
            public void delete(String directory, String deleteFile) throws SftpException, JSchException {
                ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
                channelSftp.cd(directory);
                channelSftp.rm(deleteFile);
            }

             /**
      * 列出目录下的文件
      *
      * @param directory
      *            要列出的目录
      * @param sftp
      * @return
      * @throws SftpException
      * @throws JSchException
       */
    //@SuppressWarnings("rawtypes")
      public Vector listFiles(String directory) throws JSchException, SftpException {
                ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
               return channelSftp.ls(directory);
           }
}
