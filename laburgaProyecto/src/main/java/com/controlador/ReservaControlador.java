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
        

        /*
        
        PASO 5  VALIDACIONES DEL NEGOCIO
  

        Llamamos al DAO para comprobar si ya existe  una reserva registrada para esa mesa, esa fecha y esa hora.

        Método DAO:

        validarMesaReservada()

        Devuelve un boolean.

        true  -> La mesa YA está reservada.
        false -> La mesa está libre.
         */
        boolean existeReserva = reservaDao.validarMesaReservada(idMesa, nuevaReserva.getFechaReserva(), nuevaReserva.getHoraReserva());

        /*
        Consultamos la capacidad máxima de la mesa.

        Método DAO:

        obtenerCapacidadMesa()

        Devuelve un entero con la cantidad máxima
        de personas permitidas.
         */
        int capacidad = reservaDao.obtenerCapacidadMesa(idMesa);
        /*

        
        /*
                
                PASO 6 REGLAS DEL SISTEMA
                

                Aquí comienzan las decisiones del sistema.

                Los if sirven para validar las reglas del negocio  antes de registrar la reserva.
         */


        /*
            Primer if

            Pregunta:

            ¿La mesa ya estaba reservada?

            Si el boolean existeReserva es TRUE,  el sistema NO permite registrar otra reserva porque generaría un conflicto.

            TRUE  = Ya existe reserva.
            FALSE = La mesa sigue disponible.
         */
        if (existeReserva) {

            response.sendRedirect(request.getContextPath() + "/html/m-formulario-reserva.jsp?error=mesaReservada");

        } /*
                Segundo if

                Pregunta:

                ¿La cantidad de personas supera la capacidad de la mesa?

                Si la respuesta es sí, el sistema bloquea el registro.

                Ejemplo

                Mesa para 4 personas
                Cliente solicita 6

                Resultado:

                No permite registrar.
         */ else if (personas > capacidad) {
             
         response.getWriter().println("Error: La mesa no tiene capacidad suficiente.");

        } /*
                Si ninguna regla falló,
                significa que:

                La mesa está libre.

                Tiene capacidad suficiente.

                Ahora sí se registra la reserva.
         */ else {

            /*
                Método DAO

                registrarNuevaReserva()

                Guarda la reserva en la base de datos.

                Devuelve un boolean.

                true  -> La reserva se registró correctamente.

                false -> Ocurrió un error durante el registro.
             */
            boolean registrado  = reservaDao.registrarNuevaReserva(nuevaReserva);


            /*
                Este if verifica el resultado devuelto por el DAO.

                Si registrado es TRUE, significa que la inserción fue exitosa.
             */
            if (registrado) {

                response.sendRedirect( "html/m-meserocopy.jsp?exito=true");

            } /*
                Si registrado es FALSE, ocurrió un problema durante la inserción.
             */ else {

                response.getWriter().println( "Error al registrar la reserva.");

            }

        }
    }
}
