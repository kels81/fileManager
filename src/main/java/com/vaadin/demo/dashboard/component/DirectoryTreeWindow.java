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
import com.vaadin.demo.dashboard.utils.Notifications;
import com.vaadin.event.Action;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import java.util.List;

/**
 *
 * @author Edrd
 */
public class DirectoryTreeWindow extends Window {

    private final File origenPath;
    private final VerticalLayout content;
    private HorizontalLayout root;
    private Tree tree;
    private final TabSheet detailsWrapper;
    private HierarchicalContainer container;

    private final Components comp = new Components();
    private final Notifications notification = new Notifications();

    public DirectoryTreeWindow() {
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
        root.setCaption("Archivos 88989");
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
                tree.setParent(file, event.getItemId());
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

        Label pathFile = new Label();
        pathFile.setCaption(path);

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
                //new EmailWindow().removeAttachedFile(adjLayout, pathFile);
            }
        });

        adjLayout.addComponents(nameFile, delete);

        EmailWindow emailWdw = new EmailWindow();
        //emailWdw.setListener((boolean ok) -> {
            //emailWdw.addAttachedFile(adjLayout, pathFile, name);
        //});

//         List arrayAdjuntos = new ArrayList();
//                    for (int i = 0; i < filesAttached.getComponentCount(); i++) {
//                        Component c = filesAttached.getComponent(i);
//                            arrayAdjuntos.add(c);
//                            System.out.println("c = " + filesAttached.getComponent(i).getCaption());
//                    }
    }

    public static Component findComponentById(HasComponents root, String id) {
        for (Component child : root) {
            if (id.equals(child.getId())) {
                return child; // found it!
            } else if (child instanceof HasComponents) { // recursively go through all children that themselves have children
                Component result = findComponentById((HasComponents) child, id);
                if (result != null) {
                    return result;
                }
            }
        }
        return null; // none was found
    }
}
