/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package view.system;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;
import control.pagedfiltertable.PagedFilterControlConfig;
import control.pagedfiltertable.PagedFilterTable;
import dao.factory.Factory;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import models.pzfiles.Npf;

/**
 *
 * @author 003-0823
 */
public class NPFView extends VerticalLayout implements View {

    private final JPAContainer<Npf> Npf;
    private final PagedFilterTable<IndexedContainer> npfTable;
    private final EntityManager pzFilesEntityManager;

    public NPFView() {
        final VerticalLayout mainLayout = new VerticalLayout();
        pzFilesEntityManager = Factory.getInstance().getPzFilesEmf().createEntityManager();
        Npf = JPAContainerFactory.make(Npf.class, pzFilesEntityManager);
        npfTable = new PagedFilterTable<>();
        npfTable.setWidth("100%");
        npfTable.setFilterBarVisible(true);
        npfTable.setContainerDataSource(Npf);    
        npfTable.setVisibleColumns(new Object[]{"name", "inn", "kpp", "type"});
        npfTable.setColumnHeaders(new String[]{"Название НПФ (УК)", "ИНН", "КПП", "Тип"});    
        mainLayout.addComponent(npfTable);
        mainLayout.addComponent(npfTable.createControls(new PagedFilterControlConfig()));
        mainLayout.setExpandRatio(npfTable, 1);
        addComponent(mainLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
    
}
