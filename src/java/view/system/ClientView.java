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
import models.pzfiles.Client;
import javax.persistence.EntityManager;

/**
 *
 * @author 003-0823
 */
public class ClientView extends VerticalLayout implements View {

    private final JPAContainer<Client> client;
    private final PagedFilterTable<IndexedContainer> clientTable;
    private final EntityManager pzFilesEntityManager;

    public ClientView() {
        final VerticalLayout mainLayout = new VerticalLayout();
        pzFilesEntityManager = Factory.getInstance().getPzFilesEmf().createEntityManager();
        client = JPAContainerFactory.make(Client.class, pzFilesEntityManager);
        client.addNestedContainerProperty("clientType.description");
        clientTable = new PagedFilterTable<>();
        clientTable.setWidth("100%");
        clientTable.setFilterBarVisible(true);
        clientTable.setContainerDataSource(client);    
        clientTable.setVisibleColumns(new Object[]{"idClient", "name", "pfNom", "clientType.description"});
        clientTable.setColumnHeaders(new String[]{"Ид", "Название", "Рег. номер", "Тип"});    
        mainLayout.addComponent(clientTable);
        mainLayout.addComponent(clientTable.createControls(new PagedFilterControlConfig()));
        mainLayout.setExpandRatio(clientTable, 1);
        addComponent(mainLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
    
}
