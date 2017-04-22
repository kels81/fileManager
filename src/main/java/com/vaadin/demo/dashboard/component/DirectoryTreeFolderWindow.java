/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vaadin.demo.dashboard.component;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.demo.dashboard.utils.Components;
import com.vaadin.demo.dashboard.utils.Notifications;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

/**
 *
 * @author Edrd
 */
public class DirectoryTreeFolderWindow extends Window {

    private final File origenPath;
    private final VerticalLayout content;
    private VerticalLayout body;
    private VerticalLayout root;
    private Tree tree;
    private final TabSheet detailsWrapper;
    private HierarchicalContainer container;
    private Label lblFileName;
    private File fileTo;

    private final Components comp = new Components();
    private final Notifications notification = new Notifications();

    public DirectoryTreeFolderWindow(File file) {
        this.origenPath = new File("C:\\Users\\Edrd\\Documents\\GitHub\\fileManager\\Archivos");
        this.fileTo = file;

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
        detailsWrapper.addComponent(body());

        content.addComponent(detailsWrapper);
        content.setExpandRatio(detailsWrapper, 1.0f);
        content.addComponent(buildFooter());

        setContent(content);
    }

    private VerticalLayout body() {
        body = new VerticalLayout();
        body.setCaption("Mover o Copiar");
        //body.setSizeFull();
        body.setMargin(true);
        body.setSpacing(true);
        body.addComponent(buildFileName());
        Component tree = buildTree();
        body.addComponent(tree);
        body.setExpandRatio(tree, 1.0f);

        //Page.getCurrent().getStyles().add(".v-verticallayout {border: 1px solid blue;} .v-verticallayout .v-slot {border: 1px solid red;}");
        return body;
    }

    private Label buildFileName() {
        return lblFileName = new Label(fileTo.getName());
    }

    private Component buildTree() {
        root = new VerticalLayout();
        //root.setMargin(new MarginInfo(true, false));

        Container generateContainer = getDirectoryContainer(origenPath);
        tree = new Tree();
        tree.setContainerDataSource(generateContainer);
        tree.setItemCaptionPropertyId("caption");
        tree.setItemIconPropertyId("icon");
        tree.setImmediate(true);
        tree.setSelectable(true);
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
                }
            }
        });
        tree.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                Notification.show("6666_"+event.getItem().getItemProperty("caption").getValue());
                Object itemId = event.getItemId();
                //VALIDACION PARA EXPANDIR NODE DESDE EL LABEL
                if (event.isDoubleClick()) {
                    if (tree.isExpanded(itemId)) {
                        tree.collapseItem(itemId);
                    } else {
                        tree.expandItem(itemId);
                    }
                }
            }
        });
        tree.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Notification.show("789797_"+event.getProperty().getValue().toString());
            }
        });

        root.addComponent(tree);
        
        return root;
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.setSpacing(true);
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });

        Button btnMover = new Button("Mover");
        btnMover.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnMover.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Path source = Paths.get(fileTo.getAbsolutePath());
                Path target = Paths.get(tree.getValue().toString()+"\\"+fileTo.getName());

                System.out.println("sourceMov = " + source);
                System.out.println("targetMov = " + target);
                moveFile(source, target);
                close();
            }
        });
        Button btnCopiar = new Button("Copiar");
        btnCopiar.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnCopiar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Path source = Paths.get(fileTo.getAbsolutePath());
                Path target = Paths.get(tree.getValue().toString()+"\\"+fileTo.getName());

                System.out.println("sourceCop = " + source);
                System.out.println("targetCop"
                        + " = " + target);
                copyFile(source, target);
                close();
            }
        });

        footer.addComponents(btnCancelar, btnMover, btnCopiar);
        footer.setExpandRatio(btnCancelar, 1.0f);
        footer.setComponentAlignment(btnCancelar, Alignment.TOP_RIGHT);
        footer.setComponentAlignment(btnMover, Alignment.TOP_RIGHT);
        footer.setComponentAlignment(btnCopiar, Alignment.TOP_RIGHT);
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

        File[] files = directory.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);     //PARA OBTENER UNICAMENTE DIRECTORIOS DE UN DIRECTORIO
        Arrays.sort(files);

        for (File file : files) {

            Boolean allow = Boolean.FALSE;

            //PARA SABER SI EL DIRECTORIO TIENE ADENTRO OTROS DIRECTORIOS Y PODER MOSTRAR LA FLECHA DE EXPANDIR
            if (file.isDirectory() && file.list().length != 0) {
                File[] subDirectory = file.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
                allow = subDirectory.length != 0;
            }

            container.addItem(file);
            container.getItem(file).getItemProperty("caption").setValue(file.getName());
            container.getItem(file).getItemProperty("icon").setValue(new ThemeResource("img/file_manager/folder_24.png"));
            container.getItem(file).getItemProperty("path").setValue(file.getAbsolutePath());
            container.getItem(file).getItemProperty("type").setValue("folder");
            container.setChildrenAllowed(file, allow);  // SI SE ENCUENTRA VACIA LA CARPETA, NO MOSTRARA LA FLECHA DE EXPANDIR

            if (event != null) {
                container.setParent(file, event.getItemId());
            }
        }
    }

    private void moveFile(Path sourceDir, Path targetDir) {
        
        try {
            Files.move(sourceDir, targetDir, StandardCopyOption.REPLACE_EXISTING);
            notification.createSuccess("Se movio el archivo correctamente: " + fileTo.getName());
        } catch (FileAlreadyExistsException ex) {
            notification.createFailure("Ya existe un archivo con el mismo nombre en esta carpeta");
        } catch (IOException ex) {
            notification.createFailure("Problemas al mover el archivo");
        }
    }

    private void copyFile(Path sourceDir, Path targetDir) {
        
        try {
            Files.copy(sourceDir, targetDir);
            notification.createSuccess("Se copio el archvio correctamente: " + fileTo.getName());
        } catch (FileAlreadyExistsException ex) {
            notification.createFailure("Ya existe un archivo con el mismo nombre en esta carpeta");
        } catch (IOException ex) {
            notification.createFailure("Problemas al copiar el archivo");
        }
    }

}
