/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control.pagedfiltertable;

import com.vaadin.data.Property;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import org.tepi.filtertable.FilterTable;

enum State {

    CREATED, PROCESSING, PROCESSED, FINISHED;
}

/**
 *
 * @author 003-0823
 */
public class PagedFilterTableButtons {

    public static Component buildButtons(final FilterTable relatedFilterTable) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setHeight(null);
        buttonLayout.setWidth("100%");
        buttonLayout.setSpacing(true);

        CheckBox showFilters = new CheckBox("Панель фильтрации");
//        showFilters.setValue(relatedFilterTable.isFilterBarVisible());
        showFilters.setImmediate(true);
        showFilters.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                relatedFilterTable.setFilterBarVisible((Boolean) event
                        .getProperty().getValue());

            }
        });
        buttonLayout.addComponent(showFilters);
        buttonLayout.setComponentAlignment(showFilters, Alignment.MIDDLE_RIGHT);
        buttonLayout.setExpandRatio(showFilters, 1);

        CheckBox wrapFilters = new CheckBox("Wrap Filter Fields");
        wrapFilters.setValue(relatedFilterTable.isWrapFilters());
        wrapFilters.setImmediate(true);
        wrapFilters.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                relatedFilterTable.setWrapFilters((Boolean) event.getProperty()
                        .getValue());
            }
        });
        //buttonLayout.addComponent(wrapFilters);
        //buttonLayout.setComponentAlignment(wrapFilters, Alignment.MIDDLE_RIGHT);

        final Button runNow = new Button("Применение фильтров");
        runNow.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                relatedFilterTable.runFilters();
            }
        });

        CheckBox runOnDemand = new CheckBox("Фильтрация по кнопке");
        runOnDemand.setValue(relatedFilterTable.isFilterOnDemand());
        runNow.setEnabled(relatedFilterTable.isFilterOnDemand());
        runOnDemand.setImmediate(true);
        runOnDemand.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                boolean value = (Boolean) event.getProperty().getValue();
                relatedFilterTable.setFilterOnDemand(value);
                runNow.setEnabled(value);
            }
        });
        buttonLayout.addComponent(runOnDemand);
        buttonLayout.setComponentAlignment(runOnDemand, Alignment.MIDDLE_RIGHT);
        buttonLayout.addComponent(runNow);

        Button setVal = new Button("Set the State filter to 'Processed'");
        setVal.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                relatedFilterTable
                        .setFilterFieldValue("state", State.PROCESSED);
            }
        });
        //buttonLayout.addComponent(setVal);

        Button reset = new Button("Reset");
        reset.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                relatedFilterTable.resetFilters();
            }
        });
        //buttonLayout.addComponent(reset);

        Button clear = new Button("Очистить");
        clear.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                relatedFilterTable.clearFilters();
            }
        });
        buttonLayout.addComponent(clear);

        return buttonLayout;
    }
}
