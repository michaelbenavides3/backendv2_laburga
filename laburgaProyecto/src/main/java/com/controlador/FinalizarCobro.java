package com.controlador;

import com.dao.MesaDao;
import com.dao.PedidoDao;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/FinalizarCobro")
public class FinalizarCobro extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Obtenemos los IDs enviados desde el botón en c-cajero.jsp
        String idPedido = request.getParameter("idPedido");
        String idMesa = request.getParameter("idMesa");
        
        if (idPedido != null && idMesa != null) {
            PedidoDao pedidoDao = new PedidoDao();
            MesaDao mesaDao = new MesaDao();
            
            // 1. Cerramos el pedido en la base de datos
            pedidoDao.actualizarEstadoPedido(Integer.parseInt(idPedido), "cerrada");
            
            // 2. Liberamos la mesa (la ponemos disponible de nuevo)
            mesaDao.cambiarEstado(Integer.parseInt(idMesa), "disponible");
            
            System.out.println("DEBUG: Pedido " + idPedido + " cerrado. Mesa " + idMesa + " liberada.");
        }
        
        // Redirigimos de vuelta al panel de caja para que el cajero vea la lista actualizada
        response.sendRedirect("html/c-cajero.jsp");
    }
}