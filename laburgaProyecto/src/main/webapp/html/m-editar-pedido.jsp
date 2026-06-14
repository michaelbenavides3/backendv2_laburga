<%@page import="java.util.List"%>
<%@page import="com.modelo.DetallePedido"%>
<%@page import="com.dao.DetallePedidoDao"%>
<%@page import="com.modelo.Usuario"%>

<%
    Usuario usuarioSesion
            = (Usuario) session.getAttribute("usuarioLogeadoObjeto");
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>

    <head>


        <meta http-equiv="Content-Type"
              content="text/html; charset=UTF-8">

        <title>Editar Pedido</title>

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/css/variables.css">

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/css/style.css">

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/css/m-editar-pedido.css">


    </head>

    <body>

        <header class="encabezado">


            <p>Gestión de mesas y pedidos</p>

            <p>
                Mesero:
                <strong>
                    <%= usuarioSesion.getNombreCompleto()%>
                </strong>
            </p>


        </header>

        <main class="contenedor-principal">


            <%

                String parametroIdMesa
                        = request.getParameter("idMesa");

                int identificadorMesa
                        = Integer.parseInt(parametroIdMesa);

                DetallePedidoDao detallePedidoDao
                        = new DetallePedidoDao();

                List<DetallePedido> listaDetallePedidosMesa
                        = detallePedidoDao.listarDetallesPorMesa(
                                identificadorMesa);

                double totalPedido = 0;

            %>

            <section class="contenedor-tabla">

                <h2>
                    Editar Pedido - Mesa
                    <%= identificadorMesa%>
                </h2>

                <table class="tabla-pedidos">

                    <thead>

                        <tr>

                            <th>Producto</th>

                            <th>Cantidad</th>

                            <th>Precio Unitario</th>

                            <th>Subtotal</th>

                            <th>Acciones</th>

                        </tr>

                    </thead>

                    <tbody>

                        <%

                            for (DetallePedido detallePedidoActual
                                    : listaDetallePedidosMesa) {

                                totalPedido
                                        += detallePedidoActual.getSubtotalLinea();

                        %>

                        <tr>

                            <td>
                                <%= detallePedidoActual.getNombreProducto()%>
                            </td>

                            <td>
                                <%= detallePedidoActual.getCantidad()%>
                            </td>

                            <td>
                                $ <%= detallePedidoActual.getPrecioVenta()%>
                            </td>

                            <td>
                                $ <%= detallePedidoActual.getSubtotalLinea()%>
                            </td>

                            <td>


                                <a href="../EditarDetallePedidoControlador?idDetalle=<%= detallePedidoActual.getIdDetalle()%>"
                                   class="btn-editar">
                                    Editar
                                </a>

                                <a href="../EliminarDetallePedidoControlador?idDetalle=<%= detallePedidoActual.getIdDetalle()%>&idPedido=<%= detallePedidoActual.getIdPedido()%>"class="btn-eliminar">
                                    Eliminar
                                </a>

                            </td>

                        </tr>

                        <%
                            }
                        %>

                    </tbody>

                    <tfoot>

                        <tr>

                            <td colspan="3">
                                <strong>Total Pedido</strong>
                            </td>

                            <td colspan="2">
                                <strong>
                                    $ <%= totalPedido%>
                                </strong>
                            </td>

                        </tr>

                    </tfoot>

                </table>

            </section>

            
            <!-- <a href="../html/m-meserocopy.jsp" class="btn-regresar">Regresar</a>-->
            <button type="button" class="btn--cancelar btn-regresar" onclick="window.location.href = 'm-meserocopy.jsp'">Regresar</button>


        </main>

        <footer class="footer">
            <p>&copy; 2025 Labur-Ga. Todos los derechos reservados.</p>
        </footer>

    </body>

</html>
