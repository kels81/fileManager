/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vaadin.demo.dashboard.control;

import com.vaadin.demo.dashboard.entity.Contactos;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Edrd
 */
public class ControlTransactions {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("file_manager");
    EntityManager em = emf.createEntityManager();

    /**
     *
     * @param contact
     */
    public void addContact(Contactos contact) {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }

        try {
            em.persist(contact);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            e.toString();
            e.getMessage();
            e.getCause();
            e.getLocalizedMessage();
        } finally {
            em.close();
            emf.close();
        }

    }
    
    public void commit(Object entidad) {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }

        try {
            em.persist(entidad);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            e.toString();
            e.getMessage();
            e.getCause();
            e.getLocalizedMessage();
        } finally {
            em.close();
            emf.close();
        }

    }

}
