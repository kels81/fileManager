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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Eduardo
 */
@Entity
@Table(name = "ctl_habilitado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CtlHabilitado.findAll", query = "SELECT c FROM CtlHabilitado c"),
    @NamedQuery(name = "CtlHabilitado.findByChIdHabilitado", query = "SELECT c FROM CtlHabilitado c WHERE c.chIdHabilitado = :chIdHabilitado"),
    @NamedQuery(name = "CtlHabilitado.findByChNombreHabilitado", query = "SELECT c FROM CtlHabilitado c WHERE c.chNombreHabilitado = :chNombreHabilitado")})
public class CtlHabilitado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ch_id_habilitado")
    private Integer chIdHabilitado;
    @Size(max = 20)
    @Column(name = "ch_nombre_habilitado")
    private String chNombreHabilitado;

    public CtlHabilitado() {
    }

    public CtlHabilitado(Integer chIdHabilitado) {
        this.chIdHabilitado = chIdHabilitado;
    }

    public Integer getChIdHabilitado() {
        return chIdHabilitado;
    }

    public void setChIdHabilitado(Integer chIdHabilitado) {
        this.chIdHabilitado = chIdHabilitado;
    }

    public String getChNombreHabilitado() {
        return chNombreHabilitado;
    }

    public void setChNombreHabilitado(String chNombreHabilitado) {
        this.chNombreHabilitado = chNombreHabilitado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (chIdHabilitado != null ? chIdHabilitado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CtlHabilitado)) {
            return false;
        }
        CtlHabilitado other = (CtlHabilitado) object;
        if ((this.chIdHabilitado == null && other.chIdHabilitado != null) || (this.chIdHabilitado != null && !this.chIdHabilitado.equals(other.chIdHabilitado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vaadin.demo.dashboard.CtlHabilitado[ chIdHabilitado=" + chIdHabilitado + " ]";
    }
    
}
