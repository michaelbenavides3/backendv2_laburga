package com.controlador;

import com.dao.ReservaDao;
import com.modelo.Reserva;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/registrarReserva")
public class ReservaControlador extends HttpServlet {
    
    private ReservaDao reservaDao = new ReservaDao();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Capturar datos del formulario (deben coincidir con el atributo "name" de tu HTML)
        String nombre = request.getParameter("nombre");
        String fechaStr = request.getParameter("fecha");
        String horaStr = request.getParameter("hora") + ":00"; // Formato SQL
        int personas = Integer.parseInt(request.getParameter("personas"));
        int idMesa = Integer.parseInt(request.getParameter("mesa"));
        String observacion = request.getParameter("ocasion");

        // 2. Crear el objeto Reserva
        Reserva nuevaReserva = new Reserva();
        nuevaReserva.setIdMesa(idMesa);
        nuevaReserva.setFechaReserva(Date.valueOf(fechaStr));
        nuevaReserva.setHoraReserva(Time.valueOf(horaStr));
        nuevaReserva.setPersonasReserva(personas);
        nuevaReserva.setObservacionesReserva(observacion);
        nuevaReserva.setEstadoReserva("reservada");
        // Nota: Asumimos que obtienes el idCliente de alguna sesión activa
        nuevaReserva.setIdCliente(1); 

        // 3. Validar y registrar (usando los métodos que creamos)
        boolean existeReserva = reservaDao.validarMesaReservada(idMesa, nuevaReserva.getFechaReserva(), nuevaReserva.getHoraReserva());
        int capacidad = reservaDao.obtenerCapacidadMesa(idMesa);

        if (existeReserva) {
            response.getWriter().println("Error: La mesa ya esta reservada.");
        } else if (personas > capacidad) {
            response.getWriter().println("Error: La mesa no tiene capacidad suficiente.");
        } else {
            boolean registrado = reservaDao.registrarNuevaReserva(nuevaReserva);
            if (registrado) {
                response.sendRedirect("html/m-meserocopy.jsp?exito=true"); // Redirigir a página de éxito
            } else {
                response.getWriter().println("Error al registrar en la base de datos.");
            }
        }
    }
}