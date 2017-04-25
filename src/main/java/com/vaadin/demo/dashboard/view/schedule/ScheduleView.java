package com.vaadin.demo.dashboard.view.schedule;

import com.vaadin.addon.contextmenu.MenuItem;
import com.vaadin.addon.contextmenu.ContextMenu;
import com.vaadin.addon.contextmenu.Menu;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.demo.dashboard.component.EmailWindow;
import com.vaadin.demo.dashboard.utils.Components;
import com.vaadin.demo.dashboard.utils.Notifications;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.lang3.StringUtils;
import pl.exsio.plupload.Plupload;
import pl.exsio.plupload.PluploadError;
import pl.exsio.plupload.PluploadFile;

@SuppressWarnings("serial")
public final class ScheduleView extends Panel implements View {

    private ThemeResource iconResource;
    private File path;
    private final File origenPath;
    private final VerticalLayout tabs;
    private HorizontalLayout rootPath;
    private Button btnFolder;
    private Label lblArrow;
    private final Components component = new Components();
    private Component directoryContent;
    private HorizontalLayout toolBar;
    private Window window;
    private Button create;
    private Button save;
    private Button cancel;
    private Tree tree;
    private final ProgressBar progressBar = new ProgressBar(0.0f);
    private final Notifications notification = new Notifications();

    public ScheduleView() {
        this.origenPath = new File("C:\\Users\\Edrd\\Documents\\GitHub\\fileManager\\Archivos");
        //this.path = VaadinService.getCurrent().getBaseDirectory() + "/VAADIN/themes/UPLOADS/";

        setSizeFull();
        addStyleName("schedule");
        //addStyleName(ValoTheme.PANEL_BORDERLESS);

        tabs = new VerticalLayout();
        //tabs.setSizeFull();

        tabs.addComponent(buildToolBar(origenPath));
        tabs.addComponent(buildPath());
        directoryContent = displayDirectoryContents(origenPath);
        tabs.addComponent(directoryContent);
        tabs.setExpandRatio(directoryContent, 1);

        progressBar.setCaption("Progress");

        //addComponent(tabs);
        //addComponent(progressBar);
        setContent(tabs);
        Responsive.makeResponsive(tabs);
    }

    private Component buildToolBar(File directory) {
        toolBar = new HorizontalLayout();
        //toolBar.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
        toolBar.setSpacing(true);
        //toolBar.setMargin(new MarginInfo(true, false, false, true));
        toolBar.addStyleName("toolbarFile");

        MenuBar menubar = component.createMenuBar();
        MenuBar.MenuItem menu = menubar.addItem("Nuevo", null);
        menu.setIcon(FontAwesome.PLUS);
        menu.addItem("Carpeta", FontAwesome.FOLDER_O, (MenuBar.MenuItem selectedItem) -> {
            Window w = createWindow();
            UI.getCurrent().addWindow(w);
            w.focus();
        });

        Button email = component.createButton("Email");
        email.setIcon(FontAwesome.ENVELOPE_O);
        email.addClickListener((ClickEvent event) -> {
            EmailWindow emailWdw = new EmailWindow();
            Window w = emailWdw;
            UI.getCurrent().addWindow(w);
            w.focus();
        });

        // CARGAR
        Plupload uploader = uploadContents(directory);

        toolBar.addComponent(menubar);
        toolBar.addComponent(uploader);
        toolBar.addComponent(email);

        return toolBar;
    }

