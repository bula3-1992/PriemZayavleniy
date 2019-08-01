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
import java.util.List;
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
public class CountAppNPFMenu extends Window {

    private VerticalLayout mainLayout = new VerticalLayout();

    private FileDownloader fd;
    private Button saveExcel = new Button("Скачать отчет");
    TextField surname_Field = new TextField("Фамилия");
    TextField firstname_Field = new TextField("Имя");
    TextField secondname_Field = new TextField("Отчество");
    TextField snils_Field = new TextField("Снилс");
    TextField filename_Field = new TextField("Имя файла");
    DateField birthdate_Field = new DateField("Дата рождения");
    DateField date_Field1 = new DateField("Дата заполнения");
    DateField date_Field2 = new DateField("Дата заполнения");
    ComboBox uc_Box = new ComboBox("УЦ");
    ComboBox npf_Box = new ComboBox("НПФ");
    Button report = new Button("Сформировать отчет");
    Button close = new Button("Закрыть");
    private EntityManager pzFilesEntityManager;
    private JPAContainer<Npf> npfContainer;
    private JPAContainer<Client> ucContainer;
    private JPAContainer<ContentFile> contentContainer;
    Calendar c = Calendar.getInstance();

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

    public CountAppNPFMenu() {
        setCaption("Количество поступивших заявлений");
        saveExcel.setEnabled(false);
        Date referenceDate = new Date();
        c.setTime(referenceDate);
        date_Field1.setValue(c.getTime());
        c.add(Calendar.WEEK_OF_YEAR, -1);
        date_Field2.setValue(c.getTime());
        pzFilesEntityManager = Factory.getInstance().getPzFilesEmf().createEntityManager();
        contentContainer = JPAContainerFactory.make(ContentFile.class, pzFilesEntityManager);
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
        uc_Box.setContainerDataSource(ucContainer);
        uc_Box.setItemCaptionPropertyId("name");
        uc_Box.setImmediate(true);

        npfContainer = JPAContainerFactory.make(Npf.class, pzFilesEntityManager);
        npfContainer.sort(new String[]{"name"}, new boolean[]{true});
        npf_Box.setContainerDataSource(npfContainer);
        npf_Box.setItemCaptionPropertyId("name");
        npf_Box.setImmediate(true);

        HorizontalLayout hl1 = new HorizontalLayout();
        hl1.setSizeFull();
        hl1.addComponent(surname_Field);
        hl1.addComponent(firstname_Field);
        hl1.addComponent(secondname_Field);
//        hl1.addComponent(snils_Field);

        HorizontalLayout hl2 = new HorizontalLayout();
        hl2.setSizeFull();
        hl2.addComponent(birthdate_Field);
        hl2.addComponent(snils_Field);

        HorizontalLayout hl3 = new HorizontalLayout();
        hl3.setSizeFull();
        hl3.addComponent(filename_Field);

        HorizontalLayout hl4 = new HorizontalLayout();
        hl4.setSizeFull();
        hl4.addComponent(uc_Box);

        HorizontalLayout hl5 = new HorizontalLayout();
        hl5.setSizeFull();
        hl5.addComponent(npf_Box);

        HorizontalLayout hl6 = new HorizontalLayout();
        hl6.setSizeFull();
        hl6.addComponent(date_Field1);
        hl6.addComponent(date_Field2);

        HorizontalLayout hl7 = new HorizontalLayout();
        hl7.setSizeFull();
        hl7.addComponent(report);
        hl7.addComponent(saveExcel);
        hl7.addComponent(close);
        report.setSizeFull();
        saveExcel.setSizeFull();
        close.setSizeFull();
        surname_Field.setSizeFull();
        firstname_Field.setSizeFull();
        secondname_Field.setSizeFull();
        snils_Field.setSizeFull();
        filename_Field.setSizeFull();
        uc_Box.setSizeFull();
        npf_Box.setSizeFull();
        birthdate_Field.setSizeFull();
        date_Field1.setSizeFull();
        date_Field2.setSizeFull();
        mainLayout.addComponent(hl1);
        mainLayout.addComponent(hl2);
        mainLayout.addComponent(hl3);
        mainLayout.addComponent(hl4);
        mainLayout.addComponent(hl5);
        mainLayout.addComponent(hl6);
        mainLayout.addComponent(hl7);
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);

