
package com.modelo;


public class CorreoUsuario {
    
    
    private int idCorreo;
    private int idUsuario;
    private String correoUsuario;
    
    public CorreoUsuario(){
        
    }
    
    public CorreoUsuario(int idCorreo,int idUsuario,String correoUsuario){
        
        this.idCorreo = idCorreo;
        this.idUsuario = idUsuario;
        this.correoUsuario = correoUsuario;
        
    }
    
    
    //metodo get
    public int getIdCorreo(){
        return idCorreo;
    }
    //metodo set
    public void setIdCorreo(int idCorreo){
        this.idCorreo = idCorreo;
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
    public String getCorreoUsuario(){
        return correoUsuario;
    }
    //metodo set
    public void setIdCorreo(String correoUsuario){
        this.correoUsuario = correoUsuario;
    }
}
