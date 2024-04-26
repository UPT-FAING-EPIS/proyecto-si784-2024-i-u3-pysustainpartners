package com.loscuchurrumines.model;

import java.io.Serializable;

public class Proyecto implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idProyecto;
    private String nombre;
    private String descripcion;
    private String objetivo;
    private String foto;
    private boolean estado;
    private int fkRegion;
    private int fkUser;
    private int fkFondo;

    public Proyecto() {}

    public Proyecto(
        int idProyecto,
        String nombre,
        String descripcion,
        String objetivo,
        int fkRegion,
        int fkUser,
        int fkFondo
    ) {
        this.idProyecto = idProyecto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.objetivo = objetivo;

        this.fkRegion = fkRegion;
        this.fkUser = fkUser;
        this.fkFondo = fkFondo;
    }

    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public int getFkRegion() {
        return fkRegion;
    }

    public void setFkRegion(int fkRegion) {
        this.fkRegion = fkRegion;
    }

    public int getFkUser() {
        return fkUser;
    }

    public void setFkUser(int fkUser) {
        this.fkUser = fkUser;
    }

    public int getFkFondo() {
        return fkFondo;
    }

    public void setFkFondo(int fkFondo) {
        this.fkFondo = fkFondo;
    }
}
