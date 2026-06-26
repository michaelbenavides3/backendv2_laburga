package com.controlador;

// DAO encargado de administrar los usuarios del sistema
import com.dao.UsuarioDao;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*

RESPONSABILIDAD DEL CONTROLADOR

- Recibir la solicitud para cambiar el estado de un usuario.
- Obtener el ID del usuario y el nuevo estado enviados desde el JSP.
- Ejecutar la actualización en la base de datos.
- Redireccionar nuevamente al listado de usuarios.

METODO DAO UTILIZADO

1. actualizarEstadoUsuario()
   Actualiza el estado del usuario (activo o inactivo).

*/

@WebServlet("/CambiarEstadoUsuarioControlador")
public class CambiarEstadoUsuarioControlador extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest peticionWeb,
            HttpServletResponse respuestaWeb)
            throws ServletException, IOException {

        // Mensaje para verificar que el controlador fue ejecutado
        System.out.println("Iniciando cambio de estado del usuario.");

        /*
            Utilizamos try-catch para evitar que el sistema se detenga si ocurre algún error durante el proceso.
         */
        try {

            /*
                Recuperamos los parámetros enviados desde el JSP.

                idUsuario -> Usuario que será modificado.
                estado -> Nuevo estado (activo o inactivo).
             */
            int identificadorUsuario = Integer.parseInt( peticionWeb.getParameter("idUsuario"));

            String nuevoEstadoUsuario = peticionWeb.getParameter("estado");

            // Mostramos los datos recibidos para verificar que llegaron correctamente
            System.out.println("Usuario seleccionado: " + identificadorUsuario);

            System.out.println("Nuevo estado: " + nuevoEstadoUsuario);

            /*
                Creamos el DAO encargado de comunicarse con la base de datos.
             */
            UsuarioDao administradorUsuarios = new UsuarioDao();

            /*
                Ejecutamos la actualización.

                Este método devuelve un boolean.

                true  -> La actualización fue exitosa.
                false -> La actualización falló.
             */
            boolean operacionExitosa = administradorUsuarios.actualizarEstadoUsuario( identificadorUsuario, nuevoEstadoUsuario );

            /*
                El if evalúa el valor del boolean.

                Si es true significa que el usuario fue actualizado.

                Si es false significa que ocurrió algún problema durante la actualización.
             */
            if (operacionExitosa) {

                System.out.println("Estado actualizado correctamente.");

            } else {

                System.out.println("No fue posible actualizar el estado.");
            }

        } catch (Exception errorGeneral) {

            /*
                Si ocurre cualquier excepción, mostramos el mensaje en consola para facilitar la depuración del sistema.
             */
            System.out.println(
                    "Error al cambiar estado del usuario: "
                    + errorGeneral.getMessage()
            );
        }

        /*
            Una vez finalizado el proceso, recargamos nuevamente el listado de usuarios para mostrar el estado actualizado.
         */
        respuestaWeb.sendRedirect(
                peticionWeb.getContextPath()
                + "/ListarUsuariosControlador"
        );
    }
}
