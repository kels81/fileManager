/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vaadin.demo.dashboard.component;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

/**
 *
 * @author Edrd
 */
public class ContactoForm extends FormLayout {

    @PropertyId("cNombre")
    private final TextField nameTxt = new TextField("Nombre");
    @PropertyId("cEmail")
    private final TextField emailTxt = new TextField("Email");

    public ContactoForm() {
        addComponents(nameTxt, emailTxt);
    }

}
