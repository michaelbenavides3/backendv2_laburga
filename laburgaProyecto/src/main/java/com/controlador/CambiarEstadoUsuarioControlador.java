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

          
           // atrapo el id del usuario y el estado que quiero poner (activo o inactivo)
            // los recibo como texto de la url y convierto el id a numero
            
            int identificadorUsuario = Integer.parseInt(peticionWeb.getParameter("idUsuario"));

            String nuevoEstadoUsuario = peticionWeb.getParameter("estado");

            System.out.println("usuario seleccionado: "+ identificadorUsuario);

            System.out.println("nuevo estado: "+ nuevoEstadoUsuario);

            
            // creo el da
            
            UsuarioDao administradorUsuarios= new UsuarioDao();

            
            // se ejcuta la actualizacion
            
            boolean operacionExitosa = administradorUsuarios.actualizarEstadoUsuario(identificadorUsuario,nuevoEstadoUsuario);

           
            
           //imprimo por consola para saber que esta funcionando
            if (operacionExitosa) {
                System.out.println("estado actualizado correctamente");

            } else {

                System.out.println("no fue posible actualizar el estado");

            }

        } catch (Exception errorGeneral) {

            System.out.println("error al cambiar estado del usuario: " + errorGeneral.getMessage());
        }

        
        // despues de hacer el cambio, redirecciono al listar usuarios
        // para que la pagina se refresque y se vea el cambio hecho
       
        respuestaWeb.sendRedirect(peticionWeb.getContextPath()+ "/ListarUsuariosControlador");
    }
}
