/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.forms;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import control.ReportNpf;
import dao.controllers.ClientJpaController;
import dao.controllers.NpfJpaController;
import dao.factory.Factory;
import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import javax.management.Query;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import models.pzfiles.Client;
import models.pzfiles.ContentFile;
import models.pzfiles.Filestore;
import models.pzfiles.Npf;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.util.zip.ZipOutputStream;
import org.apache.tools.zip.*;
import java.nio.charset.Charset;
import javax.xml.transform.OutputKeys;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream.UnicodeExtraFieldPolicy;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author 003-0818
 */
public class UCReportMenu extends Window {

    protected VerticalLayout mainLayout = new VerticalLayout();
    protected Button saveButton = new Button("Сохранить отчет");
    protected Button closeButton = new Button("Закрыть");
    protected DateField date_Field1 = new DateField("Дата получения из БПИ");
    protected DateField date_Field2 = new DateField("");
    protected ComboBox uc_Box = new ComboBox("УЦ");
    protected Button generateButton = new Button("Сформировать отчет");
    protected EntityManager pzFilesEntityManager;
    protected JPAContainer ucContainer;
    protected IndexedContainer contentContainer;
    Calendar c = Calendar.getInstance();
    File file, zip;
    protected FileDownloader fd;
    private Document doc;

    public UCReportMenu() {

        setCaption("Сформировать отчеты для УЦ");
        fill();

    }

