/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.forms;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
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
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.Query;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import models.pzfiles.Client;
import models.pzfiles.ContentFile;
import models.pzfiles.Npf;

/**
 *
 * @author 003-0818
 */
public class StatisticAppNPFMenu extends Window {

    private VerticalLayout mainLayout = new VerticalLayout();

    private String basepath;
    private Button saveExcel = new Button("Скачать отчет");
    DateField date_Field1 = new DateField("Дата заполнения");
    DateField date_Field2 = new DateField("Дата заполнения");
    Button report = new Button("Сформировать отчет");
    Button close = new Button("Закрыть");
    private EntityManager pzFilesEntityManager;
    Calendar c = Calendar.getInstance();

    String complDate1 = "";
    String complDate2 = "";
    File file;

    public StatisticAppNPFMenu() {
        setCaption("Статистика заявлений");
        saveExcel.setEnabled(false);
        pzFilesEntityManager = Factory.getInstance().getPzFilesEmf().createEntityManager();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss");
        Date referenceDate = new Date();
        c = Calendar.getInstance();
        c.setTime(referenceDate);
        c.getTime();
        if (date_Field1.getValue() != null && !date_Field1.getValue().equals("")) {
            date_Field1.setValue(c.getTime());
        }

        if (date_Field2.getValue() != null && !date_Field2.getValue().equals("")) {
            date_Field2.setValue(c.getTime());
        }
        center();
        setWidth("30%");
        setHeight("45%");
        HorizontalLayout hl1 = new HorizontalLayout();
        hl1.setSizeFull();
        hl1.addComponent(date_Field1);
        HorizontalLayout hl2 = new HorizontalLayout();
        hl2.setSizeFull();
        hl2.addComponent(date_Field2);
        HorizontalLayout hl3 = new HorizontalLayout();
        hl3.setSizeFull();
        hl3.addComponent(report);
        hl3.addComponent(saveExcel);
        hl3.addComponent(close);
        report.setSizeFull();
        saveExcel.setSizeFull();
        close.setSizeFull();
        date_Field1.setSizeFull();
        date_Field2.setSizeFull();
        mainLayout.addComponent(hl1);
        mainLayout.addComponent(hl2);
        mainLayout.addComponent(hl3);
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);
        close.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {

            }
        }
        );

        report.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {

                report.setEnabled(false);
                saveExcel.setEnabled(false);
                file = new File("C:\\temp\\report_" + sdf2.format(new Date()) + ".csv");
                PrintWriter writer = null;

//                
                long dif = (date_Field2.getValue().getTime() - date_Field1.getValue().getTime()) / (1000 * 60 * 60 * 24);
                boolean error = false;

//                if (dif > 7) {
//                    Notification.show("Время выборки больше недели!", Notification.Type.ERROR_MESSAGE);
//                    error = true;
//                }
                if (dif < 0) {
                    Notification.show("Начальная дата выборки больше конечной даты выборки!", Notification.Type.ERROR_MESSAGE);
                    error = true;
                    saveExcel.setEnabled(true);
                }
                if (!error) {
                    report.setComponentError(null);
                    try {
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        writer = new PrintWriter(file, "windows-1251");
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(StatisticAppNPFMenu.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(StatisticAppNPFMenu.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(StatisticAppNPFMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    CriteriaBuilder cb = pzFilesEntityManager.getCriteriaBuilder();
                    CriteriaQuery cq = cb.createQuery();
                    Root e = cq.from(ContentFile.class);
                    cq.where(cb.and(cb.greaterThan(e.get("complDate"), date_Field1.getValue()), cb.lessThan(e.get("complDate"), date_Field2.getValue())));
                    cq.orderBy(cb.asc(e.get("id")));

//                    System.out.println(result.size());
                    writer.close();
                    basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();

                    Resource res = new FileResource(file);
                    FileDownloader fd = new FileDownloader(res);

                    fd.extend(saveExcel);
                    saveExcel.setEnabled(true);

                    try {
                        new FileInputStream(file.getAbsolutePath());
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(StatisticAppNPFMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
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
                    Logger.getLogger(ApplicationTable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );

        saveExcel.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                saveExcel.setEnabled(false);
            }
        }
        );

        setContent(mainLayout);

        this.setVisible(true);
    }

    public StatisticAppNPFMenu getThis() {
        return this;
    }

}
