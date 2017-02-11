/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vaadin.demo.dashboard.control;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.demo.dashboard.entity.Contactos;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Edrd
 */
public class Querys {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("file_manager");
    EntityManager em = emf.createEntityManager();

    public Container containerContactos() {
        BeanItemContainer<Contactos> container = new BeanItemContainer<>(Contactos.class);

        ArrayList<Contactos> listUsuarios = this.getContactos();

        for (Contactos contact : listUsuarios) {
            container.addBean(contact);
        }

        return container;
    }

    public ArrayList<Contactos> getContactos() {
        Query qry = em.createQuery("SELECT c FROM Contactos c");

        ArrayList<Contactos> contactos = new ArrayList<Contactos>(qry.getResultList());

        return contactos;
    }

    public Contactos existContacto(String email) {
        try {
            Query qry = em.createQuery("SELECT c FROM Contactos c WHERE c.cEmail = '" + email + "'");
            Contactos entidad = (Contactos) (qry.getSingleResult());
            return entidad;
        } catch (Exception e) {
            return null;
        }

    }

}
