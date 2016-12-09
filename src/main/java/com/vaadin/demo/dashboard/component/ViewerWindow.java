/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vaadin.demo.dashboard.component;

import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.Responsive;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 *
 * @author Edrd
 */
public class ViewerWindow extends Window {

    public ViewerWindow(File file) {
        //((VerticalLayout) this.getContent()).setSizeFull();

        setModal(true);
        setResizable(false);
        setClosable(true);
        center();

        setHeight(90.0f, Unit.PERCENTAGE);
        setWidth(90.0f, Unit.PERCENTAGE);

        StreamSource s = new StreamResource.StreamSource() {

            @Override
            public InputStream getStream() {
                try {
                    //File f = new File("C:/themes/repy.pdf");
                    FileInputStream fis = new FileInputStream(file);
                    return fis;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };

        StreamResource r = new StreamResource(s, file.getName());
//        r.setMIMEType("application/pdf");
//        r.getStream().setParameter(
//                "Content-Disposition",
//                "attachment; filename="+file.getName());
        
        BrowserWindowOpener opener =
                new BrowserWindowOpener(r);
        //opener.extend(print);

        
        Embedded e = new Embedded();
        e.setMimeType("application/pdf");
        e.setSizeFull();
        e.setType(Embedded.TYPE_BROWSER);
        e.setSource(r);
        


        setContent(e);

    }

}
