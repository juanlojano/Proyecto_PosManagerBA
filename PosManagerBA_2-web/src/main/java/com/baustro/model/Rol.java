/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baustro.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ue01000632
 */
@Entity
@Table(name = "rol")
@XmlRootElement
public class Rol implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.AUTO)
    
    private Long id;
    private String gru_nombre;
    private String gru_id;

    @ManyToMany(mappedBy = "roles", cascade = {CascadeType.ALL}, targetEntity = Usuario.class)
    private List<Usuario> usuarios;

    public List<Usuario> getUsuario() {
        return usuarios;
    }

    public void setUsuario(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public String getGru_nombre() {
        return gru_nombre;
    }

    public void setGru_nombre(String gru_nombre) {
        this.gru_nombre = gru_nombre;
    }

    public String getGru_id() {
        return gru_id;
    }

    public void setGru_id(String gru_id) {
        this.gru_id = gru_id;
    }
}