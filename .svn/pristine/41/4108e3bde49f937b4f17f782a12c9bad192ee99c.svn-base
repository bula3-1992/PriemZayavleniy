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
import dao.controllers.ClientJpaController;
import dao.controllers.NpfJpaController;
import dao.factory.Factory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.Query;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import models.pzfiles.Client;
import models.pzfiles.ContentFile;
import models.pzfiles.Filestore;
import models.pzfiles.Npf;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 *
 * @author 003-0818
 */
public class MonitoringCommon extends Window {

    private VerticalLayout mainLayout = new VerticalLayout();

    int rowNumber;
    EntityManager em;
    private FileDownloader fd;
    private Button saveButton = new Button("Скачать отчет");
    DateField date_Field1 = new DateField("Дата заполнения");
    DateField date_Field2 = new DateField("Дата заполнения");
    Button report = new Button("Сформировать отчет");
    Button close = new Button("Закрыть");
    private EntityManager pzFilesEntityManager;
    private JPAContainer<Npf> npfContainer;
    private JPAContainer<Client> ucContainer;
    private IndexedContainer contentContainer, contentContainer2;
    private List<Filestore> filestores;
    CriteriaBuilder cb;
    Calendar c = Calendar.getInstance();

    private String[] titles = {
        "",
        "в форме эл.документа через БПИ",
        "в форме эл.документа через ЕПГУ",
        "в форме эл.документа иным способом",
        "в бумажном виде при личном обращении",
        "Всего за 2015 г."

    };
    String fio = "";
    String snils = "";
    String complDate1 = "";
    String complDate2 = "";
    String crPackName = "";
    String fileName = "";
    String npfName = "";
    ClientJpaController cjc = new ClientJpaController();
    NpfJpaController njc = new NpfJpaController();
    File file;

    public MonitoringCommon() {
        setCaption("Количество поступивших заявлений");
        containerInit();

        saveButton.setEnabled(false);
        Date referenceDate = new Date();
        c.setTime(referenceDate);
        date_Field1.setValue(c.getTime());
        c.add(Calendar.WEEK_OF_YEAR, -1);
        date_Field2.setValue(c.getTime());
        pzFilesEntityManager = Factory.getInstance().getPzFilesEmf().createEntityManager();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        final SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss");

        if (date_Field1.getValue() != null && !date_Field1.getValue().equals("")) {
            date_Field1.setValue(c.getTime());
        }

        if (date_Field2.getValue() != null && !date_Field2.getValue().equals("")) {
            date_Field2.setValue(c.getTime());
        }
        center();
        setWidth("35%");
        setHeight("60%");

        ucContainer = JPAContainerFactory.make(Client.class, pzFilesEntityManager);
        ucContainer.sort(new String[]{"name"}, new boolean[]{true});
        ucContainer.addContainerFilter(new Compare.Equal("status", 1));

        npfContainer = JPAContainerFactory.make(Npf.class, pzFilesEntityManager);
        npfContainer.sort(new String[]{"name"}, new boolean[]{true});

        HorizontalLayout hl6 = new HorizontalLayout();
        hl6.setSizeFull();
        hl6.addComponent(date_Field1);
        hl6.addComponent(date_Field2);

        HorizontalLayout hl7 = new HorizontalLayout();
        hl7.setSizeFull();
        hl7.addComponent(report);
        hl7.addComponent(saveButton);
        hl7.addComponent(close);
        report.setSizeFull();
        saveButton.setSizeFull();
        close.setSizeFull();
        date_Field1.setSizeFull();
        date_Field2.setSizeFull();
        mainLayout.addComponent(hl6);
        mainLayout.addComponent(hl7);
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);

