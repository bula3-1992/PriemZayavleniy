/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.generatedlink;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;

/**
 *
 * @author 003-0823
 */
public class CheckResultColumn  implements com.vaadin.ui.Table.ColumnGenerator {

    public CheckResultColumn() {
    }

    @Override
    public Object generateCell(final Table source, final Object itemId, Object columnId) {
        final Item item = source.getItem(itemId);
        final Button checkButton = new Button(item.getItemProperty("commonChk").getValue().toString());
        checkButton.setStyleName(BaseTheme.BUTTON_LINK);
        checkButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Window subWindow = new Window("Статусы проверок");
                VerticalLayout content = new VerticalLayout();
                content.addComponent(new Label("Проверка ФИО: " + (String) item.getItemProperty("chkFio").getValue()));
                content.addComponent(new Label("Проверка СКПЭП: " + (String) item.getItemProperty("chkSkpep").getValue()));
                content.addComponent(new Label("Проверка ЭЦП: " + (String) item.getItemProperty("chkEp").getValue()));
                content.addComponent(new Label("Проверка CheckXML: " + (String) item.getItemProperty("chkXml").getValue()));
                content.addComponent(new Label("Проверка ПТК СПУ: " + (String) item.getItemProperty("chkConas").getValue()));
                content.addComponent(new Label("Возникшие исключения: " + (String) item.getItemProperty("chkException").getValue()));
                subWindow.setContent(content);
                subWindow.center();
                UI.getCurrent().addWindow(subWindow);
            }
        }
        );
        return checkButton;
    }

}

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package control.generatedlink;
//
//import com.vaadin.data.Item;
//import com.vaadin.ui.Button;
//import com.vaadin.ui.CustomTable;
//import com.vaadin.ui.Label;
//import com.vaadin.ui.UI;
//import com.vaadin.ui.VerticalLayout;
//import com.vaadin.ui.Window;
//import com.vaadin.ui.themes.BaseTheme;
//import models.pzfiles.CommonCheck;
//
///**
// *
// * @author 003-0823
// */
//public class CheckResultColumn implements CustomTable.ColumnGenerator {
//
//    public CheckResultColumn() {
//    }
//
//    @Override
//    public Object generateCell(final CustomTable source, final Object itemId, Object columnId) {
//        final Item item = source.getItem(itemId);
//        CommonCheck commonCheck = (CommonCheck) item.getItemProperty("commonChk").getValue();
//        final Button checkButton = new Button(commonCheck.getDescription());
//        checkButton.setStyleName(BaseTheme.BUTTON_LINK);
//        checkButton.addListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                Window subWindow = new Window("Статусы проверок");
//                VerticalLayout content = new VerticalLayout();
//                content.addComponent(new Label("Проверка ФИО: " + (String) item.getItemProperty("chkFio").getValue()));
//                content.addComponent(new Label("Проверка СКПЭП: " + (String) item.getItemProperty("chkSkpep").getValue()));
//                content.addComponent(new Label("Проверка ЭЦП: " + (String) item.getItemProperty("chkEp").getValue()));
//                content.addComponent(new Label("Проверка CheckXML: " + (String) item.getItemProperty("chkXml").getValue()));
//                content.addComponent(new Label("Проверка ПТК СПУ: " + (String) item.getItemProperty("chkConas").getValue()));
//                content.addComponent(new Label("Возникшие исключения: " + (String) item.getItemProperty("chkException").getValue()));
//                subWindow.setContent(content);
//                subWindow.center();
//                UI.getCurrent().addWindow(subWindow);
//            }
//        }
//        );
//        return checkButton;
//    }
//}
