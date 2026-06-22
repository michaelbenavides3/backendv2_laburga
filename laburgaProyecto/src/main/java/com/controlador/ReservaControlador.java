/*

    Identifica quién es el cliente, creándolo si es la primera vez que visita el restaurante (evita duplicados).

    pregunta si la mesa está disponible y si tiene la capacidad necesaria.

    Solo si las reglas se cumplen, procede al registro final.


    ClienteDao->	obtenerIdClientePorTelefono
    ClienteDao->	registrarClienteYRetornarId
    ReservaDao->	validarMesaReservada
    ReservaDao->	obtenerCapacidadMesa
    ReservaDao-> 	registrarNuevaReserva


*/
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

        
        // 1. CAPTURA DEL FORMULARIO
       
        String nombre = request.getParameter("nombre");
        String telefono = request.getParameter("telefono");
        String fechaStr = request.getParameter("fecha");
        String horaStr = request.getParameter("hora") + ":00";
        int personas = Integer.parseInt(request.getParameter("personas"));
        int idMesa = Integer.parseInt(request.getParameter("mesa"));
        String observacion = request.getParameter("ocasion");

        
        // 2. BUSCAR CLIENTE
        
        int idCliente = clienteDao.obtenerIdClientePorTelefono(telefono);

        
        // 3. SI NO EXISTE → CREARLO
      
        if (idCliente == -1) {

            idCliente = clienteDao.registrarClienteYRetornarId(nombre, telefono);

            // validación extra por seguridad
            if (idCliente == -1) {
                response.getWriter().println("Error: no se pudo crear el cliente.");
                return;
            }
        }

      
        // 4. CREAR OBJETO RESERVA
        
        Reserva nuevaReserva = new Reserva();
        nuevaReserva.setIdCliente(idCliente);
        nuevaReserva.setIdMesa(idMesa);
        nuevaReserva.setFechaReserva(Date.valueOf(fechaStr));
        nuevaReserva.setHoraReserva(Time.valueOf(horaStr));
        nuevaReserva.setPersonasReserva(personas);
        nuevaReserva.setObservacionesReserva(observacion);
        nuevaReserva.setEstadoReserva("reservada");

       
        // 5. VALIDACIONES NEGOCIO
        
        boolean existeReserva = reservaDao.validarMesaReservada(idMesa, nuevaReserva.getFechaReserva(), nuevaReserva.getHoraReserva() );

        int capacidad = reservaDao.obtenerCapacidadMesa(idMesa);

        
        // 6. REGLAS DEL SISTEMA
       
        if (existeReserva) {

            response.getWriter().println("Error: La mesa ya está reservada.");

        } else if (personas > capacidad) {

            response.getWriter().println("Error: La mesa no tiene capacidad suficiente.");

        } else {

            boolean registrado = reservaDao.registrarNuevaReserva(nuevaReserva);

            if (registrado) {

                response.sendRedirect("html/m-meserocopy.jsp?exito=true");

            } else {

                response.getWriter().println("Error al registrar la reserva.");
            }
        }
    }
}
