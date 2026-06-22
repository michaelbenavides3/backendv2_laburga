/*



PedidoDao -> obtenerPedidoPorId->	Recupera el objeto Pedido completo con su total.
PedidoDao -> actualizarEstadoPedido->	Cambia el estado del pedido a 'cerrada'.
FacturaDao -> registrarFacturaRetornandoId->	Inserta la factura y devuelve el id generado para vincularlo al pago.
FacturaDao -> actualizarEstadoFactura->	Cambia el estado de la factura a 'pagada'.
PagoDao	-> registrarPago ->	Registra el movimiento financiero del pago.
MesaDao-> cambiarEstado ->	Cambia el estado de la mesa a 'disponible'.

*/



package com.controlador;

import com.dao.FacturaDao;
import com.dao.MesaDao;
import com.dao.PagoDao;
import com.dao.PedidoDao;

import com.modelo.Factura;
import com.modelo.Pago;
import com.modelo.Pedido;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet( name = "CobrarPedidoControlador", urlPatterns = {"/CobrarPedidoControlador"}
)
public class CobrarPedidoControlador extends HttpServlet {

    protected void processRequest( HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            /*
            
            PASO 1 RECUPERAR DATOS
            
             */
            int idPedido = Integer.parseInt( request.getParameter("idPedido"));

            int idMesa = Integer.parseInt( request.getParameter("idMesa"));

            /*int idMetodoPago = Integer.parseInt( request.getParameter("idMetodoPago"));*/
            int idMetodoPago = 1;

            /*
           
            PASO 2 OBTENER PEDIDO
            
             */
            PedidoDao pedidoDao = new PedidoDao();

            Pedido pedidoEncontrado = pedidoDao.obtenerPedidoPorId(idPedido);

            if (pedidoEncontrado == null) {

                response.sendRedirect( "html/c-cajero.jsp?error=pedido");

                return;
            }

            /*
           
            PASO 3  CALCULAR FACTURA
            */
            
            double total = pedidoEncontrado.getTotal();

            double subtotal = Math.round((total / 1.19) * 100.0) / 100.0;

            double iva = Math.round((total - subtotal) * 100.0) / 100.0;

            /*
            
            PASO 4 CREAR FACTURA
            
             */
            Factura nuevaFactura = new Factura();

            nuevaFactura.setIdPedido(idPedido);

            nuevaFactura.setSubtotal(subtotal);

            nuevaFactura.setIva(iva);

            nuevaFactura.setTotal(total);

            nuevaFactura.setEstadoPago("pendiente");

            FacturaDao facturaDao = new FacturaDao();

            int idFacturaGenerada = facturaDao.registrarFacturaRetornandoId( nuevaFactura);

            if (idFacturaGenerada <= 0) {

                response.sendRedirect( "html/c-cajero.jsp?error=factura");

                return;
            }

            /*
           
            PASO 5  REGISTRAR PAGO
            
             */
            Pago nuevoPago  = new Pago();

            nuevoPago.setIdFactura( idFacturaGenerada);

            nuevoPago.setIdMetodoPago(idMetodoPago);

            PagoDao pagoDao = new PagoDao();

            boolean pagoRegistrado  = pagoDao.registrarPago( nuevoPago);

            if (!pagoRegistrado) {

                response.sendRedirect( "html/c-cajero.jsp?error=pago");

                return;
            }

            /*
           
            PASO 6 ACTUALIZAR FACTURA
           
             */
            facturaDao.actualizarEstadoFactura(idFacturaGenerada, "pagada");

            /*
            
            PASO 7 CERRAR PEDIDO
            
             */
            pedidoDao.actualizarEstadoPedido( idPedido, "cerrada");

            /*
            
            PASO 8 LIBERAR MESA
            
             */
            MesaDao mesaDao = new MesaDao();

            mesaDao.cambiarEstado(idMesa, "disponible");

            /*
           
            PASO 9 REDIRECCIONAR
            
             */
            response.sendRedirect("html/c-cajero.jsp?cobro=exitoso");

        } catch (Exception e) {

            System.out.println("Error cobrando pedido: " + e.getMessage());

            response.sendRedirect( "html/c-cajero.jsp?error=general");
        }
    }

    @Override
    protected void doGet( HttpServletRequest request,HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }
}
