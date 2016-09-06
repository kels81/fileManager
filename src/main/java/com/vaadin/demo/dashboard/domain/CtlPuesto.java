/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vaadin.demo.dashboard.domain;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Eduardo
 */
@Entity
@Table(name = "ctl_puesto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CtlPuesto.findAll", query = "SELECT c FROM CtlPuesto c"),
    @NamedQuery(name = "CtlPuesto.findByCpIdPuesto", query = "SELECT c FROM CtlPuesto c WHERE c.cpIdPuesto = :cpIdPuesto"),
    @NamedQuery(name = "CtlPuesto.findByCpNombrePuesto", query = "SELECT c FROM CtlPuesto c WHERE c.cpNombrePuesto = :cpNombrePuesto"),
    @NamedQuery(name = "CtlPuesto.findByCpHabilitado", query = "SELECT c FROM CtlPuesto c WHERE c.cpHabilitado = :cpHabilitado")})
public class CtlPuesto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cp_id_puesto")
    private Integer cpIdPuesto;
    @Size(max = 30)
    @Column(name = "cp_nombre_puesto")
    private String cpNombrePuesto;
    @Column(name = "cp_habilitado")
    private Integer cpHabilitado;
    @OneToMany(mappedBy = "uIdPuesto")
    private Collection<Usuarios> usuariosCollection;

    public CtlPuesto() {
    }

    public CtlPuesto(Integer cpIdPuesto) {
        this.cpIdPuesto = cpIdPuesto;
    }

    public Integer getCpIdPuesto() {
        return cpIdPuesto;
    }

    public void setCpIdPuesto(Integer cpIdPuesto) {
        this.cpIdPuesto = cpIdPuesto;
    }

    public String getCpNombrePuesto() {
        return cpNombrePuesto;
    }

    public void setCpNombrePuesto(String cpNombrePuesto) {
        this.cpNombrePuesto = cpNombrePuesto;
    }

    public Integer getCpHabilitado() {
        return cpHabilitado;
    }

    public void setCpHabilitado(Integer cpHabilitado) {
        this.cpHabilitado = cpHabilitado;
    }

    @XmlTransient
    public Collection<Usuarios> getUsuariosCollection() {
        return usuariosCollection;
    }

    public void setUsuariosCollection(Collection<Usuarios> usuariosCollection) {
        this.usuariosCollection = usuariosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cpIdPuesto != null ? cpIdPuesto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CtlPuesto)) {
            return false;
        }
        CtlPuesto other = (CtlPuesto) object;
        if ((this.cpIdPuesto == null && other.cpIdPuesto != null) || (this.cpIdPuesto != null && !this.cpIdPuesto.equals(other.cpIdPuesto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vaadin.demo.dashboard.CtlPuesto[ cpIdPuesto=" + cpIdPuesto + " ]";
    }
    
}
