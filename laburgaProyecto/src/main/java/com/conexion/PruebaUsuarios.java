package com.conexion;

import com.dao.UsuarioDao;
import com.modelo.Usuario;
import java.util.List;

public class PruebaUsuarios {

    public static void main(String[] args) {

        System.out.println("inicio de prueba de control de usuarios");

        //metodo post registar un nuevo empleado 
        //instacion mi administrador de datos para usuarios el mandara los datos a mysql
        UsuarioDao administradorUsuarios = new UsuarioDao();
        //UsuarioDao empleoAdministrador = new UsuarioDao();

        //creo mi variable vacia unsando el molde usuario lo limpio oara configuar cada empleado
        Usuario empleadoTemporal;

        //registro de personal rol=2 cajero
        System.out.println("registrando cajero rol=2");
        //1cajero
        //limpio mi variable y creo un contenedor totalmente nuevo de memoria
        empleadoTemporal = new Usuario();
        empleadoTemporal.setNombreCompleto("Jose Roa");
        empleadoTemporal.setNombreUsuario("jgard24");
        empleadoTemporal.setContraseñaUsuario("cajero123");
        empleadoTemporal.setIdRol(2);
        administradorUsuarios.registrarNuevoUsuario(empleadoTemporal);
        //2cajero
        empleadoTemporal = new Usuario();//limpio mi variale para ingresar nuevo empleado
        empleadoTemporal.setNombreCompleto("Laura Luna");
        empleadoTemporal.setNombreUsuario("luna94");
        empleadoTemporal.setContraseñaUsuario("cajero987");
        empleadoTemporal.setIdRol(2);
        administradorUsuarios.registrarNuevoUsuario(empleadoTemporal);
        //3cajero
        empleadoTemporal = new Usuario();//limpio mi variale para ingresar nuevo empleado
        empleadoTemporal.setNombreCompleto("Cuentas Clara");
        empleadoTemporal.setNombreUsuario("clara93");
        empleadoTemporal.setContraseñaUsuario("cajero741");
        empleadoTemporal.setIdRol(2);
        administradorUsuarios.registrarNuevoUsuario(empleadoTemporal);

        //registro personal de mesero
        System.out.println("registrando datos de meseros");
        //idrol(1)
        empleadoTemporal = new Usuario();//limpio mi variale para ingresar nuevo empleado
        empleadoTemporal.setNombreCompleto("Elba Lazo");
        empleadoTemporal.setNombreUsuario("elazo");
        empleadoTemporal.setContraseñaUsuario("mesero123");
        empleadoTemporal.setIdRol(1);
        administradorUsuarios.registrarNuevoUsuario(empleadoTemporal);
        //2empleado mesero
        empleadoTemporal = new Usuario();//limpio mi variale para ingresar nuevo empleado
        empleadoTemporal.setNombreCompleto("Armando Paredes");
        empleadoTemporal.setNombreUsuario("arparedes");
        empleadoTemporal.setContraseñaUsuario("mesero987");
        empleadoTemporal.setIdRol(1);
        administradorUsuarios.registrarNuevoUsuario(empleadoTemporal);
        //3empleado mesero
        empleadoTemporal = new Usuario();//limpio mi variale para ingresar nuevo empleado
        empleadoTemporal.setNombreCompleto("Ramon Valdes");
        empleadoTemporal.setNombreUsuario("donramon");
        empleadoTemporal.setContraseñaUsuario("mesero741");
        empleadoTemporal.setIdRol(1);
        administradorUsuarios.registrarNuevoUsuario(empleadoTemporal);
        //4empleado mesero
        empleadoTemporal = new Usuario();//limpio mi variale para ingresar nuevo empleado
        empleadoTemporal.setNombreCompleto("La puerca");
        empleadoTemporal.setNombreUsuario("puerca");
        empleadoTemporal.setContraseñaUsuario("mesero963");
        empleadoTemporal.setIdRol(1);
        administradorUsuarios.registrarNuevoUsuario(empleadoTemporal);
        //creaacion de rol para cocinero id(rol3)
        System.out.println("registrando rol cocinero");
        empleadoTemporal.setNombreCompleto("Cristiano Messi");
        empleadoTemporal.setNombreUsuario("cr7");
        empleadoTemporal.setContraseñaUsuario("cocina123");
        empleadoTemporal.setIdRol(3);
        administradorUsuarios.registrarNuevoUsuario(empleadoTemporal);
        //registro al admin, rol4
        System.out.println("registrando administrador");
        empleadoTemporal.setNombreCompleto("Michael Benavides");
        empleadoTemporal.setNombreUsuario("michaelben3");
        empleadoTemporal.setContraseñaUsuario("123456789");
        empleadoTemporal.setIdRol(4);
        administradorUsuarios.registrarNuevoUsuario(empleadoTemporal);

        //guardo por medio de un boolean 
        // Guardo el ID generado del último usuario registrado
        int idUsuarioGenerado = administradorUsuarios.registrarNuevoUsuario(empleadoTemporal);

// Verifico si se creó correctamente
        if (idUsuarioGenerado > 0) {

            System.out.println("Paso 1 completado: se confirma creación de usuario");
            System.out.println("ID generado: " + idUsuarioGenerado);

        } else {

            System.out.println("No se pudo completar la tarea de creación");

        }

        System.out.println("----------------------------------------------------------------");
        System.out.println("leer lista completa de usuarios");

        //llamo al metodo de lectura del dato y guardo los resultados en mi lista
        List<Usuario> listaDeUsuarios = administradorUsuarios.obtenerListaTodosLosUsuarios();
        //reviso si la carpte de lista de usuarios no regreso vacia de mysql
        if (listaDeUsuarios.isEmpty() == false) {
            System.out.println("paso 2 completado: la operacion get funcioan perfecto");

            System.out.println("ttoal de usuarios encontrados en la tabla: " + listaDeUsuarios.size());

            //con un for recorro la lista fila por fila con un ciclo para ver la informacion
            for (Usuario usuarioFilaActual : listaDeUsuarios) {
                System.out.println("* empleado: " + usuarioFilaActual.getNombreCompleto() + "| login: " + usuarioFilaActual.getNombreUsuario() + "| estado: " + usuarioFilaActual.getEstadoUsuario());

            }
        } else {
            System.out.println("aviso del get: la bse de datos respondio bien");
        }

        System.out.println("fin de la prueba");

    }

}
