
package com.modelo;



public class Roles {
    
    //declaro las propiedades que se encuentra dentro de la tabla de mi base de datos
    private int idRol;
    private String nombreRol;
    private String descripcionRol;
    
    //creamos el constuctos vacio, para despues ingresarle los datos
    public Roles(){
    }
    
    public Roles (int idRol, String nombreRol, String descripcionRol){
        this.idRol = idRol;
        this.nombreRol = nombreRol;
        this.descripcionRol = descripcionRol;
    }
    
    //creamos los metodos getter y setters, para cada variable 
    
    //metodo para obtener o leer el id del rol
    //utilizamos int, porque necesitamos que nos devuelva un valor
    public int getIdRol(){
        return idRol;
    }
    //permite que java ele se asigne al objeto el id que ya se genero en la base de datos
    //utilizamos void, porque no necesita que nos devuelva nada, solo haga cambios y lo sguarde dentro del objeto
    public void setIdRol(int idRol){
        this.idRol = idRol;
    }
    
    //metodo para obtener o para leer, el nombre del rol (mesero, admin, cocinero, cajero) 
    public String getNombreRol(){
        return nombreRol;
    }
    
    //metodo para cambiar o asignar el nombre al rol
    public void setNombreRol(String nombreRol){
        this.nombreRol = nombreRol;
    }
    
    //metodo para obtener la descripcopn del rol
    public String getDescripcionRol(){
        return descripcionRol; 
    }
    
    //metodo para cambiar o asignar descripciones al rol
    public void setDescripcionRol(String descripcionrol){
        this.descripcionRol = descripcionrol;
    }
    
    
}
