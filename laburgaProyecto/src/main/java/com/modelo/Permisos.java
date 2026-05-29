
package com.modelo;
public class Permisos {
    
    //declaro las variables reales que son las mismas que se encutran en la columnas de mi tabla
    private int idPermisos;
    private String slugPermisos;
    private String nombrePermiso;
    private String descripcionPermiso;
    
    //constuctuor vacio para cuando desera crear un nuveo permiso
    public Permisos(){
    }
    
    //mi consructor completo para armar un objeto permiso con todos sus datos
    public Permisos(int idPermisos, String slugPermiso, String nombrePermiso, String descripcionPermiso){
        //con .this, guardo toda la informacion que viene de afuera
        this.idPermisos = idPermisos;
        this.slugPermisos = slugPermiso; //ej "crear.pedido, enviar.pedido"
        this.nombrePermiso = nombrePermiso; //el nombre que recibe el permiso
        this.descripcionPermiso = descripcionPermiso; //una breve deficion de que hace el permiso
    }
    
    //se crean los metodos de lectura get(lectura) y setter(modificacion) 
    
    //get lo uso cuando quiero leer, el numero de identificacion del permiso
    //un metodo pubico que me devuele el numero del o el identificador del permiso
    public int getIdPermisos(){
        return idPermisos;
    }
    //set lo utilizo cuando quiero modificar o agregaer el numero de identificacion del permiso
    public void setIdPermisos(int idPermisos){
        this.idPermisos = idPermisos;
    }
    
    //lo uso cuando quiere leer la etiqueta clave del permiso
    public String getSlugPermisos(){
        return slugPermisos;
    }
    //lo uso cuando necesito modificar o asignar la etiqueta clave del permiso
    public void setSlugPermisos(String slugPermisos){
        this.slugPermisos = slugPermisos;
    }
    
    //get lo usamos cuando deseamos leeer el nombre o la descripcion del permiso
    public String getNombrePermiso(){
        return nombrePermiso;
    }
    //set lo usamos cuando necesitamos asigna o modificar el nombre del permiso
    public void setNombrePermiso(String nombrePermiso){
        this.nombrePermiso = nombrePermiso;
    }
    public String getDescripcionPermiso(){
        return descripcionPermiso;
    }
    //usamos un metodo void, porque no necesitamos que nods devuelva nada, solamente se quedara alamacenado
    //usamos set para modificar o asignar la explicacion de un permiso
    public void setDescripcionPermiso(String descripcionPermiso){
        this.descripcionPermiso = descripcionPermiso;
    }
    
    
        
        
        
}
