/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewers;

import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import control.generatedlink.CheckResultColumn;
import control.generatedlink.ContentColumn;
import java.util.Collection;
import models.pzfiles.Filestore;
import org.tepi.filtertable.FilterTable;

/**
 *
 * @author 003-0818
 */
public class FilestoreViewer extends CustomField<Object> {

    private BeanContainer<String, Filestore> beanFilestore;
    HorizontalSplitPanel filestoreSplitPanel;
    FilterTable FilestoreRowTable;

    FieldGroup fg;

    public FilestoreViewer() {
        beanFilestore = new BeanContainer<String, Filestore>(Filestore.class);
        beanFilestore.setBeanIdProperty("id");

        fg = new FieldGroup();

        filestoreSplitPanel = new HorizontalSplitPanel();
        filestoreSplitPanel.setSizeFull();

        FilestoreRowTable = new FilterTable();
        FilestoreRowTable.setContainerDataSource(beanFilestore);
        FilestoreRowTable.setVisibleColumns(new Object[]{"sendTime", "receiveTime", "chkFio", "chkSkpep", "chkEp","chkConas", "chkXml"});
        FilestoreRowTable.setColumnHeaders(new String[]{"Время отправления", "Время получения", "Проверка ФИО", "Проверка СКПЭП","Проверка ЭЦП","Проверка ФИО-СНИЛС", "Форматно-логический контроль"});
//        FilestoreRowTable.addGeneratedColumn("chkFio", new CheckResultColumn());
//        FilestoreRowTable.addGeneratedColumn("chkSkpep", new CheckResultColumn());
//        FilestoreRowTable.addGeneratedColumn("chkEp", new CheckResultColumn());
//        FilestoreRowTable.addGeneratedColumn("chkConas", new CheckResultColumn());
//        FilestoreRowTable.addGeneratedColumn("chkXml", new CheckResultColumn());
        FilestoreRowTable.setSelectable(true);

        FilestoreRowTable.setSizeFull();

        FilestoreRowTable.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {

                fg.setItemDataSource(FilestoreRowTable.getItem(event.getProperty().getValue()));

            }

        });

        filestoreSplitPanel.setFirstComponent(FilestoreRowTable);

        VerticalLayout rightPanel = new VerticalLayout();
        rightPanel.setMargin(true);
        rightPanel.setSpacing(true); 
        filestoreSplitPanel.setSecondComponent(rightPanel);

    }

    @Override
    protected Component initContent() {
        if (this.getValue() != null) {
            beanFilestore.addAll((Collection<Filestore>) this.getValue());
        }
        return filestoreSplitPanel;
    }

    @Override
    public Class<? extends Object> getType() {
        return Object.class;
    }

}
