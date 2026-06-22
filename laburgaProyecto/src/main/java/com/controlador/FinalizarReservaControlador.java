/*
    es el encargado de gestionar el ciclo de vida final de una reserva.

    Método en ReservaDao-> finalizarReserva

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

    private ReservaDao reservaDao = new ReservaDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idRecibido = request.getParameter("id");

        System.out.println("ID recibido: [" + idRecibido + "]");

        // 1. recibir ID de la reserva
        int idReserva = Integer.parseInt(request.getParameter("id"));

        // 2. finalizar reserva
        boolean ok = reservaDao.finalizarReserva(idReserva);
        
        if (idRecibido == null || idRecibido.trim().isEmpty()) {
        response.getWriter().println("El parametro id llego vacio");
         return;
}

        /*int idReserva = Integer.parseInt(idRecibido);*/

        // 3. redirigir o mostrar resultado
        if (ok) {
            response.sendRedirect("html/m-meserocopy.jsp?finalizado=true");
        } else {
            response.getWriter().println("Error al finalizar la reserva");
        }
    }
}