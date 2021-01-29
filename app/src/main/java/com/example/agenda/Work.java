package com.example.agenda;

public class Work {
    private String nombre;
    private String fecha;
    private String FM;
    private String desc;
    private String hora;
    private String HM;
    private String activo;
    private int id_work;

    public Work() {
    }

    public Work(int id_work, String nombre, String fecha,String FM, String desc, String hora, String HM, String activo) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.FM = FM;
        this.desc = desc;
        this.hora = hora;
        this.HM = HM;
        this.activo = activo;
        this.id_work = id_work;
    }

    public String getFM() {
        return FM;
    }

    public void setFM(String FM) {
        this.FM = FM;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getHM() {
        return HM;
    }

    public void setHM(String HM) {
        this.HM = HM;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

    public int getId_work() {
        return id_work;
    }

    public void setId_work(int id_work) {
        this.id_work = id_work;
    }
}