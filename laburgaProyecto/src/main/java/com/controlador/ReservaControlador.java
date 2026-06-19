package com.controlador;

import com.dao.ClienteDao;
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
    private ClienteDao clienteDao = new ClienteDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Captura de datos del formulario
        String telefono = request.getParameter("telefono"); // 
        String fechaStr = request.getParameter("fecha");
        String horaStr = request.getParameter("hora") + ":00";
        int personas = Integer.parseInt(request.getParameter("personas"));
        int idMesa = Integer.parseInt(request.getParameter("mesa"));
        String observacion = request.getParameter("ocasion");

        // 2. Buscar el cliente real en la base de datos
        int idCliente = clienteDao.obtenerIdClientePorTelefono(telefono);

        // 3. Validar si el cliente existe
        if (idCliente == -1) {
            response.getWriter().println("Error: el cliente no existe. Regístrelo primero.");
            return;
        }

        // 4. Crear objeto reserva
        Reserva nuevaReserva = new Reserva();
        nuevaReserva.setIdCliente(idCliente); //
        nuevaReserva.setIdMesa(idMesa);
        nuevaReserva.setFechaReserva(Date.valueOf(fechaStr));
        nuevaReserva.setHoraReserva(Time.valueOf(horaStr));
        nuevaReserva.setPersonasReserva(personas);
        nuevaReserva.setObservacionesReserva(observacion);
        nuevaReserva.setEstadoReserva("reservada");

        // 5. Validaciones
        boolean existeReserva = reservaDao.validarMesaReservada( idMesa, nuevaReserva.getFechaReserva(),nuevaReserva.getHoraReserva() );

        int capacidad = reservaDao.obtenerCapacidadMesa(idMesa);

        // 6. Reglas del negocio
        if (existeReserva) {

            response.getWriter().println("Error: La mesa ya está reservada.");

        } else if (personas > capacidad) {

            response.getWriter().println("Error: La mesa no tiene capacidad suficiente.");

        } else {

            boolean registrado = reservaDao.registrarNuevaReserva(nuevaReserva);

            if (registrado) {
                response.sendRedirect("html/m-meserocopy.jsp?exito=true");
            } else {
                response.getWriter().println("Error al registrar en la base de datos.");
            }
        }
    }
}
