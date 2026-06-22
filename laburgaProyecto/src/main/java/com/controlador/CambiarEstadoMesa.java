/*

    RESPONSABILIDAD:

        - Modificar el estado de una mesa.
        - Detectar cuando una mesa solicita cuenta.
        - Buscar el pedido activo asociado a la mesa.
        - Enviar el pedido a caja.
        - Regresar al panel principal del mesero.


    METODOS DAO UTILIZADOS:

    1. cambiarEstado -> Actualiza el estado actual de una mesa.

    2. obtenerPedidoActivoPorMesa -> Busca el pedido que se encuentra activo en la mesa seleccionada.

    3. solicitarCuenta -> Cambia el estado del pedido para indicar que fue enviado a caja.




*/




package com.controlador;

import com.dao.PedidoDao;

import com.dao.MesaDao;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "CambiarEstadoMesa", urlPatterns = {"/CambiarEstadoMesa"})
public class CambiarEstadoMesa extends HttpServlet {

    // Centralizamos la lógica aquí para que GET y POST hagan lo mismo
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("===== ENTRE AL SERVLET =====");

        String idMesa = request.getParameter("idMesa");
        String nuevoEstado = request.getParameter("estado");

        System.out.println("Mesa recibida: " + idMesa);
        System.out.println("Estado recibido: " + nuevoEstado);

//        String idMesa = request.getParameter("idMesa");
//        String nuevoEstado = request.getParameter("estado");

        if (idMesa != null && nuevoEstado != null) {

            MesaDao mesaDao = new MesaDao();

            mesaDao.cambiarEstado(Integer.parseInt(idMesa), nuevoEstado);

            if (nuevoEstado.equals("pendiente_cobro")) {

                PedidoDao pedidoDao = new PedidoDao();

                int idPedido = pedidoDao.obtenerPedidoActivoPorMesa(
                        Integer.parseInt(idMesa)
                );
                System.out.println("Pedido encontrado: " + idPedido);

                if (idPedido > 0) {
                    pedidoDao.solicitarCuenta(idPedido);
                    System.out.println("Pedido actualizado a pendiente_cobro");
                }

                System.out.println("Mesa recibida: " + idMesa);
                System.out.println("Estado recibido: " + nuevoEstado);
            }
        }
        response.sendRedirect("html/m-meserocopy.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
