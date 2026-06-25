/*
    actúa como un limpiador automático. Su propósito no es solo eliminar un registro en la tabla detalle_pedido, 
    sino mantener la integridad del estado de las mesas. Si un mesero borra el último producto de un pedido, 
    el sistema automáticamente cierra el pedido y libera la mesa para nuevos clientes, evitando errores humanos.

    DetallePedidoDao-> eliminarDetallePedido
    DetallePedidoDao-> contarDetallesPorPedido
    PedidoDao-> obtenerMesaPorPedido
    PedidoDao-> actualizarEstadoPedido
    MesaDao-> cambiarEstado




*/
package com.controlador;

import com.dao.DetallePedidoDao;
import com.dao.MesaDao;
import com.dao.PedidoDao;
import com.modelo.Pedido;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "EliminarDetallePedidoControlador",
        urlPatterns = {"/EliminarDetallePedidoControlador"})
public class EliminarDetallePedidoControlador extends HttpServlet {

    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        /*
        
        1. RECUPERAR PARÁMETROS
        
         */
        int identificadorDetallePedido = Integer.parseInt(request.getParameter("idDetalle"));

        int identificadorPedido = Integer.parseInt(request.getParameter("idPedido"));

        /*
        
        2. ELIMINAR PRODUCTO DEL PEDIDO
        
         */
        DetallePedidoDao detallePedidoDao = new DetallePedidoDao();

        boolean detalleEliminadoCorrectamente = detallePedidoDao.eliminarDetallePedido(identificadorDetallePedido);
        
        

        if (detalleEliminadoCorrectamente) {

            /*
            
            3. CONTAR PRODUCTOS RESTANTES
            
             */
            int cantidadProductosRestantes = detallePedidoDao.contarDetallesPorPedido(identificadorPedido);

            System.out.println("Cantidad productos restantes: " + cantidadProductosRestantes);

            System.out.println("Pedido evaluado: " + identificadorPedido);
            /*
            
            4. SI NO QUEDAN PRODUCTOS
            
             */
            if (cantidadProductosRestantes == 0) {

                System.out.println("NO QUEDAN PRODUCTOS EN EL PEDIDO");

                PedidoDao pedidoDao = new PedidoDao();

                int identificadorMesa = pedidoDao.obtenerMesaPorPedido(identificadorPedido);

                System.out.println("Mesa encontrada: " + identificadorMesa);

                /*
                    5. CERRAR PEDIDO
                 */
                pedidoDao.actualizarEstadoPedido( identificadorPedido, "cerrada");

                /*
                    6. LIBERAR MESA
                 */
                MesaDao mesaDao = new MesaDao();

                mesaDao.cambiarEstado(identificadorMesa, "disponible");

                response.sendRedirect("html/m-meserocopy.jsp?pedidoCerrado=true");

                return;

            }
        }

        /*
        
        7. VOLVER A LA VISTA DE EDICIÓN
        
         */
        response.sendRedirect(request.getHeader("referer"));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        processRequest(request, response);
    }
}
