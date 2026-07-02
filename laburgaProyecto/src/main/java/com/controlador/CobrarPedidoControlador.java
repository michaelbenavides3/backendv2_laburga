package com.controlador;

// DAO encargados de acceder a la base de datos
import com.dao.FacturaDao;
import com.dao.MesaDao;
import com.dao.PagoDao;
import com.dao.PedidoDao;

// Modelos utilizados durante el proceso
import com.modelo.Factura;
import com.modelo.Pago;
import com.modelo.Pedido;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*

RESPONSABILIDAD DEL CONTROLADOR

- Recibir la solicitud de cobro realizada por el cajero.
- Consultar el pedido activo.
- Calcular subtotal, IVA y total.
- Registrar la factura.
- Registrar el pago.
- Actualizar el estado de la factura.
- Cerrar el pedido.
- Liberar la mesa.
- Regresar nuevamente al panel del cajero.

METODOS DAO UTILIZADOS

1. obtenerPedidoPorId()
   Recupera toda la información del pedido.

2. registrarFacturaRetornandoId()
   Registra la factura y devuelve el id generado.

3. registrarPago()
   Registra el pago realizado por el cliente.

4. actualizarEstadoFactura()
   Cambia el estado de la factura a pagada.

5. actualizarEstadoPedido()
   Cambia el estado del pedido a cerrada.

6. cambiarEstado()
   Cambia el estado de la mesa a disponible.

*/

@WebServlet(
        name = "CobrarPedidoControlador",
        urlPatterns = {"/CobrarPedidoControlador"}
)
public class CobrarPedidoControlador extends HttpServlet {

    /*
        processRequest()

        Contiene toda la lógica del proceso de cobro.

        Tanto doGet() como doPost() llaman este método.
     */
    protected void processRequest(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        /*
            Utilizamos try-catch para capturar cualquier error durante el proceso de cobro.
         */
        try {

            /*
            
            PASO 1 RECUPERAR LOS DATOS ENVIADOS DESDE EL JSP
           
             */

            // Pedido que será cobrado
            int idPedido = Integer.parseInt(request.getParameter("idPedido"));

            // Mesa donde se encuentra el pedido
            int idMesa = Integer.parseInt(request.getParameter("idMesa"));

            /*
                Método de pago.

                Actualmente queda fijo en 1 porque solamenteexiste un método registrado.
             */
            int idMetodoPago = 1;

            /*
            
            PASO 2 CONSULTAR EL PEDIDO
            
             */

            PedidoDao pedidoDao = new PedidoDao();

            /*
                Recuperamos toda la información del pedido.

                El método devuelve un objeto Pedido.

                Si no existe devuelve null.
             */
            Pedido pedidoEncontrado = pedidoDao.obtenerPedidoPorId(idPedido);

            /*
                Validamos si realmente el pedido existe.

                null significa que no fue encontrado.
             */
            if (pedidoEncontrado == null) {

                response.sendRedirect( "html/c-cajero.jsp?error=pedido" );

                return;
            }

            /*
            
            PASO 3 CALCULAR FACTURA
            
             */

            // Total del pedido
            double total = pedidoEncontrado.getTotal();

            // Cálculo del subtotal
            double subtotal = Math.round((total / 1.19) * 100.0) / 100.0;

            // Cálculo del IVA
            double iva = Math.round((total - subtotal) * 100.0) / 100.0;

            /*
           
            PASO 4 CREAR Y REGISTRAR LA FACTURA
            
             */

            Factura nuevaFactura = new Factura();

            nuevaFactura.setIdPedido(idPedido);

            nuevaFactura.setSubtotal(subtotal);

            nuevaFactura.setIva(iva);

            nuevaFactura.setTotal(total);

            nuevaFactura.setEstadoPago("pendiente");

            FacturaDao facturaDao = new FacturaDao();

            /*
                El método devuelve el id generado automáticamente por MySQL.
             */
            int idFacturaGenerada = facturaDao.registrarFacturaRetornandoId( nuevaFactura );

            /*
                Si el id es menor o igual a cero, significa que la factura no pudo registrarse.
             */
            if (idFacturaGenerada <= 0) {

                response.sendRedirect( "html/c-cajero.jsp?error=factura" );

                return;
            }

            /*
           
            PASO 5 REGISTRAR EL PAGO
            
             */

            Pago nuevoPago = new Pago();

            nuevoPago.setIdFactura(idFacturaGenerada);

            nuevoPago.setIdMetodoPago(idMetodoPago);

            PagoDao pagoDao = new PagoDao();

            /*
                El método devuelve un boolean.

                true  -> Pago registrado.

                false -> Error al registrar.
             */
            boolean pagoRegistrado = pagoDao.registrarPago(nuevoPago);

            /*
                Si el boolean es false, detenemos el proceso.
             */
            if (!pagoRegistrado) {

                response.sendRedirect( "html/c-cajero.jsp?error=pago" );

                return;
            }

            /*
            
            PASO 6 MARCAR FACTURA COMO PAGADA
            
             */

            facturaDao.actualizarEstadoFactura( idFacturaGenerada, "pagada" );

            /*
            
            PASO 7 CERRAR EL PEDIDO
            
             */

            pedidoDao.actualizarEstadoPedido( idPedido, "cerrada");

            /*
            
            PASO 8 LIBERAR LA MESA
            
             */

            MesaDao mesaDao = new MesaDao();

            mesaDao.cambiarEstado( idMesa, "disponible" );

            /*
            
            PASO 9 REGRESAR AL PANEL DEL CAJERO
            
             */

            response.sendRedirect( "html/c-cajero.jsp?cobro=exitoso");

        } catch (Exception e) {

            /*
                Si ocurre cualquier excepción, mostramos el error en consola y redireccionamos indicando que ocurrió un error general.
             */
            System.out.println( "Error cobrando pedido: " + e.getMessage()
            );

            response.sendRedirect( "html/c-cajero.jsp?error=general" );
        }
    }

    /*
        Tanto GET como POST utilizan el mismo método processRequest().
     */

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }
}