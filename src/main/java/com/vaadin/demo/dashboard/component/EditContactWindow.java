/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vaadin.demo.dashboard.component;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.demo.dashboard.entity.Contactos;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.tokenfield.TokenField;

/**
 *
 * @author Edrd
 */
public class EditContactWindow extends Window {

    private final VerticalLayout content;
    private FormLayout body;
    private final TabSheet detailsWrapper;
    private Button save;
    private Button cancel;

    private Contactos contact;
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

        //Contacto contactos = new Contacto("", "pedro@live.com");
        //[* 1 *]
//        BeanItem<Contactos> item = new BeanItem<>(contact);
//        Form form = new Form();
//        form.setItemDataSource(item);
//        form.setVisibleItemProperties(new Object[]{"CNombre","CEmail"});
//        form.setImmediate(true);
        //[* 2 *]
        //BeanItem<Contacto> item = new BeanItem<Contacto>(contact);
        //FieldGroup binder = new FieldGroup();
        //binder.setItemDataSource(item);
        //ContactoForm form = new ContactoForm();
        //binder.bindMemberFields(form);
        //binder.setBuffered(false);
        //[* 3 *]
//        BeanItem<Contactos> item = new BeanItem<Contactos>(contact);
//        FieldGroup binder = new FieldGroup();
//        binder.setItemDataSource(item);
//        FormLayout form = new FormLayout();
//        nameTxt = new TextField("Nombre");
//        form.addComponent(nameTxt);
//        emailTxt = new TextField("Email");
//        form.addComponent(emailTxt);
//        binder.bindMemberFields(this);
//        binder.setBuffered(false);
        //[* 4 *]
//        FormLayout form = new FormLayout();
//        nameTxt = new TextField("Nombre");
//        form.addComponent(nameTxt);
//        emailTxt = new TextField("Email");
//        form.addComponent(emailTxt);
//        form.setImmediate(true);
//        fieldGroup = new BeanFieldGroup<>(Contactos.class);
//        fieldGroup.bindMemberFields(this);
//        fieldGroup.setItemDataSource(contact);
//        fieldGroup.setBuffered(false);
        //[* 5 *]
//        FormLayout form = new FormLayout();
//        form.setImmediate(true);
//        nameTxt = new TextField("Nombre_f");
//        emailTxt = new TextField("Email_f");
//
//        BeanItem<Contactos> item = new BeanItem<Contactos>(contact);
//        final FieldGroup binder = new FieldGroup(item);
//
//        //binder.setItemDataSource(item);
//        form.addComponent(binder.buildAndBind("cNombre"));
//        form.addComponent(binder.buildAndBind("cEmail"));
//        binder.setBuffered(true);
    }

    private FormLayout buildForm() {
        body = new FormLayout();

        body.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        body.setCaption("Nuevo Contacto");
        body.setSizeFull();
        body.setSpacing(true);
        body.setMargin(true);

        FormLayout form = new FormLayout();
        nameTxt = new TextField("Nombre");
        form.addComponent(nameTxt);
        emailTxt = new TextField("Email");
        form.addComponent(emailTxt);
        form.setImmediate(true);
        fieldGroup = new BeanFieldGroup<>(Contactos.class);
        fieldGroup.bindMemberFields(this);
        fieldGroup.setItemDataSource(contact);
        fieldGroup.setBuffered(false);

        body.addComponents(emailTxt, nameTxt);

        return body;
    }

    private HorizontalLayout buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.setSpacing(true);
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);

        cancel = new Button("Cancelar", new Button.ClickListener() {

            private static final long serialVersionUID = -1198191849568844582L;

            public void buttonClick(ClickEvent event) {
                if (contact.getcEmail() == null
                        || contact.getcEmail().length() < 1) {
                    contact.setcEmail(contact.getcNombre());
                }
                
//                    f.addToken(contact);
//                    f.getUI().removeWindow(EditContactWindow.this);
                //close();
            }
        });

        save = new Button("Guardar",
                new Button.ClickListener() {

            private static final long serialVersionUID = 1L;

            public void buttonClick(ClickEvent event) {
                if (contact.getcEmail() == null
                        || contact.getcEmail().length() < 1) {
                    contact.setcEmail(contact.getcNombre());
                }
//                            ((BeanItemContainer) f.getContainerDataSource())
//                                    .addBean(contact);
//                            f.addToken(contact);
//                            f.getUI().removeWindow(EditContactWindow.this);
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
