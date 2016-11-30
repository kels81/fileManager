package com.vaadin.demo.dashboard.view;

import com.vaadin.demo.dashboard.event.DashboardEvent.UserLoginRequestedEvent;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.demo.dashboard.utils.Notifications;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class LoginView extends VerticalLayout {
    
    private Notifications notification = new Notifications();

    public LoginView() {
        setSizeFull();

        Component loginForm = buildLoginForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
    }

    private Component buildLoginForm() {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setSpacing(true);
        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");

        loginPanel.addComponent(buildFields());
        return loginPanel;
    }

    private Component buildFields() {
        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");

        final TextField username = new TextField("Usuario");
        username.focus();
        username.setIcon(FontAwesome.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        username.setNullRepresentation("");

        final PasswordField password = new PasswordField("Contraseña");
        password.setIcon(FontAwesome.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        password.setNullRepresentation("");

        final Button signin = new Button("Entrar");
        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(KeyCode.ENTER);
        signin.focus();

        fields.addComponents(username, password, signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

        signin.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                if (username.getValue().isEmpty() && password.getValue().isEmpty()) {
                    /*
                    MessageBox.setDialogDefaultIconFactory(new FontAwesomeDialogIconFactory());
                    MessageBox.createError()
                            .withWidth("255px")
                            .withCaption("Error")
                            .withMessage("Acceso Denegado")
                            .withCustomButton(
                                    ButtonOption.focus(),
                                    ButtonOption.caption("Aceptar"),
                                    ButtonOption.style("mystyle"),
                                    ButtonOption.style(ValoTheme.BUTTON_SMALL))
                            .withButtonAlignment(Alignment.MIDDLE_CENTER)
                            .open();
                    //MessageBox mb = messageBox.createError("prueba");
                     */
                    notification.createFailure("Acceso denegado");
                    
                } else {
                    DashboardEventBus.post(new UserLoginRequestedEvent(username
                            .getValue(), password.getValue()));
                }

            }
        });
        return fields;
    }

}
