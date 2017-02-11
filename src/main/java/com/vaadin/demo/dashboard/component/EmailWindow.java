/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vaadin.demo.dashboard.component;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.demo.dashboard.utils.Components;
import com.vaadin.demo.dashboard.utils.Mail;
import com.vaadin.demo.dashboard.utils.Notifications;
import com.vaadin.event.Action;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Edrd
 */
public class EmailWindow extends Window {

    private final File origenPath;
    private final VerticalLayout content;
    private VerticalLayout filesAttached;
    private VerticalLayout root;
    public HorizontalLayout hl;
    public CssLayout adjuntar;
    public FormLayout form;
    private Tree tree;
    private final TabSheet detailsWrapper;
    private final Components comp = new Components();

    private Button btnAdjuntar;
    private Button cancelar;
    private Button enviar;
    private AddressEditor paraTxt;
    private TextField asuntoTxt;
    private RichTextArea cuerpoCorreo;
    private final Notifications notification = new Notifications();

    public EmailWindow() {
        this.origenPath = new File("C:\\Users\\Edrd\\Documents\\GitHub\\fileManager\\Archivos");

        addStyleName("emailwindow");
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

        hl = new HorizontalLayout();
        hl.setSpacing(true);
        hl.setWidth(100.0f, Unit.PERCENTAGE);

        adjuntar = new CssLayout();
        //adjuntar.setStyleName("attachedfiles");

        filesAttached = new VerticalLayout();
        filesAttached.setVisible(false);

        btnAdjuntar = new Button("Adjuntar");
        btnAdjuntar.setIcon(FontAwesome.PAPERCLIP);
        btnAdjuntar.addStyleName(ValoTheme.BUTTON_SMALL);
        btnAdjuntar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                DirectoryTreeWindow emailWdw = new DirectoryTreeWindow();
                //Window w = emailWdw;
                Window w = createWindow();
                UI.getCurrent().addWindow(w);
                w.focus();
            }
        });

        hl.addComponents(btnAdjuntar, adjuntar);
        hl.setExpandRatio(adjuntar, 1);

        paraTxt = new AddressEditor();
        paraTxt.setCaption("Para");
        paraTxt.setWidth(100.0f, Unit.PERCENTAGE);

