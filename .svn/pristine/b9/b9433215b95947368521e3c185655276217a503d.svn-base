/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import view.system.MessageView;
import view.system.SettingsView;
import view.system.SyncView;
import view.system.ClientView;
import view.system.CertificateView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.p03.makpsb.auth.AuthUser;
import view.system.NPFView;

/**
 *
 * @author 003-0823
 */
@Title("ПК Прием Заявлений НПФ")
@Theme("makpsbtheme")
public class Main extends UI {

    public static MainMenu mainMenu;
    public static AuthUser authenticate;
    public static String ROOT = "";
    public static String MAIN = "";
    HorizontalLayout header = new HorizontalLayout();
    VerticalLayout root = new VerticalLayout();
    CssLayout content = new CssLayout();
//    
//    Label l1 = new Label();
//    Label l2 = new Label();

    //Навигатор
    private Navigator navigator;

    public static Main getCurrent() {
        return (Main) UI.getCurrent();
    }

    @Override
    protected void init(VaadinRequest request) {
        ROOT = VaadinServlet.getCurrent().getServletContext().getRealPath(
                    VaadinServlet.getCurrent().getServletContext().getContextPath()) + "\\";
        
//        header.setSizeFull();
//        l1.setValue("Раздел: Поступившие ;");
//        l2.setValue("Фильтры: сегодня ");
//        header.addComponent(l1);
//        header.addComponent(l2);
//        l1.setSizeFull();
//        l2.setSizeFull();
//        root.addComponent(header);
        root.setSizeFull();

        setContent(root);

        //Main Menu
        VerticalLayout menu = new VerticalLayout();
        menu.addStyleName("topbar");
        menu.setWidth("100%");
        menu.setSpacing(true);
//        menu.addComponent(new MainMenu(l1, l2));
        mainMenu = new MainMenu();
        menu.addComponent(mainMenu);
        root.addComponent(menu);

        //Content pane
        content.setSizeFull();
        root.addComponent(content);
        root.setExpandRatio(content, 1);

        //Navigator
        navigator = new Navigator(this, content);
        navigator.addView("", new MainView());
        navigator.addView("LoginView", LoginView.class);
        navigator.addView("ClientView", new ClientView());
        navigator.addView("NpfView", new NPFView());
        navigator.addView("SyncView", new SyncView());
        navigator.addView("SettingsView", new SettingsView());
        navigator.addView("CertificateView", new CertificateView());
        navigator.addView("MessageView", new MessageView());
        getNavigator().addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(ViewChangeListener.ViewChangeEvent event) {

                // Check if a user has logged in
                boolean isLoggedIn = getSession().getAttribute("user") != null;
                boolean isLoginView = event.getNewView() instanceof LoginView;

                if (!isLoggedIn && !isLoginView) {
                    // Redirect to login view always if a user has not yet
                    // logged in
                    mainMenu.setEnabled(false);
                    getNavigator().navigateTo("LoginView");
                    return false;

                } else if (isLoggedIn && isLoginView) {
                    // If someone tries to access to login view while logged in,
                    // then cancel
                    return false;
                }

                return true;
            }

            @Override
            public void afterViewChange(ViewChangeListener.ViewChangeEvent event) {

            }
        });        
    }

    @Override
    public Navigator getNavigator() {
        return this.navigator;
    }
    
}
