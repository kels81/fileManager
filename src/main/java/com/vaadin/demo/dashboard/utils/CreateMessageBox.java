/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vaadin.demo.dashboard.utils;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.themes.ValoTheme;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import de.steinwedel.messagebox.icons.FontAwesomeDialogIconFactory;

/**
 *
 * @author Eduardo
 */
public class CreateMessageBox {

    public void createWarning(String message) {
        MessageBox.setDialogDefaultIconFactory(new FontAwesomeDialogIconFactory());
        MessageBox.createWarning()
                .withWidth("255px")
                .withCaption("Advertencia")
                .withMessage(message)
                .withCustomButton(
                        ButtonOption.focus(),
                        ButtonOption.caption("Aceptar"),
                        ButtonOption.style("mystyle"),
                        ButtonOption.style(ValoTheme.BUTTON_SMALL))
                .withButtonAlignment(Alignment.MIDDLE_CENTER)
                .withButtonAlignment(Alignment.MIDDLE_CENTER)
                .open();
    }

    public void createError(String message) {
        MessageBox.setDialogDefaultIconFactory(new FontAwesomeDialogIconFactory());
        MessageBox.createError()
                .withWidth("255px")
                .withCaption("Error")
                .withMessage("Acceso Denegado")
                .withCustomButton(
                        ButtonOption.focus(),
                        ButtonOption.caption("Aceptar"),
                        ButtonOption.style("mystyle"),
                        ButtonOption.style(ValoTheme.BUTTON_SMALL))
                .withButtonAlignment(Alignment.MIDDLE_CENTER)
                .open();
    }

    public void createInformation(String message) {
        MessageBox.setDialogDefaultIconFactory(new FontAwesomeDialogIconFactory());
        MessageBox.createError()
                .withWidth("255px")
                .withCaption("Error")
                .withMessage("Acceso Denegado")
                .withCustomButton(
                        ButtonOption.focus(),
                        ButtonOption.caption("Aceptar"),
                        ButtonOption.style("mystyle"),
                        ButtonOption.style(ValoTheme.BUTTON_SMALL))
                .withButtonAlignment(Alignment.MIDDLE_CENTER)
                .open();
    }

    public void createQuestion(String message) {
        MessageBox.setDialogDefaultIconFactory(new FontAwesomeDialogIconFactory());
        MessageBox.createQuestion()
                .withWidth("255px")
                .withCaption("Confirmar")
                .withMessage(message)
                .withCustomButton(
                        ButtonOption.focus(),
                        ButtonOption.caption("Si"),
                        ButtonOption.style("mystyle"),
                        ButtonOption.style(ValoTheme.BUTTON_SMALL))
                .withButtonAlignment(Alignment.MIDDLE_CENTER)
                .withCustomButton(
                        ButtonOption.focus(),
                        ButtonOption.caption("No"),
                        ButtonOption.style("mystyle"),
                        ButtonOption.style(ValoTheme.BUTTON_SMALL))
                .withButtonAlignment(Alignment.MIDDLE_CENTER)
                .withButtonAlignment(Alignment.MIDDLE_CENTER)
                .open();
    }

}
