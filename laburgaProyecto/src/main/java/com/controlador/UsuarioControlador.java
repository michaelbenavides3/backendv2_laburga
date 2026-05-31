package com.controlador;

import com.dao.UsuarioDao;
import com.dao.TelefonoUsuarioDao;
import com.dao.CorreoUsuarioDao;
import com.modelo.Usuario;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.conexion.claseConexion; // Necesitamos esto para la conexión

@WebServlet("/UsuarioControlador")
public class UsuarioControlador extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("¡EL SERVLET HA RECIBIDO UNA PETICIÓN!");
        // DEBUG: Ver si llegan los datos a la consola de NetBeans
        System.out.println("DEBUG: Recibiendo datos...");
        System.out.println("Nombre: " + request.getParameter("nombre"));
        System.out.println("Usuario: " + request.getParameter("usuario"));

        // Recibir datos del formulario
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        String usuario = request.getParameter("usuario");
        String password = request.getParameter("password");
        int idRol = Integer.parseInt(request.getParameter("idRol"));

        // Crear objeto Usuario (Modelo correcto)
        Usuario u = new Usuario();
        u.setNombreCompleto(nombre);
        u.setNombreUsuario(usuario);
        u.setContraseñaUsuario(password);
        u.setIdRol(idRol);

        // nstanciar los DAOs
        UsuarioDao uDao = new UsuarioDao();
        TelefonoUsuarioDao tDao = new TelefonoUsuarioDao();
        CorreoUsuarioDao cDao = new CorreoUsuarioDao();

        // ógica de inserción múltiple
        int idUsuarioCreado = uDao.registrarNuevoUsuario(u);

        if (idUsuarioCreado > 0) {

            // INSERTAR DATOS SATÉLITE CON EL ID REAL
            tDao.insertarTelefono(idUsuarioCreado, telefono);
            cDao.insertarCorreo(idUsuarioCreado, email);

            response.sendRedirect("html/a-panel-principal-admin.jsp?exito=1");

        } else {
            response.sendRedirect("html/a-nuevo-usuario.jsp?error=1");
        }
    }
}