    private Component buildPath() {
        rootPath = new HorizontalLayout();
        //rootPath.setMargin(new MarginInfo(true, false, false, true));
        rootPath.addStyleName("barPath");
        //rootPath.setWidth(100.0f, Unit.PERCENTAGE);

        Button button = component.createButtonPath("Archivos");
        button.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                cleanAndBuild(origenPath);
            }
        });

        rootPath.addComponent(button);

        return rootPath;
    }

    private Component displayDirectoryContents(File pathDirectory) {
        path = pathDirectory;
        System.out.println("pathDirectoryContents = " + path);

        CssLayout catalog = new CssLayout();
        catalog.addStyleName("catalog");
        catalog.setSizeUndefined();

        File currentDir = new File(path.getAbsolutePath());
        List<File> files = (List<File>) component.directoryContents(currentDir);

        //for (final File file : files) {
        files.stream().map((file) -> {
            CssLayout mainPanel = new CssLayout();
            mainPanel.addStyleName("mainPanel");
            VerticalLayout frame = new VerticalLayout();
            frame.addStyleName("frame");
            frame.setMargin(true);
            frame.addStyleName(ValoTheme.LAYOUT_CARD);
            frame.setWidth(190.0f, Unit.PIXELS);
            HorizontalLayout fileLayout = new HorizontalLayout();
            //fileLayout.setSpacing(true);
            //fileLayout.setDescription(file.getName());
            frame.addComponent(fileLayout);
            Image fileType = new Image(null, iconExtension(file));
            fileType.setWidth(45.0f, Unit.PIXELS);
            fileType.setHeight(48.0f, Unit.PIXELS);
            fileLayout.addComponent(fileType);
            VerticalLayout nameDetailsFile = new VerticalLayout();
            fileLayout.addComponent(nameDetailsFile);
            fileLayout.setComponentAlignment(nameDetailsFile, Alignment.BOTTOM_LEFT);
            String name = file.getName();
            name = (name.length() > 18 ? name.substring(0, 15) + "..." : name);
            Label nameFile = new Label(name);
            nameFile.addStyleName(ValoTheme.LABEL_TINY);
            nameFile.addStyleName(ValoTheme.LABEL_BOLD);
            nameFile.setWidth(115.0f, Unit.PIXELS);
            nameDetailsFile.addComponent(nameFile);
            nameDetailsFile.setComponentAlignment(nameFile, Alignment.BOTTOM_LEFT);
            long fileSize = file.length();
            String fileSizeDisplay = FileUtils.byteCountToDisplaySize(fileSize);
            String label = (file.isDirectory()
                    ? String.valueOf(file.list().length == 0
                            ? "" : file.list().length) + (file.list().length > 1
                            ? " elementos" : file.list().length == 0
                                    ? "vacío" : " elemento")
                    : fileSizeDisplay);
            Label detailsFile = new Label(label);
            detailsFile.addStyleName(ValoTheme.LABEL_TINY);
            nameDetailsFile.addComponent(detailsFile);
            nameDetailsFile.setComponentAlignment(detailsFile, Alignment.BOTTOM_LEFT);
            frame.addLayoutClickListener((LayoutClickEvent event) -> {
                if (event.getButton() == MouseButton.LEFT) {

                    if (file.isDirectory()) {
                        displaySubDirectoryContents(file);
                    } else if (file.isFile()) {
                        //downloadContents(file);
//                        Window w = new ViewerWindow(file);;
//                        UI.getCurrent().addWindow(w);
//                        w.focus();
                    }
                }
            });
            ContextMenu contextMenu = new ContextMenu(this, false);
            fillMenu(contextMenu, file);
            contextMenu.setAsContextMenuOf(frame);
            mainPanel.addComponent(frame);
            return mainPanel;
        }).forEach((mainPanel) -> {
            catalog.addComponent(mainPanel);
        });

        //File currentDir2 = new File("C:\\Users\\Edrd\\Documents\\GitHub\\fileManager\\Archivos"); // current directory
        //directoryContents23(currentDir2);
        return catalog;
    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }

    public List<File> directoryContents(File directory) {
        // ARRAY QUE VA A ACONTENER TODOS LOS ARCHIVOS ORDENADOS POR TIPO Y ALFABETICAMENTE
        List<File> allDocsLst = new ArrayList<>();
        File[] files = directory.listFiles();
        List<File> fileLst = new ArrayList<>();
        List<File> directoryLst = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) {
                //System.out.println("directory:" + file.getCanonicalPath());
                directoryLst.add(file);
                //recursiveFileDisplay(file);   //para conocer los archivos de las subcarpetas
            } else {
                //System.out.println("     file:" + file.getCanonicalPath());
                fileLst.add(file);
            }
        }
        allDocsLst.addAll(directoryLst);
        allDocsLst.addAll(fileLst);

        return allDocsLst;
    }

    public List<File> directoryContents23(File directory) {
        // ARRAY QUE VA A ACONTENER TODOS LOS ARCHIVOS ORDENADOS POR TIPO Y ALFABETICAMENTE
        List<File> allDocsLst = new ArrayList<>();
        try {
            File[] files = directory.listFiles();
            // ARRAY DONDE SE ALMACENAN LOS ARCHIVOS DE TIPO "DIRECTORY"
            List<File> fileLst = new ArrayList<>();
            // ARRAY DONDE SE ALMACENAN LOS ARCHIVOS DE TIPO "FILE"
            List<File> directoryLst = new ArrayList<>();

            // RECORRO LA LISTA DE LOS ARCHIVOS QUE CONTIENE EL DIRECTORIO, PARA LUEGO SEPARARLOS POR TIPO
            for (File file : files) {
                if (file.isDirectory()) {
                    System.out.println("directory:" + file.getCanonicalPath());
                    directoryLst.add(file);
                    directoryContents23(file);   //para conocer los archivos de las subcarpetas
                } else {
                    System.out.println("     file:" + file.getCanonicalPath());
                    fileLst.add(file);
                }
            }
            // AGREGO LOS VALORES DE CADA ARRAY DE TIPO "DIRECTORY" Y "FILE" AL ARRAY FINAL
            allDocsLst.addAll(directoryLst);
            allDocsLst.addAll(fileLst);

//            allDocsLst.stream().forEach((docs) -> {
//                System.out.println("docs = " + docs);
//            });
        } catch (IOException e) {
        }

        return allDocsLst;
    }

    private ThemeResource iconExtension(File file) {

        String extension = FilenameUtils.getExtension(file.getPath()).toLowerCase();
        if (file.isDirectory()) {
            iconResource = new ThemeResource("img/file_manager/folder_" + (file.list().length == 0 ? "empty" : "full") + ".png");
        } else {
            iconResource = new ThemeResource("img/file_manager/" + extension + ".png");
        }
        return iconResource;
    }

    private void displaySubDirectoryContents(File file) {
        cleanAndBuild(file);

        String root = "Archivos";
        String directory = nameDir(file, origenPath.getAbsolutePath());
        String[] arrayDirectories = directory.split("\\\\");

        for (String lblDirectory : arrayDirectories) {
            lblArrow = new Label(FontAwesome.ANGLE_RIGHT.getHtml(), ContentMode.HTML);
            lblArrow.addStyleName(ValoTheme.LABEL_COLORED);
            btnFolder = component.createButtonPath(lblDirectory);
            btnFolder.addClickListener(e -> {
                String newRoot = e.getComponent().getCaption();
                String directorys = file.getAbsolutePath();
                int inicio = directorys.indexOf(newRoot);
                String dir = directorys.substring(0, inicio + newRoot.length());

                cleanAndBuild(new File(dir));
                displaySubDirectoryContents(new File(dir));

            });

            rootPath.addComponent(lblArrow);
            rootPath.setComponentAlignment(lblArrow, Alignment.MIDDLE_CENTER);
            rootPath.addComponent(btnFolder);
        }
    }

    private void downloadContents(File file) {
        FileResource resPath = new FileResource(new File(file.getAbsolutePath()));
        /*
                            FileDownloader fileDownloader = new FileDownloader(resPath);
                            fileDownloader.markAsDirty();
                            fileDownloader.extend(frame);
         */
        Page.getCurrent().open(resPath, null, false);
    }

    private String nameDir(File file, String nameDir) {
        String root = nameDir;
        String directory = file.getAbsolutePath();
        int inicio = directory.indexOf(root);
        // SE REALIZA ESTA VALIDACION PARA EVITAR ERRORES EN LA VISTA DE LOS ARCHIVOS DEL FOLDER "ARCHIVOS" UNICAMENTE
        int uno = root.equals(directory) ? 0 : 1;
        String substring = directory.substring(inicio + root.length() + uno);

        return substring;
    }

    private void cleanAndBuild(File directory) {
        tabs.removeAllComponents();
        tabs.addComponent(buildToolBar(directory));
        tabs.addComponent(buildPath());
        directoryContent = displayDirectoryContents(directory);
        tabs.addComponent(directoryContent);
        tabs.setExpandRatio(directoryContent, 1);
    }

    private Window createWindow() {
        window = new Window();
        window.addStyleName("createfolder-window");
        Responsive.makeResponsive(this);

        window.setModal(true);
        window.setResizable(false);
        window.setClosable(true);
        window.setHeight(90.0f, Sizeable.Unit.PERCENTAGE);

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        window.setContent(content);

        TabSheet detailsWrapper = new TabSheet();
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        content.addComponent(detailsWrapper);
        content.setExpandRatio(detailsWrapper, 1f);

        /*[ NAMEFOLDER ]*/
        VerticalLayout body = new VerticalLayout();
        body.setCaption("Carpeta");
        body.setSizeFull();
        body.setSpacing(true);
        body.setMargin(true);

        TextField txtNameFolder = new TextField();
        txtNameFolder.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
        txtNameFolder.setInputPrompt("Escriba el nombre de carpeta");
        txtNameFolder.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);          //EAGER, Para que evento no sea lento
        txtNameFolder.setTextChangeTimeout(200);                                  //Duración para iniciar el evento
        txtNameFolder.setImmediate(true);
        txtNameFolder.addTextChangeListener((FieldEvents.TextChangeEvent event) -> {
            create.setEnabled(StringUtils.isNotBlank(event.getText()));
        });

        body.addComponent(txtNameFolder);
        body.setComponentAlignment(txtNameFolder, Alignment.MIDDLE_CENTER);
        /*[ /NAMEFOLDER ]*/

 /*[ FOOTER ]*/
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);

        create = component.createButton("Crear");
        create.focus();
        create.addStyleName(ValoTheme.BUTTON_PRIMARY);
        create.setEnabled(false);
        create.addClickListener((ClickEvent event) -> {
            File directory = new File(path + "\\" + txtNameFolder.getValue());
            System.out.println("directory = " + directory);
            directory.mkdir();

            notification.createSuccess("Se cargó con éxito");
            window.close();
            System.out.println("path = " + path.getAbsolutePath());
            cleanAndBuild(path);
            displaySubDirectoryContents(path);
        });
        create.setClickShortcut(ShortcutAction.KeyCode.ENTER, null);

        footer.addComponent(create);
        footer.setComponentAlignment(create, Alignment.TOP_RIGHT);
        /*[ /FOOTER ]*/

        detailsWrapper.addComponent(body);
        content.addComponent(footer);

        return window;
    }

    private Window createWindowEdit(File file) {
        window = new Window();
        window.addStyleName("createfolder-window");
        Responsive.makeResponsive(this);

        window.setModal(true);
        window.setResizable(false);
        window.setClosable(true);
        window.setHeight(90.0f, Sizeable.Unit.PERCENTAGE);
        //window.setWidth(300.0f, Unit.PIXELS);

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        window.setContent(content);

        TabSheet detailsWrapper = new TabSheet();
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        content.addComponent(detailsWrapper);
        content.setExpandRatio(detailsWrapper, 1f);

        /*[ NAMEFOLDER ]*/
        VerticalLayout body = new VerticalLayout();
        body.setCaption("Renombrar");
        body.setSizeFull();
        body.setSpacing(true);
        body.setMargin(true);

        TextField txtEditName = new TextField();
        txtEditName.focus();
        txtEditName.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
        txtEditName.setValue(FilenameUtils.getBaseName(file.getName()));    //Para mostrar solamente el nombre del archivo sin la extensión
        txtEditName.setInputPrompt("Nuevo nombre del archivo");
        txtEditName.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.EAGER);          //EAGER, Para que evento no sea lento
        txtEditName.setTextChangeTimeout(200);
        txtEditName.setImmediate(true);
        txtEditName.addTextChangeListener((FieldEvents.TextChangeEvent event) -> {
            save.setEnabled(StringUtils.isNotBlank(event.getText()));
        });

        body.addComponent(txtEditName);
        body.setComponentAlignment(txtEditName, Alignment.MIDDLE_CENTER);
        /*[ /NAMEFOLDER ]*/

 /*[ FOOTER ]*/
        HorizontalLayout footer = new HorizontalLayout();
        footer.setSpacing(true);
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);

        cancel = new Button("Cancelar");
        cancel.addStyleName(ValoTheme.BUTTON_SMALL);
        cancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                window.close();
            }
        });
        cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE, null);

        save = component.createButton("Guardar");
        save.addStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setEnabled(false);
        save.addClickListener((ClickEvent event) -> {
            // NUEVO NOMBRE
            String newName = txtEditName.getValue();
            // EXTENSION DEL ARCHIVO
            String extension = file.getName().substring(file.getName().lastIndexOf('.') + 1);
            // PATH DEL ARCHIVO
            String pathFile = file.getAbsolutePath();
            pathFile = pathFile.substring(0, pathFile.lastIndexOf("\\"));
            // SE CREA EL OBJETO DE TIPO FILE DEL NUEVO NOMBRE
            File fileNew = new File(pathFile + "\\" + newName + "." + extension);
            // SE REALIZA EL CAMBIO DE NOMBRE DEL ARCHIVO
            boolean cambio = file.renameTo(fileNew);

            if (cambio) {
                // SE RECARGA LA PAGINA, PARA MOSTRAR EL ARCHIVO CARGADO
                cleanAndBuild(new File(pathFile));
                displaySubDirectoryContents(new File(pathFile));
                notification.createSuccess("Se guardo con éxito");
            } else {
                notification.createFailure("No se guardo el cambio");
            }
            window.close();
        });
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER, null);

        footer.addComponents(cancel, save);
        footer.setExpandRatio(cancel, 1);
        footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);
        /*[ /FOOTER ]*/

        detailsWrapper.addComponent(body);
        content.addComponent(footer);

        return window;
    }

    private Plupload uploadContents(File directory) {

        String uploadPath = directory.getAbsolutePath();
        System.out.println("uploadPath = " + uploadPath);
        Plupload uploader = new Plupload("Cargar", FontAwesome.UPLOAD);
        //uploader.addFilter(new PluploadFilter("music", "mp3, flac"));
        uploader.setPreventDuplicates(true);
        uploader.addStyleName(ValoTheme.BUTTON_PRIMARY);
        uploader.addStyleName(ValoTheme.BUTTON_SMALL);
        uploader.setUploadPath(uploadPath);
        uploader.setMaxFileSize("5mb");

//show notification after file is uploaded
        uploader.addFileUploadedListener(new Plupload.FileUploadedListener() {
            @Override
            public void onFileUploaded(PluploadFile file) {

                /**
                 * CAMBIAR EL NOMBRE DEL ARCHIVO QUE SE SUBE, YA QUE NO RESPETA
                 * EL NOMBRE DEL ARCHIVO ORIGINAL
                 */
                File uploadedFile = (File) file.getUploadedFile();
                // NOMBRE CORRECTO
                String realName = file.getName();
                // NOMBRE INCORRECTO
                String falseName = uploadedFile.getName();
                // PATH DEL ARCHIVO
                String pathFile = uploadedFile.getAbsolutePath();
                pathFile = pathFile.substring(0, pathFile.lastIndexOf("\\"));
                System.out.println("pathFile = " + pathFile);
                // SE CREAN LOS OBJETIPOS DE TIPO FILE DE CADA UNO
                File fileFalse = new File(pathFile + "\\" + falseName);
                File fileReal = new File(pathFile + "\\" + realName);
                // SE REALIZA EL CAMBIO DE NOMBRE DEL ARCHIVO
                boolean cambio = fileFalse.renameTo(fileReal);

                // SE RECARGA LA PAGINA, PARA MOSTRAR EL ARCHIVO CARGADO
                cleanAndBuild(new File(uploadPath));
                displaySubDirectoryContents(new File(uploadPath));
                notification.createSuccess("Se cargó el archivo: " + file.getName());
            }
        });

//update upload progress
        uploader.addUploadProgressListener(new Plupload.UploadProgressListener() {
            @Override
            public void onUploadProgress(PluploadFile file) {

                progressBar.setWidth("128px");
                //progressBar.setStyleName(ValoTheme.PROGRESSBAR_POINT);
                progressBar.setVisible(true);

                progressBar.setValue(new Long(file.getPercent()).floatValue() / 100);
                progressBar.setDescription(file.getPercent() + "%");

                System.out.println("I'm uploading " + file.getName()
                        + "and I'm at " + file.getPercent() + "%");
            }
        });

//autostart the uploader after addind files
        uploader.addFilesAddedListener(new Plupload.FilesAddedListener() {
            @Override
            public void onFilesAdded(PluploadFile[] files) {
                progressBar.setValue(0f);
                progressBar.setVisible(true);
                uploader.start();
            }
        });

//notify, when the upload process is completed
        uploader.addUploadCompleteListener(new Plupload.UploadCompleteListener() {
            @Override
            public void onUploadComplete() {
                System.out.println("upload is completed!");
            }
        });

//handle errors
        uploader.addErrorListener(new Plupload.ErrorListener() {
            @Override
            public void onError(PluploadError error) {
                Notification.show("There was an error: "
                        + error.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        });

        return uploader;
    }

    private void fillMenu(ContextMenu menu, File file) {
        MenuItem editar = menu.addItem("Renombrar", e -> {
            Window w = createWindowEdit(file);
            UI.getCurrent().addWindow(w);
            w.focus();
        });
        editar.setIcon(FontAwesome.PENCIL);

        MenuItem borrar = menu.addItem("Borrar", new Menu.Command() {
            @Override
            public void menuSelected(MenuItem e) {
                Window w = createWindowConfirm(file);
                UI.getCurrent().addWindow(w);
                w.focus();
            }
        });
        borrar.setIcon(FontAwesome.TRASH);

        // SEPARADOR
        if (menu instanceof ContextMenu) {
            ((ContextMenu) menu).addSeparator();
        }

        MenuItem moverCopiar = menu.addItem("Mover o Copiar", e -> {
            //Notification.show("invisible");
            DirectoryTreeFolderWindow2 directoryTreeWindow = new DirectoryTreeFolderWindow2(file);
            Window w = directoryTreeWindow;
            UI.getCurrent().addWindow(w);
            w.focus();
        });
        moverCopiar.setIcon(FontAwesome.COPY);

//        MenuItem item6 = menu.addItem("Submenu", e -> {
//        });
//        item6.addItem("Subitem", e -> Notification.show("SubItem"));
//        item6.addSeparator();
//        item6.addItem("Subitem", e -> Notification.show("SubItem"))
//                .setDescription("Test");
    }

    private Window createWindowConfirm(File file) {
        window = new Window();
        window.addStyleName("confirm-window");
        Responsive.makeResponsive(this);

        window.setModal(true);
        window.setResizable(false);
        window.setClosable(true);
        window.setHeight(90.0f, Sizeable.Unit.PERCENTAGE);

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        window.setContent(content);

        TabSheet detailsWrapper = new TabSheet();
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        content.addComponent(detailsWrapper);
        content.setExpandRatio(detailsWrapper, 1.0f);

        /*[ BODY TEXT ]*/
        VerticalLayout body = new VerticalLayout();
        body.setCaption("Confirmar");
        body.setSizeFull();
        //body.setSpacing(true);
        body.setMargin(true);

        Label messageLbl = new Label("¿Está seguro de que desea eliminar este archivo?");
        messageLbl.setSizeUndefined();
        //messageLbl.addStyleName(ValoTheme.LABEL_LIGHT);
        //messageLbl.addStyleName(ValoTheme.LABEL_H4);

        body.addComponent(messageLbl);
        body.setComponentAlignment(messageLbl, Alignment.MIDDLE_LEFT);
        /*[ /BODY TEXT ]*/

 /*[ FOOTER ]*/
        HorizontalLayout footer = new HorizontalLayout();
        footer.setSpacing(true);
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);

        cancel = new Button("Cancelar");
        cancel.addStyleName(ValoTheme.BUTTON_SMALL);
        cancel.addClickListener((ClickEvent event) -> {
            window.close();
        });
        cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE, null);

        create = component.createButton("Aceptar");
        create.focus();
        create.addClickListener((ClickEvent event) -> {
            // PATH DEL ARCHIVO
            String pathFile = file.getAbsolutePath();
            pathFile = pathFile.substring(0, pathFile.lastIndexOf("\\"));
            // SE ELIMINA EL ARCHIVO
            boolean eliminar = file.delete();

            if (eliminar) {
                // SE RECARGA LA PAGINA, PARA MOSTRAR LOS DEMAS ARCHIVOS
                cleanAndBuild(new File(pathFile));
                displaySubDirectoryContents(new File(pathFile));
                notification.createSuccess("Se eliminó con éxito");
            } else {
                notification.createFailure("No se elimino el archivo");
            }

            window.close();
        });
        create.setClickShortcut(ShortcutAction.KeyCode.ENTER, null);

        footer.addComponents(cancel, create);
        footer.setExpandRatio(cancel, 1);
        footer.setComponentAlignment(cancel, Alignment.TOP_RIGHT);
        /*[ /FOOTER ]*/

        detailsWrapper.addComponent(body);
        content.addComponent(footer);

        return window;
    }
    
