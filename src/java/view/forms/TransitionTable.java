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
import models.pzfiles.Client;
import models.pzfiles.ContentFile;
import models.pzfiles.Npf;

/**
 *
 * @author 003-0818
 */
public class TransitionTable extends Window {

    private VerticalLayout mainLayout = new VerticalLayout();

    private Button saveExcel = new Button("Скачать отчет");
    TextField surname_Field = new TextField("Фамилия");
    TextField firstname_Field = new TextField("Имя");
    TextField secondname_Field = new TextField("Отчество");
    TextField snils_Field = new TextField("Снилс");
    TextField filename_Field = new TextField("Имя файла");
    DateField date_Field1 = new DateField("Дата заполнения");
    DateField date_Field2 = new DateField("Дата заполнения");
    Button report = new Button("Сформировать отчет");
    Button close = new Button("Закрыть");
    private EntityManager pzFilesEntityManager;
//    private JPAContainer<Npf> npfContainer;
//    private JPAContainer<Client> ucContainer;
    private JPAContainer<ContentFile> contentContainer;
    Calendar c = Calendar.getInstance();

    Resource res;
    FileDownloader fd;
    String snils = "";
    String complDate1 = "";
    String complDate2 = "";
    String crPackName = "";
    String fileName = "";
    String npfName = "";
    ClientJpaController cjc = new ClientJpaController();
    NpfJpaController njc = new NpfJpaController();
    File file;