//        paraTxt.addTextChangeListener(new FieldEvents.TextChangeListener() {
//            @Override
//            public void textChange(FieldEvents.TextChangeEvent event) {
//                String texto = event.getText();
//                if (texto.contains("@")) {
//                    paraTxt.setValue(texto+"; ");
//                }
//            }
//        });
        asuntoTxt = new TextField("Asunto");
        asuntoTxt.setWidth(100.0f, Unit.PERCENTAGE);

        cuerpoCorreo = new RichTextArea();
        cuerpoCorreo.setHeight(100.0f, Unit.PERCENTAGE);
        cuerpoCorreo.setWidth(100.0f, Unit.PERCENTAGE);

        form = new FormLayout();
        form.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        form.addComponents(paraTxt, asuntoTxt);

        root.addComponent(form);
        root.addComponent(hl);
        root.addComponent(filesAttached);
        root.addComponent(cuerpoCorreo);

        return root;
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.setSpacing(true);
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        cancelar = new Button("Cancelar");
        cancelar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });

        enviar = new Button("Enviar");
        enviar.addStyleName(ValoTheme.BUTTON_PRIMARY);
        enviar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                System.out.println("para: " + paraTxt.getValue());
                //boolean enviar = (StringUtils.isNotBlank(paraTxt.getValue()) && StringUtils.isNotBlank(asuntoTxt.getValue()) && StringUtils.isNotBlank(cuerpoCorreo.getValue()));
                boolean enviar = (StringUtils.isNotBlank(paraTxt.getValue().toString()) && StringUtils.isNotBlank(asuntoTxt.getValue()) && StringUtils.isNotBlank(cuerpoCorreo.getValue()));
                if (enviar) {
                    Mail sendMail = new Mail();

                    String asunto = asuntoTxt.getValue();
                    String mensaje = cuerpoCorreo.getValue();
                    //List<String> receptores = new ArrayList<String>();
                    //receptores.add(paraTxt.getValue());

                    //String receptores = paraTxt.getValue();
                    String receptores = paraTxt.getValue().toString();
                    System.out.println("receptores = " + receptores);

                    List<String> adjuntos = new ArrayList<>();
                    for (int i = 0; i < filesAttached.getComponentCount(); i++) {
                        Component c = filesAttached.getComponent(i);
                        adjuntos.add(filesAttached.getComponent(i).getCaption());
                    }

                    boolean envio = sendMail.enviarSpring(asunto, receptores, mensaje, adjuntos);
                    System.out.println("envio = " + envio);
                    if (envio) {
                        notification.createSuccess("Se envio con éxito");
                    } else {
                        notification.createFailure("Problemas con el envio");
                    }

                    close();
                } else {
                    notification.createFailure("Favor de revisar los campos");
                }

            }
        });

        footer.addComponents(cancelar, enviar);
        footer.setExpandRatio(cancelar, 1);
        footer.setComponentAlignment(cancelar, Alignment.TOP_RIGHT);
        return footer;
    }

    private Window createWindow() {
        Window window = new Window();
        Responsive.makeResponsive(this);

        window.addStyleName("directorywindow");
        Responsive.makeResponsive(this);
        window.setModal(true);
        window.setResizable(false);
        window.setClosable(true);
        window.center();

        window.setHeight(90.0f, Unit.PERCENTAGE);

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setMargin(true);

        TabSheet detailsWrapper = new TabSheet();
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        detailsWrapper.addComponent(tree());

        content.addComponent(detailsWrapper);
        content.setExpandRatio(detailsWrapper, 1f);
        content.addComponent(buildFooter2());

        window.setContent(content);

        return window;
    }

    private HorizontalLayout tree() {
        HorizontalLayout root = new HorizontalLayout();
        root.setCaption("Archivos");
        root.setMargin(true);

        tree = new Tree();
        Container generateContainer = getHardwareContainer(origenPath);
        tree.setContainerDataSource(generateContainer);
        tree.setItemCaptionPropertyId("caption");
        tree.setItemIconPropertyId("icon");
        tree.setImmediate(true);
        tree.addActionHandler(new Action.Handler() {
            private final Action ADJUNTAR = new Action("Adjuntar");
            private final Action[] ACTIONS = new Action[]{ADJUNTAR};

            @Override
            public Action[] getActions(Object target, Object sender) {
                return ACTIONS;
            }

            @Override
            public void handleAction(Action action, Object sender, Object target) {
                if (action == ADJUNTAR) {
                    //AQUI VER DSADA                   
                    Item item = tree.getItem(target);
                    String path = item.getItemProperty("path").getValue().toString();
                    String name = path.substring(path.lastIndexOf('\\') + 1);

                    Label pathFile = new Label();
                    pathFile.setCaption(path);

                    HorizontalLayout adjLayout = new HorizontalLayout();
                    adjLayout.addStyleName("attachedlayout");

                    Label nameFile = new Label("&nbsp;" + FontAwesome.FILE_TEXT_O.getHtml() + "&nbsp;" + name + "&nbsp;");
                    nameFile.setContentMode(ContentMode.HTML);
                    nameFile.addStyleName(ValoTheme.LABEL_LIGHT);
                    nameFile.addStyleName(ValoTheme.LABEL_SMALL);
                    //nameFile.setSizeUndefined();

                    Button delete = new Button("×");
                    delete.setDescription("Eliminar");
                    delete.setPrimaryStyleName("deleteBtn");
                    delete.addClickListener(new Button.ClickListener() {
                        @Override
                        public void buttonClick(final Button.ClickEvent event) {
                            // ELIMINAR LABEL DEL ARCHIVO QUE VE EL USUARIO
                            adjuntar.removeComponent(adjLayout);
                            // ELIMINAR TAMBIEN EL PATH DEL ARCHIVO QUE SE ENCUENTRA OCULTO
                            // EL QUE SE MANDA POR CORREO
                            filesAttached.removeComponent(pathFile);
                        }
                    });

                    adjLayout.addComponents(nameFile, delete);
                    //adjLayout.setComponentAlignment(nameFile, Alignment.MIDDLE_LEFT);

                    // NOMBRE DE ARCHIVOS QUE VE EL USAURIO
                    adjuntar.addComponent(adjLayout);
                    // LA RUTA DE LOS ARCHIVOS QUE VE EL USUARIO, PERO QUE SE ENCUENTRA OCULTO
                    filesAttached.addComponent(pathFile);

//                    List arrayAdjuntos = new ArrayList();
//                    
//                    for (int i = 0; i < filesAttached.getComponentCount(); i++) {
//                        Component c = filesAttached.getComponent(i);
//                            arrayAdjuntos.add(c);
//                            System.out.println("c = " + filesAttached.getComponent(i).getCaption());
//                    }
                }
            }
        });
        tree.addExpandListener(new Tree.ExpandListener() {
            @Override
            public void nodeExpand(Tree.ExpandEvent event) {

                //List<File> files = (List<File>) directoryContents(new File(event.getItemId().toString()));
                List<File> files = (List<File>) comp.directoryContents(new File(event.getItemId().toString()));

                for (File file : files) {
                    if (file.isDirectory()) {
                        tree.addItem(file);
                        tree.getItem(file).getItemProperty("caption").setValue(file.getName());
                        tree.getItem(file).getItemProperty("icon").setValue(new ThemeResource("img/file_manager/folder_24.png"));
                        tree.getItem(file).getItemProperty("path").setValue(file.getAbsolutePath());
                        tree.setParent(file, event.getItemId());
                        // SI SE ENCUENTRA VACIO LA CARPETA NO MOSTRARA LA FLECHA DE EXPANDIR
                        if (file.list().length == 0) {
                            tree.setChildrenAllowed(file, false);
                        }

                    } else if (file.isFile()) {
                        tree.addItem(file);
                        tree.getItem(file).getItemProperty("caption").setValue(file.getName());
                        tree.getItem(file).getItemProperty("icon").setValue(new ThemeResource("img/file_manager/file_24.png"));
                        tree.getItem(file).getItemProperty("path").setValue(file.getAbsolutePath());
                        tree.setParent(file, event.getItemId());
                        tree.setChildrenAllowed(file, false);
                    }
                }

            }
        });
        tree.addCollapseListener(new Tree.CollapseListener() {
            @Override
            public void nodeCollapse(Tree.CollapseEvent event) {
                // Remove all children of the collapsing node
                Object children[] = tree.getChildren(event.getItemId()).toArray();
                for (Object children1 : children) {
                    tree.collapseItemsRecursively(children1);
                    //tree.collapseItem(children1);
                }
            }
        });
//        tree.addItemClickListener(new ItemClickEvent.ItemClickListener() {
//
//            public void itemClick(ItemClickEvent event) {
//                Notification.show(event.getItem().getItemProperty("path").getValue().toString(), Notification.Type.HUMANIZED_MESSAGE);
//            }
//
//        });

        root.addComponent(tree);

        return root;
    }

    private Component buildFooter2() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);
        footer.setHeight(30.0f, Unit.PIXELS);

        Button ok = new Button("Cerrar");
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });
        //ok.focus();
        //footer.addComponent(ok);
        //footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);
        return footer;
    }

    public HierarchicalContainer getHardwareContainer(File directory) {

        List<File> files = (List<File>) directoryContents(directory);

        // Create new container
        HierarchicalContainer container = new HierarchicalContainer();
        // Create containerproperty for name
        container.addContainerProperty("icon", ThemeResource.class, null);
        container.addContainerProperty("caption", String.class, null);
        container.addContainerProperty("path", String.class, null);

        for (File file : files) {

            if (file.isDirectory()) {
                container.addItem(file);
                container.getItem(file).getItemProperty("caption").setValue(file.getName());
                container.getItem(file).getItemProperty("icon").setValue(new ThemeResource("img/file_manager/folder_24.png"));
                container.getItem(file).getItemProperty("path").setValue(file.getAbsolutePath());
                // SI SE ENCUENTRA VACIO LA CARPETA NO MOSTRARA LA FLECHA DE EXPANDIR
                if (file.list().length == 0) {
                    container.setChildrenAllowed(file, false);
                }

            } else if (file.isFile()) {
                container.addItem(file);
                container.getItem(file).getItemProperty("caption").setValue(file.getName());
                container.getItem(file).getItemProperty("icon").setValue(new ThemeResource("img/file_manager/file_24.png"));
                container.getItem(file).getItemProperty("path").setValue(file.getAbsolutePath());
                //container.setParent(file, file);
                container.setChildrenAllowed(file, false);
            }
        }
        return container;
    }

    public List<File> directoryContents(File directory) {
        // ARRAY QUE VA A ACONTENER TODOS LOS ARCHIVOS ORDENADOS POR TIPO Y ALFABETICAMENTE
        List<File> allDocsLst = new ArrayList<>();
        File[] files = directory.listFiles();
        List<File> fileLst = new ArrayList<>();
        List<File> directoryLst = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) {
                directoryLst.add(file);
                //directoryContents(file);   //para conocer los archivos de las subcarpetas
            } else {
                fileLst.add(file);
            }
        }
        allDocsLst.addAll(directoryLst);
        allDocsLst.addAll(fileLst);

        return allDocsLst;
    }

}
