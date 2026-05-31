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

        // 1. Recibir datos del formulario
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        String usuario = request.getParameter("usuario");
        String password = request.getParameter("password");
        int idRol = Integer.parseInt(request.getParameter("idRol"));

        // 2. Crear objeto Usuario (Modelo correcto)
        Usuario u = new Usuario();
        u.setNombreCompleto(nombre);
        u.setNombreUsuario(usuario);
        u.setContraseñaUsuario(password);
        u.setIdRol(idRol);

        // 3. Instanciar los DAOs
        UsuarioDao uDao = new UsuarioDao();
        TelefonoUsuarioDao tDao = new TelefonoUsuarioDao();
        CorreoUsuarioDao cDao = new CorreoUsuarioDao();

        // 4. Lógica de inserción múltiple
        boolean registrado = uDao.registrarNuevoUsuario(u);

        if (registrado) {
            try (Connection con = claseConexion.getConexion()) {
                // Obtenemos el ID que se acaba de generar para asociar los datos
                int idRecienCreado = uDao.obtenerUltimoIdInsertado(con);

                // Registramos en tablas satélite
                tDao.insertarTelefono(idRecienCreado, telefono);
                cDao.insertarCorreo(idRecienCreado, email);

                response.sendRedirect("html/a-panel-principal-admin.jsp?exito=1");
            } catch (SQLException e) {
                System.out.println("Error al registrar datos satélite: " + e.getMessage());
                response.sendRedirect("html/a-nuevo-usuario.jsp?error=2");
            }
        } else {
            response.sendRedirect("html/a-nuevo-usuario.jsp?error=1");
        }
    }
}
