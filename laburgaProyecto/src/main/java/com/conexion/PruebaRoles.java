package com.conexion;

import com.dao.RolesDao;
import com.modelo.Roles;
import java.util.List;

public class PruebaRoles {

    public static void main(String[] args) {

        System.out.println("inicia la prueba.");

        RolesDao administradorRoles = new RolesDao();

        //operador post para registrar
        Roles rolDePrueba = new Roles();

        //rellenamos los objetos con los roles validos
        rolDePrueba.setNombreRol("mesero");
        rolDePrueba.setDescripcionRol("encargado de atender las mesas, unir la mesas, tomar los pedidos enviarlos a caja y crear clientes nuevos");
        administradorRoles.registrarNuevoRol(rolDePrueba);
        //registro cajero
        rolDePrueba.setNombreRol("cajero");
        rolDePrueba.setDescripcionRol("encargador de cerrar las ordenes, liberar las mesas, verificar los productos consumidos");
        administradorRoles.registrarNuevoRol(rolDePrueba);
        //registro cocinero
        rolDePrueba.setNombreRol("cocinero");
        rolDePrueba.setDescripcionRol("este rol, no tiene aun descripcion fisica, no interactura con el sistema");
        administradorRoles.registrarNuevoRol(rolDePrueba);
        //registro admin
        rolDePrueba.setNombreRol("administrador");
        rolDePrueba.setDescripcionRol("encargado de crear nuevos usuarios, dar debaja, recuparar las contraseñas");
        administradorRoles.registrarNuevoRol(rolDePrueba);
        //prueba rol mensajero falsa
        rolDePrueba.setNombreRol("mensajero");
        rolDePrueba.setDescripcionRol("encargado de crear nuevos usuarios, dar debaja, recuparar las contraseñas");
        administradorRoles.registrarNuevoRol(rolDePrueba);

       
        //llamo al metodo registrar de mi dao y guardo la respuesta (verdadero o falso ) en una variale testigo
        boolean resultadoRegistro = administradorRoles.registrarNuevoRol(rolDePrueba);

        //reviso si la base de datos acepto y guardo mi objeto
        if (resultadoRegistro == true) {
            System.out.println("registro confirmado");
        } else {
            System.out.println("aviso: no se creo la fila, ");
        }

        System.out.println("----------------------------------------------------------");

        System.out.println("leer lo roles");

        List<Roles> listaDeRoles = administradorRoles.obtenerListaTodosLosRoles();

        if (listaDeRoles.isEmpty() == false) {
            System.out.println("confirmado tu operacion funciona de maravilla");

            System.out.println("total de roles encontrados en la tabla " + listaDeRoles.size());

            for (Roles rolFilaActual : listaDeRoles) {
                System.out.println("* puesto en el sistema: ID [" + rolFilaActual.getIdRol() + "] -> cargo: " + rolFilaActual.getNombreRol());
            }
        } else {
            System.out.println("la base de datos respondio bien pero la tabal se encutra vacia");
        }
        System.out.println("fin de la prueba");
    }

}
