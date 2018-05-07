package com.sugon.test;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class WebConf {
    public static void main(String[] args) throws InterruptedException, SftpException, JSchException {
        WebConf conf=new WebConf();
        //创建与web网页的连接
          WebDriver driver=conf.openLogin("http://172.16.0.72:8080");
        //下载id_rsa文件
        String src="D:/id_rsa";
        File file=new File(src);
        if(file.exists()) {
            file.delete();
            conf.downId_rsa("172.16.0.72","Sugon123");
        }
        else{
            conf.downId_rsa("172.16.0.72","Sugon123");
        }
        //输入用户名和密码登录
        conf.login(driver,"172.16.0.72","72","75","90");

    }
     //masterIpLastByte指的是主节点ip地址的最后一位。
    public  void login(WebDriver driver,String masterIp,String masterIpLastByte,String slave01IpLastByte,String slave02IpLastByte) throws InterruptedException, SftpException, JSchException {
        WebDriverWait wait = new WebDriverWait(driver, 600);
        WebDriverWait waitLong=new WebDriverWait(driver,6000);
        //定位用户名输入框，并输入用户名
         String idadmin="ember3933";
         wait.until(ExpectedConditions.elementToBeClickable(By.id(idadmin)));
         driver.findElement(By.id(idadmin)).sendKeys("admin");
        //driver.findElement(By.id("ember3933")).sendKeys("admin");

        //定位密码输入框，并输入密码
        //driver.findElement(By.id("ember3950")).sendKeys( "admin");
        String idpass="ember3950";
        wait.until(ExpectedConditions.elementToBeClickable(By.id(idpass)));
        driver.findElement(By.id(idpass)).sendKeys("admin");
        //定位登录按钮，并点击
        driver.findElement(By.xpath("//*[@id=\"i18n-2\"]")).click();
        //尝试使用智能等待的方法去解决License窗口的确定不可选的问题，可以执行
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.findElement(By.xpath("//*[@id=\"ember4030\"]/div/button")).click();
       //在集群管理界面点击启动安装向导
        driver.findElement(By.xpath("//*[@id=\"main-operations-boxes\"]/div[2]/div[3]/a")).click();
        //开始安装输入集群的名字
        WebElement clusterName=driver.findElement(By.xpath("//*[@id=\"cluster-name\"]"));
                   clusterName.sendKeys("SugonTest");
                   clusterName.sendKeys(Keys.ENTER);
        //使用双击的方式来实现点击使用本地库的操作。
        WebElement choose=driver.findElement(By.xpath("//label[@class=\"radio big-radio\"]"));
        Actions action=new Actions(driver);
        action.doubleClick(choose).perform();
        //定位下一步按钮，并点击，定位的时候要注意从第一个可定位的地方开始写
        //填写本地hdp安装包的库
        WebElement inputhdp=driver.findElement(By.xpath("//*[@id=\"repoVersionInfoForm\"]/div/div[2]/div/div[6]/div[2]/div/div[3]/input"));
        inputhdp.sendKeys("http://"+masterIp+"/hdp/HDP/centos6");
        WebElement inputhdpgpl=driver.findElement(By.xpath("//*[@id=\"repoVersionInfoForm\"]/div/div[2]/div/div[6]/div[2]/div[2]/div[3]/input"));
        inputhdpgpl.sendKeys("http://"+masterIp+"/hdp/HDP/centos6");
        WebElement inputhdputil=driver.findElement(By.xpath("//*[@id=\"repoVersionInfoForm\"]/div/div[2]/div/div[6]/div[2]/div[3]/div[3]/input"));
        inputhdputil.sendKeys("http://"+masterIp+"/hdp-utils");
        //注意有些按钮没法按的话，可以采用键盘输入enter的方法。
        WebElement nextStep02=driver.findElement(By.xpath("//*[@id=\"select-stack\"]/button[2]"));
        nextStep02.sendKeys(Keys.ENTER);
        //输入要注册的主机域名
        WebElement domin=driver.findElement(By.xpath("//*[@id=\"targetHosts\"]/div/div/textarea"));
        domin.sendKeys("node"+masterIpLastByte+".xdata\nnode"+slave01IpLastByte+".xdata\nnode"+slave02IpLastByte+".xdata");
       //上传下载的id_rsa文件
        WebElement uploadrsa=driver.findElement(By.xpath("//*[@id=\"hostConnectivity\"]/div/div/div/input"));
        uploadrsa.sendKeys("D:/id_rsa");
        //注册集群中的主机节点
        String registXpath="//*[@id=\"installOptions\"]/div[3]/button[2]";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(registXpath)));
        WebElement  regist=driver.findElement(By.xpath(registXpath));
                    regist.sendKeys(Keys.ENTER);
        //等待主机节点注册完毕，点击下一步按钮。这里采用的是显示等待的方法来查看一个元素在页面中是否可以被点击
        String xpathNext="//*[@id=\"confirm-hosts\"]/div[3]/button[2]";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathNext)));
        driver.findElement(By.xpath(xpathNext)).click();
       //处理模态窗口，采用显示等待按钮的方法
       String xpathconfim="//*[@id=\"modal\"]/div[3]/button[2]";
       wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathconfim)));
       driver.findElement(By.xpath(xpathconfim)).click();
       //取消默认安装全部组件
        String xpathNextPage="//*[@id=\"ember3856\"]/div/div/div/div/div[2]/div/div/div/table//th/input";
        WebElement  zuJian=driver.findElement(By.xpath(xpathNextPage));
        zuJian.click();
        zuJian.click();
        //选择一些组件进行安装
        String chooseZuJian="//*[@id=\"ember3856\"]/div/div/div/div/div[2]/div/div/div/table/tbody/tr";
        int[] array={1,2,3,4,5,6,9,15,20,25};
       for(int i=0;i<array.length;i++)
       {
           driver.findElement(By.xpath(chooseZuJian+"["+array[i]+"]"+"//input")).click();
       }

        //选择完服务组件后，点击下一页按钮
        String nextPageXpath="//*[@id=\"ember3856\"]/div/div/div/div/div[2]/div/div/div/div/button[2]" ;
        driver.findElement(By.xpath(nextPageXpath)).click();

        /*
        //选择相应的服务组件。
        //修改hivemetastor到主节点
         String selectPath01="//*[@id=\"ember3856\"]/div/div/div/div/div[2]/div/div/div/div[2]/div[1]/div/div[2]/div/form/div[6]/div[2]/div/select";
        WebElement e01=driver.findElement(By.xpath(selectPath01));
         Select se01=new Select(e01);
         se01.selectByValue("node72.xdata");
         Thread.sleep(5000);
      //修改hiveserver2到主节点
        String selectPath02="//*[@id=\"ember3856\"]/div/div/div/div/div[2]/div/div/div/div[2]/div[1]/div/div[2]/div/form/div[7]/div[2]/div/select";
        WebElement e02=driver.findElement(By.xpath(selectPath02));
        Select se02=new Select(e02);
        se02.selectByValue("node72.xdata");
        Thread.sleep(5000);
      */
        //选择默认的服务组件。
        //点击下一页按钮
        String nextPage01Xpath="//*[@id=\"ember3856\"]/div/div/div/div/div[2]/div/div/div/div[3]/button[2]";
        decidClick(driver,wait,nextPage01Xpath);

        //往不同的节点上分配相应的组件。
        String selectPath03="//*[@id=\"ember3856\"]/div/div/div/div/div[2]/div/div/div/div[2]/table/tbody/tr[1]/td[2]/label/input";
        decidClick(driver,wait,selectPath03);

        String selectPath04="//*[@id=\"ember3856\"]/div/div/div/div/div[2]/div/div/div/div[2]/table/tbody/tr[2]/td[2]/label/input";
        decidClick(driver,wait,selectPath04);

        String selectPath05="//*[@id=\"ember3856\"]/div/div/div/div/div[2]/div/div/div/div[2]/table/tbody/tr[1]/td[4]/label/input";
        decidClick(driver,wait,selectPath05);

        String selectPath06="//*[@id=\"ember3856\"]/div/div/div/div/div[2]/div/div/div/div[2]/table/tbody/tr[1]/td[5]/label/input";
        decidClick(driver,wait,selectPath06);

        String selectPath07="//*[@id=\"ember3856\"]/div/div/div/div/div[2]/div/div/div/div[2]/table/tbody/tr[2]/td[4]/label/input";
        decidClick(driver,wait,selectPath07);

        String selectPath08="//*[@id=\"ember3856\"]/div/div/div/div/div[2]/div/div/div/div[2]/table/tbody/tr[2]/td[5]/label/input";
        decidClick(driver,wait,selectPath08);

        String selectPath09="//*[@id=\"ember3856\"]/div/div/div/div/div[2]/div/div/div/div[2]/table/tbody/tr[2]/td[6]/label/input";
        decidClick(driver,wait,selectPath09);

        String selectPath10="//*[@id=\"ember3856\"]/div/div/div/div/div[2]/div/div/div/div[2]/table/tbody/tr[2]/td[7]/label/input";
        decidClick(driver,wait,selectPath10);

        String newxtPage02Xpath="//*[@id=\"step6\"]/div[4]/button[2]";
        decidClick(driver,wait,newxtPage02Xpath);

        /*
        //默认相关的服务，执行下一步
         String nextPage01Xpath="//*[@id=\"ember3856\"]/div/div/div/div/div[2]/div/div/div/div[3]/button[2]";
         decidClick(driver,wait,nextPage01Xpath);
        //默认分配给各个节点的服务，执行下一步
        String newxtPage02Xpath="//*[@id=\"step6\"]/div[4]/button[2]";
        decidClick(driver,wait,newxtPage02Xpath);
        */

        //自定义服务，主要是修改hive和metrics的配置文件
        //处理hive中的问题
         String xpath01="//*[@id=\"serviceConfig\"]/div[2]/ul/li[5]/a";
        decidClick(driver,wait,xpath01);

        //处理hive中的Tez Container Size问题，将其设置为512MB
         String xpath02="//*[@id=\"serviceConfig\"]/div[2]/div/div/div[2]/div/div/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[1]/div/div/div[2]/div/div[4]/div/div/input";
         WebElement e4=driver.findElement(By.xpath(xpath02));
         e4.clear();
         e4.sendKeys("512");

         //处理hive中的数据库连通性问题。
         //跳转页面到hive的高级配置
         String xpath03="//*[@id=\"serviceConfig\"]/div[2]/div[2]/div/ul/li[2]/a";
         decidClick(driver,wait,xpath03);
          //测试hive数据库的连通性
         //选择已存在的数据库
         String xpath04="//*[@id=\"serviceConfig\"]/div[2]/div[2]/div/div[2]/div[2]/div/div/div[2]/div/form/div[2]/div/div/div/label[2]/input";
         decidClick(driver,wait,xpath04);
         //输入hive数据库的名称和密码
         String xpath05="//*[@id=\"serviceConfig\"]/div[2]/div[2]/div/div[2]/div[2]/div/div/div[2]/div/form/div[6]/div/div/input";
         decidClear(driver,wait,xpath05).sendKeys("hive");
         String xpath06="//*[@id=\"serviceConfig\"]/div[2]/div[2]/div/div[2]/div[2]/div/div/div[2]/div/form/div[6]/div/div/input[2]";
         decidClear(driver,wait,xpath06).sendKeys("hive");
         //修改数据库的端口
         String xpath07="//*[@id=\"serviceConfig\"]/div[2]/div[2]/div/div[2]/div[2]/div/div/div[2]/div/form/div[8]/div/div/input";
        decidClear(driver,wait,xpath07).sendKeys("jdbc:mysql://node"+masterIpLastByte+".xdata:3309/hive");
        //测试数据库连通性
        String xpath08="//*[@id=\"serviceConfig\"]/div[2]/div[2]/div/div[2]/div[2]/div/div/div[2]/div/form/div[9]/div/div/div/div/span";
        decidClick(driver,wait,xpath08);
        //当数据库连通性测试成功，再执行下一步
        String xpath081="//*[@id=\"serviceConfig\"]/div[2]/div[2]/div/div[2]/div[2]/div/div/div[2]/div/form/div[9]/div/div/div/div/div/a";
        wait.until(ExpectedConditions.invisibilityOfElementWithText(By.xpath(xpath081),"连接成功"));
        Thread.sleep(10000);
        //修改metrics的配置
        //跳转到metrics的页面
        String xpath09="//*[@id=\"serviceConfig\"]/div[2]/ul/li[9]/a";
        decidClick(driver,wait,xpath09);
        //设置用户名和密码为admin/admin
        String xpath10="//*[@id=\"serviceConfig\"]/div[2]/div[2]/div/div[2]/div/div[2]/div/form/div[7]/div/div/input";
        decidClear(driver,wait,xpath10).sendKeys("admin");
        String xpath11="//*[@id=\"serviceConfig\"]/div[2]/div[2]/div/div[2]/div/div[2]/div/form/div[7]/div/div/input[2]";
        decidClear(driver,wait,xpath11).sendKeys("admin");
        //点击下一页按钮
        String xpath12="//*[@id=\"serviceConfig\"]/div[3]/div/button/span";
        decidClick(driver,wait,xpath12);
        //安装部署
        String xpath13="//*[@id=\"ember3856\"]/div/div/div/div/div[2]/div/div/div/div[3]/button[2]";
        decidClick(driver,wait,xpath13);
        //安装完后点击下一页
        String xpath14="//*[@id=\"ember3856\"]/div/div/div/div/div[2]/div/div/div/div[3]/div/button";
        decidClick(driver,waitLong,xpath14);

        //完成组件安装
        //String xpath15="//*[@id=\"ember4042\"]/div/div/div/div/div[2]/div/div/div/div[3]/button";
        String xpath15="//*[@id=\"ember3856\"]/div/div/div/div/div[2]/div/div/div/div[3]/button";
        decidClick(driver,wait,xpath15);

    }
    //点击一个元素前先判断其是否可点击
    public static void decidClick(WebDriver driver,WebDriverWait wait,String xpath)
    {
         WebElement element=driver.findElement(By.xpath(xpath));
         wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
         element.click();
    }
    public static WebElement decidClear(WebDriver driver,WebDriverWait wait,String xpath)
    {
        WebElement element=driver.findElement(By.xpath(xpath));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        element.clear();
        return element;
    }


    public  WebDriver openLogin(String loginURL)
    {
        //创建一个用于打开浏览器的驱动
        WebDriver driver = new ChromeDriver();
        //使用创建的驱动打开相应的网址
        driver.get(loginURL);
        return driver;
    }
    public  void  downId_rsa(String hostIp,String hostPasswd) throws JSchException, SftpException {
        Shell shell=new Shell();
        Session session=shell.connectHost(hostIp,hostPasswd);
        shell.download(session,"/root/.ssh/id_rsa","D:/");
    }


}
