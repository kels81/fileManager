/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vaadin.demo.dashboard.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Eduardo
 */
@Entity
@Table(name = "usuarios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuarios.findAll", query = "SELECT u FROM Usuarios u")})
public class Usuarios implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "u_id_usuario")
    private Integer uIdUsuario;
    @Size(max = 30)
    @Column(name = "u_nombre")
    private String uNombre;
    @Size(max = 30)
    @Column(name = "u_apellido_paterno")
    private String uApellidoPaterno;
    @Size(max = 30)
    @Column(name = "u_apellido_materno")
    private String uApellidoMaterno;
    @Size(max = 10)
    @Column(name = "u_sexo")
    private String uSexo;
    @Size(max = 30)
    @Column(name = "u_role")
    private String uRole;
    @Size(max = 30)
    @Column(name = "u_usuario")
    private String uUsuario;
    @Size(max = 30)
    @Column(name = "u_password")
    private String uPassword;
    @Column(name = "u_habilitado")
    private Integer uHabilitado;
    @JoinColumn(name = "u_id_puesto", referencedColumnName = "cp_id_puesto")
    @ManyToOne
    private CtlPuesto uIdPuesto;

    public Usuarios() {
    }

    public Usuarios(Integer uIdUsuario) {
        this.uIdUsuario = uIdUsuario;
    }

    public Integer getUIdUsuario() {
        return uIdUsuario;
    }

    public void setUIdUsuario(Integer uIdUsuario) {
        this.uIdUsuario = uIdUsuario;
    }

    public String getuNombre() {
        return uNombre;
    }

    public void setuNombre(String uNombre) {
        this.uNombre = uNombre;
    }

    public String getuApellidoPaterno() {
        return uApellidoPaterno;
    }

    public void setuApellidoPaterno(String uApellidoPaterno) {
        this.uApellidoPaterno = uApellidoPaterno;
    }

    public String getUApellidoMaterno() {
        return uApellidoMaterno;
    }

    public void setUApellidoMaterno(String uApellidoMaterno) {
        this.uApellidoMaterno = uApellidoMaterno;
    }

    public String getuSexo() {
        return uSexo;
    }

    public void setuSexo(String uSexo) {
        this.uSexo = uSexo;
    }

    public String getUUsuario() {
        return uUsuario;
    }

    public String getuRole() {
        return uRole;
    }

    public void setuRole(String uRole) {
        this.uRole = uRole;
    }

    public void setUUsuario(String uUsuario) {
        this.uUsuario = uUsuario;
    }

    public String getUPassword() {
        return uPassword;
    }

    public void setUPassword(String uPassword) {
        this.uPassword = uPassword;
    }

    public Integer getUHabilitado() {
        return uHabilitado;
    }

    public void setUHabilitado(Integer uHabilitado) {
        this.uHabilitado = uHabilitado;
    }

    public CtlPuesto getUIdPuesto() {
        return uIdPuesto;
    }

    public void setUIdPuesto(CtlPuesto uIdPuesto) {
        this.uIdPuesto = uIdPuesto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uIdUsuario != null ? uIdUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuarios)) {
            return false;
        }
        Usuarios other = (Usuarios) object;
        if ((this.uIdUsuario == null && other.uIdUsuario != null) || (this.uIdUsuario != null && !this.uIdUsuario.equals(other.uIdUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vaadin.demo.dashboard.Usuarios[ uIdUsuario=" + uIdUsuario + " ]";
    }

    
    
}
