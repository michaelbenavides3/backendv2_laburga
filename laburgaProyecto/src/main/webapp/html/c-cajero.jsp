<%@ page 
    import="com.dao.PedidoDao, 
    com.modelo.Pedido, 
    java.util.List" %>
<%@page import="com.modelo.Usuario"%>


<%
    String cobro = request.getParameter("cobro");
%>
<%
    Usuario usuarioSesion = (Usuario) session.getAttribute("usuarioLogeadoObjeto");
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Labur-Ga | Panel Cajero</title>
        <link rel="stylesheet" href="../css/cajero.css">
        <link rel="stylesheet" href="../css/variables.css">
        <link rel="stylesheet" href="../css/style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/variables.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/ticket.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cajero.css">
    </head>
    <body class="cajero">

        <header class="encabezado">
            <h1>Panel del Cajero</h1>
            <p>Gestiona órdenes y facturas</p>
            <p>
                Cajero:
                <strong><%= usuarioSesion.getNombreCompleto()%></strong>
            </p>
            <a href="../cerrarSesion" class="btn_cerrar_sesion">Cerrar Sesión</a>
        </header>

        <section class="ordenes">
            <h2>Órdenes pendientes</h2>
            <table class="tabla-ordenes">
                <thead>
                    <tr>
                        <th>Mesa</th>
                        <th>Detalle</th>
                        <th>Total</th>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        // 1. Instanciamos el DAO de Pedidos
                        PedidoDao pDao = new PedidoDao();
                        // 2. Traemos solo los pedidos que están en estado 'pendiente_cobro' (o como los llames en tu BD)
                        List<Pedido> lista = pDao.listarPedidosPendientes();

                        for (Pedido p : lista) {
                    %>
                    <tr>
                        <td>Mesa <%= p.getIdMesa()%></td>
                        <td><%= p.getDetalle()%></td>
                        <td>$<%= p.getTotal()%></td>
                        <td>
                            <a href="ticket.jsp?idPedido=<%= p.getIdPedido()%>" class="btn btn-verde">Ver Ticket</a>
                            <!-- <a href="../FinalizarCobro?idPedido=<%= p.getIdPedido()%>&idMesa=<%= p.getIdMesa()%>" class="btn btn-naranja">Cerrar Cuenta</a> -->
                            <!--<a href="../RegistrarCobroControlador?idPedido=<%= p.getIdPedido()%>&idMesa=<%= p.getIdMesa()%>"class="btn btn-naranja">Cobrar Pedido</a>-->
                            <a href="../CobrarPedidoControlador?idPedido=<%= p.getIdPedido()%>&idMesa=<%= p.getIdMesa()%>"class="btn btn-naranja">Cobrar Pedido</a>
                        </td>
                    </tr>
                    <% }%>

                </tbody>
            </table>
        </section>


        <footer class="footer">
            <p>&copy; 2025 Labur-Ga. Todos los derechos reservados.</p>
        </footer>


        <% if ("exitoso".equals(cobro)) { %>

        <script>
            alert("Pedido cerrado con éxito");
        </script>

        <% }%>

    </body>
</html>