<%@ page 
    import="com.dao.PedidoDao, 
    com.modelo.Pedido, 
    java.util.List" %>

<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Labur-Ga | Panel Cajero</title>
  <link rel="stylesheet" href="../css/cajero.css">
  <link rel="stylesheet" href="../css/variables.css">
</head>
<body class="cajero">

  <header class="encabezado">
    <h1>Panel del Cajero</h1>
    <p>Gestiona órdenes y facturas</p>
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
            
            for(Pedido p : lista) {
        %>
        <tr>
          <td>Mesa <%= p.getIdMesa() %></td>
          <td><%= p.getDetalle() %></td>
          <td>$<%= p.getTotal() %></td>
          <td>
              <a href="ticket.jsp?idPedido=<%= p.getIdPedido() %>" class="btn btn-verde">Ver Ticket</a>
            <a href="../FinalizarCobro?idPedido=<%= p.getIdPedido() %>&idMesa=<%= p.getIdMesa() %>" class="btn btn-naranja">Cerrar Cuenta</a>
          </td>
        </tr>
        <% } %>
      </tbody>
    </table>
  </section>

  <footer class="footer">
    <p>&copy; 2025 Labur-Ga. Todos los derechos reservados.</p>
  </footer>

</body>
</html>