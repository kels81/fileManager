/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vaadin.demo.dashboard.component;

//import com.vaadin.addon.contextmenu.ContextMenu;
//import com.vaadin.addon.contextmenu.MenuItem;
//import com.vaadin.addon.contextmenu.Menu;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.demo.dashboard.utils.Components;
import com.vaadin.demo.dashboard.utils.Mail;
import com.vaadin.demo.dashboard.utils.Notifications;
import com.vaadin.event.Action;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
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
import java.io.Serializable;
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
    private HierarchicalContainer container;
    private final Components comp = new Components();

    private Button btnAdjuntar;
    private Button btnCancelar;
    private Button btnEnviar;
    private AddressEditor paraTxt;
    private TextField asuntoTxt;
    private RichTextArea cuerpoCorreo;
    private final Notifications notification = new Notifications();
    //private final ContextMenu contextMenu = new ContextMenu(this, false);

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
        content.setExpandRatio(detailsWrapper, 1.0f);
        content.addComponent(buildFooter());

        setContent(content);
    }

    private VerticalLayout fields() {
        root = new VerticalLayout();
        root.setCaption("Email");
        root.setWidth(100.0f, Unit.PERCENTAGE);
        root.setSpacing(true);
        //root.setMargin(new MarginInfo(true, false, true, false));
        root.setMargin(true);

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
                DirectoryTreeWindow2 directoryTreeWindow = new DirectoryTreeWindow2();
                Window w = directoryTreeWindow;
                //Window w = createWindow();
                UI.getCurrent().addWindow(w);
                w.focus();
            }
        });

        hl.addComponents(btnAdjuntar, adjuntar);
        hl.setExpandRatio(adjuntar, 1.0f);

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

        //Page.getCurrent().getStyles().add(".v-verticallayout {border: 1px solid blue;} .v-verticallayout .v-slot {border: 1px solid red;}");
        return root;
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.setSpacing(true);
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        btnCancelar = new Button("Cancelar");
        btnCancelar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });

        btnEnviar = new Button("Enviar");
        btnEnviar.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnEnviar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                //System.out.println("para: " + paraTxt.getValue());
                //boolean enviar = (StringUtils.isNotBlank(paraTxt.getValue()) && StringUtils.isNotBlank(asuntoTxt.getValue()) && StringUtils.isNotBlank(cuerpoCorreo.getValue()));
                boolean enviar = (StringUtils.isNotBlank(paraTxt.getValue().toString()) && StringUtils.isNotBlank(asuntoTxt.getValue()) && StringUtils.isNotBlank(cuerpoCorreo.getValue()));
                if (enviar) {
                    Mail sendMail = new Mail();

                    String asunto = asuntoTxt.getValue();
                    String mensaje = cuerpoCorreo.getValue();
                    //List<String> receptores = new ArrayList<String>();
                    //receptores.add(paraTxt.getValue());

                    String receptores = paraTxt.getValue().toString();
                    System.out.println("receptores = " + receptores);

                    List<String> adjuntos = new ArrayList<>();
                    for (int i = 0; i < filesAttached.getComponentCount(); i++) {
                        Component c = filesAttached.getComponent(i);
                        adjuntos.add(filesAttached.getComponent(i).getCaption());
                    }

                    boolean envio = sendMail.enviarSpring(asunto, receptores, mensaje, adjuntos);
                    System.out.println("envio = " + envio);

                    String message = envio ? "Se envió con éxito" : "Problemas con el envío";
                    notification.createSuccess(message);

                    close();
                } else {
                    notification.createFailure("Favor de revisar los campos");
                }
            }
        });

        footer.addComponents(btnCancelar, btnEnviar);
        footer.setExpandRatio(btnCancelar, 1.0f);
        footer.setComponentAlignment(btnCancelar, Alignment.TOP_RIGHT);
        return footer;
    }

//    private Window createWindow() {
//        Window window = new Window();
//        Responsive.makeResponsive(this);
//
//        window.addStyleName("directorywindow");
//        window.setModal(true);
//        window.setResizable(false);
//        window.setClosable(true);
//        window.center();
//
//        window.setHeight(90.0f, Unit.PERCENTAGE);
//
//        VerticalLayout content = new VerticalLayout();
//        content.setSizeFull();
//        content.setMargin(true);
//
//        TabSheet detailsWrapper = new TabSheet();
//        detailsWrapper.setSizeFull();
//        detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
//        detailsWrapper.addComponent(tree());
//
//        content.addComponent(detailsWrapper);
//        content.setExpandRatio(detailsWrapper, 1f);
//        content.addComponent(buildFooterWindow());
//
//        window.setContent(content);
//
//        return window;
//    }

