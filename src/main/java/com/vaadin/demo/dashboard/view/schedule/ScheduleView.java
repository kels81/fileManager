package com.vaadin.demo.dashboard.view.schedule;

import java.util.List;

import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

@SuppressWarnings("serial")
public final class ScheduleView extends CssLayout implements View {

    private ThemeResource iconResource;
    private String path;
    private TabSheet tabs;
    private HorizontalLayout rootPath;
    private Button btnFolder;
    private Label lblArrow;

    public ScheduleView() {
        this.path = "D:/vaadin/fileManager/files/";
        //this.path = VaadinService.getCurrent().getBaseDirectory() + "/VAADIN/themes/UPLOADS/";
        setSizeFull();
        addStyleName("schedule");
        DashboardEventBus.register(this);

        tabs = new TabSheet();
        tabs.setSizeFull();
        tabs.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);

        //tabs.addComponent(buildCalendarView());
        //tabs.addComponent(buildPath());
        tabs.addComponent(displayDirectoryContents(path));

        addComponent(tabs);
    }

    private Component buildPath() {
        rootPath = new HorizontalLayout();
        //rootPath.setSizeFull();
        rootPath.setWidth(100.0f, Unit.PERCENTAGE);

        Button button = createButton("Archivos");
        button.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                tabs.removeAllComponents();
                tabs.addComponent(displayDirectoryContents(path));
            }
        });

        rootPath.addComponent(button);

        return rootPath;
    }

    private Component displayDirectoryContents(String path) {

        CssLayout catalog = new CssLayout();
        catalog.setCaption("Catalog");
        catalog.addStyleName("catalog");

        catalog.addComponent(buildPath());

        File currentDir = new File(path);
        File[] files = currentDir.listFiles();

        for (final File file : files) {
            VerticalLayout frame = new VerticalLayout();
            frame.addStyleName("frame");
            frame.setMargin(true);
            frame.addStyleName(ValoTheme.LAYOUT_CARD);
            frame.setWidth(270.0f, Unit.PIXELS);

            HorizontalLayout fileLayout = new HorizontalLayout();
            fileLayout.setSpacing(true);
            fileLayout.setDescription(file.getName());
            frame.addComponent(fileLayout);

            Image fileType = new Image(null, iconExtension(file));
            fileType.setWidth(65.0f, Unit.PIXELS);
            fileType.setHeight(57.0f, Unit.PIXELS);
            fileLayout.addComponent(fileType);

            VerticalLayout nameDetailsFile = new VerticalLayout();
            fileLayout.addComponent(nameDetailsFile);
            fileLayout.setComponentAlignment(nameDetailsFile, Alignment.BOTTOM_RIGHT);

            String name = file.getName();
            name = (name.length() > 17 ? name.substring(0, 14) + "..." : name);
            Label nameFile = new Label(name);
            nameFile.addStyleName(ValoTheme.LABEL_SMALL);
            nameFile.addStyleName(ValoTheme.LABEL_BOLD);
            nameFile.setWidth(120.0f, Unit.PIXELS);
            nameDetailsFile.addComponent(nameFile);

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

            frame.addLayoutClickListener((LayoutClickEvent event) -> {
                if (event.getButton() == MouseButton.LEFT) {
                    if (file.isDirectory()) {
                        displaySubDirectoryContents(file);
                    } else if (file.isFile()) {
                        downloadContents(file);
                    }
                }
            });
            catalog.addComponent(frame);

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

        tabs.removeAllComponents();
        tabs.addComponent(displayDirectoryContents(file.getAbsolutePath()));

        String root = "files";
        String directory = nameDir(file, path);
        String[] arrayDirectories = directory.split("\\\\");

//        List<Component> components = IteratorUtils.toList(rootPath.iterator());
//        
//        for (Component comp : components) {
//            Button btnDir = (Button) comp;
//            System.out.println("btnDir = " + btnDir.getCaption());
//        }

        for (String lblDirectory : arrayDirectories) {
            btnFolder = createButton(lblDirectory);
            btnFolder.setIcon(FontAwesome.ANGLE_RIGHT);
            btnFolder.addClickListener(e -> {

                String newRoot = e.getComponent().getCaption();
                String directorys = file.getAbsolutePath();
                int inicio = directorys.indexOf(newRoot);
                String dir = directorys.substring(0, inicio + newRoot.length());

                tabs.removeAllComponents();
                tabs.addComponent(displayDirectoryContents(dir));

                File newPath = new File(dir);
                displaySubDirectoryContents(newPath);

            });

            rootPath.addComponent(btnFolder);
        }

        rootPath.setExpandRatio(btnFolder, 1.0f);
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

    public Button createButton(String caption) {
        Button btn = new Button(caption);
        btn.addStyleName(ValoTheme.BUTTON_SMALL);
        btn.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        //btnFolder.setEnabled(false);
        return btn;
    }

    private String nameDir(File file, String nameDir) {
        String root = nameDir;
        String directory = file.getAbsolutePath();
        int inicio = directory.indexOf(root);
        String substring = directory.substring(inicio + root.length() + 1);

        return substring;
    }

}
