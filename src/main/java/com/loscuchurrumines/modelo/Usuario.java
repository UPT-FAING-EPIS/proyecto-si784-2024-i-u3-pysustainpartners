package com.loscuchurrumines.model;

import java.io.Serializable;

public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idUser;
    private String username;
    private String password;
    private String email;
    private boolean estado;
    private int fkCargo;

    public Usuario() {}

    public Usuario(
        int idUser,
        String username,
        String password,
        String email,
        boolean estado,
        int fkCargo
    ) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
        this.email = email;
        this.estado = estado;
        this.fkCargo = fkCargo;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getUser() {
        return username;
    }

    public void setUser(String username) {
        this.username = username;
    }

    public String passwod() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public int getFkCargo() {
        return fkCargo;
    }

    public void setFkCargo(int fkCargo) {
        this.fkCargo = fkCargo;
    }
}