//    private HorizontalLayout tree() {
//        HorizontalLayout root = new HorizontalLayout();
//        root.setCaption("Archivos");
//        root.setMargin(true);
//
//        Container generateContainer = getDirectoryContainer(origenPath);
//        tree = new Tree();
//        tree.setId("idTreeDirectory");
//        tree.setContainerDataSource(generateContainer);
//        tree.setItemCaptionPropertyId("caption");
//        tree.setItemIconPropertyId("icon");
//        tree.setImmediate(true);
//        tree.setSelectable(false);
//        //tree.addActionHandler(actionHandler);
//        //tree.addActionHandler(new Action.Handler() { //VA EL CODIGO QUE TIENE EL actionHandler});
//        tree.addExpandListener(getTreeExpandListener());
//        tree.addCollapseListener(new Tree.CollapseListener() {
//            @Override
//            public void nodeCollapse(Tree.CollapseEvent event) {
//                // Remove all children of the collapsing node
//                Object children[] = tree.getChildren(event.getItemId()).toArray();
//                for (Object childrenItem : children) {
//                    tree.collapseItemsRecursively(childrenItem);
//                    //tree.collapseItem(childrenItem);
//                }
//            }
//        });
//        //ESTE METODO SIRVE CUANDO SELECCIONAS UN ITEM CON EL BUTTON_RIGHT DEL MOUSE
//        tree.addItemClickListener(new ItemClickEvent.ItemClickListener() {
//            @Override
//            public void itemClick(ItemClickEvent event) {
//                Object itemId = event.getItemId();
//                if (event.getItem().getItemProperty("type").getValue().equals("file")) {
//                    tree.select(event.getItemId());
//                }
//                //VALIDACION PARA EXPANDIR NODO DESDE EL LABEL
//                if (event.isDoubleClick()) {
//                    //Notification.show("789797_"+event.getItem().getItemProperty("caption").getValue());
//                    if (tree.isExpanded(itemId)) {
//                        tree.collapseItem(itemId);
//                    } else {
//                        tree.expandItem(itemId);
//                    }
//                }
//            }
//        });
//        //ESTE METODO SIRVE CUANDO SELECCIONAS UN ITEM CON EL BUTTON_LEFT DEL MOUSE
////        tree.addValueChangeListener(new Property.ValueChangeListener() {
////            @Override
////            public void valueChange(Property.ValueChangeEvent event) {
////                Notification.show("789797_"+event.getProperty().getValue().toString());
////            }
////        });
//
//        ContextMenu contextMenu = new ContextMenu(this, false);
//        fillMenu(contextMenu);
//        contextMenu.setAsContextMenuOf(tree);
//
//        root.addComponent(tree);
//
//        return root;
//    }

//    private void fillMenu(ContextMenu menu) {
//        MenuItem addFile = menu.addItem("Adjuntar", e -> {
//            //Notification.show("checked: " + e.isChecked());
//            Item item = tree.getItem(tree.getValue());
//            //VALIDACION PARA ADJUNTAR SOLAMENTE ARCHIVOS NO CARPETAS
//            if (item.getItemProperty("type").getValue().equals("file")) {
//                buildAttachedFile(item);
//            } else {
//                notification.createFailure("No se pueden adjuntar carpetas.");  //PARA QUE SE VEA ESTE MENSAJE, SETSELECTABLE = TRUE
//            }
//        });
//        addFile.setIcon(FontAwesome.PAPERCLIP);
//    }

//    private Component buildFooterWindow() {
//        HorizontalLayout footer = new HorizontalLayout();
//        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
//        footer.setWidth(100.0f, Unit.PERCENTAGE);
//        footer.setHeight(30.0f, Unit.PIXELS);
//
//        Button btnCerrar = new Button("Cerrar");
//        btnCerrar.addStyleName(ValoTheme.BUTTON_PRIMARY);
//        btnCerrar.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                close();
//            }
//        });
//        //ok.focus();
//        //footer.addComponent(ok);
//        //footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);
//        return footer;
//    }

