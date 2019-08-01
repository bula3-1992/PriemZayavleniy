/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.system;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import dao.controllers.ParamJpaController;
import dao.factory.Factory;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import models.pzfiles.Param;

/**
 *
 * @author 003-0823
 */
public class SettingsView extends VerticalLayout implements View {

    ParamJpaController pjc = new ParamJpaController();

    public SettingsView() {
        Table table = new Table();
        table.addContainerProperty("Параметр", String.class, null);
        table.addContainerProperty("Значение", TextField.class, null);
        final Param cert = pjc.findParam("certpath");
        final TextField certField = new TextField();
        certField.setValue(cert.getDescription());
        certField.setWidth("400px");
        table.addItem(new Object[]{"Путь к папке с сертификатами", certField}, 1);
        final Param crl = pjc.findParam("crlpath");
        final TextField crlField = new TextField();
        crlField.setValue(crl.getDescription());
        crlField.setWidth("400px");
        table.addItem(new Object[]{"Путь к папке с CRL", crlField}, 2);
        final Param checkxml = pjc.findParam("checkxmlpath");
        final TextField checkxmlField = new TextField();
        checkxmlField.setValue(checkxml.getDescription());
        checkxmlField.setWidth("400px");
        table.addItem(new Object[]{"Путь к папке с CheckXML", checkxmlField}, 3);
        
        final Param sysnameAS = pjc.findParam("sysnameAS");
        final TextField sysnameASField = new TextField();
        sysnameASField.setValue(sysnameAS.getDescription());
        sysnameASField.setWidth("400px");
        table.addItem(new Object[]{"Система AS400 (не изменять)", sysnameASField}, 4);
        final Param userAS = pjc.findParam("userAS");
        final TextField userASField = new TextField();
        userASField.setValue(userAS.getDescription());
        userASField.setWidth("400px");
        table.addItem(new Object[]{"Профайл AS400 (не изменять)", userASField}, 5);
        final Param passwordAS = pjc.findParam("passwordAS");
        final TextField passwordASField = new TextField();
        passwordASField.setValue("******");
        passwordASField.setWidth("400px");
        table.addItem(new Object[]{"Пароль AS400 (не изменять)", passwordASField}, 6);        
        final Param filestore = pjc.findParam("filestorepath");
        final TextField filestoreField = new TextField();
        filestoreField.setValue(filestore.getDescription());
        filestoreField.setWidth("400px");
        table.addItem(new Object[]{"Путь для сохранения отчетов", filestoreField}, 7);
        
        addComponent(table);
        Button save = new Button("Сохранить");
        save.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    cert.setDescription(certField.getValue());
                    pjc.edit(cert);
                    crl.setDescription(crlField.getValue());
                    pjc.edit(crl);
                    checkxml.setDescription(checkxmlField.getValue());
                    pjc.edit(checkxml);
//                    sysnameAS.setDescription(sysnameASField.getValue());
//                    pjc.edit(sysnameAS);
//                    userAS.setDescription(userASField.getValue());
//                    pjc.edit(userAS);
//                    passwordAS.setDescription(passwordASField.getValue());
//                    pjc.edit(passwordAS);
                    filestore.setDescription(filestoreField.getValue());
                    pjc.edit(filestore);
                } catch (Exception ex) {
                    Logger.getLogger(SettingsView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        addComponent(save);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}
