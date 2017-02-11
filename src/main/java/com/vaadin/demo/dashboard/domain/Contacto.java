/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vaadin.demo.dashboard.domain;

import java.io.Serializable;

/**
 *
 * @author Edrd
 */
public class Contacto implements Serializable {
    
    private Integer id;
    private String nombre;
    private String email;
    
    public Contacto(String name, String email) {
            this.nombre = name;
            this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
    
}
