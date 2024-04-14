package com.loscuchurrumines.Model;

public class Participante {
    private int idParticipante;
    private int fkUser;
    private int fkRol;
    private int fkProyecto;

    public Participante(){

    }
    public Participante(int idParticipante, int fkUser, int fkRol, int fkProyecto) {
        this.idParticipante = idParticipante;
        this.fkUser = fkUser;
        this.fkRol = fkRol;
        this.fkProyecto = fkProyecto;
    }

    public int getIdParticipante() {
        return idParticipante;
    }
    public void setIdParticipante(int idParticipante) {
        this.idParticipante = idParticipante;
    }

    public int getFkUser() {
        return fkUser;
    }
    public void setFkUser(int fkUser) {
        this.fkUser = fkUser;
    }

    public int getFkRol() {
        return fkRol;
    }
    public void setFkRol(int fkRol) {
        this.fkRol = fkRol;
    }

    public int getFkProyecto() {
        return fkProyecto;
    }
    public void setFkProyecto(int fkProyecto) {
        this.fkProyecto = fkProyecto;
    }
}