    public TransitionTable() {
        setCaption("Таблица переходов");
        saveExcel.setEnabled(false);
        pzFilesEntityManager = Factory.getInstance().getPzFilesEmf().createEntityManager();
        contentContainer = JPAContainerFactory.make(ContentFile.class, pzFilesEntityManager);
        final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        final SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss");
        Date referenceDate = new Date();
        c.setTime(referenceDate);
        date_Field1.setValue(c.getTime());
        c.add(Calendar.WEEK_OF_YEAR, -1);
        date_Field2.setValue(c.getTime());
        if (date_Field1.getValue() != null && !date_Field1.getValue().equals("")) {
            date_Field1.setValue(c.getTime());
        }

        if (date_Field2.getValue() != null && !date_Field2.getValue().equals("")) {
            date_Field2.setValue(c.getTime());
        }
        center();
        setWidth("35%");
        setHeight("40%");
//        ucContainer = JPAContainerFactory.make(Client.class, pzFilesEntityManager);
//        uc_Box.setContainerDataSource(ucContainer);
//        uc_Box.setItemCaptionPropertyId("name");
//        uc_Box.setImmediate(true);
//
//        npfContainer = JPAContainerFactory.make(Npf.class, pzFilesEntityManager);
//        npf_Box.setContainerDataSource(npfContainer);
//        npf_Box.setItemCaptionPropertyId("name");
//        npf_Box.setImmediate(true);

        HorizontalLayout hl1 = new HorizontalLayout();
        hl1.setSizeFull();
        hl1.addComponent(surname_Field);
        hl1.addComponent(firstname_Field);
        hl1.addComponent(secondname_Field);

        HorizontalLayout hl2 = new HorizontalLayout();
        hl2.setSizeFull();
        hl2.addComponent(snils_Field);
        hl2.addComponent(filename_Field);

//        HorizontalLayout hl3 = new HorizontalLayout();
//        hl3.setSizeFull();
//        hl3.addComponent(uc_Box);
//
//        HorizontalLayout hl4 = new HorizontalLayout();
//        hl4.setSizeFull();
//        hl4.addComponent(npf_Box);
        HorizontalLayout hl5 = new HorizontalLayout();
        hl5.setSizeFull();
        hl5.addComponent(date_Field1);
        hl5.addComponent(date_Field2);

        HorizontalLayout hl6 = new HorizontalLayout();
        hl6.setSizeFull();
        hl6.addComponent(report);
        hl6.addComponent(saveExcel);
        hl6.addComponent(close);
        report.setSizeFull();
        saveExcel.setSizeFull();
        close.setSizeFull();
        surname_Field.setSizeFull();
        firstname_Field.setSizeFull();
        secondname_Field.setSizeFull();
        snils_Field.setSizeFull();
        filename_Field.setSizeFull();
//        uc_Box.setSizeFull();
//        npf_Box.setSizeFull();
        date_Field1.setSizeFull();
        date_Field2.setSizeFull();
        mainLayout.addComponent(hl1);
        mainLayout.addComponent(hl2);
//        mainLayout.addComponent(hl3);
//        mainLayout.addComponent(hl4);
        mainLayout.addComponent(hl5);
        mainLayout.addComponent(hl6);
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
                contentContainer.removeAllContainerFilters();
                file = new File("C:\\temp\\translation_report_" + sdf2.format(new Date()) + ".csv");
                PrintWriter writer = null;

                if (!surname_Field.getValue().equals("")) {
                    surname_Field.setValue(surname_Field.getValue().toUpperCase());
                    contentContainer.addContainerFilter(new Compare.Equal("surname", surname_Field.getValue()));
                }
                if (!firstname_Field.getValue().equals("")) {
                    firstname_Field.setValue(firstname_Field.getValue().toUpperCase());
                    contentContainer.addContainerFilter(new Compare.Equal("firstname", firstname_Field.getValue()));
                }
                if (!secondname_Field.getValue().equals("")) {
                    secondname_Field.setValue(secondname_Field.getValue().toUpperCase());
                    contentContainer.addContainerFilter(new Compare.Equal("secondname", secondname_Field.getValue()));
                }
                if (!snils_Field.getValue().equals("")) {
                    contentContainer.addContainerFilter(new Compare.Equal("snils", snils_Field.getValue()));
                }
                if (!filename_Field.getValue().equals("")) {
                    contentContainer.addContainerFilter(new Compare.Equal("fileName", filename_Field.getValue()));
                }
//                if (uc_Box.getValue() != null && !uc_Box.getValue().equals("")) {
//                    contentContainer.addContainerFilter(new Compare.Equal("crPackName", cjc.findClient((int) uc_Box.getValue()).getName()));
//                }
//                if (npf_Box.getValue() != null && !npf_Box.getValue().equals("")) {
//                    contentContainer.addContainerFilter(new Compare.Equal("npfName", njc.findNpf((Long) uc_Box.getValue()).getName()));
//                }
                if (date_Field1.getValue() != null && !date_Field1.getValue().equals("")) {
                    contentContainer.addContainerFilter(new Compare.GreaterOrEqual("complDate", date_Field1.getValue()));
                }
                if (date_Field2.getValue() != null && !date_Field2.getValue().equals("")) {
                    contentContainer.addContainerFilter(new Compare.LessOrEqual("complDate", date_Field2.getValue()));
                }
                contentContainer.sort(new String[]{"snils, surname, firstname, secondname, compl_date"}, new boolean[]{true, true, true, true, true});
                boolean error = false;
                Long begin = null, end = null;
                if (date_Field1.getValue() != null) {
                    if (!date_Field1.getValue().equals("")) {
                        begin = date_Field1.getValue().getTime();
                    }
                }

                if (date_Field2.getValue() != null) {
                    if (!date_Field2.getValue().equals("")) {
                        end = date_Field2.getValue().getTime();
                    }
                }
                if (end != null && begin != null) {
                    long dif = (end - begin) / (1000 * 60 * 60 * 24);
                    if (dif < 0) {
                        Notification.show("Начальная дата выборки больше конечной даты выборки!", Notification.Type.ERROR_MESSAGE);
                        error = true;
                    }
                }
//                if (surname_Field.getValue().equals("")
//                        && firstname_Field.getValue().equals("")
//                        && secondname_Field.getValue().equals("")
//                        && snils_Field.getValue().equals("")) {
//                    Notification.show("Поля ФИО и снилс незаполены", Notification.Type.ERROR_MESSAGE);
//                    error = true;
//                }

                if (!error) {
                    report.setComponentError(null);
                    try {
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        writer = new PrintWriter(file, "windows-1251");
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(CountAppNPFMenu.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(CountAppNPFMenu.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(CountAppNPFMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    System.out.println("| | " + contentContainer.size() + " " + date_Field1.getValue() + " " + date_Field2.getValue());

                    int i = 0;
                    writer.write("\"#\";\"СНИЛС\";\"Фамилия\";\"Имя\";\"Отчество\";\"Дата рождения\";\"Имя файла 1\";\"УЦ 1\";\"НПФ(УК) 1\";\"Дата заполнения 1\";\"Тип заявления 1\";\"Имя файла 2\";\"УЦ 2\";\"НПФ(УК) 2\";\"Дата заполнения 2\";\"Тип заявления 2\"");
                    writer.write("\n");

                    writer.close();

                    res = new FileResource(file);
                    fd = new FileDownloader(res);
                    fd.extend(saveExcel);
                    saveExcel.setEnabled(true);
                }
            }
        }
        );

        saveExcel.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                saveExcel.setEnabled(false);
                fd = null;
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

        setContent(mainLayout);

        this.setVisible(true);
    }

    public TransitionTable getThis() {
        return this;
    }

}
