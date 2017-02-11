/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vaadin.demo.dashboard.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Edrd
 */
@Entity
@Table(name = "contactos")
@XmlRootElement
@NamedQueries({@NamedQuery(name = "Contactos.findAll", query = "SELECT c FROM Contactos c")})
public class Contactos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = true)
    @Column(name = "c_id_contacto")
    private Integer cIdContacto;
    @Size(max = 255)
    @Column(name = "c_nombre")
    private String cNombre;
    @Size(max = 255)
    @Column(name = "c_email")
    private String cEmail;
    
/**
 * PARA QUE FUNCIONE EL BINDING FIELD GROUP SE REQUIERE HACER CAMBIOS EN LA ENTIDAD:
 * @Basic(optional = false)
 * LOS SETTERS Y GETTERS LA PRIMER LETRA HAY QUE CONVERTIRLA EN MINUSCULAS
 * getCIdContacto >> getcIdContacto
 * 
 * CON ESTOS CAMBIOS YA FUNCIONA TODO BIEN
 */
    

    public Contactos() {

    }

    public Contactos(String name, String email) {
        this.cNombre = name;
        this.cEmail = email;
    }

    public Contactos(Integer cIdContacto) {
        this.cIdContacto = cIdContacto;
    }
    public Integer getcIdContacto() {
        return cIdContacto;
    }

    public void setcIdContacto(Integer cIdContacto) {
        this.cIdContacto = cIdContacto;
    }
    public String getcNombre() {
        return cNombre;
    }

    public void setcNombre(String cNombre) {
        this.cNombre = cNombre;
    }

    public String getcEmail() {
        return cEmail;
    }

    public void setcEmail(String cEmail) {
        this.cEmail = cEmail;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cIdContacto != null ? cIdContacto.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Contactos)) {
            return false;
        }
        Contactos other = (Contactos) object;
        if ((this.cIdContacto == null && other.cIdContacto != null) || (this.cIdContacto != null && !this.cIdContacto.equals(other.cIdContacto))) {
            return false;
        }
        return true;
    }
    
//    @Override
//    public String toString() {
//        return "com.vaadin.demo.dashboard.Contactos[ cIdContacto=" + cIdContacto + " ]";
//    }
}
