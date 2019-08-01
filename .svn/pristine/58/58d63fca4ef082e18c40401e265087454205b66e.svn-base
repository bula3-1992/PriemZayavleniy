/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.p03.makpsb.auth.AuthKey;
import ru.p03.makpsb.auth.AuthUser;
import ru.p03.makpsb.auth.AuthenticationUtil;
import static view.Main.authenticate;
import static view.Main.mainMenu;

/**
 *
 * @author 003-0824
 */
public class LoginView extends CustomComponent implements View, Button.ClickListener {

    private static final String LOGIN_ERROR_MSG = "\n\nПожалуйста проверьте ваш логин или пароль.";

    public static final String NAME = "login";

    private final TextField user;

    private final PasswordField password;

    private final Button loginButton;

    public LoginView() {

        setSizeFull();
        // Create the user input field
        user = new TextField("Логин:");
        user.setWidth("300px");
        user.setRequired(true);
//        user.setInputPrompt("Введите доменую учетную запись");
        //       user.addValidator(new EmailValidator(
//                "Username must be an email address"));
        user.setInvalidAllowed(false);

        user.addShortcutListener(new ShortcutListener(null, ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                isValidAuth();
            }
        });
        // Create the password input field
        password = new PasswordField("Пароль:");
        password.setWidth("300px");
        password.addValidator(new PasswordValidator());
        password.setRequired(true);
        password.setValue("");
        password.setNullRepresentation("");

        password.addShortcutListener(new ShortcutListener(null, ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                isValidAuth();
            }
        });
        // Create login button
        loginButton = new Button("Вход", this);

        GridLayout titleLayout = createHeaderLayout();
        // Add both to a panel
        VerticalLayout fields = new VerticalLayout(user, password, loginButton);
        fields.setCaption("Пожалуйста вход доступа в приложение");
        fields.setSpacing(true);
        fields.setMargin(new MarginInfo(true, true, true, false));
        fields.setSizeUndefined();

        // The view root layout
        VerticalLayout viewLayout = new VerticalLayout(titleLayout, fields);

        GridLayout gdl = new GridLayout(3, 1);
        gdl.setStyleName("backColorSide");
        gdl.setSizeFull();
        gdl.setWidth("100%");
        gdl.addComponent(viewLayout, 1, 0);
        //gdl.addComponent(menu, 1, 1);
        //gdl.addComponent(content, 1, 2);

        gdl.setColumnExpandRatio(0, 0.1f);
        gdl.setColumnExpandRatio(1, 0.8f);
        gdl.setColumnExpandRatio(2, 0.1f);
        setCompositionRoot(gdl);

        viewLayout.setSizeFull();

        viewLayout.setComponentAlignment(titleLayout, Alignment.TOP_CENTER);
        viewLayout.setComponentAlignment(fields, Alignment.TOP_CENTER);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // focus the username field when user arrives to the login view
        user.focus();
    }

    // Validator for validating the passwords
    private static final class PasswordValidator extends
            AbstractValidator<String> {

        public PasswordValidator() {
            super("Пароль не действительный");
        }

        @Override
        protected boolean isValidValue(String value) {
            //
            // Password must be at least 8 characters long and contain at least
            // one number
            //
//            if (value != null && (value.length() < 8 || !value.matches(".*\\d.*"))) { 
//                return false;
//            }
            return true;
        }

        @Override
        public Class<String> getType() {
            return String.class;
        }
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        isValidAuth();
    }

    public void isValidAuth() {
        //
        // Validate the fields using the navigator. By using validors for the
        // fields we reduce the amount of queries we have to use to the database
        // for wrongly entered passwords
        //
        if (!user.isValid() || !password.isValid()) {
            return;
        }

        String login = user.getValue();
        String password = this.password.getValue();

        //
        // Validate username and password with database here. For examples sake
        // I use a dummy username and password.
        //
        boolean isValid = false;//login.equals("1") && password.equals("1");
        try {
            AuthenticationUtil.init(new URL("http://10.3.30.92:8084/MakPSBAuth/MakPSBAuthService?wsdl"));
        } catch (MalformedURLException ex) {
            Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex);
        }

       authenticate = AuthenticationUtil.authenticate(login, password);

        if (authenticate.getAuthenticateStatus() == AuthKey.AUTH_FAILED) {
            isValid = false;
        } else {
            isValid = true;
            mainMenu.setEnabled(true);
            System.out.println("Пользователь " + authenticate.getLogin() + " " + authenticate.getRoles());
            AuthenticationUtil.log("Пользователь вошел в ПК Прием заявлений НПФ, login:", login);
        }

        if (isValid) {

            // Store the current user in the service session
            getSession().setAttribute("user", login);

            // Navigate to main view
            getUI().getNavigator().navigateTo(Main.MAIN);//

        } else {
            Notification.show("Неправильный логин или пароль", LOGIN_ERROR_MSG, Notification.Type.WARNING_MESSAGE);
            // Wrong password clear the password field and refocuses it
            this.password.setValue(null);
            this.password.focus();

        }
    }

    public GridLayout createHeaderLayout() {

        Embedded logo = new Embedded();
        logo.setImmediate(false);
        logo.setSource(new ThemeResource("img/logo.png"));
        logo.setWidth("60px");
        logo.setHeight("60px");
        logo.setType(1);
        logo.setMimeType("image/png");
        Label userLabel = new Label();
        userLabel.setValue("Пользователь: Гость");
        VerticalLayout vLogoUser = new VerticalLayout();
        vLogoUser.addComponents(logo, userLabel);
        vLogoUser.setSpacing(true);
        vLogoUser.setStyleName("backColorTitle");

        Label title = new Label(" <div align=\"center\"> <h1><b>ПК Прием заявлений НПФ</b></h1></div>", ContentMode.HTML);
        GridLayout titleLayout = new GridLayout(2, 1);
        titleLayout.setWidth("100%");
        titleLayout.setMargin(true);
        titleLayout.setSpacing(true);
        titleLayout.setColumnExpandRatio(0, 0.9f);
        titleLayout.setColumnExpandRatio(1, 0.1f);
        titleLayout.addComponent(title, 0, 0);
        titleLayout.addComponent(vLogoUser, 1, 0);
        titleLayout.setStyleName("backColorTitle");

        titleLayout.setComponentAlignment(title, Alignment.MIDDLE_CENTER);
        titleLayout.setComponentAlignment(vLogoUser, Alignment.MIDDLE_CENTER);

        return titleLayout;
    }
}
