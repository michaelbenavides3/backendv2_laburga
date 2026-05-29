package com.modelo;

import java.util.Date;

public class Usuario {

//declaracion de variables privadas    
    //se declarana privadas, para que despues los demas archivos no se altereen
    
    private int idUsuario;
    private String nombreCompleto;
    private String nombreUsuario;
    private String contraseñaUsuario;
    private int idRol;
    private String estadoUsuario;
    private Date fechaCreacionUsuario;
    
    //creo mi contructor vacio , para despues poder inyectarle informacion
    
    public Usuario(){
    }
    
    //creo mi constructor con todo los parametros me sirve para armar un objeto de empleados con toda su informacion cargada
    public Usuario(int idUsuario, String nombreCompleto, String nombreUsuario, String contraseñaUsuario, int idRol, String estadoUsuario, Date fechaCreacionUsuario){
        
        //con la palabra this le digo a java que tome los datos que vienen de afueray los guarde ordenadamente dentro de mis variables
        this.idUsuario = idUsuario;
        this.nombreCompleto = nombreCompleto;
        this.nombreUsuario = nombreUsuario;
        this.contraseñaUsuario = contraseñaUsuario;
        this.idRol = idRol;
        this.estadoUsuario = estadoUsuario;
        this.fechaCreacionUsuario = fechaCreacionUsuario;
        
    }
    
    //metodo get cuando quiere leer el numero de identificaciond el usuario
    
    public int getIdUsuario(){
        return idUsuario;
    }
    
    //metodo set cuando necesito asignar o cambiar el numero de identificacion
    public void setIdUsuario(int idUsuario){
        this.idUsuario = idUsuario;
    }
    
    //metodo get cuando quiere leer el numero de identificaciond el usuario
    
    public String getNombreCompleto(){
        return nombreCompleto;
    }
    
    //metodo set cuando necesito asignar o cambiar el numero de identificacion
    public void setNombreCompleto(String nombreCompleto){
        this.nombreCompleto = nombreCompleto;
    }
    
    //metodo get cuando quiere leer el numero de identificaciond el usuario
    
    public String getNombreUsuario(){
        return nombreUsuario;
    }
    
    //metodo set cuando necesito asignar o cambiar el numero de identificacion
    public void setNombreUsuario(String nombreUsuario){
        this.nombreUsuario = nombreUsuario;
    }
    
    //metodo get cuando quiere leer el numero de identificaciond el usuario
    
    public String getContraseñaUsuario(){
        return contraseñaUsuario;
    }
    
    //metodo set cuando necesito asignar o cambiar el numero de identificacion
    public void setContraseñaUsuario(String contraseñaUsuario){
        this.contraseñaUsuario = contraseñaUsuario;
    }
    
    //metodo get cuando quiere leer el numero de identificaciond el usuario
    
    public int getIdRol(){
        return idRol;
    }
    
    //metodo set cuando necesito asignar o cambiar el numero de identificacion
    public void setIdRol(int idRol){
        this.idRol = idRol;
    }
    
    //metodo get cuando quiere leer el numero de identificaciond el usuario
    
    public String getEstadoUsuario(){
        return estadoUsuario;
    }
    
    //metodo set cuando necesito asignar o cambiar el numero de identificacion
    public void setEstadoUsuario(String estadoUsuario){
        this.estadoUsuario = estadoUsuario;
    }
    
    //metodo get cuando quiere leer el numero de identificaciond el usuario
    
    public Date getFechaCreacionUsuario(){
        return fechaCreacionUsuario;
    }
    
    //metodo set cuando necesito asignar o cambiar el numero de identificacion
    public void setFechaCreacionUsuario(Date fechaCreacionUsuario){
        this.fechaCreacionUsuario = fechaCreacionUsuario;
    }
    
    
    
    
    
}
