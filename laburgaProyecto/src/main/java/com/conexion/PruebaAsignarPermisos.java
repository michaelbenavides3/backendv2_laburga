package com.conexion;

import com.dao.RolPermisoDao;
import com.modelo.RolPermisos;

public class PruebaAsignarPermisos {

    //metodo principal del programa
    public static void main(String[] args) {

        System.out.println("iniciando pruebas de permisos");

        //metodos para dar y quitar permiso 
        
        
        asignarPermisosMesero();
        asignarPermisosCajero();
        asignarPermisosAministrador();
        quitarPermisosMesero();
        quitarPermisosCajero();

    }

    //metodo para asignar permisos al administrador
    public static void asignarPermisosMesero() {

        System.out.println("asignando permisos al mesero");

        //creo el administrador encargado de hablar con mysql
        RolPermisoDao administradorAsignaciones = new RolPermisoDao();

        //administrador -> usuario.crear
        administradorAsignaciones.asignarPermisoARol(new RolPermisos(1, 1, true));

        //administrador -> usuario.editar
        administradorAsignaciones.asignarPermisoARol(new RolPermisos(1, 2, true));

        //administrador -> usuario.eliminar
        administradorAsignaciones.asignarPermisoARol(new RolPermisos(1, 3, true));

        //administrador -> rol.crear
        administradorAsignaciones.asignarPermisoARol(new RolPermisos(1, 4, true));

        //administrador -> permiso.crear
        administradorAsignaciones.asignarPermisoARol(new RolPermisos(1, 5, true));

        System.out.println("permisos del administrador mesero");
    }

    //metodo para desactivar permisos
    public static void quitarPermisosMesero() {

        System.out.println("desactivando permisos del mesero");

        //creo el administrador encargado de hablar con mysql
        RolPermisoDao administradorAsignaciones = new RolPermisoDao();

        //desactiva el permiso 15 del rol 1
        administradorAsignaciones.camibiarEstadoPermisoDeRol(1,15,false);
        //desactiva el permiso 16 del rol 1
        administradorAsignaciones.camibiarEstadoPermisoDeRol(1,16,false);
        //desactiva el permiso 17 del rol 1
        administradorAsignaciones.camibiarEstadoPermisoDeRol(1,17,false);


        System.out.println("permisos desactivados correctamente");
    }
    
    public static void quitarPermisosCajero(){
        
        RolPermisoDao administradorAsignaciones = new RolPermisoDao();
        
        administradorAsignaciones.camibiarEstadoPermisoDeRol(2,11,false);
        administradorAsignaciones.camibiarEstadoPermisoDeRol(2,12,false);
        administradorAsignaciones.camibiarEstadoPermisoDeRol(2,13,false);
        administradorAsignaciones.camibiarEstadoPermisoDeRol(2,18,false);
        administradorAsignaciones.camibiarEstadoPermisoDeRol(2,19,false);
        administradorAsignaciones.camibiarEstadoPermisoDeRol(2,20,false);
        administradorAsignaciones.camibiarEstadoPermisoDeRol(2,21,false);
        administradorAsignaciones.camibiarEstadoPermisoDeRol(2,22,false);
        administradorAsignaciones.camibiarEstadoPermisoDeRol(2,23,false);
        administradorAsignaciones.camibiarEstadoPermisoDeRol(2,24,false);        
    }
    public static void asignarPermisosCajero() {

        System.out.println("asignando permisos cajero");

        RolPermisoDao administradorAsignaciones = new RolPermisoDao();

        //pedido.ver
        administradorAsignaciones.asignarPermisoARol(new RolPermisos(2, 3, true));
        //pedido.ver
        administradorAsignaciones.asignarPermisoARol(new RolPermisos(2, 8, true));
        //pedido.ver
        administradorAsignaciones.asignarPermisoARol(new RolPermisos(2, 14, true));

        //factura.crear
        administradorAsignaciones.asignarPermisoARol(new RolPermisos(2, 15, true));

        //factura.ver
        administradorAsignaciones.asignarPermisoARol(new RolPermisos(2, 16, true));

        //pago.registrar
        administradorAsignaciones.asignarPermisoARol(new RolPermisos(2, 17, true));


        System.out.println("permisos cajero registrados");
    }
    
    
    public static void asignarPermisosAministrador(){
        
        RolPermisoDao administradorAsignaciones = new RolPermisoDao();
        
        administradorAsignaciones.asignarPermisoARol(new RolPermisos(4, 11, true));
        administradorAsignaciones.asignarPermisoARol(new RolPermisos(4, 12, true));
        administradorAsignaciones.asignarPermisoARol(new RolPermisos(4, 13, true));
        administradorAsignaciones.asignarPermisoARol(new RolPermisos(4, 18, true));
        administradorAsignaciones.asignarPermisoARol(new RolPermisos(4, 19, true));
        administradorAsignaciones.asignarPermisoARol(new RolPermisos(4, 20, true));
        administradorAsignaciones.asignarPermisoARol(new RolPermisos(4, 21, true));
        administradorAsignaciones.asignarPermisoARol(new RolPermisos(4, 22, true));
        administradorAsignaciones.asignarPermisoARol(new RolPermisos(4, 23, true));
        administradorAsignaciones.asignarPermisoARol(new RolPermisos(4, 24, true));
    }
    
    
}
