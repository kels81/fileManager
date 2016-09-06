/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vaadin.demo.dashboard.data;

import com.vaadin.demo.dashboard.domain.Usuarios;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Eduardo
 */
public class Consultas {
    
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("file_manager");
    EntityManager em = emf.createEntityManager();
    
    public ArrayList<Usuarios> getUsuario(String usuario, String password){
        Query qry = em.createNativeQuery("Select u.u_Id_Usuario "
                + "From Usuarios u Where u.u_Usuario Collate utf8_bin = '"+usuario+"' And u.u_Password  Collate utf8_bin = '"+password+"' "
                + "And u.u_Habilitado = 1");
        
        ArrayList<Usuarios> usuarios = new ArrayList<Usuarios>(qry.getResultList());
        
        return usuarios;
    }
    
}