//    public HierarchicalContainer getDirectoryContainer(File directory) {
//
//        // Create new container
//        container = new HierarchicalContainer();
//        // Create containerproperty for name
//        container.addContainerProperty("icon", ThemeResource.class, null);
//        container.addContainerProperty("caption", String.class, null);
//        container.addContainerProperty("path", String.class, null);
//        container.addContainerProperty("type", String.class, null);
//
//        createTreeContent(directory, null);
//
//        return container;
//    }

    /*
    * CODIGO PARA USAR EN METODO addActionHandler
     */
//    Action.Handler actionHandler = new Action.Handler() {
//        private final Action ADJUNTAR = new Action(FontAwesome.PAPERCLIP.getHtml() + " Adjuntar");
//        private final Action[] ACTIONS = new Action[]{ADJUNTAR};
//
//        @Override
//        public Action[] getActions(Object target, Object sender) {
//            return ACTIONS;
//        }
//
//        @Override
//        public void handleAction(Action action, Object sender, Object target) {
//            //Notification.show(action.getCaption());
//            Item item = tree.getItem(target);
//            //VALIDACION PARA ADJUNTAR SOLAMENTE ARCHIVOS NO CARPETAS
//            if (action == ADJUNTAR && item.getItemProperty("type").getValue().equals("file")) {
//                buildAttachedFile(item);
//            } else {
//                notification.createFailure("No se pueden adjuntar carpetas.");  //PARA QUE SE VEA ESTE MENSAJE, SETSELECTABLE = TRUE
//            }
//        }
//    };
//
//    Action.Handler getActionHandler() {
//        return actionHandler;
//    }
//
//    /**
//     * CODIGO PARA USAR EN METODO addExpandListener
//     */
//    Tree.ExpandListener treeExpand = new Tree.ExpandListener() {
//        @Override
//        public void nodeExpand(Tree.ExpandEvent event) {
//            createTreeContent(new File(event.getItemId().toString()), event);
//        }
//    };
//
//    Tree.ExpandListener getTreeExpandListener() {
//        return treeExpand;
//    }
//
//    private void createTreeContent(File directory, Tree.ExpandEvent event) {
//
//        List<File> files = comp.directoryContents(directory);
//
//        for (File file : files) {
//            String source = file.isDirectory() ? "folder" : "file";
//
//            container.addItem(file);
//            container.getItem(file).getItemProperty("caption").setValue(file.getName());
//            container.getItem(file).getItemProperty("icon").setValue(new ThemeResource("img/file_manager/" + source + "_24.png"));
//            container.getItem(file).getItemProperty("path").setValue(file.getAbsolutePath());
//            container.getItem(file).getItemProperty("type").setValue(source);
//
//            if (event != null) {
//                tree.setParent(file, event.getItemId());
//            }
//
//            // SI SE ENCUENTRA VACIA LA CARPETA, NO MOSTRARA LA FLECHA DE EXPANDIR
//            if (file.isFile() || (file.isDirectory() && file.list().length == 0)) {
//                container.setChildrenAllowed(file, false);
//            }
//        }
//    }

//    private void buildAttachedFile(Item item) {
//        String path = item.getItemProperty("path").getValue().toString();
//        String name = path.substring(path.lastIndexOf('\\') + 1);
//
//        HorizontalLayout adjLayout = new HorizontalLayout();
//        adjLayout.addStyleName("attachedlayout");
//
//        Label pathFile = new Label();
//        pathFile.setCaption(path);
//
//        Label nameFile = new Label("&nbsp;" + FontAwesome.FILE_TEXT_O.getHtml() + "&nbsp;" + name + "&nbsp;");
//        nameFile.setContentMode(ContentMode.HTML);
//        nameFile.addStyleName(ValoTheme.LABEL_LIGHT);
//        nameFile.addStyleName(ValoTheme.LABEL_SMALL);
//
//        Button delete = new Button("×");
//        delete.setDescription("Eliminar");
//        delete.setPrimaryStyleName("deleteBtn");
//        delete.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(final Button.ClickEvent event) {
//                removeAttachedFile(adjLayout, pathFile);
//            }
//        });
//
//        adjLayout.addComponents(nameFile, delete);
//
//        addAttachedFile(adjLayout, pathFile, name);
//        
//    }