        report.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {

//                report.setEnabled(false);
                saveExcel.setEnabled(false);
                contentContainer.removeAllContainerFilters();
                file = new File("C:\\temp\\report.csv");
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
                if (uc_Box.getValue() != null && !uc_Box.getValue().equals("")) {

                    contentContainer.addContainerFilter(new Compare.Equal("regNum", cjc.findClient((int) uc_Box.getValue()).getPfNom()));
                }
                if (npf_Box.getValue() != null && !npf_Box.getValue().equals("")) {
                    contentContainer.addContainerFilter(new Compare.Equal("npfName", njc.findNpf((int) npf_Box.getValue()).getName()));
                }
                if (birthdate_Field.getValue() != null && !birthdate_Field.getValue().equals("")) {
                    contentContainer.addContainerFilter(new Compare.GreaterOrEqual("birthDate", birthdate_Field.getValue()));
                }
                if (date_Field1.getValue() != null && !date_Field1.getValue().equals("")) {
                    contentContainer.addContainerFilter(new Compare.GreaterOrEqual("complDate", date_Field1.getValue()));
                } else {
                    date_Field1.setValue(c.getTime());
                    contentContainer.addContainerFilter(new Compare.GreaterOrEqual("complDate", date_Field1.getValue()));
                }
                if (date_Field2.getValue() != null && !date_Field2.getValue().equals("")) {
                    contentContainer.addContainerFilter(new Compare.LessOrEqual("complDate", date_Field2.getValue()));
                } else {
                    date_Field2.setValue(c.getTime());
                    contentContainer.addContainerFilter(new Compare.LessOrEqual("complDate", date_Field2.getValue()));
                }
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
                        Logger.getLogger(CountAppNPFMenu.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(CountAppNPFMenu.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(CountAppNPFMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }

//                    System.out.println("|SIZE| " + contentContainer.size() + " " + date_Field1.getValue() + " " + date_Field2.getValue());
                    int i = 0;
                    writer.write("\"#\";\"СНИЛС\";\"Фамилия\";\"Имя\";\"Отчество\";\"Дата рождения\";\"Имя файла\";\"УЦ\";\"НПФ\";\"Дата заполнения\";\"Тип заявления\"");
                    writer.write("\n");
                    for (Object ob : contentContainer.getItemIds()) {
                        if (contentContainer.getItem(ob).getEntity().getIdMessage() == null) {
                            continue;
                        }
                        i++;
                        System.out.println("|" + contentContainer.getItem(ob).getEntity().getIdMessage() + "|");
                        writer.write("\"" + i + "\";\"" + contentContainer.getItem(ob).getEntity().getSnils() + "\";\"" + contentContainer.getItem(ob).getEntity().getSurname() + "\";\"" + contentContainer.getItem(ob).getEntity().getFirstname() + "\";\"" + contentContainer.getItem(ob).getEntity().getSecondname() + "\";\"" + sdf.format(contentContainer.getItem(ob).getEntity().getBirthDate()) + "\";\"" + contentContainer.getItem(ob).getEntity().getFileName() + "\";\"" + contentContainer.getItem(ob).getEntity().getCrPackName() + "\";\"" + contentContainer.getItem(ob).getEntity().getNpfName() + "\";\"" + sdf.format(contentContainer.getItem(ob).getEntity().getComplDate()) + "\";\"" + contentContainer.getItem(ob).getEntity().getDocType() + "\"");
                        writer.write("\n");
                    }
                    writer.close();

                    Resource res = new FileResource(file);
                    if (fd != null) {
                        fd.remove();
                    }
                    fd = new FileDownloader(res) {

                        @Override
                        public boolean handleConnectorRequest(VaadinRequest request, VaadinResponse response, String path) throws IOException {
                            if (true) {
                                return super.handleConnectorRequest(request, response, path);
                            } else {
                                Notification.show("Введены недопустимые параметры!");
                                return true;
                            }

                        }
                    };
                    fd.extend(saveExcel);
                    saveExcel.setEnabled(true);

                    try {
                        new FileInputStream(file.getAbsolutePath());
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(CountAppNPFMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }
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

    public CountAppNPFMenu getThis() {
        return this;
    }

}
