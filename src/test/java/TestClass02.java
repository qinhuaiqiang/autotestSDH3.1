import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.sugon.test.TestMR;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;

public class TestClass02 {
    @Test(priority = 0)
    @Parameters({"ip01","passWord","desFile","localFilePath"})
    public void testMR(String ip01,String passWord,String desFile,String localFilePath) throws JSchException, SftpException, IOException, InterruptedException {
        TestMR testmr=new TestMR();
        testmr.loadjarFile(ip01,passWord,desFile,localFilePath);
        testmr.putdataExc(ip01,passWord,desFile,localFilePath);
    }
}