//    public void addAttachedFile(Component attachedFile, Label pathFile, String name) {
//        // NOMBRE DE ARCHIVOS QUE VE EL USAURIO
//        adjuntar.addComponent(attachedFile);
//        UI.getCurrent().setContent(attachedFile);
//        Notification.show(adjuntar.getId()+","+adjuntar.getClass());
//        // LA RUTA DE LOS ARCHIVOS QUE VE EL USUARIO, PERO QUE SE ENCUENTRA OCULTO
//        filesAttached.addComponent(pathFile);
//        notification.createSuccess("Se adjunto el archivo: " + name);
//    }

//    public void removeAttachedFile(Component attachedFile, Label pathFile) {
//        // ELIMINAR LABEL DEL ARCHIVO QUE VE EL USUARIO
//        adjuntar.removeComponent(attachedFile);
//        // ELIMINAR TAMBIEN EL PATH DEL ARCHIVO QUE SE ENCUENTRA OCULTO
//        // EL QUE SE MANDA POR CORREO
//        filesAttached.removeComponent(pathFile);
//    }

    EmailWindowListener listener = null;

    void close(boolean ok) {
        if (listener != null) {
            listener.close(ok);
        }
    }

    public void setListener(EmailWindowListener listener) {
        this.listener = listener;
    }

    public interface EmailWindowListener extends Serializable {
        public void close(boolean ok);
    }

    public class DirectoryTreeWindow2 extends Window {

        private final File origenPath;
        private final VerticalLayout content;
        private HorizontalLayout root;
        private Tree tree;
        private final TabSheet detailsWrapper;
        private HierarchicalContainer container;

        private final Components comp = new Components();
        private final Notifications notification = new Notifications();

        public DirectoryTreeWindow2() {
            this.origenPath = new File("C:\\Users\\Edrd\\Documents\\GitHub\\fileManager\\Archivos");

            Responsive.makeResponsive(this);

            addStyleName("directorywindow");
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
            detailsWrapper.addComponent(tree());

            content.addComponent(detailsWrapper);
            content.setExpandRatio(detailsWrapper, 1.0f);
            content.addComponent(buildFooter());

            setContent(content);
        }

        private HorizontalLayout tree() {
            root = new HorizontalLayout();
            root.setCaption("Archivos ");
            root.setMargin(true);

            Container generateContainer = getDirectoryContainer(origenPath);
            tree = new Tree();
            tree.setContainerDataSource(generateContainer);
            tree.setItemCaptionPropertyId("caption");
            tree.setItemIconPropertyId("icon");
            tree.setImmediate(true);
            tree.setSelectable(false);
            tree.addActionHandler(new Action.Handler() {
                private final Action ADJUNTAR = new Action(FontAwesome.PAPERCLIP.getHtml() + " Adjuntar");
                private final Action[] ACTIONS = new Action[]{ADJUNTAR};

                @Override
                public Action[] getActions(Object target, Object sender) {
                    return ACTIONS;
                }

                @Override
                public void handleAction(Action action, Object sender, Object target) {
                    //Notification.show(action.getCaption());
                    Item item = tree.getItem(target);
                    //VALIDACION PARA ADJUNTAR SOLAMENTE ARCHIVOS NO CARPETAS
                    if (action == ADJUNTAR && item.getItemProperty("type").getValue().equals("file")) {
                        buildAttachedFile(item);
                    } else {
                        notification.createFailure("No se pueden adjuntar carpetas.");  //PARA QUE SE VEA ESTE MENSAJE, SETSELECTABLE = TRUE
                    }
                }
            });
            tree.addExpandListener(new Tree.ExpandListener() {
                @Override
                public void nodeExpand(Tree.ExpandEvent event) {
                    createTreeContent(new File(event.getItemId().toString()), event);
                }
            });
            tree.addCollapseListener(new Tree.CollapseListener() {
                @Override
                public void nodeCollapse(Tree.CollapseEvent event) {
                    // Remove all children of the collapsing node
                    Object children[] = tree.getChildren(event.getItemId()).toArray();
                    for (Object childrenItem : children) {
                        tree.collapseItemsRecursively(childrenItem);
                        //tree.collapseItem(childrenItem);
                    }
                }
            });
            tree.addItemClickListener(new ItemClickEvent.ItemClickListener() {
                @Override
                public void itemClick(ItemClickEvent event) {
                    Object itemId = event.getItemId();
                    if (event.getItem().getItemProperty("type").getValue().equals("file")) {
                        tree.select(event.getItemId());
                    }
                    //VALIDACION PARA EXPANDIR NODE DESDE EL LABEL
                    if (event.isDoubleClick()) {
                        //Notification.show("789797_"+event.getItem().getItemProperty("caption").getValue());
                        if (tree.isExpanded(itemId)) {
                            tree.collapseItem(itemId);
                        } else {
                            tree.expandItem(itemId);
                        }
                    }
                }
            });

            root.addComponent(tree);

            return root;
        }

        private Component buildFooter() {
            HorizontalLayout footer = new HorizontalLayout();
            footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
            footer.setWidth(100.0f, Unit.PERCENTAGE);
            footer.setHeight(30.0f, Unit.PIXELS);

            Button btnCerrar = new Button("Cerrar");
            btnCerrar.addStyleName(ValoTheme.BUTTON_PRIMARY);
            btnCerrar.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    close();
                }
            });
            //footer.addComponent(btnCerrar);
            //footer.setComponentAlignment(btnCerrar, Alignment.TOP_RIGHT);
            return footer;
        }

        public HierarchicalContainer getDirectoryContainer(File directory) {

            // Create new container
            container = new HierarchicalContainer();
            // Create containerproperty for name
            container.addContainerProperty("icon", ThemeResource.class, null);
            container.addContainerProperty("caption", String.class, null);
            container.addContainerProperty("path", String.class, null);
            container.addContainerProperty("type", String.class, null);

            createTreeContent(directory, null);

            return container;
        }

        private void createTreeContent(File directory, Tree.ExpandEvent event) {

            List<File> files = comp.directoryContents(directory);

            for (File file : files) {
                String source = file.isDirectory() ? "folder" : "file";

                container.addItem(file);
                container.getItem(file).getItemProperty("caption").setValue(file.getName());
                container.getItem(file).getItemProperty("icon").setValue(new ThemeResource("img/file_manager/" + source + "_24.png"));
                container.getItem(file).getItemProperty("path").setValue(file.getAbsolutePath());
                container.getItem(file).getItemProperty("type").setValue(source);

                if (event != null) {
                    container.setParent(file, event.getItemId());
                }
                // SI SE ENCUENTRA VACIA LA CARPETA, NO MOSTRARA LA FLECHA DE EXPANDIR
                if (file.isFile() || (file.isDirectory() && file.list().length == 0)) {
                    container.setChildrenAllowed(file, false);
                }
            }
        }

        private void buildAttachedFile(Item item) {
            String path = item.getItemProperty("path").getValue().toString();
            String name = path.substring(path.lastIndexOf('\\') + 1);

            HorizontalLayout adjLayout = new HorizontalLayout();
            adjLayout.addStyleName("attachedlayout");

            Label pathFile = new Label(path);
            pathFile.addStyleName(ValoTheme.LABEL_TINY);

            Label nameFile = new Label("&nbsp;" + FontAwesome.FILE_TEXT_O.getHtml() + "&nbsp;" + name + "&nbsp;");
            nameFile.setContentMode(ContentMode.HTML);
            nameFile.addStyleName(ValoTheme.LABEL_LIGHT);
            nameFile.addStyleName(ValoTheme.LABEL_SMALL);

            Button delete = new Button("×");
            delete.setDescription("Eliminar");
            delete.setPrimaryStyleName("deleteBtn");
            delete.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    removeAttachedFIle(adjLayout, pathFile);
                }
            });

            adjLayout.addComponents(nameFile, delete);
            addAttachedFile(adjLayout, pathFile, name);
        }
        
        private void addAttachedFile(Component attachedFile, Label pathFile, String name) {
        // NOMBRE DE ARCHIVOS QUE VE EL USAURIO
        adjuntar.addComponent(attachedFile);
        // LA RUTA DE LOS ARCHIVOS QUE VE EL USUARIO, PERO QUE SE ENCUENTRA OCULTO
        filesAttached.addComponent(pathFile);
        notification.createSuccess("Se adjunto el archivo: " + name);
    }

    private void removeAttachedFIle(Component attachedFile, Label pathFile) {
        // ELIMINAR LABEL DEL ARCHIVO QUE VE EL USUARIO
        adjuntar.removeComponent(attachedFile);
        // ELIMINAR TAMBIEN EL PATH DEL ARCHIVO QUE SE ENCUENTRA OCULTO
        // EL QUE SE MANDA POR CORREO
        filesAttached.removeComponent(pathFile);
    }
    
    }
    
}
