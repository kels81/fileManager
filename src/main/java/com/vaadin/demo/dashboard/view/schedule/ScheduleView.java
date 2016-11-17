package com.vaadin.demo.dashboard.view.schedule;

import com.vaadin.demo.dashboard.utils.Components;
import com.vaadin.demo.dashboard.utils.Notifications;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.shared.ui.MarginInfo;
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
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import pl.exsio.plupload.Plupload;
import pl.exsio.plupload.PluploadError;
import pl.exsio.plupload.PluploadFile;

@SuppressWarnings("serial")
public final class ScheduleView extends CssLayout implements View {

    private ThemeResource iconResource;
    private final String path;
    private final VerticalLayout tabs;
    private HorizontalLayout rootPath;
    private Button btnFolder;
    private Label lblArrow;
    private final Components component = new Components();
    private Component directoryContent;
    private HorizontalLayout toolBar;
    private Window window;
    private Button create;

    public ScheduleView() {
        this.path = "C:\\Users\\Edrd\\Documents\\GitHub\\fileManager\\Archivos";
        //this.path = VaadinService.getCurrent().getBaseDirectory() + "/VAADIN/themes/UPLOADS/";
        setSizeFull();
        addStyleName("schedule");

        tabs = new VerticalLayout();
        tabs.setSizeFull();
        tabs.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);

        tabs.addComponent(buildToolBar());
        tabs.addComponent(buildPath());
        directoryContent = displayDirectoryContents(path);
        tabs.addComponent(directoryContent);
        tabs.setExpandRatio(directoryContent, 1);

