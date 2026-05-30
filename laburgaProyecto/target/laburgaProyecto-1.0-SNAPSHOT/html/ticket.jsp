<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.dao.PedidoDao, com.modelo.Pedido"%>
<%@page import="java.util.Date, java.text.SimpleDateFormat" %>
<%
    // Recuperamos el ID y cargamos el pedido
    String idPedidoStr = request.getParameter("idPedido");
    Pedido p = null;
    if (idPedidoStr != null) {
        PedidoDao pDao = new PedidoDao();
        p = pDao.obtenerPedidoPorId(Integer.parseInt(idPedidoStr));
    }
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Ticket Labur-Ga</title>
        <link rel="stylesheet" href="../css/variables.css">
        <link rel="stylesheet" href="../css/ticket.css">
        <link rel="stylesheet" href="../css/style.css">
    </head>
    <body>

        <header class="encabezado">
            <h1>Ticket de Venta</h1>
            <p>Detalle de la orden</p>
        </header>

        <main class="ticket-container">
            <% if (p != null) { %>
            <div class="ticket-header">
                <img src="../recurso/logo-burguer.png" alt="Logo Labur-Ga" class="ticket-logo">
                <h2>LABUR-GA</h2>
            </div>

            <%
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String fechaActual = sdf.format(new Date());
            %>
            <p style="font-size: 0.8em;">Fecha: <%= fechaActual%></p>

            <div class="ticket-body">
                <p><strong>Pedido N°:</strong> <%= p.getIdPedido()%></p>
                <p><strong>Mesa:</strong> <%= p.getIdMesa()%></p>
                <p><strong>Detalle:</strong></p>
                <p><%= p.getDetalle()%></p>
            </div>

            <div class="ticket-total">
                <p>TOTAL: $<%= String.format("%.2f", p.getTotal())%></p>
            </div>

            <div class="no-print">
                <button onclick="window.print()">Imprimir Ticket</button>
                <a href="c-cajero.jsp">Volver al Panel</a>
            </div>
            <% } else { %>
            <p>Error: No se encontró el pedido.</p>
            <% }%>
        </main>

        <footer class="footer">
            <p>&copy; 2025 Labur-Ga. Todos los derechos reservados.</p>
        </footer>

    </body>
</html>