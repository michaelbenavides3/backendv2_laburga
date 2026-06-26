/*

RESPONSABILIDAD

Este controlador finaliza una reserva realizada por un cliente.

Cuando el mesero indica que la reserva terminó, este controlador:

1. Recibe el ID de la reserva.
2. Valida que el ID realmente llegó desde el formulario.
3. Solicita al ReservaDao cambiar el estado de la reserva a "finalizada".
4. Informa el resultado redireccionando al panel del mesero.



MÉTODO DAO UTILIZADO

ReservaDao

- finalizarReserva() Cambia el estado de la reserva a "finalizada" dentro de la base de datos.

*/
package com.controlador;

import com.dao.ReservaDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/finalizarReserva")
public class FinalizarReservaControlador extends HttpServlet {

    // Se crea una instancia del DAO para poder acceder a la base de datos.
    private ReservaDao reservaDao = new ReservaDao();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        /*
       
        PASO 1  RECIBIR EL ID DE LA RESERVA
       

        El parámetro "id" llega desde la URL.

        Ejemplo:

        finalizarReserva?id=15

        */
        String idRecibido = request.getParameter("id");

        System.out.println("ID recibido: " + idRecibido);

        /*
  
        PASO 2 VALIDAR EL PARÁMETRO
       

        Este if evita errores.

        Si el parámetro viene vacío o no existe, no es posible finalizar una reserva.

        trim() elimina espacios en blanco.
        */
        if (idRecibido == null || idRecibido.trim().isEmpty()) {

            response.getWriter().println("El parametro id llego vacio");

            return;

            /*
            return finaliza inmediatamente el controlador.

            Todo el código que está debajo ya NO se ejecuta.
            */
        }

        /*
        
        PASO 3 CONVERTIR EL ID A ENTERO
        

        getParameter() siempre devuelve texto.

        Integer.parseInt() convierte ese texto en un número entero.
        */
        int idReserva = Integer.parseInt(idRecibido);

        /*
       
        PASO 4 FINALIZAR LA RESERVA
        

        Se llama al método finalizarReserva() del DAO.

        Este método actualiza el estado en MySQL.

        El método devuelve un boolean.
        */
        boolean reservaFinalizada = reservaDao.finalizarReserva(idReserva);

        /*
        
        ¿PARA QUÉ SIRVE EL BOOLEAN?
        

        Un boolean solamente puede tener dos valores:

        true
        false

        true  -> la actualización fue exitosa.

        false -> ocurrió algún error.
        */

        /*
       
        PASO 5 VALIDAR EL RESULTADO
        

        Este if verifica si el DAO respondió correctamente.
        */
        if (reservaFinalizada) {

            /*
            Si el boolean es true significa que la reserva fue finalizada correctamente.
            */

            response.sendRedirect("html/m-meserocopy.jsp?finalizado=true");

        } else {

            /*
            Si el boolean es false significa queocurrió un error al actualizar la base de datos.
            */

            response.getWriter().println("Error al finalizar la reserva");
        }

    }

}