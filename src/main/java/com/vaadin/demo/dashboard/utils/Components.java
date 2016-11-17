/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vaadin.demo.dashboard.utils;

import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * @author Eduardo
 */
public class Components {
    
    public CheckBox createCheckBox(String caption) {
        CheckBox cb = new CheckBox(caption);
        cb.setImmediate(true);
        return cb;
    }

    public TextField createTextField(String caption) {
        TextField f = new TextField(caption);
        f.setNullRepresentation("");
        //f.addFocusListener(focusListener);
        //f.addBlurListener(blurListener);
        return f;
    }
    
     public Button createButton(String caption) {
        Button btn = new Button(caption);
        btn.addStyleName(ValoTheme.BUTTON_SMALL);
        btn.addStyleName(ValoTheme.BUTTON_PRIMARY);
        //btnFolder.setEnabled(false);
        return btn;
    }
     
     public MenuBar createMenuBar() {
        MenuBar menu = new MenuBar();
        menu.addStyleName(ValoTheme.MENUBAR_SMALL);
        menu.addStyleName("primary");
        return menu;
    }
}
