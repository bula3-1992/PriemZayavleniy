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
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import control.pagedfiltertable.PagedFilterControlConfig;
import control.pagedfiltertable.PagedFilterTable;
import dao.factory.Factory;
import models.pzfiles.Journsync;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import module.AgentBpi;

/**
 *
 * @author 003-0823
 */
public class SyncView extends VerticalLayout implements View {  
    private final JPAContainer<Journsync> journsync;
    private final PagedFilterTable<IndexedContainer> journsyncTable;
    private final EntityManager pzFilesEntityManager;
    private long TIMEOUT = 1000L;

    public SyncView() {
        final VerticalLayout mainLayout = new VerticalLayout();
        pzFilesEntityManager = Factory.getInstance().getPzFilesEmf().createEntityManager();
        Button sync = new Button("Загрузить последние данные");
        sync.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (AgentBPIService.agentBpi.isAlive()) {
                    Window subWindow = new Window("Сообщение");
                    subWindow.setContent(new Label("В данный момент выполняется загрузка данных"));
                    subWindow.center();
                    UI.getCurrent().addWindow(subWindow);
                } else {
                    Date dateFrom = (Date) pzFilesEntityManager.createNativeQuery("select MAX(SYNC_TIME) from pz_s.JOURNSYNC").getSingleResult();
                    if (dateFrom != null) {
                        AgentBPIService.agentBpi = new Thread(new AgentBpi(dateFrom));
                        AgentBPIService.agentBpi.start();
                    }
                }
            }
        });
        Button brakeSync = new Button("Прервать загрузку");
        brakeSync.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (AgentBPIService.agentBpi.isAlive()) {
                    AgentBPIService.agentBpi.interrupt();
                    try {
                        AgentBPIService.agentBpi.join(TIMEOUT);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SyncView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    Window subWindow = new Window("Сообщение");
                    subWindow.setContent(new Label("В данный момент не выполняется никаких загрузок данных"));
                    subWindow.center();
                    UI.getCurrent().addWindow(subWindow);
                }
            }
        });
        HorizontalLayout hz = new HorizontalLayout();
        hz.addComponent(sync);
        hz.addComponent(brakeSync);
        mainLayout.addComponent(hz);
        journsync = JPAContainerFactory.make(Journsync.class, pzFilesEntityManager);
        journsyncTable = new PagedFilterTable<>();
        journsyncTable.setSizeFull();
        journsyncTable.setFilterBarVisible(true);
        journsyncTable.setContainerDataSource(journsync);
        journsyncTable.setVisibleColumns(new Object[]{"syncTime", "syncSize"});
        journsyncTable.setColumnHeaders(new String[]{"Время запуска", "Количество полученных посылок"});  
        mainLayout.addComponent(journsyncTable);
        mainLayout.addComponent(journsyncTable.createControls(new PagedFilterControlConfig()));
        mainLayout.setExpandRatio(journsyncTable, 1);              
        addComponent(mainLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        journsyncTable.refreshRowCache();
    }

}
