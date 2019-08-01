
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.security.cert.CertificateFactory;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.security.cert.X509Certificate;
import module.CertCRLUpdater;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import verifier.VerifyCertificate;

/**
 *
 * @author 003-0823
 */
public class ListCerts {
    
    public static void main(String[] args) throws Exception {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat converter = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss");
        SimpleDateFormat converter2 = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss");
        Date signingTime = converter2.parse(converter.format(calendar.getTime()));
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");

        File crlDir = new File("t:\\OTDELENIE\\18_OZI\\SOS\\БПИ для УПИ\\");
        File[] listOfCRLFile = crlDir.listFiles();
        for (File tempFile : listOfCRLFile) {
            String filename = tempFile.getName();
            if (filename.length() < 4) {
                continue;
            }
            if (filename.substring(filename.length() - 4, filename.length()).toLowerCase().equals(".crl")) {
                RandomAccessFile raf = new RandomAccessFile(tempFile, "r");
                byte[] crl = new byte[(int) raf.length()];
                raf.read(crl);
                CertCRLUpdater ccu = new CertCRLUpdater();
                ccu.updateCrl(crl);
            }
        }

        File certDir = new File("t:\\OTDELENIE\\18_OZI\\SOS\\БПИ для УПИ\\сер\\");
        File[] listOfCertFile = certDir.listFiles();
        for (File tempFile : listOfCertFile) {
            String filename = tempFile.getName();
            if (filename.length() < 4) {
                continue;
            }
            if (filename.substring(filename.length() - 4, filename.length()).toLowerCase().equals(".cer")) {
                RandomAccessFile raf = new RandomAccessFile(tempFile, "r");
                byte[] cert = new byte[(int) raf.length()];
                raf.read(cert);
                CertCRLUpdater ccu = new CertCRLUpdater();
                ccu.updateCertificate(cert);

                ByteArrayInputStream bais = new ByteArrayInputStream(cert);
                X509Certificate x509 = (X509Certificate) certFactory.generateCertificate(bais);
                X500Name x500name = new X500Name(x509.getSubjectX500Principal().getName());
                RDN cn = x500name.getRDNs(BCStyle.CN)[0];
                VerifyCertificate vc = new VerifyCertificate();
                long signingTimeFormatted = (signingTime.getTime() + 11644473600000L) * 10000L;
                System.out.println(IETFUtils.valueToString(cn.getFirst().getValue()).toUpperCase()
                        + " " + VerifyCertificate.translateErrorMessage(vc.verify(cert, signingTimeFormatted)));
            }
        }
    }
}