        report.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {

//                report.setEnabled(false);
                saveButton.setEnabled(false);
                contentContainer.removeAllContainerFilters();
                file = new File("C:\\temp\\report.csv");
                PrintWriter writer = null;

                boolean error = false;

                long dif = (date_Field2.getValue().getTime() - date_Field1.getValue().getTime()) / (1000 * 60 * 60 * 24);
                if (dif < 0) {
                    Notification.show("Начальная дата выборки больше конечной даты выборки!", Notification.Type.ERROR_MESSAGE);
                    error = true;
                }
                if (!error) {
                    em = Factory.getInstance().getPzFilesEmf().createEntityManager();
                    cb = em.getCriteriaBuilder();
                    CriteriaQuery cqF = cb.createQuery();

                    Root eF = cqF.from(Filestore.class);
                    cqF.where(cb.and(cb.greaterThan(eF.get("receiveTime"), date_Field1.getValue()), cb.lessThan(eF.get("receiveTime"), date_Field2.getValue())));
                    cqF.orderBy(cb.asc(eF.get("senderId")));
                    javax.persistence.Query qF = em.createQuery(cqF);
                    filestores = qF.getResultList();
                    List<String> ids = new ArrayList();
                    for (int i = 0; i < filestores.size(); i++) {
                        ids.add(filestores.get(i).getId());
                    }

                    if (ids.size() > 0) {
                        CriteriaQuery cqC = cb.createQuery();
                        Root eC = cqC.from(ContentFile.class);
                        cqC.where(eC.get("idMessage").in(ids));
                        javax.persistence.Query qC = em.createQuery(cqC);
                        queryToContentContainer(qC);

                        HSSFWorkbook workbook = new HSSFWorkbook();
                        Map<String, CellStyle> styles = createStyles(workbook);
                        createPage1(workbook, styles);
                        createPage2(workbook, styles);
                        try {
                            file = new File("c:\\temp\\monitoring.xls");
                            FileOutputStream out = new FileOutputStream(file);
                            workbook.write(out);
                            out.close();
                            Resource res = new FileResource(file);
                            if (fd != null) {
                                fd.remove();
                            }
                            fd = new FileDownloader(res) {
                                @Override
                                public boolean handleConnectorRequest(VaadinRequest request, VaadinResponse response, String path) throws IOException {
                                    if (date_Field1.getValue() != null && date_Field2.getValue() != null) {
                                        return super.handleConnectorRequest(request, response, path);
                                    } else {
                                        return true;
                                    }
                                }
                            };
                            fd.extend(saveButton);
                            saveButton.setEnabled(true);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            private void createPage1(HSSFWorkbook workbook, Map<String, CellStyle> styles) throws FormulaParseException {
                HSSFSheet sheet = workbook.createSheet("Мониторинг общий");
                sheet.setColumnWidth(0, 7000);
                sheet.setColumnWidth(1, 7000);
                sheet.setColumnWidth(2, 7000);
                sheet.setColumnWidth(3, 7000);
                sheet.setColumnWidth(4, 7000);
                sheet.setColumnWidth(5, 7000);
                int rowNumber = 0;
                Row titleRow = sheet.createRow(rowNumber);
                titleRow.setHeightInPoints(45);
                Cell titleCell = titleRow.createCell(0);
                titleCell.setCellValue("Мониторинг приема заявлений застрахованных лиц о выборе инвестиционного портфеля (управляющей компании), о переходе в негосударственный пенсионный фонд или о переходе в Пенсионный фонд Российской Федерации из негосударственного пенсионного фонда");
                titleCell.setCellStyle(styles.get("title"));
                sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$F$1"));
                rowNumber++;
                Row header = sheet.createRow(rowNumber);
                header.createCell(0).setCellValue("");
                header.createCell(1).setCellValue("в форме эл.документа через БПИ");
                header.createCell(2).setCellValue("в форме эл.документа через ЕПГУ");
                header.createCell(3).setCellValue("в форме эл.документа иным способом");
                header.createCell(4).setCellValue("в бумажном виде при личном обращении");
                header.createCell(5).setCellValue("Всего");
                header.getCell(0).setCellStyle(styles.get("header"));
                header.getCell(1).setCellStyle(styles.get("header"));
                header.getCell(2).setCellStyle(styles.get("header"));
                header.getCell(3).setCellStyle(styles.get("header"));
                header.getCell(4).setCellStyle(styles.get("header"));
                header.getCell(5).setCellStyle(styles.get("header"));
                rowNumber++;
                int success = 0, failure = 0, prepared = 0, downloaded = 0;
                for (Object item : contentContainer.getItemIds()) {

                    if (contentContainer.getItem(item).getItemProperty("idMessage").toString() == null) {
                        continue;
                    }
                    if (contentContainer.getItem(item).getItemProperty("commonChk").toString().equals("Успешно")) {
                        success++;
                    } else {
                        failure++;
                    }
                }
                Row successDataRow = sheet.createRow(rowNumber);
                successDataRow.createCell(0).setCellValue("Принято всего");
                successDataRow.createCell(1).setCellValue(success);
                successDataRow.createCell(2).setCellValue(0);
                successDataRow.createCell(3).setCellValue(0);
                successDataRow.createCell(4).setCellValue(0);
                successDataRow.createCell(5).setCellFormula("SUM(B3:E3)");
                successDataRow.getCell(0).setCellStyle(styles.get("cell"));
                successDataRow.getCell(1).setCellStyle(styles.get("cell"));
                successDataRow.getCell(2).setCellStyle(styles.get("cell"));
                successDataRow.getCell(3).setCellStyle(styles.get("cell"));
                successDataRow.getCell(4).setCellStyle(styles.get("cell"));
                successDataRow.getCell(5).setCellStyle(styles.get("cell"));
                rowNumber++;
                Row failureDataRow = sheet.createRow(rowNumber);
                failureDataRow.createCell(0).setCellValue("Ошибочных");
                failureDataRow.createCell(1).setCellValue(failure);
                failureDataRow.createCell(2).setCellValue(0);
                failureDataRow.createCell(3).setCellValue(0);
                failureDataRow.createCell(4).setCellValue(0);
                failureDataRow.createCell(5).setCellFormula("SUM(B4:E4)");
                failureDataRow.getCell(0).setCellStyle(styles.get("cell"));
                failureDataRow.getCell(1).setCellStyle(styles.get("cell"));
                failureDataRow.getCell(2).setCellStyle(styles.get("cell"));
                failureDataRow.getCell(3).setCellStyle(styles.get("cell"));
                failureDataRow.getCell(4).setCellStyle(styles.get("cell"));
                failureDataRow.getCell(5).setCellStyle(styles.get("cell"));
                rowNumber++;
                Row preparedDataRow = sheet.createRow(rowNumber);
                preparedDataRow.createCell(0).setCellValue("Готово к загрузке");
                preparedDataRow.createCell(1).setCellValue(prepared);
                preparedDataRow.createCell(2).setCellValue(0);
                preparedDataRow.createCell(3).setCellValue(0);
                preparedDataRow.createCell(4).setCellValue(0);
                preparedDataRow.createCell(5).setCellFormula("SUM(B5:E5)");
                preparedDataRow.getCell(0).setCellStyle(styles.get("cell"));
                preparedDataRow.getCell(1).setCellStyle(styles.get("cell"));
                preparedDataRow.getCell(2).setCellStyle(styles.get("cell"));
                preparedDataRow.getCell(3).setCellStyle(styles.get("cell"));
                preparedDataRow.getCell(4).setCellStyle(styles.get("cell"));
                preparedDataRow.getCell(5).setCellStyle(styles.get("cell"));
                rowNumber++;
                Row downloadedDataRow = sheet.createRow(rowNumber);
                downloadedDataRow.createCell(0).setCellValue("Загружено в ПТК СПУ");
                downloadedDataRow.createCell(1).setCellValue(downloaded);
                downloadedDataRow.createCell(2).setCellValue(0);
                downloadedDataRow.createCell(3).setCellValue(0);
                downloadedDataRow.createCell(4).setCellValue(0);
                downloadedDataRow.createCell(5).setCellFormula("SUM(B6:E6)");
                downloadedDataRow.getCell(0).setCellStyle(styles.get("cell"));
                downloadedDataRow.getCell(1).setCellStyle(styles.get("cell"));
                downloadedDataRow.getCell(2).setCellStyle(styles.get("cell"));
                downloadedDataRow.getCell(3).setCellStyle(styles.get("cell"));
                downloadedDataRow.getCell(4).setCellStyle(styles.get("cell"));
                downloadedDataRow.getCell(5).setCellStyle(styles.get("cell"));
            }

            private void createPage2(HSSFWorkbook workbook, Map<String, CellStyle> styles) {
                HSSFSheet sheet = workbook.createSheet("Мониторинг по УЦ");
                sheet.setColumnWidth(0, 10000);
                sheet.setColumnWidth(1, 5000);
                sheet.setColumnWidth(2, 5000);
                sheet.setColumnWidth(3, 5000);
                sheet.setColumnWidth(4, 5000);
                sheet.setColumnWidth(5, 5000);
                sheet.setColumnWidth(6, 5000);
                sheet.setColumnWidth(7, 5000);
                sheet.setColumnWidth(8, 5000);
                sheet.setColumnWidth(9, 5000);
                sheet.setColumnWidth(10, 5000);
                rowNumber = 0;
                Row titleRow = sheet.createRow(rowNumber);
                titleRow.setHeightInPoints(45);
                Cell titleCell = titleRow.createCell(0);
                titleCell.setCellValue("Информация по принятым заявлениям застрахованных лиц о выборе инвестиционного портфеля (управляющей компании), о переходе в негосударственный пенсионный фонд или о переходе в Пенсионный фонд Российской Федерации из негосударственного пенсионного фонда и уведомлениям НПФ в форме электронного документа в разрезе УЦ и НПФ");
                titleCell.setCellStyle(styles.get("title"));
                sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$K$1"));
                rowNumber++;
                Row header = sheet.createRow(rowNumber);
                header.setHeightInPoints(20);
                sheet.addMergedRegion(CellRangeAddress.valueOf("$A$2:$A$4"));

                sheet.addMergedRegion(CellRangeAddress.valueOf("$B$2:$E$2"));
                sheet.addMergedRegion(CellRangeAddress.valueOf("$B$3:$B$4"));
                sheet.addMergedRegion(CellRangeAddress.valueOf("$C$3:$D$3"));
                sheet.addMergedRegion(CellRangeAddress.valueOf("$E$3:$E$4"));

                sheet.addMergedRegion(CellRangeAddress.valueOf("$F$2:$I$2"));
                sheet.addMergedRegion(CellRangeAddress.valueOf("$F$3:$F$4"));
                sheet.addMergedRegion(CellRangeAddress.valueOf("$G$3:$H$3"));
                sheet.addMergedRegion(CellRangeAddress.valueOf("$I$3:$I$4"));

                sheet.addMergedRegion(CellRangeAddress.valueOf("$J$2:$J$4"));
                sheet.addMergedRegion(CellRangeAddress.valueOf("$K$2:$K$4"));

                header.createCell(0).setCellValue("УЦ/НПФ");
                header.createCell(1).setCellValue("Заявления");
                header.createCell(2).setCellValue("");
                header.createCell(3).setCellValue("");
                header.createCell(4).setCellValue("");
                header.createCell(5).setCellValue("Уведомления");
                header.createCell(6).setCellValue("");
                header.createCell(7).setCellValue("");
                header.createCell(8).setCellValue("");
                header.createCell(9).setCellValue("Итого заявлений и договоров");
                header.createCell(10).setCellValue("Загружено в ПТК СПУ");
                header.getCell(0).setCellStyle(styles.get("header"));
                header.getCell(1).setCellStyle(styles.get("header"));
                header.getCell(2).setCellStyle(styles.get("header"));
                header.getCell(3).setCellStyle(styles.get("header"));
                header.getCell(4).setCellStyle(styles.get("header"));
                header.getCell(5).setCellStyle(styles.get("header"));
                header.getCell(6).setCellStyle(styles.get("header"));
                header.getCell(7).setCellStyle(styles.get("header"));
                header.getCell(8).setCellStyle(styles.get("header"));
                header.getCell(9).setCellStyle(styles.get("header"));
                header.getCell(10).setCellStyle(styles.get("header"));
                rowNumber++;
                Row header2 = sheet.createRow(rowNumber);
                header2.setHeightInPoints(20);
                header2.createCell(0).setCellValue("");
                header2.createCell(1).setCellValue("Принято всего");
                header2.createCell(2).setCellValue("из них,");
                header2.createCell(3).setCellValue("");
                header2.createCell(4).setCellValue("загружено в ПТК СПУ");
                header2.createCell(5).setCellValue("Принято всего");
                header2.createCell(6).setCellValue("из них,");
                header2.createCell(7).setCellValue("");
                header2.createCell(8).setCellValue("загружено в ПТК СПУ");
                header2.createCell(9).setCellValue("");
                header2.createCell(10).setCellValue("");
                header2.getCell(0).setCellStyle(styles.get("header"));
                header2.getCell(1).setCellStyle(styles.get("header"));
                header2.getCell(2).setCellStyle(styles.get("header"));
                header2.getCell(3).setCellStyle(styles.get("header"));
                header2.getCell(4).setCellStyle(styles.get("header"));
                header2.getCell(5).setCellStyle(styles.get("header"));
                header2.getCell(6).setCellStyle(styles.get("header"));
                header2.getCell(7).setCellStyle(styles.get("header"));
                header2.getCell(8).setCellStyle(styles.get("header"));
                header2.getCell(9).setCellStyle(styles.get("header"));
                header2.getCell(10).setCellStyle(styles.get("header"));
                rowNumber++;
                Row header3 = sheet.createRow(rowNumber);
                header3.setHeightInPoints(20);
                header3.createCell(0).setCellValue("");
                header3.createCell(1).setCellValue("");
                header3.createCell(2).setCellValue("с  ошибками");
                header3.createCell(3).setCellValue("без ошибок");
                header3.createCell(4).setCellValue("");
                header3.createCell(5).setCellValue("");
                header3.createCell(6).setCellValue("с  ошибками");
                header3.createCell(7).setCellValue("без ошибок");
                header3.createCell(8).setCellValue("");
                header3.createCell(9).setCellValue("");
                header3.createCell(10).setCellValue("");
                header3.getCell(0).setCellStyle(styles.get("header"));
                header3.getCell(1).setCellStyle(styles.get("header"));
                header3.getCell(2).setCellStyle(styles.get("header"));
                header3.getCell(3).setCellStyle(styles.get("header"));
                header3.getCell(4).setCellStyle(styles.get("header"));
                header3.getCell(5).setCellStyle(styles.get("header"));
                header3.getCell(6).setCellStyle(styles.get("header"));
                header3.getCell(7).setCellStyle(styles.get("header"));
                header3.getCell(8).setCellStyle(styles.get("header"));
                header3.getCell(9).setCellStyle(styles.get("header"));
                header3.getCell(10).setCellStyle(styles.get("header"));
                String s = "";
                rowNumber++;
                List<Integer> ucRowNum = new ArrayList();
                for (Object itemId : ucContainer.getItemIds()) {
                    List<String> curFilestoreList = new ArrayList();
                    for (Filestore f : filestores) {
                        if (f.getSenderId().getIdClient().equals(itemId)) {
                            curFilestoreList.add(f.getId());
                        }
                    }
                    if (curFilestoreList.size() > 0) {
                        Map<String, String> npf = new HashMap<String, String>();
                        Map<String, Integer> npf_success = new HashMap<String, Integer>();
                        Map<String, Integer> npf_failure = new HashMap<String, Integer>();
                        CriteriaQuery cqC = cb.createQuery();
                        Root eC = cqC.from(ContentFile.class);
                        cqC.where(eC.get("idMessage").in(curFilestoreList));
                        javax.persistence.Query qC = em.createQuery(cqC);
                        contentContainer.removeAllItems();
                        queryToContentContainer(qC);
                        s += "(" + itemId + " " + contentContainer.size() + ")";
                        for (Object item : contentContainer.getItemIds()) {
                            if (contentContainer.getItem(item).getItemProperty("commonChk").toString().equals("Успешно")) {
                                if (!npf_success.containsKey(contentContainer.getItem(item).getItemProperty("npfName").toString())) {
                                    npf_success.put(contentContainer.getItem(item).getItemProperty("npfName").toString(), 1);
                                } else {
                                    npf_success.put(contentContainer.getItem(item).getItemProperty("npfName").toString(), npf_success.get(contentContainer.getItem(item).getItemProperty("npfName").toString()) + 1);
                                }
                            } else {
                                if (!npf_failure.containsKey(contentContainer.getItem(item).getItemProperty("npfName").toString())) {
                                    npf_failure.put(contentContainer.getItem(item).getItemProperty("npfName").toString(), 1);
                                } else {
                                    npf_failure.put(contentContainer.getItem(item).getItemProperty("npfName").toString(), npf_failure.get(contentContainer.getItem(item).getItemProperty("npfName").toString()) + 1);
                                }
                            }
                        }

                        for (String nameNpf : npf_success.keySet()) {
                            npf.put(nameNpf, nameNpf);
                        }
                        for (String nameNpf : npf_failure.keySet()) {
                            npf.put(nameNpf, nameNpf);
                        }

                        int firstNpfRowNum = rowNumber;
                        int lastNpfRowNum = rowNumber;
                        for (Entry<String, String> entry : npf.entrySet()) {
                            Row npfRow = sheet.createRow(rowNumber);
                            npfRow.createCell(0).setCellValue(entry.getKey());
                            npfRow.createCell(1).setCellFormula("SUM(C" + (rowNumber + 1) + ":D" + (rowNumber + 1) + ")");
                            if (npf_failure.containsKey(entry.getKey())) {
                                npfRow.createCell(2).setCellValue(npf_failure.get(entry.getKey()));
                            } else {
                                npfRow.createCell(2).setCellValue(0);
                            }

                            if (npf_success.containsKey(entry.getKey())) {
                                npfRow.createCell(3).setCellValue(npf_success.get(entry.getKey()));
                            } else {
                                npfRow.createCell(3).setCellValue(0);
                            }
                            npfRow.createCell(4).setCellValue(0);
                            npfRow.createCell(5).setCellFormula("SUM(G" + (rowNumber + 1) + ":H" + (rowNumber + 1) + ")");
                            npfRow.createCell(6).setCellValue(0);
                            npfRow.createCell(7).setCellValue(0);
                            npfRow.createCell(8).setCellValue(0);
                            npfRow.createCell(9).setCellFormula("D" + (rowNumber + 1) + "+H" + (rowNumber + 1));
                            npfRow.createCell(10).setCellFormula("E" + (rowNumber + 1) + "+I" + (rowNumber + 1));
                            npfRow.getCell(0).setCellStyle(styles.get("cell"));
                            npfRow.getCell(1).setCellStyle(styles.get("cell"));
                            npfRow.getCell(2).setCellStyle(styles.get("cell"));
                            npfRow.getCell(3).setCellStyle(styles.get("cell"));
                            npfRow.getCell(4).setCellStyle(styles.get("cell"));
                            npfRow.getCell(5).setCellStyle(styles.get("cell"));
                            npfRow.getCell(6).setCellStyle(styles.get("cell"));
                            npfRow.getCell(7).setCellStyle(styles.get("cell"));
                            npfRow.getCell(8).setCellStyle(styles.get("cell"));
                            npfRow.getCell(9).setCellStyle(styles.get("cell"));
                            npfRow.getCell(10).setCellStyle(styles.get("cell"));
                            lastNpfRowNum = rowNumber;
                            rowNumber++;

                        }

                        if (contentContainer.size() > 0) {

                            Row ucRow = sheet.createRow(rowNumber);
                            ucRow.createCell(0).setCellValue(ucContainer.getItem(itemId).getItemProperty("name").toString());
                            ucRow.createCell(1).setCellFormula("SUM(B" + (firstNpfRowNum + 1) + ":B" + (lastNpfRowNum + 1) + ")");
                            ucRow.createCell(2).setCellFormula("SUM(C" + (firstNpfRowNum + 1) + ":C" + (lastNpfRowNum + 1) + ")");
                            ucRow.createCell(3).setCellFormula("SUM(D" + (firstNpfRowNum + 1) + ":D" + (lastNpfRowNum + 1) + ")");
                            ucRow.createCell(4).setCellFormula("SUM(E" + (firstNpfRowNum + 1) + ":E" + (lastNpfRowNum + 1) + ")");
                            ucRow.createCell(5).setCellFormula("SUM(F" + (firstNpfRowNum + 1) + ":F" + (lastNpfRowNum + 1) + ")");
                            ucRow.createCell(6).setCellFormula("SUM(G" + (firstNpfRowNum + 1) + ":G" + (lastNpfRowNum + 1) + ")");
                            ucRow.createCell(7).setCellFormula("SUM(H" + (firstNpfRowNum + 1) + ":H" + (lastNpfRowNum + 1) + ")");
                            ucRow.createCell(8).setCellFormula("SUM(I" + (firstNpfRowNum + 1) + ":I" + (lastNpfRowNum + 1) + ")");
                            ucRow.createCell(9).setCellFormula("SUM(J" + (firstNpfRowNum + 1) + ":J" + (lastNpfRowNum + 1) + ")");
                            ucRow.createCell(10).setCellFormula("SUM(K" + (firstNpfRowNum + 1) + ":K" + (lastNpfRowNum + 1) + ")");
                            ucRow.getCell(0).setCellStyle(styles.get("header"));
                            ucRow.getCell(1).setCellStyle(styles.get("header"));
                            ucRow.getCell(2).setCellStyle(styles.get("header"));
                            ucRow.getCell(3).setCellStyle(styles.get("header"));
                            ucRow.getCell(4).setCellStyle(styles.get("header"));
                            ucRow.getCell(5).setCellStyle(styles.get("header"));
                            ucRow.getCell(6).setCellStyle(styles.get("header"));
                            ucRow.getCell(7).setCellStyle(styles.get("header"));
                            ucRow.getCell(8).setCellStyle(styles.get("header"));
                            ucRow.getCell(9).setCellStyle(styles.get("header"));
                            ucRow.getCell(10).setCellStyle(styles.get("header"));
                            ucRowNum.add(rowNumber);
                            rowNumber++;
                        }
                    }
                }
//                if (ucRowNum.size() > 0) {
//                    
//                    for(int i = 0; i < ucRowNum.size(); i++)
//                    {
//                        
//                    }
//                    Row allRow = sheet.createRow(rowNumber);
//                    allRow.createCell(0).setCellValue(ucContainer.getItem(itemId).getItemProperty("name").toString());
//                    allRow.createCell(1).setCellFormula("SUM(B" + (firstNpfRowNum + 1) + ":B" + (lastNpfRowNum + 1) + ")");
//                    allRow.createCell(2).setCellFormula("SUM(C" + (firstNpfRowNum + 1) + ":C" + (lastNpfRowNum + 1) + ")");
//                    allRow.createCell(3).setCellFormula("SUM(D" + (firstNpfRowNum + 1) + ":D" + (lastNpfRowNum + 1) + ")");
//                    allRow.createCell(4).setCellFormula("SUM(E" + (firstNpfRowNum + 1) + ":E" + (lastNpfRowNum + 1) + ")");
//                    allRow.createCell(5).setCellFormula("SUM(F" + (firstNpfRowNum + 1) + ":F" + (lastNpfRowNum + 1) + ")");
//                    allRow.createCell(6).setCellFormula("SUM(G" + (firstNpfRowNum + 1) + ":G" + (lastNpfRowNum + 1) + ")");
//                    allRow.createCell(7).setCellFormula("SUM(H" + (firstNpfRowNum + 1) + ":H" + (lastNpfRowNum + 1) + ")");
//                    allRow.createCell(8).setCellFormula("SUM(I" + (firstNpfRowNum + 1) + ":I" + (lastNpfRowNum + 1) + ")");
//                    allRow.createCell(9).setCellFormula("SUM(J" + (firstNpfRowNum + 1) + ":J" + (lastNpfRowNum + 1) + ")");
//                    allRow.createCell(10).setCellFormula("SUM(K" + (firstNpfRowNum + 1) + ":K" + (lastNpfRowNum + 1) + ")");
//                    allRow.getCell(0).setCellStyle(styles.get("header"));
//                    allRow.getCell(1).setCellStyle(styles.get("header"));
//                    allRow.getCell(2).setCellStyle(styles.get("header"));
//                    allRow.getCell(3).setCellStyle(styles.get("header"));
//                    allRow.getCell(4).setCellStyle(styles.get("header"));
//                    allRow.getCell(5).setCellStyle(styles.get("header"));
//                    allRow.getCell(6).setCellStyle(styles.get("header"));
//                    allRow.getCell(7).setCellStyle(styles.get("header"));
//                    allRow.getCell(8).setCellStyle(styles.get("header"));
//                    allRow.getCell(9).setCellStyle(styles.get("header"));
//                    allRow.getCell(10).setCellStyle(styles.get("header"));
//                    rowNumber++;
//                }

            }
        }
        );
        saveButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                saveButton.setEnabled(false);
            }
        }
        );

        close.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    if (file != null && file.exists()) {
                        java.nio.file.Path path = Paths.get(file.getAbsolutePath());
                        Files.deleteIfExists(path);
                    }
                    getThis().close();
                } catch (IOException ex) {
                    Logger.getLogger(MonitoringCommon.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );

        setContent(mainLayout);

        this.setVisible(true);
    }

    protected void containerInit() {
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
        contentContainer.addContainerProperty("idMessage", Filestore.class, "");
    }

    public MonitoringCommon getThis() {
        return this;
    }

    private List<ContentFile> queryToContentContainer(javax.persistence.Query q) {
        List<ContentFile> out = q.getResultList();
        for (ContentFile content : out) {
            Item item = contentContainer.addItem(content.getId());
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
        }
        return out;
    }

    private static Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        CellStyle style;
        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short) 11);
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFont(titleFont);
        style.setWrapText(true);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        styles.put("title", style);

        Font monthFont = wb.createFont();
        monthFont.setFontHeightInPoints((short) 11);
        monthFont.setColor(IndexedColors.BLACK.getIndex());
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(monthFont);
        style.setWrapText(true);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        styles.put("header", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setWrapText(true);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("cell", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        styles.put("formula", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        styles.put("formula_2", style);

        return styles;
    }

}
