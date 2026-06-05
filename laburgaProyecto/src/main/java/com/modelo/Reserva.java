package com.modelo;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class Reserva {

    // identificador unico de la reserva
    private int idReserva;

    // llave foranea hacia la tabla mesas
    private int idMesa;

    // llave foranea hacia la tabla clientes
    private int idCliente;

    // fecha para la cual se solicita la reserva
    private Date fehcaReserva;

    // hora programada para la reserva
    private Time horaReserva;

    // cantidad de personas que asistirán
    private int personasReserva;

    // observaciones adicionales del cliente
    private String observacionesReserva;

    // estado actual de la reserva
    // reservada o finalizada
    private String estadoReserva;

    // fecha y hora en que se registró la reserva
    private Timestamp fechaRegistroReserva;

    
    //campos adicionales que nos ayudaran cuando hagamos los join, nos sirven para mostrar la informacion en la interfaz
    private String nombreCliente;

    private int numeroMesa;

    // CONSTRUCTOR VACIO
    public Reserva() {
    }

    // GETTERS Y SETTERS
    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public int getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(int idMesa) {
        this.idMesa = idMesa;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public Date getFechaReserva() {
        return fehcaReserva;
    }

    public void setFechaReserva(Date fehcaReserva) {
        this.fehcaReserva = fehcaReserva;
    }

    public Time getHoraReserva() {
        return horaReserva;
    }

    public void setHoraReserva(Time horaReserva) {
        this.horaReserva = horaReserva;
    }

    public int getPersonasReserva() {
        return personasReserva;
    }

    public void setPersonasReserva(int personasReserva) {
        this.personasReserva = personasReserva;
    }

    public String getObservacionesReserva() {
        return observacionesReserva;
    }

    public void setObservacionesReserva(String observacionesReserva) {
        this.observacionesReserva = observacionesReserva;
    }

    public String getEstadoReserva() {
        return estadoReserva;
    }

    public void setEstadoReserva(String estadoReserva) {
        this.estadoReserva = estadoReserva;
    }

    public Timestamp getFechaRegistroReserva() {
        return fechaRegistroReserva;
    }

    public void setFechaRegistroReserva(Timestamp fechaRegistroReserva) {
        this.fechaRegistroReserva = fechaRegistroReserva;
    }
    //campos adicioanles que no estan dentro de la tabla reserva
    public String getNombreCliente(){
        return nombreCliente;
    }
    public void setNombreCliente(String nombreCliente){
        this.nombreCliente = nombreCliente;
    }
    
    public int getNumeroMesa(){
        return numeroMesa;
    }
    
    public void setNumeroMesa(int numeroMesa){
        this.numeroMesa = numeroMesa;
    }
}
