package com.controlador;

import com.dao.UsuarioDao;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador encargado de activar o desactivar usuarios.
 *
 * Recibe: idUsuario estado
 *
 * Ejemplo: CambiarEstadoUsuarioControlador?idUsuario=5&estado=inactivo
 *
 * CambiarEstadoUsuarioControlador?idUsuario=5&estado=activo
 */
@WebServlet("/CambiarEstadoUsuarioControlador")
public class CambiarEstadoUsuarioControlador extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest peticionWeb,HttpServletResponse respuestaWeb)
            throws ServletException, IOException {

        System.out.println("iniciando cambio de estado de usuario");

        try {

          
            // RECIBIR LOS DATOS ENVIADOS DESDE EL JSP
            
            int identificadorUsuario = Integer.parseInt(peticionWeb.getParameter("idUsuario"));

            String nuevoEstadoUsuario = peticionWeb.getParameter("estado");

            System.out.println("usuario seleccionado: "+ identificadorUsuario);

            System.out.println("nuevo estado: "+ nuevoEstadoUsuario);

            
            // CREAR EL DAO
            
            UsuarioDao administradorUsuarios= new UsuarioDao();

            
            // EJECUTAR LA ACTUALIZACIÓN
            
            boolean operacionExitosa = administradorUsuarios.actualizarEstadoUsuario(identificadorUsuario,nuevoEstadoUsuario);

           
            // MOSTRAR RESULTADO EN CONSOLA
           
            if (operacionExitosa) {
                System.out.println("estado actualizado correctamente");

            } else {

                System.out.println("no fue posible actualizar el estado");

            }

        } catch (Exception errorGeneral) {

            System.out.println("error al cambiar estado del usuario: " + errorGeneral.getMessage());
        }

        
        // VOLVER A CARGAR EL LISTADO
       
        respuestaWeb.sendRedirect(peticionWeb.getContextPath()+ "/ListarUsuariosControlador");
    }
}
