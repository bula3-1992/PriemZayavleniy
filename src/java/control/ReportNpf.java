/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Item;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import models.pzfiles.ContentFile;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author 003-0823
 */
public class ReportNpf {

    public String npf;
    private Document doc;
    private Element rootElement;
    public String date;
    private String chk;
    public File file;
    List<Item> items;

    public ReportNpf(DocumentBuilder docBuilder, String date, String chk, String npf) {

        items = new ArrayList<>();
        this.date = date;
        this.chk = chk;
        this.npf = StringUtils.substringBefore(npf, " ");
        doc = docBuilder.newDocument();
        rootElement = doc.createElement("ProcessingReport");
        doc.appendChild(rootElement);
    }

    public File getFileReport() {
        return this.file;
    }
    
    public void setFileReport(File file) {
        this.file = file;
    }

    public Document getDocument() {
        return this.doc;
    }

    public void addItemToList(Item cf) {
        items.add(cf);
    }

    public void writerXmlFile() {
        Logger.getLogger(ReportNpf.class.getName()).log(Level.SEVERE, "Количество заявлений НПФ " + npf + " : " + items.size());
        for (Item cf : items) {
//            Logger.getLogger(ReportNpf.class.getName()).log(Level.SEVERE, "Заявление " + cf.getItemProperty("Фамилия").toString());
            String chk = "true";
            if (!cf.getItemProperty("commonChk").toString().equals("Успешно")) {
                chk = "false";
            }

            Element reportElement = doc.createElement("FilesReports");
            reportElement.setAttribute("NpfName", cf.getItemProperty("npfName").toString());
            reportElement.setAttribute("SostavitelName", cf.getItemProperty("crPackName").toString());
            reportElement.setAttribute("DocumentType", cf.getItemProperty("docType").toString());
            reportElement.setAttribute("MiddleName", cf.getItemProperty("Отчество").toString());
            reportElement.setAttribute("FirstName", cf.getItemProperty("Имя").toString());
            reportElement.setAttribute("LastName", cf.getItemProperty("Фамилия").toString());
            reportElement.setAttribute("Snils", cf.getItemProperty("Снилс").toString());
            reportElement.setAttribute("SignFileName", StringUtils.substringAfterLast(cf.getItemProperty("signName").toString(), "\\"));
            reportElement.setAttribute("XmlFileName", cf.getItemProperty("fileName").toString());
            reportElement.setAttribute("IsOk", chk);

            if (!cf.getItemProperty("chkConas").toString().equalsIgnoreCase("успешно")) {
                Element errorElement = doc.createElement("ErrorDescription");
                errorElement.setTextContent("ChkConasError: " + cf.getItemProperty("chkConas").toString());
                reportElement.appendChild(errorElement);
            }
            if (!cf.getItemProperty("chkXml").toString().equalsIgnoreCase("успешно")) {
                Element errorElement = doc.createElement("ErrorDescription");
                errorElement.setTextContent("ChkXmlError: " + cf.getItemProperty("chkXml").toString());
                reportElement.appendChild(errorElement);
            }
            if (!cf.getItemProperty("chkFio").toString().equalsIgnoreCase("успешно")) {
                Element errorElement = doc.createElement("ErrorDescription");
                errorElement.setTextContent("ChkFioError: " + cf.getItemProperty("chkFio").toString());
                reportElement.appendChild(errorElement);
            }
            if (!cf.getItemProperty("chkEp").toString().equalsIgnoreCase("успешно")) {
                Element errorElement = doc.createElement("ErrorDescription");
                errorElement.setTextContent("ChkEpError: " + cf.getItemProperty("chkEp").toString());
                reportElement.appendChild(errorElement);
            }
            if (!cf.getItemProperty("chkSkpep").toString().equalsIgnoreCase("успешно")) {
                Element errorElement = doc.createElement("ErrorDescription");
                errorElement.setTextContent("ChkSkpep: " + cf.getItemProperty("chkSkpep").toString());
                reportElement.appendChild(errorElement);
            }
//        if (cf.getItemProperty("chkException") != null  || !cf.getItemProperty("chkException").equals("")) {
//            Element errorElement = doc.createElement("ErrorDescription");
//            errorElement.setTextContent("ChkException: " + cf.getItemProperty("chkException").toString());
//            reportElement.appendChild(errorElement);
//        }
            rootElement.appendChild(reportElement);
        }
    }

    public byte[] saveReport() throws UnsupportedEncodingException {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(doc), new StreamResult(baos));
        } catch (TransformerException ex) {
            Logger.getLogger(ReportNpf.class.getName()).log(Level.SEVERE, null, ex);
        }
        return baos.toByteArray();

    }
    public String getNpf()
    {
        return this.npf;
    }
    public String getDate()
    {
        return this.date;
    }

}