        addComponent(tabs);
    }

    private Component buildToolBar() {
        toolBar = new HorizontalLayout();
        //toolBar.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
        toolBar.setSpacing(true);
        //toolBar.setMargin(new MarginInfo(true, false, false, true));
        toolBar.addStyleName("toolbarFile");

        MenuBar menubar = component.createMenuBar();
        MenuItem menu = menubar.addItem("Nuevo", null);
        menu.setIcon(FontAwesome.PLUS);
        menu.addItem("Carpeta", FontAwesome.FOLDER_O, (MenuItem selectedItem) -> {
            Window w = createWindow();
            UI.getCurrent().addWindow(w);
            w.focus();
        });

        Plupload uploader = new Plupload("Subir", FontAwesome.UPLOAD);
        uploader.addStyleName(ValoTheme.BUTTON_PRIMARY);
        uploader.addStyleName(ValoTheme.BUTTON_SMALL);
        Label info = new Label();

        uploader.setMaxFileSize("5mb");

//show notification after file is uploaded
        uploader.addFileUploadedListener(new Plupload.FileUploadedListener() {
            @Override
            public void onFileUploaded(PluploadFile file) {
                Notification.show("I've just uploaded file: " + file.getName());
            }
        });

//update upload progress
        uploader.addUploadProgressListener(new Plupload.UploadProgressListener() {
            @Override
            public void onUploadProgress(PluploadFile file) {
                info.setValue("I'm uploading " + file.getName()
                        + "and I'm at " + file.getPercent() + "%");
            }
        });

//autostart the uploader after addind files
        uploader.addFilesAddedListener(new Plupload.FilesAddedListener() {
            @Override
            public void onFilesAdded(PluploadFile[] files) {
                uploader.start();
            }
        });

//notify, when the upload process is completed
        uploader.addUploadCompleteListener(new Plupload.UploadCompleteListener() {
            @Override
            public void onUploadComplete() {
                info.setValue("upload is completed!");
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
        
        Button email = component.createButton("Email");
        email.setIcon(FontAwesome.ENVELOPE_O);
        email.addClickListener((ClickEvent event) -> {
            Window w = createWindow();
            UI.getCurrent().addWindow(w);
            w.focus();
        });

        toolBar.addComponent(menubar);
        toolBar.addComponent(uploader);
        toolBar.addComponent(email);

        return toolBar;
    }

    private Component buildPath() {
        rootPath = new HorizontalLayout();
        //rootPath.setMargin(new MarginInfo(true, false, false, true));
        rootPath.addStyleName("toolbarFile");
        //rootPath.setWidth(100.0f, Unit.PERCENTAGE);

        Button button = component.createButton("Archivos");
        button.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                cleanAndBuild(path);
            }
        });

        rootPath.addComponent(button);

        return rootPath;
    }

    private Component displayDirectoryContents(String path) {

        CssLayout catalog = new CssLayout();
        catalog.addStyleName("catalog");

        //catalog.addComponent(buildPath());
        File currentDir = new File(path);
        File[] files = currentDir.listFiles();

        for (final File file : files) {
            
            CssLayout mainPanel = new CssLayout();
            mainPanel.addStyleName("mainPanel");
            
            VerticalLayout frame = new VerticalLayout();
            frame.addStyleName("frame");
            frame.setMargin(true);
            frame.addStyleName(ValoTheme.LAYOUT_CARD);
            frame.setWidth(200.0f, Unit.PIXELS);

            HorizontalLayout fileLayout = new HorizontalLayout();
            //fileLayout.setSpacing(true);
            fileLayout.setDescription(file.getName());
            frame.addComponent(fileLayout);

            Image fileType = new Image(null, iconExtension(file));
            fileType.setWidth(55.0f, Unit.PIXELS);
            fileType.setHeight(50.0f, Unit.PIXELS);
            fileLayout.addComponent(fileType);

            VerticalLayout nameDetailsFile = new VerticalLayout();
            fileLayout.addComponent(nameDetailsFile);
            fileLayout.setComponentAlignment(nameDetailsFile, Alignment.BOTTOM_LEFT);

            String name = file.getName();
            name = (name.length() > 17 ? name.substring(0, 14) + "..." : name);
            Label nameFile = new Label(name);
            nameFile.addStyleName(ValoTheme.LABEL_SMALL);
            nameFile.addStyleName(ValoTheme.LABEL_BOLD);
            nameFile.setWidth(100.0f, Unit.PIXELS);
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
                        downloadContents(file);
                    }
                }
            });
            mainPanel.addComponent(frame);
            catalog.addComponent(mainPanel);

        }
        //File currentDir2 = new File("D:/vaadin/fileManager/files/"); // current directory
        //displayDirectoryContents(currentDir2);

        return catalog;
    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }

    public static void displayDirectoryContents23(File dir) {
//        try {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                //System.out.println("directory:");
                //System.out.println("directory:" + file.getCanonicalPath());
                System.out.println("DIR: " + file.getName());
                System.out.println("No. Items DIR = " + file.list().length);
                //displayDirectoryContents(file);   //para conocer los archivos de las subcarpetas
                //System.out.println("DIR_PATH: " + fileEntry.getAbsolutePath());
            } else {
                //System.out.println("     file:");
                //System.out.println("     file:" + file.getCanonicalPath());
                System.out.println("FILE: " + file.getName());
                long fileSize = file.length();
                String fileSizeDisplay = FileUtils.byteCountToDisplaySize(fileSize);
                System.out.println("Size Display: " + fileSizeDisplay);
            }
            //System.out.println(file.getCanonicalPath());

        }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
        cleanAndBuild(file.getAbsolutePath());

        String root = "Archivos";
        String directory = nameDir(file, path);
        String[] arrayDirectories = directory.split("\\\\");

        for (String lblDirectory : arrayDirectories) {
            lblArrow = new Label(FontAwesome.ANGLE_RIGHT.getHtml(), ContentMode.HTML);
            lblArrow.addStyleName(ValoTheme.LABEL_COLORED);
            btnFolder = component.createButton(lblDirectory);
            btnFolder.addClickListener(e -> {
                String newRoot = e.getComponent().getCaption();
                String directorys = file.getAbsolutePath();
                int inicio = directorys.indexOf(newRoot);
                String dir = directorys.substring(0, inicio + newRoot.length());
                cleanAndBuild(dir);

                File newPath = new File(dir);
                displaySubDirectoryContents(newPath);

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
        String substring = directory.substring(inicio + root.length() + 1);

        return substring;
    }

    private void cleanAndBuild(String directory) {
        tabs.removeAllComponents();
        tabs.addComponent(buildToolBar());
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

        create = new Button("Crear");
        create.addStyleName(ValoTheme.BUTTON_PRIMARY);
        create.setEnabled(false);
        create.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                File directory = new File(path + "/" + txtNameFolder.getValue());
                System.out.println("directory = " + directory);
                directory.mkdir();

                //notification.createSuccess();
                Notifications notification = new Notifications();
                notification.createFailure("Problemas con la creación");
                window.close();
            }
        });
        footer.addComponent(create);
        footer.setComponentAlignment(create, Alignment.TOP_RIGHT);
        /*[ /FOOTER ]*/

        detailsWrapper.addComponent(body);
        content.addComponent(footer);

        return window;
    }

}
