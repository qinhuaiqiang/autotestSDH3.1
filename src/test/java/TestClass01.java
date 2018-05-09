import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.sugon.test.Install;
import com.sugon.test.WebConf;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class TestClass01 {
    @Test(priority = 0)
    @Parameters({"ip01","passWord"})
    public void install(String ip01,String passWord) throws JSchException, SftpException, IOException, InterruptedException {
        Install ins=new Install();
        ins.install(ip01,passWord);
        System.out.println("等待30s的时间,等待SDH3.1启动");
        Thread.sleep(30000);
        System.out.println("SDH3.1启动成功");
    }
    @Test(priority = 1)
    @Parameters({"loginURL","ip01","passWord","masterIpLastByte","slave01IpLastByte","slave02IpLastByte"})
    public void  conWebPag(String loginURL,String ip01,String passWord,String masterIpLastByte,String slave01IpLastByte,String slave02IpLastByte) throws SftpException, JSchException, InterruptedException {
        WebConf conf=new WebConf();
        //创建与远程linux机的连接
        WebDriver driver=conf.openLogin(loginURL);
        //下载id_rsa文件
        String src="D:/id_rsa";
        File file=new File(src);
        if(file.exists()) {
            file.delete();
            conf.downId_rsa(ip01,passWord);
        }
        else{
            conf.downId_rsa(ip01,passWord);
        }
        //输入用户名和密码登录
        conf.login(driver,ip01,masterIpLastByte,slave01IpLastByte,slave02IpLastByte);
    }
}
