
import control.ReportNpf;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author 003-0823
 */
public class Reprt {

    public static void main(String[] args) {
      
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element rootElement = doc.createElement("ProcessingReport");
            for (int i = 0; i < 2; i++) {
                
                Element reportElement = doc.createElement("FilesReports");
                reportElement.setAttribute("IsOk", "false");
                reportElement.setAttribute("XmlFileName", "lala.xml");
                reportElement.setAttribute("SignFileName", "lala.p7s");
                reportElement.setAttribute("Snils", "123-456-789 12");
                reportElement.setAttribute("LastName", "F");
                reportElement.setAttribute("FirstName", "I");
                reportElement.setAttribute("MiddleName", "O");
                reportElement.setAttribute("DocumentType", "TEST_O_ZAYAV_NPF");
                reportElement.setAttribute("SostavitelName", "SMTH_UC");
                reportElement.setAttribute("NpfName", "ELDORADO NPF");
                if (i == 0) {
                    Element errorElement = doc.createElement("ErrorDescription");
                    errorElement.setTextContent("Сертификат не прошел проверку: Не удалось проверить на отзыв один или несколько сертификатов цепочки");
                    reportElement.appendChild(errorElement);
                }
                rootElement.appendChild(reportElement);
                
            }
            doc.appendChild(rootElement);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Reprt.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
