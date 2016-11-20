/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vaadin.demo.dashboard.component;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * @author Edrd
 */
public class EmailWindow extends Window {

    private final VerticalLayout content;
    private VerticalLayout root;
    private HorizontalLayout adjuntar;
    private FormLayout form;
    private final TabSheet detailsWrapper;

    private Button btnAdjuntar;
    private TextField paraTxt;
    private TextField ccTxt;
    private TextField asuntoTxt;
    private RichTextArea cuerpoCorreo;

    public EmailWindow() {
        addStyleName("moviedetailswindow");
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
        detailsWrapper.addComponent(fields());

        content.addComponent(detailsWrapper);
        content.setExpandRatio(detailsWrapper, 1f);
        content.addComponent(buildFooter());

        setContent(content);
    }

    private VerticalLayout fields() {
        root = new VerticalLayout();
        root.setCaption("Email");
        root.setWidth(100.0f, Unit.PERCENTAGE);
        root.setSpacing(true);
        root.setMargin(new MarginInfo(true, false, true, false));

        adjuntar = new HorizontalLayout();
        adjuntar.setSpacing(true);
        adjuntar.setSizeUndefined();

        btnAdjuntar = new Button("Adjuntar");
        btnAdjuntar.setIcon(FontAwesome.PAPERCLIP);
        btnAdjuntar.addStyleName(ValoTheme.BUTTON_SMALL);
        btnAdjuntar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                DirectoryTreeWindow emailWdw = new DirectoryTreeWindow();
                Window w = createWindow();
                UI.getCurrent().addWindow(w);
                w.focus();
            }
        });
        adjuntar.addComponent(btnAdjuntar);

        form = new FormLayout();
        form.setMargin(false);
        form.setWidth(100.0f, Unit.PERCENTAGE);

        paraTxt = new TextField("Para");
        paraTxt.setWidth(100.0f, Unit.PERCENTAGE);
        ccTxt = new TextField("Cc");
        ccTxt.setWidth(100.0f, Unit.PERCENTAGE);
        asuntoTxt = new TextField("Asunto");
        asuntoTxt.setWidth(100.0f, Unit.PERCENTAGE);

        cuerpoCorreo = new RichTextArea();
        cuerpoCorreo.setWidth(100.0f, Unit.PERCENTAGE);

        form.addComponents(paraTxt,
                ccTxt,
                asuntoTxt);

        root.addComponent(form);
        root.addComponent(adjuntar);
        root.addComponent(cuerpoCorreo);

        return root;
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        Button ok = new Button("Enviar");
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                
            }
        });
        ok.focus();
        footer.addComponent(ok);
        footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);
        return footer;
    }
    
    private Window createWindow() {
        Window window = new Window();
        Responsive.makeResponsive(this);

        window.setModal(true);
        window.setResizable(false);
        window.setClosable(true);
        window.setHeight(90.0f, Sizeable.Unit.PERCENTAGE);
        window.center();
        window.setWidth(90.0f, Unit.PERCENTAGE);

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setMargin(true);
        
        Tree tree = new Tree("My Tree");
        tree.setSelectable(true);
        tree.setMultiSelect(true);
        tree.setStyleName(ValoTheme.TREETABLE_BORDERLESS);
        // Create the tree nodes
        tree.addItem("UI");
        tree.addItem("Branch 1");
        tree.addItem("Branch 2");
        tree.addItem("Leaf 1");
        tree.addItem("Leaf 2");
        tree.addItem("Leaf 3");
        tree.addItem("Leaf 4");

        // Set the hierarchy
        tree.setParent("Branch 1", "UI");
        tree.setParent("Branch 2", "UI");
        tree.setParent("Leaf 1", "Branch 1");
        tree.setParent("Leaf 2", "Branch 1");
        tree.setParent("Leaf 3", "Branch 2");
        tree.setParent("Leaf 4", "Branch 2");

        // Disallow children for leaves
        tree.setChildrenAllowed("Leaf 1", false);
        tree.setChildrenAllowed("Leaf 2", false);
        tree.setChildrenAllowed("Leaf 3", false);
        tree.setChildrenAllowed("Leaf 4", false);

        content.addComponents(tree);
        
        window.setContent(content);

        return window;
    }

}
