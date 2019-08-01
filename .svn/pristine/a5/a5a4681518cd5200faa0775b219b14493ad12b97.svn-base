/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.generatedlink;

import com.vaadin.data.Item;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.Label;
import models.pzfiles.Filestore;

public class ContentColumn implements CustomTable.ColumnGenerator {

    String field;

    public ContentColumn(String field) {
        this.field = field;
    }

    @Override
    public Object generateCell(CustomTable source, Object itemId, Object columnId) {
        Item contentFile = source.getItem(itemId);
        if (this.field.equals("Фамилия")) {
            Label label = new Label();
            label.setSizeFull();
            label.setValue("<center>" + contentFile.getItemProperty("Фамилия").getValue() +  "</center>");
            label.setContentMode(ContentMode.HTML);
            return label;
        }
        if (this.field.equals("Имя")) {
            Label label = new Label();
            label.setSizeFull();
            label.setValue("<left>" + contentFile.getItemProperty("Имя").getValue() +  "</left>");
            label.setContentMode(ContentMode.HTML);
            return label;
        }
        if (this.field.equals("Отчество")) {
            Label label = new Label();
            label.setSizeFull();
            label.setValue("<center>" + contentFile.getItemProperty("Отчество").getValue() +  "</center>");
            label.setContentMode(ContentMode.HTML);
            return label;
        }
        if (this.field.equals("snils")) {
            Label label = new Label();
            label.setSizeFull();
            label.setValue("<center>" + contentFile.getItemProperty("Снилс").getValue() +  "</center>");
            label.setContentMode(ContentMode.HTML);
            return label;
        }
        if (this.field.equals("birthDate")) {
            Label label = new Label();
            label.setSizeFull();
            label.setValue("<center>" + contentFile.getItemProperty("birthDate").getValue() +  "</center>");
            label.setContentMode(ContentMode.HTML);
            return label;
        }
        if (this.field.equals("npfName")) {
            Label label = new Label();
            label.setSizeFull();
            label.setValue("<center>" + contentFile.getItemProperty("npfName").getValue() +  "</center>");
            label.setContentMode(ContentMode.HTML);
            return label;
        }
        if (this.field.equals("crPackName")) {
            Label label = new Label();
            label.setSizeFull();
            label.setValue("<left>" + contentFile.getItemProperty("crPackName").getValue() +  "</left>");
            label.setContentMode(ContentMode.HTML);
            return label;
        }
        if (this.field.equals("crPackInn")) {
            Label label = new Label();
            label.setSizeFull();
            label.setValue("<left>" + contentFile.getItemProperty("crPackInn").getValue() +  "</left>");
            label.setContentMode(ContentMode.HTML);
            return label;
        }
        if (this.field.equals("complDate")) {
            Label label = new Label();
            label.setSizeFull();
            label.setValue("<center>" + contentFile.getItemProperty("complDate").getValue() +  "</center>");
            label.setContentMode(ContentMode.HTML);
            return label;
        }
        if (this.field.equals("docType")) {
            Label label = new Label();
            label.setSizeFull();
            String str = contentFile.getItemProperty("docType").getValue().toString().replaceAll("_", " ");
            label.setValue("<center>" + str +  "</center>");
            label.setContentMode(ContentMode.HTML);
            return label;
        }
//        if (this.field.equals("idMessage")) {
//            Label label = new Label();
//            label.setSizeFull();
//            Filestore f = (Filestore) contentFile.getItemProperty("idMessage").getValue();
//            label.setValue("<center>" + f.getReceiveTime() +  "</center>");
//            label.setContentMode(ContentMode.HTML);
//            return label;
//        }
        if (this.field.equals("commonChk")) {
            Label label = new Label();
            label.setSizeFull();
            label.setValue("<center>" + contentFile.getItemProperty("commonChk").getValue() +  "</center>");
            label.setContentMode(ContentMode.HTML);
            return label;
        }
        return null;
    }
    

}
