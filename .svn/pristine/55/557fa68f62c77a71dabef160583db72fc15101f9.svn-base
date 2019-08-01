/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.forms;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import control.MainViewController;
import dao.controllers.ParamJpaController;
import models.pzfiles.ContentFile;

/**
 *
 * @author 003-0818
 */
public class ItemCard extends Window {

    private final VerticalLayout mainLayout = new VerticalLayout();
    TextField uc_Field = new TextField("УЦ");
    TextField chkfio_Field = new TextField("Проверка ФИО");
    TextField chkxml_Field = new TextField("Форматно-логический контроль");
    TextField chkconas_Field = new TextField("Проверка СНИЛС");
    TextField chkskpep_Field = new TextField("Проверка СКПЕП");
    TextField chkep_Field = new TextField("Проверка ЭЦП");
    ParamJpaController pjc = new ParamJpaController();
    final Button checkButton = new Button("Проверить");

    public ItemCard(final CustomTable table, final Item itemCard, final Object itemId) {

        center();
        setWidth("40%");
        setHeight("60%");
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);
        mainLayout.setWidth("100%");
        this.setVisible(true);

        checkButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                ContentFile content = MainViewController.checkOne(table, (Long) itemId);
                chkfio_Field.setValue(content.getChkFio());
                chkskpep_Field.setValue(content.getChkSkpep());
                chkep_Field.setValue(content.getChkEp());
                chkconas_Field.setValue(content.getChkConas());
                chkxml_Field.setValue(content.getChkXml());
            }
        }
        );
        setCaption(itemCard.getItemProperty("Фамилия").getValue().toString() + " " + itemCard.getItemProperty("Имя").getValue().toString() + " " + itemCard.getItemProperty("Отчество").getValue().toString());
        chkfio_Field.setValue(itemCard.getItemProperty("chkFio").getValue().toString());
        chkxml_Field.setValue(itemCard.getItemProperty("chkXml").getValue().toString());
        chkconas_Field.setValue(itemCard.getItemProperty("chkConas").getValue().toString());
        chkskpep_Field.setValue(itemCard.getItemProperty("chkSkpep").getValue().toString());
        chkep_Field.setValue(itemCard.getItemProperty("chkEp").getValue().toString());
        uc_Field.setValue(itemCard.getItemProperty("crPackName").getValue().toString());
        uc_Field.setWidth("100%");
        chkfio_Field.setWidth("100%");
        chkxml_Field.setWidth("100%");
        chkconas_Field.setWidth("100%");
        chkskpep_Field.setWidth("100%");
        chkep_Field.setWidth("100%");
        mainLayout.addComponent(uc_Field);
        mainLayout.addComponent(chkfio_Field);
        mainLayout.addComponent(chkxml_Field);
        mainLayout.addComponent(chkconas_Field);
        mainLayout.addComponent(chkskpep_Field);
        mainLayout.addComponent(chkep_Field);
        mainLayout.addComponent(checkButton);
        setContent(mainLayout);
    }
}
