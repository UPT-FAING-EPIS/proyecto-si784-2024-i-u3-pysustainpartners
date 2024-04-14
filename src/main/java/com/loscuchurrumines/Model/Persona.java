package com.loscuchurrumines.Model;

public class Persona {
    private int idPersona;
    private String nombre;
    private String apellido;
    private String celular;
    private String fechaNacimiento;
    private String fotoPersona;
    private String sexo;
    private int fkUser;
    public Persona(){

    }
    public Persona(int idPersona, String nombre, String apellido, String celular, String fechaNacimiento, String fotoPersona, String sexo, int fkUser) {
        this.idPersona = idPersona;
        this.nombre = nombre;
        this.apellido = apellido;
        this.celular = celular;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.fkUser = fkUser;
        this.fotoPersona = fotoPersona;
    }

    public int getIdPersona() {
        return idPersona;
    }
    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFotoPersona() {
        return fotoPersona;
    }
    public void setFotoPersona(String fotoPersona) {
        this.fotoPersona = fotoPersona;
    }

    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCelular() {
        return celular;
    }
    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }
    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getFkUser() {
        return fkUser;
    }
    public void setFkUser(int fkUser) {
        this.fkUser = fkUser;
    }
}
