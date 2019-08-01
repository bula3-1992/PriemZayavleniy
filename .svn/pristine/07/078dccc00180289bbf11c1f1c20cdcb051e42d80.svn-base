/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import dao.factory.Factory;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import models.pzfiles.ContentFile;
import module.AgentBpi;
import view.forms.CountAppNPFMenu;
import view.forms.StatisticAppNPFMenu;
import view.forms.ApplicationTable;
import view.forms.MonitoringCommon;
import view.forms.UCReportMenu;

/**
 *
 * @author 003-0823
 */
class MainMenu extends MenuBar {
    
    public MainMenu() {
        setWidth("100%");
        MenuBar.MenuItem workspaces1 = this.addItem("Поступившие ", new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
//                l1.setValue("Раздел: Поступившие ;");
                UI.getCurrent().getNavigator().navigateTo("");
            }
        });

        MenuBar.MenuItem workspacesStatistics = this.addItem("Отчеты",
                FontAwesome.TASKS, null);
        
        MenuBar.MenuItem workplaceStatistic0 = workspacesStatistics.addItem("Отчет для УЦ", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                UI.getCurrent().addWindow(new UCReportMenu());
            }
        });
        MenuBar.MenuItem workplaceStatistic1 = workspacesStatistics.addItem("Количество заявлений в разрезе НПФ/УК", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                Date referenceDate = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(referenceDate);
                c.add(Calendar.WEEK_OF_YEAR, -1);
//                l1.setValue("Раздел: Количество заявлений в разрезе НПФ/УК ;");
                UI.getCurrent().addWindow(new CountAppNPFMenu());
//                UI.getCurrent().getNavigator().navigateTo("CountNPFView");
            }
        });

        MenuBar.MenuItem workplaceStatistic2 = workspacesStatistics.addItem("Статистика заявлений в разрезе НПФ/УК", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                UI.getCurrent().addWindow(new StatisticAppNPFMenu());
            }
        });
        MenuBar.MenuItem workplaceStatistic3 = workspacesStatistics.addItem("Таблица заявлений ", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
//                l1.setValue("Раздел: Таблица переходов ;");
                UI.getCurrent().addWindow(new ApplicationTable());
            }
        });
//        MenuBar.MenuItem workplaceStatistic4 = workspacesStatistics.addItem("История заявлений ", new MenuBar.Command() {
//            @Override
//            public void menuSelected(MenuBar.MenuItem selectedItem) {
//                l1.setValue("Раздел: История заявлений ;");
//                UI.getCurrent().getNavigator().navigateTo("ClientView");
//            }
//        });
        MenuBar.MenuItem workplaceStatistic5 = workspacesStatistics.addItem("Мониторинг общий ", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
//                l1.setValue("Раздел: История заявлений ;");
                UI.getCurrent().addWindow(new MonitoringCommon());
            }
        });

        MenuBar.MenuItem workspacesSystem = this.addItem("Система",
                FontAwesome.TASKS, null);
        MenuBar.MenuItem workplaceSystem1 = workspacesSystem.addItem("Реестр сообщений", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
//                l1.setValue("Раздел: Отправители, получатели ;");
                UI.getCurrent().getNavigator().navigateTo("MessageView");
            }
        });
        MenuBar.MenuItem workplaceSystem2 = workspacesSystem.addItem("Отправители, получатели", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
//                l1.setValue("Раздел: Отправители, получатели ;");
                UI.getCurrent().getNavigator().navigateTo("ClientView");
            }
        });
        MenuBar.MenuItem workplaceSystem6 = workspacesSystem.addItem("Справочник НФП и УК", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
//                l1.setValue("Раздел: Отправители, получатели ;");
                UI.getCurrent().getNavigator().navigateTo("NpfView");
            }
        });
        MenuBar.MenuItem workplaceSystem3 = workspacesSystem.addItem("Загрузка данных", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
//                l1.setValue("Раздел: Загрузка данных ;");
                UI.getCurrent().getNavigator().navigateTo("SyncView");
            }
        });
        MenuBar.MenuItem workplaceSystem4 = workspacesSystem.addItem("Настройки", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
//                l1.setValue("Раздел: Настройки ;");
                UI.getCurrent().getNavigator().navigateTo("SettingsView");
            }
        });
        MenuBar.MenuItem workplaceSystem5 = workspacesSystem.addItem("Сертификаты", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
//                l1.setValue("Раздел: Сертификаты ;");
                UI.getCurrent().getNavigator().navigateTo("CertificateView");
            }
        });
        
    }
}
