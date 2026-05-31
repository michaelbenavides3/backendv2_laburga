
package com.controlador;
import com.dao.PedidoDao;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author usuario
 */
@WebServlet(name = "SolicitarCuenta", urlPatterns = {"/SolicitarCuenta"})
public class SolicitarCuenta extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        
        
        
        //Recibo el id del pedido desde la vista 
        int idPedido = Integer.parseInt(request.getParameter("idPedido"));

        PedidoDao pedidoDao = new PedidoDao();
        // Ejecuto el método que cambia el estado en la tabla 'pedidos'
        // Esto mueve el pedido de 'activo' a 'pendiente_cobro'
        pedidoDao.solicitarCuenta(idPedido);
        // Redirijo al mesero nuevamente a su pantalla principal
        // Esto hace que la página se actualice y el pedido ya no aparezca como activo
       
        response.sendRedirect("html/m-meserocopy.jsp");
    }
}
