
package com.modelo;

public class RolPermisos {
    
    private int idRol;
    private int idPermisos;
    //guarda solo dos valores, true paara permiso activa false permiso desactivado
    private boolean activoRolpermiso;
    
    //declaro mi constructor vacio
    public RolPermisos(){
        
    }
    public RolPermisos(int idRol, int idPermisos, boolean activoRolpermiso){
        this.idRol = idRol;
        this.idPermisos = idPermisos;
        this.activoRolpermiso = activoRolpermiso;
    }
    
    public int getIdRol(){
        return idRol;
    }
    
    public void setIdRol(int idRol){
        this.idRol = idRol;
    }
    
    public int getIdPermisos(){
        return idPermisos;
    }
    public void setIdPermisos(int idPermisos){
        this.idPermisos = idPermisos;
    }
    //para los metodos get en java cuando se utiliza boolean se empieza con is en vez de get
    public boolean isActivoRolpermiso(){
        return activoRolpermiso;
    }
    //variable para cambiar el estado del permiso
    public void setActivoRolpermiso(boolean activoRolpermiso){
        this.activoRolpermiso = activoRolpermiso;
    }
    
    
}