public class DirectoryTreeFolderWindow2 extends Window {

    private final File origenPath;
    private final VerticalLayout content;
    private VerticalLayout body;
    private VerticalLayout root;
    private Tree tree;
    private final TabSheet detailsWrapper;
    private HierarchicalContainer container;
    private Label lblFileName;
    private final File fileTo;

    private final Components comp = new Components();
    private final Notifications notification = new Notifications();

    public DirectoryTreeFolderWindow2(File file) {
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
                Notification.show("6666_" + event.getItem().getItemProperty("caption").getValue());
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
                Notification.show("789797_" + event.getProperty().getValue().toString());
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
                Path target = Paths.get(tree.getValue().toString() + "\\" + fileTo.getName());

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
                Path target = Paths.get(tree.getValue().toString() + "\\" + fileTo.getName());

                System.out.println("sourceCop = " + source);
                System.out.println("targetCop   = " + target);
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
            // SE RECARGA LA PAGINA, PARA MOSTRAR EL ARCHIVO CARGADO
            String dir = sourceDir.getParent().toString();
            cleanAndBuild(new File(dir));
            displaySubDirectoryContents(new File(dir));
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
            // SE RECARGA LA PAGINA, PARA MOSTRAR EL ARCHIVO CARGADO
            String dir = sourceDir.getParent().toString();
            cleanAndBuild(new File(dir));
            displaySubDirectoryContents(new File(dir));
            notification.createSuccess("Se copio el archivo correctamente: " + fileTo.getName());
        } catch (FileAlreadyExistsException ex) {
            notification.createFailure("Ya existe un archivo con el mismo nombre en esta carpeta");
        } catch (IOException ex) {
            notification.createFailure("Problemas al copiar el archivo");
        }
    }

}

}
