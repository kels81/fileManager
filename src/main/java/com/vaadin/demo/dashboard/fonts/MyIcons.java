/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vaadin.demo.dashboard.fonts;

import com.vaadin.server.FontIcon;
import com.vaadin.server.GenericFontIcon;

/**
 *
 * @author Eduardo
 */
public enum MyIcons implements FontIcon {

    ADD_FOLDER(0XF100),
    ADD_FOLDER_BLACK(0XF101),
    CROSS(0XF102),
    DOWNLOAD(0XF103),
    DOWNLOAD_CLOUD(0XF104),
    TICK(0XF105),
    UPLOAD(0XF106),
    UPLOAD_CLOUD(0XF107),    
    ;
    
    private final int codepoint;
    public static final String FONT_FAMILY = "MyIcons";

    MyIcons(int codepoint) {
        this.codepoint = codepoint;
    }

    @Override
    public String getFontFamily() {
        return FONT_FAMILY;
    }

    @Override
    public int getCodepoint() {
        return codepoint;
    }

    @Override
    public String getHtml() {
        return GenericFontIcon.getHtml(MyIcons.FONT_FAMILY, codepoint);
    }

    @Override
    public String getMIMEType() {
        throw new UnsupportedOperationException(
                FontIcon.class.getSimpleName()
                + " should not be used where a MIME type is needed.");
    }
}
