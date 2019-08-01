/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.generatedlink;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import dao.bpi.BpiJpaController;
import java.text.SimpleDateFormat;
import models.pzfiles.ContentFile;
import models.pzfiles.Filestore;

public class FilestoreColumnGenerator implements com.vaadin.ui.Table.ColumnGenerator {

    String field;

    public FilestoreColumnGenerator(String field) {
        this.field = field;
    }

    @Override
    public Object generateCell(Table source, Object itemId, Object columnId) {
        EntityItem<ContentFile> content = (EntityItem<ContentFile>) source.getItem(itemId);
        if (this.field.equals("surname")) {
            Label label = new Label();
            label.setSizeFull();
            label.setValue("<center>" + content.getEntity().getSurname() +  "</center>");
            label.setContentMode(ContentMode.HTML);
            return label;
        }
        if (this.field.equals("snils")) {
            Label label = new Label();
            label.setSizeFull();
            label.setValue("<center>" + content.getEntity().getSnils() +  "</center>");
            label.setContentMode(ContentMode.HTML);
            return label;
        }
        if (this.field.equals("birthDate")) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Label label = new Label();
            label.setSizeFull();
            label.setValue("<center>" + sdf.format(content.getEntity().getBirthDate()) +  "</center>");
            label.setContentMode(ContentMode.HTML);
            return label;
        }
        if (this.field.equals("npfName")) {
            Label label = new Label();
            label.setSizeFull();
            label.setValue("<center>" + content.getEntity().getNpfName() +  "</center>");
            label.setContentMode(ContentMode.HTML);
            return label;
        }
        if (this.field.equals("senderId.name")) {
            Label label = new Label();
            label.setSizeFull();
            label.setValue("<center>" + content.getEntity().getRegNum() +  "</center>");
            label.setContentMode(ContentMode.HTML);
            return label;
        }
        if (this.field.equals("complDate")) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Label label = new Label();
            label.setSizeFull();
            label.setValue("<center>" + sdf.format(content.getEntity().getComplDate()) +  "</center>");
            label.setContentMode(ContentMode.HTML);
            return label;
        }
        if (this.field.equals("docType")) {
            Label label = new Label();
            label.setSizeFull();
            String str = content.getEntity().getDocType().replaceAll("_", " ");
            label.setValue("<center>" + str +  "</center>");
            label.setContentMode(ContentMode.HTML);
            return label;
        }
        if (this.field.equals("commonChk.description")) {
            Label label = new Label();
            label.setSizeFull();
            label.setValue("<center>" + content.getEntity().getCommonChk() +  "</center>");
            label.setContentMode(ContentMode.HTML);
            return label;
        }
        return null;
    }

    
    

}
