/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vaadin.demo.dashboard.component;

import com.vaadin.data.Container;
import com.vaadin.data.Validator.EmptyValueException;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.demo.dashboard.control.ControlTransactions;
import com.vaadin.demo.dashboard.control.Querys;
import com.vaadin.demo.dashboard.entity.Contactos;
import com.vaadin.demo.dashboard.utils.Notifications;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.tokenfield.TokenField;

/**
 *
 * @author Edrd
 */
public class AddressEditor extends CssLayout implements LayoutClickListener {

    private final TokenField tokenField;
    private final Notifications notification = new Notifications();

    public AddressEditor() {
        super();
        setWidth("100%");
        addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        addStyleName("address-editor");

        // generate container
        Container tokens = new Querys().containerContactos();

        tokenField = new TokenField() {
            @Override
            protected void onTokenInput(Object tokenId) {
//                String[] tokens = ((String) tokenId).split(";");
//                for (String token : tokens) {
//                    token = token.trim();
//                    if (token.length() > 0) {
//                        super.onTokenInput(token);
//                    }
//                }
                System.out.println("entra aqui");
                Contactos con = tokenId instanceof Contactos ? (Contactos) tokenId : new Querys().existContacto(tokenId.toString().trim());

                if (!cb.containsId(con)) {
                    getUI().addWindow(new EditContactWindow(tokenId.toString().trim(), this));
                } else {
                    addToken(con);
                }
            }

            @Override

            protected void configureTokenButton(Object tokenId, Button button) {
                super.configureTokenButton(tokenId, button);
                // custom caption
                Contactos con = (Contactos) tokenId;
                button.setCaption(getTokenCaption(con.getcNombre()) + " <" + con.getcEmail() + ">;");
            }
        };
        tokenField.addStyleName("mytoken");
        tokenField.setInputWidth(250.0f, Unit.PIXELS);
        tokenField.setValidationVisible(true);
        tokenField.setRememberNewTokens(false);
        tokenField.setContainerDataSource(tokens);
        tokenField.setImmediate(true);
        //tokenField.setFilteringMode(FilteringMode.CONTAINS); // suggest
        tokenField.setTokenCaptionPropertyId("cNombre"); // use name in input

        addComponent(tokenField);
        addLayoutClickListener(this);
    }

    @Override

    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (null == event.getClickedComponent().getCaption()) {
            tokenField.focus();

        }
    }

    public List<String> getValue() {
        List<String> values = new ArrayList<>();
        Set<Contactos> contact = (LinkedHashSet) tokenField.getValue();
        for (Contactos con : contact) {
            values.add(con.getcEmail());
        }
        return values;
    }

    public class EditContactWindow extends Window {

        private final VerticalLayout content;
        private FormLayout body;
        private final TabSheet detailsWrapper;
        private Button save;
        private Button cancel;

        private final Contactos contact;
        private BeanFieldGroup<Contactos> fieldGroup;

        @PropertyId("cNombre")
        private TextField nameTxt;
        @PropertyId("cEmail")
        private TextField emailTxt;

        EditContactWindow(final String t, final TokenField f) {

            contact = t.contains("@") ? new Contactos("", t) : new Contactos(t, "");

            addStyleName("newcontact-window");
            Responsive.makeResponsive(this);
            setModal(true);
            setResizable(false);
            setClosable(true);
            center();

            setHeight(90.0f, Unit.PERCENTAGE);
            content = new VerticalLayout();
            content.setSizeFull();
            content.setMargin(true);

            detailsWrapper = new TabSheet();
            detailsWrapper.setSizeFull();
            detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
            detailsWrapper.addComponent(buildForm());

            content.addComponent(detailsWrapper);
            content.setExpandRatio(detailsWrapper, 1f);
            content.addComponent(buildFooter());

            setContent(content);

        }

        private FormLayout buildForm() {
            body = new FormLayout();

            body.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
            body.setCaption("Nuevo Contacto");
            body.setSizeFull();
            body.setSpacing(true);
            body.setMargin(true);

            nameTxt = new TextField("Nombre");
            nameTxt.addValidator(new StringLengthValidator("El nombre es requerido", 2, 24, false));
            nameTxt.setValidationVisible(false);
            nameTxt.setRequired(true);
            nameTxt.setImmediate(true);

            emailTxt = new TextField("Email");
            emailTxt.addValidator(new EmailValidator("Debe ingresar un email valido"));
            emailTxt.setValidationVisible(false);
            emailTxt.setRequired(true);
            emailTxt.setImmediate(true);

            fieldGroup = new BeanFieldGroup<>(Contactos.class);
            fieldGroup.bindMemberFields(this);
            fieldGroup.setItemDataSource(contact);
            fieldGroup.setBuffered(true);

            body.addComponents(emailTxt, nameTxt);

            return body;
        }

        private HorizontalLayout buildFooter() {
            HorizontalLayout footer = new HorizontalLayout();
            footer.setSpacing(true);
            footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
            footer.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);

            cancel = new Button("Cancelar", new Button.ClickListener() {

                public void buttonClick(Button.ClickEvent event) {
                    tokenField.addToken(contact);
                    tokenField.getUI().removeWindow(EditContactWindow.this);
                }
            });

            save = new Button("Agregar");
            save.addClickListener(new Button.ClickListener() {
                public void buttonClick(Button.ClickEvent event) {
                    boolean agregar = (StringUtils.isNotBlank(nameTxt.getValue()) && StringUtils.isNotBlank(emailTxt.getValue()));
                    if (agregar) {
                        try {
                            emailTxt.setValidationVisible(false);
                            emailTxt.validate();
//                            nameTxt.setValidationVisible(false);
//                            nameTxt.validate(); 

                            contact.setcNombre(nameTxt.getValue());
                            //new ControlTransactions().commit(contact);
                            tokenField.addToken(contact);
                            tokenField.getUI().removeWindow(EditContactWindow.this);

                            notification.createSuccess("Se guardo el contacto");
                        } //                        catch (EmptyValueException e) {       // PARA QUE FUNCIONE ESTA EXCEPTION EL TEXTFIELD TIENE QUE ESTAR SETREQUIRED(TRUE)
                        //                            Notification.show("Email es requerido");
                        //                            emailTxt.setValidationVisible(true);
                        //                            Notification.show("Nombre es requerido");
                        //                            nameTxt.setValidationVisible(true);
                        //                        }
                        catch (InvalidValueException e) {
                            emailTxt.setValidationVisible(true);
                            notification.createFailure("Debe ingresar un email valido");
                            //nameTxt.setValidationVisible(true);
                        }
                    } else {
                        notification.createFailure("Favor de revisar los campos");
                    }
                }
            });
            save.addStyleName(ValoTheme.BUTTON_PRIMARY);

            footer.addComponents(cancel, save);
            footer.setExpandRatio(cancel, 1);
            footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);
            /*[ /FOOTER ]*/

            return footer;
        }
    }
}
