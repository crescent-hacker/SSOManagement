import java.io.FileInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Company: jetsun
 * Author: chendf
 * Date: 2014/9/5
 * Desc:
 */
public class TestCertificate {

    public static X509Certificate getCert(String fileName) throws Exception{
        CertificateFactory certificate_factory = CertificateFactory.getInstance("X.509");
        FileInputStream file_inputstream = new FileInputStream(fileName);
        X509Certificate x509certificate = (X509Certificate) certificate_factory.generateCertificate
                (file_inputstream);
        return x509certificate;
        //以下类似，这里省略
//        System.out.println(x509certificate.getType());
//        System.out.println(x509certificate.getNotBefore().toString());//得到开始有效日期
//        System.out.println(x509certificate.getNotAfter().toString());//得到截止日期
//        System.out.println(x509certificate.getSerialNumber().toString());//得到序列号
//        System.out.println(x509certificate.getIssuerDN().getName());//得到发行者名
//        System.out.println(x509certificate.getSigAlgName());//得到签名算法
//        System.out.println(x509certificate.getPublicKey().getAlgorithm());//得到公钥算法
    }

    public static void main(String[] args) throws Exception {
        System.out.println(TestCertificate.getCert("F:/省操作员.cer").getSerialNumber());//15
        System.out.println(TestCertificate.getCert("F:/省审批员.cer").getSerialNumber());//16
        System.out.println(TestCertificate.getCert("F:/广州市.cer").getSerialNumber());//13
        System.out.println(TestCertificate.getCert("F:/广东省中医院.cer").getSerialNumber());//14
        System.out.println(TestCertificate.getCert("F:/超级管理员.cer").getSerialNumber());//14
    }
}
