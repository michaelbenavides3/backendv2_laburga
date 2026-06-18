<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="com.dao.PedidoDao"%>
<%@page import="com.dao.FacturaDao"%>
<%@page import="com.dao.PagoDao"%>
<%@page import="com.dao.MedioPagoDao"%>

<%@page import="com.modelo.Pedido"%>
<%@page import="com.modelo.Factura"%>
<%@page import="com.modelo.Pago"%>
<%@page import="com.modelo.MedioPago"%>

<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>

<%

    String idPedidoStr = request.getParameter("idPedido");

    Pedido pedidoEncontrado = null;
    Factura facturaEncontrada = null;
    Pago pagoEncontrado = null;
    MedioPago medioPagoEncontrado = null;

    /*
    Valores para mostrar en el ticket
     */
    double subtotal = 0;
    double iva = 0;
    double total = 0;

    if (idPedidoStr != null) {

        int idPedido = Integer.parseInt(idPedidoStr);

        PedidoDao pedidoDao = new PedidoDao();

        pedidoEncontrado = pedidoDao.obtenerPedidoPorId(idPedido);

        if (pedidoEncontrado != null) {

            FacturaDao facturaDao = new FacturaDao();

            facturaEncontrada = facturaDao.obtenerFacturaPorPedido(idPedido);

            if (facturaEncontrada != null) {

                subtotal = facturaEncontrada.getSubtotal();
                iva = facturaEncontrada.getIva();
                total = facturaEncontrada.getTotal();
            } else {

                total = pedidoEncontrado.getTotal();
                subtotal = total / 1.19;
                iva = total - subtotal;
            }

            System.out.println("ID PEDIDO: " + idPedido);

            if (facturaEncontrada == null) {
                System.out.println("FACTURA NO ENCONTRADA");
            } else {
                System.out.println("FACTURA ENCONTRADA: " + facturaEncontrada.getIdFactura());
            }

            if (facturaEncontrada != null) {

                PagoDao pagoDao = new PagoDao();

                pagoEncontrado = pagoDao.obtenerPagoPorFactura(facturaEncontrada.getIdFactura());

                if (pagoEncontrado != null) {

                    MedioPagoDao medioPagoDao = new MedioPagoDao();

                    medioPagoEncontrado = medioPagoDao.obtenerMedioPagoPorId(pagoEncontrado.getIdMetodoPago());
                }
            }
        }
    }


%>

<!DOCTYPE html>

<html lang="es">

    <head>


        <meta charset="UTF-8">

        <title>Factura Labur-Ga</title>

        <link rel="stylesheet" href="../css/variables.css">

        <link rel="stylesheet" href="../css/ticket.css">

        <link rel="stylesheet" href="../css/style.css">


    </head>

    <body>

        <header class="encabezado">


            <h1>Factura</h1>

            <p>Detalle de la venta</p>


        </header>

        <main class="ticket-container">

            <% if (pedidoEncontrado != null) { %>


            <div class="ticket-header">

                <img src="../recurso/logo-burguer.png" alt="Logo Labur-Ga" class="ticket-logo">

                <h2>LABUR-GA</h2>

                <% if (facturaEncontrada != null) {%>

                <p>

                    Factura N°

                    <%= facturaEncontrada.getIdFactura()%>

                </p>

                <% } %>

            </div>

            <%

                SimpleDateFormat formatoFecha
                        = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                String fechaActual
                        = formatoFecha.format(new Date());

            %>

            <p>

                Fecha:

                <%= fechaActual%>

            </p>

            <div class="ticket-body">

                <p>

                    <strong>Pedido:</strong>

                    <%= pedidoEncontrado.getIdPedido()%>

                </p>

                <p>

                    <strong>Mesa:</strong>

                    <%= pedidoEncontrado.getIdMesa()%>

                </p>

                <% if (facturaEncontrada != null) {%>

                <p>

                    <strong>Estado:</strong>

                    <%= facturaEncontrada.getEstadoPago()%>

                </p>

                <% }%>

                <hr>

                <p>

                    <strong>Detalle:</strong>

                </p>

                <p>

                    <%= pedidoEncontrado.getDetalle().replace(",", "<br>")%>

                </p>

            </div>

            <div class="ticket-resumen">

                <hr>

                <p>
                    <strong>Subtotal:</strong>
                    $<%= String.format("%,.0f", subtotal)%>
                </p>

                <p>
                    <strong>IVA (19%):</strong>
                    $<%= String.format("%,.0f", iva)%>
                </p>

                <hr>

                <p style="font-size:18px; font-weight:bold;">
                    <strong>TOTAL:</strong>
                    $<%= String.format("%,.0f", total)%>
                </p>

            </div>

        

            <% if (medioPagoEncontrado != null) {%>

            <div class="ticket-pago">

                <p>

                    Método de Pago:

                    <strong>

                        <%= medioPagoEncontrado.getMetodoPago()%>

                    </strong>

                </p>

            </div>

            <% } %>

            <div class="no-print">

                <button onclick="window.print()">

                    Imprimir Factura

                </button>

                <a href="c-cajero.jsp">Volver al Panel</a>

            </div>


            <% }

                
                
            else { %>


            <p>

                No se encontró el pedido.

            </p>


            <% }%>

        </main>

        <footer class="footer">


            <p>

                &copy; 2025 Labur-Ga. Todos los derechos reservados.

            </p>


        </footer>

    </body>

</html>
