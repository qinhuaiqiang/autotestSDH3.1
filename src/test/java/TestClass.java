
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.sugon.test.*;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class TestClass {
    @Test(priority = 0)
    @Parameters({ "ip01","ip02","ip03","passWord" })
    public void preEnvSet(String ip01,String ip02,String ip03,String passWord) throws JSchException, IOException, InterruptedException, SftpException {
        PreEnvSet pre=new PreEnvSet();
        pre.uploadAndExc(ip01,ip02,ip03,passWord);
        System.out.println("这里要等待大约1分钟的时间，等Linux机重启成功");
        Thread.sleep(60000);
        System.out.println("等待结束");
    }

 }
