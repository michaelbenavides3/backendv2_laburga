
package com.modelo;


public class TelefonoUsuarios {
    
    private int idTelefono;
    private int idUsuario;
    private String numeroTelefono;
    
    public TelefonoUsuarios(){
        
    }
    
    
    public TelefonoUsuarios(int idTelefono, int idUsuario, String numeroTelefono){
        
        this.idTelefono = idTelefono;
        this.idUsuario = idUsuario;
        this.numeroTelefono = numeroTelefono;
    }
    
    //metodo get
    public int getIdTelefono(){
        return idTelefono;
    }
    //metodo set
    public void setIdTelefono(int idTelefono){
        this.idTelefono = idTelefono;
    }
    
    //metodo get
    public int getIdUsuario(){
        return idUsuario;
    }
    //metodo set
    public void setIdUsuario(int idUsuario){
        this.idUsuario = idUsuario;
    }
    
    //metodo get
    public String getNumeroTelefono(){
        return numeroTelefono;
    }
    //metodo set
    public void setNumeroTelefono(String numeroTelefono){
        this.numeroTelefono = numeroTelefono;
    }
}