    protected void fill() {
        saveButton.setEnabled(false);
        Date referenceDate = new Date();
        c.setTime(referenceDate);
        date_Field1.setValue(c.getTime());
        c.add(Calendar.WEEK_OF_YEAR, -1);
        date_Field2.setValue(c.getTime());
        pzFilesEntityManager = Factory.getInstance().getPzFilesEmf().createEntityManager();

        initContainer();

        ucContainer = JPAContainerFactory.make(Client.class, pzFilesEntityManager);
        ucContainer.sort(new String[]{"name"}, new boolean[]{true});
        ucContainer.addContainerFilter(new Compare.Equal("status", 1));
        uc_Box.setContainerDataSource(ucContainer);
        uc_Box.setItemCaptionPropertyId("name");
        uc_Box.setImmediate(true);
        final SimpleDateFormat sdfr = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

        if (date_Field1.getValue() != null && !date_Field1.getValue().equals("")) {
            date_Field1.setValue(c.getTime());
        }

        if (date_Field2.getValue() != null && !date_Field2.getValue().equals("")) {
            date_Field2.setValue(c.getTime());
        }
        center();
        setWidth("40%");
        setHeight("40%");

        HorizontalLayout dateLayout = new HorizontalLayout();
        dateLayout.setSizeFull();
        dateLayout.addComponent(date_Field1);
        dateLayout.addComponent(date_Field2);

        HorizontalLayout ucLayout = new HorizontalLayout();
        ucLayout.setSizeFull();
        ucLayout.addComponent(uc_Box);

        HorizontalLayout controlLayout = new HorizontalLayout();
        controlLayout.setSizeFull();
        controlLayout.addComponent(generateButton);
        controlLayout.addComponent(saveButton);
        controlLayout.addComponent(closeButton);
        generateButton.setSizeFull();
        saveButton.setSizeFull();
        closeButton.setSizeFull();
        date_Field1.setSizeFull();
        date_Field2.setSizeFull();
        uc_Box.setSizeFull();
        mainLayout.addComponent(dateLayout);
        mainLayout.addComponent(ucLayout);
        mainLayout.addComponent(controlLayout);
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);
        generateButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
//                event.getButton().setEnabled(false);
                saveButton.setEnabled(false);
                EntityManager em = Factory.getInstance().getPzFilesEmf().createEntityManager();
                String date = sdfr.format(new Date());
                contentContainer.removeAllContainerFilters();
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery cqF = cb.createQuery();
                long dif = (date_Field2.getValue().getTime() - date_Field1.getValue().getTime()) / (1000 * 60 * 60 * 24);
                if (dif < 0) {
                    Notification.show("Неверно заданы критерии выборки!", Notification.Type.ERROR_MESSAGE);
                } else {
                    Root eF = cqF.from(Filestore.class);
                    Client client = new Client();
                    client.setIdClient((Integer) uc_Box.getValue());
                    cqF.where(cb.and(cb.greaterThanOrEqualTo(eF.get("receiveTime"), date_Field1.getValue()), cb.lessThanOrEqualTo(eF.get("receiveTime"), date_Field2.getValue())), cb.equal(eF.get("senderId"), client));
                    cqF.orderBy(cb.asc(eF.get("receiveTime")));
                    javax.persistence.Query qF = em.createQuery(cqF);
                    List<Filestore> stores = qF.getResultList();
                    List<String> ids = new ArrayList();
                    for (int i = 0; i < stores.size(); i++) {
                        ids.add(stores.get(i).getId());
                    }
                    if (ids.size() != 0) {
                        CriteriaQuery cqC = cb.createQuery();
                        Root eC = cqC.from(ContentFile.class);
                        cqC.where(eC.get("idMessage").in(ids));

                        cqC.orderBy(cb.asc(eC.get("idMessage")));
                        javax.persistence.Query qC = em.createQuery(cqC);
                        queryToContentContainer(qC);

                        try {
                            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                            Map<String, ReportNpf> docs = new HashMap<String, ReportNpf>();
                            //root
                            doc = docBuilder.newDocument();
                            Element rootElement = doc.createElement("ProcessingReport");
                            doc.appendChild(rootElement);
                            //child
                            for (Object ob : contentContainer.getItemIds()) {
                                if (contentContainer.getItem(ob).getItemProperty("idMessage").toString() == null) {
                                    continue;
                                }
                                Item cf = contentContainer.getItem(ob);
                                String chk = "true";
//                                System.out.println(".. " + cf.getItemProperty("Фамилия").toString()+ " " + cf.getItemProperty("Имя").toString()+ " " + cf.getItemProperty("Отчество").toString());
                                if (!cf.getItemProperty("commonChk").toString().equals("Успешно")) {
                                    chk = "false";
                                }
                                if (!docs.containsKey(cf.getItemProperty("npfName").toString())) {
                                    Logger.getLogger(UCReportMenu.class.getName()).log(Level.SEVERE, "Создание шаблона отчета");
                                    ReportNpf reportNpf = new ReportNpf(docBuilder, date, chk, cf.getItemProperty("npfName").toString());
                                    reportNpf.addItemToList(cf);
                                    docs.put(cf.getItemProperty("npfName").toString(), reportNpf);
                                } else {
                                    Logger.getLogger(UCReportMenu.class.getName()).log(Level.SEVERE, "Добавление заявления " + cf.getItemProperty("Фамилия").toString() + " НПФ " + cf.getItemProperty("npfName").toString());
                                    docs.get(cf.getItemProperty("npfName").toString()).addItemToList(cf);
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
                                rootElement.appendChild(reportElement);
                            }
                            List<File> npfFileList = new ArrayList<>();
                            for (Entry<String, ReportNpf> entry : docs.entrySet()) {
                                ReportNpf reportNpf = entry.getValue();
                                reportNpf.writerXmlFile();
                                try {
                                    FileOutputStream fos = new FileOutputStream("c://temp//" + reportNpf.getNpf() + "_" + reportNpf.getDate() + ".xml");
                                    reportNpf.setFileReport(new File("c://temp//" + reportNpf.getNpf() + "_" + reportNpf.getDate() + ".xml"));
                                    fos.write(reportNpf.saveReport());
                                    fos.close();

                                } catch (UnsupportedEncodingException ex) {
                                    Logger.getLogger(UCReportMenu.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IOException ex) {
                                    Logger.getLogger(UCReportMenu.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                npfFileList.add(reportNpf.getFileReport());
                            }
                            TransformerFactory transformerFactory = TransformerFactory.newInstance();
                            Transformer transformer = transformerFactory.newTransformer();
                            DOMSource source = new DOMSource(doc);
                            FileOutputStream fos = new FileOutputStream("c:\\temp\\" + date + ".xml");
                            fos.write(saveReport());
                            fos.close();
                            file = new File("c:\\temp\\" + date + ".xml");
                            StreamResult result = new StreamResult(file);
                            transformer.transform(source, result);
                            npfFileList.add(file);
                            zip = toZip(npfFileList);
                            Resource res = new FileResource(zip);
                            if (fd != null) {
                                fd.remove();
                            }
                            fd = new FileDownloader(res) {
                                @Override
                                public boolean handleConnectorRequest(VaadinRequest request, VaadinResponse response, String path) throws IOException {
                                    if (date_Field1.getValue() != null && date_Field2.getValue() != null && uc_Box.getValue() != null) {
                                        return super.handleConnectorRequest(request, response, path);
                                    } else {
                                        return true;
                                    }
                                }
                            };

                            fd.extend(saveButton);
                            saveButton.setEnabled(true);
                        } catch (ParserConfigurationException ex) {
                            Logger.getLogger(UCReportMenu.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (TransformerConfigurationException ex) {
                            Logger.getLogger(UCReportMenu.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (TransformerException ex) {
                            Logger.getLogger(UCReportMenu.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(UCReportMenu.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(UCReportMenu.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        Notification.show("Не найдены заявления поступившие за выбранный период", Notification.Type.WARNING_MESSAGE);
                    }
                }

            }

            private byte[] saveReport() {
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
        }
        );
        saveButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                saveButton.setEnabled(false);
//                getThis().close();
            }
        }
        );

        closeButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    if (zip != null) {
                        java.nio.file.Path path = Paths.get(zip.getAbsolutePath());
                        Files.deleteIfExists(path);
                    }
                    getThis().close();
                } catch (IOException ex) {
                    Logger.getLogger(ApplicationTable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );
        setContent(mainLayout);
        this.setVisible(true);
    }

    protected void initContainer() {
        contentContainer = new IndexedContainer();
        contentContainer.addContainerProperty("Снилс", String.class, "");
        contentContainer.addContainerProperty("Фамилия", String.class, "");
        contentContainer.addContainerProperty("Имя", String.class, "");
        contentContainer.addContainerProperty("Отчество", String.class, "");
        contentContainer.addContainerProperty("УЦ", String.class, "");
        contentContainer.addContainerProperty("НПФ", String.class, "");
        contentContainer.addContainerProperty("Дата регистрации", String.class, "");
        contentContainer.addContainerProperty("Тип заявления", String.class, "");
        contentContainer.addContainerProperty("xml_path", String.class, "");
        contentContainer.addContainerProperty("birthDate", String.class, "");
        contentContainer.addContainerProperty("npfName", String.class, "");
        contentContainer.addContainerProperty("npfInn", String.class, "");
        contentContainer.addContainerProperty("crPackName", String.class, "");
        contentContainer.addContainerProperty("crPackInn", String.class, "");
        contentContainer.addContainerProperty("complDate", String.class, "");
        contentContainer.addContainerProperty("docType", String.class, "");
        contentContainer.addContainerProperty("commonChk", String.class, "");
        contentContainer.addContainerProperty("chkEp", String.class, "");
        contentContainer.addContainerProperty("chkFio", String.class, "");
        contentContainer.addContainerProperty("chkSkpep", String.class, "");
        contentContainer.addContainerProperty("chkConas", String.class, "");
        contentContainer.addContainerProperty("chkXml", String.class, "");
        contentContainer.addContainerProperty("chkException", String.class, "");
        contentContainer.addContainerProperty("fileName", String.class, "");
        contentContainer.addContainerProperty("signName", String.class, "");
        contentContainer.addContainerProperty("idMessage", Filestore.class, "");
    }

    protected void createReport() {

    }

    private List<ContentFile> queryToContentContainer(javax.persistence.Query q) {
        List<ContentFile> out = q.getResultList();
        contentContainer.removeAllItems();
        initContainer();
        for (ContentFile content : out) {
            System.out.println("СНИЛС_" + content.getSnils());
            Item item = contentContainer.addItem("заявление :" + content.getId());
            item.getItemProperty("Снилс").setValue(content.getSnils());
            item.getItemProperty("Фамилия").setValue(content.getSurname());
            item.getItemProperty("Имя").setValue(content.getFirstname());
            item.getItemProperty("Отчество").setValue(content.getSecondname());
            item.getItemProperty("УЦ").setValue(content.getNpfInn());
            item.getItemProperty("НПФ").setValue(content.getNpfName());
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            item.getItemProperty("Дата регистрации").setValue(sdf.format(content.getCrDate()));
            item.getItemProperty("Тип заявления").setValue(content.getDocType());
            item.getItemProperty("xml_path").setValue(content.getXmlPath());
            item.getItemProperty("birthDate").setValue(sdf.format(content.getBirthDate()));
            item.getItemProperty("npfName").setValue(content.getNpfName());
            item.getItemProperty("npfInn").setValue(content.getNpfInn());
            item.getItemProperty("crPackName").setValue(content.getCrPackName());
            item.getItemProperty("crPackInn").setValue(content.getCrPackInn());
            item.getItemProperty("complDate").setValue(sdf.format(content.getComplDate()));
            item.getItemProperty("docType").setValue(content.getDocType());
            item.getItemProperty("commonChk").setValue(content.getCommonChk());
            item.getItemProperty("chkEp").setValue(content.getChkEp());
            item.getItemProperty("chkFio").setValue(content.getChkFio());
            item.getItemProperty("chkSkpep").setValue(content.getChkSkpep());
            item.getItemProperty("chkConas").setValue(content.getChkConas());
            item.getItemProperty("chkXml").setValue(content.getChkXml());
            item.getItemProperty("idMessage").setValue(content.getIdMessage());
            item.getItemProperty("fileName").setValue(content.getFileName());
            item.getItemProperty("chkException").setValue(content.getChkException());
            item.getItemProperty("signName").setValue(content.getP7sPath());
        }
        return out;
    }

    public UCReportMenu getThis() {
        return this;
    }

    public File toZip(List<File> files) throws FileNotFoundException {

        String zipPath = "c:\\temp\\reports.zip";
        String encoding = "cp866";
        if ((new File(zipPath)).exists()) {
            new File(zipPath).delete();
        }
        try {
            FileOutputStream f = new FileOutputStream(zipPath);
            ZipArchiveOutputStream zos = (ZipArchiveOutputStream) new ArchiveStreamFactory()
                    .createArchiveOutputStream(ArchiveStreamFactory.ZIP, f);
            if (null != encoding) {
                zos.setEncoding(encoding);
            }
            for (int i = 0; i < files.size(); i++) {
                File file = new File(files.get(i).getAbsolutePath());
                System.out.println(file.getAbsoluteFile() + " " + file.exists());
            }
            for (int i = 0; i < files.size(); i++) {
                File file = new File(files.get(i).getAbsolutePath());
                String entryName = file.getName();
                ZipArchiveEntry entry = new ZipArchiveEntry(entryName);
                zos.putArchiveEntry(entry);
                FileInputStream fis = new FileInputStream(file.getAbsoluteFile());
                IOUtils.copy(fis, zos);
                fis.close();
                zos.closeArchiveEntry();
            }
            zos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < files.size(); i++) {
            files.get(i).delete();
        }
        return new File(zipPath);
    }

}
