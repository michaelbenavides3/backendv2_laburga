
<%@ page import="com.dao.MesaDao" %>
<%@ page import="com.modelo.Mesa" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.modelo.Usuario"%>

<%
    String estado = request.getParameter("estado");
%>
<%
    String pedido = request.getParameter("pedido");
%>
<%
    Usuario usuarioSesion = (Usuario) session.getAttribute("usuarioLogeadoObjeto");
%>
<%
    String mensaje = request.getParameter("mensaje");
%>

<!DOCTYPE html>
<html lang="es">

    <head>
        <meta charset="UTF-8">
        <title>Labur-Ga | Mesero</title>
        <link rel="stylesheet" href="../css/style.css">
        <link rel="stylesheet" href="../css/mesero copy.css">
        <link rel="stylesheet" href="../css/variables.css">
    </head>

    <body class="mesero">

        <!-- Encabezado -->
        <header class="encabezado">
            <!-- <h1>Panel del Mesero</h1> -->
            <p>Gestion de mesas y pedidos</p>
            <p>
                Mesero:
                <strong><%= usuarioSesion.getNombreCompleto()%></strong>
            </p>
        </header>

        <div class="layout">
            <!-- Sidebar -->
            <aside class="sidebar">
                <h2>Panel Menu Mesero</h2>
                <nav>
                    <img src="../recurso/logo-burguer.png" alt="imagen-logo-lagurga">
                    <!-- <a href="m-mesas.jsp" class="btn">Mesas</a> -->                    
                    <!-- <a href="separar-mesas.jsp" class="btn">Enviar cuenta</a> -->
                    <!-- <a href="verificarcuenta.jsp" class="btn">Verificar cuenta</a> -->                   
                    <a href="m-listar-reservas.jsp" class="btn">Ver reservas</a>
                    <a href="m-formulario-reserva.jsp" class="btn">Realizar reserva</a>
                    <a href="m-formulario-clientenuevo-mesero.jsp" class="btn">Nuevo cliente</a>
                    <a href="../cerrarSesion" class="btn">Cerrar Sesión</a>
                </nav>
            </aside>

            <!-- Contenido principal -->
            <main class="contenido">
                <!-- Contenido principal -->

                <!-- CuadrÃ­cula de mesas -->
                <section class="mesas">

                    <%
                        //creamos un dao para saber el esatdo de la mesa
                        com.dao.MesaDao dao = new com.dao.MesaDao();
                        //buscamos la mesa con el id1
                        String estadoMesa1 = dao.listarMesas().get(0).getEstadoMesa();
                    %>
                    <div class="mesa <%= estadoMesa1%>">
                        <h3>Mesa 1</h3>
                        <p class="estado--mesa">Estado: <%= estadoMesa1.toUpperCase()%> </p>
                        <%if (estadoMesa1.equals("ocupada")) { %>
                        <a href="../CambiarEstadoMesa?idMesa=1&estado=pendiente_cobro" class=" btn-amarillo pendiente_cobro ">Solicitar Cuenta</a>

                        <%} else if (estadoMesa1.equals("pendiente_cobro")) { %>
                        <span class="etiqueta-amarilla">Esperando en Caja....</span>

                        <% } else { %>
                       <!-- <a href="m-registrar-pedido.jsp?idMesa=1" class="btn btn--registrar-pedido btn-naranja">Registrar pedido</a>-->
                        <a href="../MenuMeseroControlador?idMesa=1" class="btn btn--registrar-pedido btn-naranja">Registrar pedido</a>
                        
                        <% } %>
                        <% if (estadoMesa1.equals("ocupada")) { %>
                        <a href="../AgregarProductoMeseroControlador?idMesa=1" ...>Agregar producto</a>
                        <a href="m-agregar-producto.jsp?idMesa=1" class="btn btn--agregar-producto btn-verde">Agregar producto</a>
                        <a href="m-editar-pedido.jsp?idMesa=1" class="btn btn--agregar-producto btn-verde">Editar Pedido</a>
                        <% } %>    
                    </div>
                    <%
                        //creamos un dao para saber el esatdo de la mesa
                        //com.dao.MesaDao dao = new com.dao.MesaDao();
                        //buscamos la mesa con el id1
                        String estadoMesa2 = dao.listarMesas().get(1).getEstadoMesa();
                    %>
                    <div class="mesa <%= estadoMesa2%>">
                        <h3>Mesa 2</h3>
                        <p class="estado--mesa">Estado: <%= estadoMesa2.toUpperCase()%> </p>
                        <%if (estadoMesa2.equals("ocupada")) { %>
                        <a href="../CambiarEstadoMesa?idMesa=2&estado=pendiente_cobro" class=" btn-amarillo pendiente_cobro">Solicitar Cuenta</a>

                        <%} else if (estadoMesa2.equals("pendiente_cobro")) { %>
                        <span class="etiqueta-amarilla">Esperando en Caja....</span>

                        <% } else { %>
                        <a href="../MenuMeseroControlador?idMesa=2" class="btn btn--registrar-pedido btn-naranja">Registrar pedido</a>
                        <% } %>
                        <% if (estadoMesa2.equals("ocupada")) { %>
                        <a href="m-agregar-producto.jsp?idMesa=2" class="btn btn--agregar-producto btn-verde">Agregar producto</a>
                        <a href="m-editar-pedido.jsp?idMesa=2" class="btn btn--agregar-producto btn-verde">Editar Pedido</a>
                        <% } %>
                    </div>
                    <%
                        //creamos un dao para saber el esatdo de la mesa
                        //com.dao.MesaDao dao = new com.dao.MesaDao();
                        //buscamos la mesa con el id1
                        String estadoMesa3 = dao.listarMesas().get(2).getEstadoMesa();
                    %>
                    <div class="mesa <%= estadoMesa3%>">
                        <h3>Mesa 3</h3>
                        <p class="estado--mesa">Estado: <%= estadoMesa3.toUpperCase()%> </p>
                        <%if (estadoMesa3.equals("ocupada")) { %>
                        <a href="../CambiarEstadoMesa?idMesa=3&estado=pendiente_cobro" class=" btn-amarillo pendiente_cobro">Solicitar Cuenta</a>

                        <%} else if (estadoMesa3.equals("pendiente_cobro")) { %>
                        <span class="etiqueta-amarilla">Esperando en Caja....</span>

                        <% } else { %>
                        <a href="../MenuMeseroControlador?idMesa=3" class="btn btn--registrar-pedido btn-naranja">Registrar pedido</a>
                        <% } %>
                        <% if (estadoMesa3.equals("ocupada")) { %>
                        <a href="m-agregar-producto.jsp?idMesa=3" class="btn btn--agregar-producto btn-verde">Agregar producto</a>
                        <a href="m-editar-pedido.jsp?idMesa=3" class="btn btn--agregar-producto btn-verde">Editar Pedido</a>
                        <% } %>
                    </div>
                    <%
                        //creamos un dao para saber el esatdo de la mesa
                        //com.dao.MesaDao dao = new com.dao.MesaDao();
                        //buscamos la mesa con el id1
                        String estadoMesa4 = dao.listarMesas().get(3).getEstadoMesa();
                    %>
                    <div class="mesa <%= estadoMesa4%>">
                        <h3>Mesa 4</h3>
                        <p class="estado--mesa">Estado: <%= estadoMesa4.toUpperCase()%> </p>
                        <%if (estadoMesa4.equals("ocupada")) { %>
                        <a href="../CambiarEstadoMesa?idMesa=4&estado=pendiente_cobro" class=" btn-amarillo pendiente_cobro">Solicitar Cuenta</a>

                        <%} else if (estadoMesa4.equals("pendiente_cobro")) { %>
                        <span class="etiqueta-amarilla">Esperando en Caja....</span>

                        <% } else { %>
                        <a href="../MenuMeseroControlador?idMesa=4" class="btn btn--registrar-pedido btn-naranja">Registrar pedido</a>
                        <% } %>
                        <% if (estadoMesa4.equals("ocupada")) { %>
                        <a href="m-agregar-producto.jsp?idMesa=4" class="btn btn--agregar-producto btn-verde">Agregar producto</a>
                        <a href="m-editar-pedido.jsp?idMesa=4" class="btn btn--agregar-producto btn-verde">Editar Pedido</a>
                        <% } %>
                    </div>
                    <%
                        //creamos un dao para saber el esatdo de la mesa
                        //com.dao.MesaDao dao = new com.dao.MesaDao();
                        //buscamos la mesa con el id1
                        String estadoMesa5 = dao.listarMesas().get(4).getEstadoMesa();
                    %>
                    <div class="mesa <%= estadoMesa5%>">
                        <h3>Mesa 5</h3>
                        <p class="estado--mesa">Estado: <%= estadoMesa5.toUpperCase()%> </p>
                        <%if (estadoMesa5.equals("ocupada")) { %>
                        <a href="../CambiarEstadoMesa?idMesa=5&estado=pendiente_cobro" class=" btn-amarillo pendiente_cobro">Solicitar Cuenta</a>

                        <%} else if (estadoMesa5.equals("pendiente_cobro")) { %>
                        <span class="etiqueta-amarilla">Esperando en Caja....</span>

                        <% } else { %>
                        <a href="../MenuMeseroControlador?idMesa=5" class="btn btn--registrar-pedido btn-naranja">Registrar pedido</a>
                        <% } %>
                        <% if (estadoMesa5.equals("ocupada")) { %>
                        <a href="m-agregar-producto.jsp?idMesa=5" class="btn btn--agregar-producto btn-verde">Agregar producto</a>
                        <a href="m-editar-pedido.jsp?idMesa=5" class="btn btn--agregar-producto btn-verde">Editar Pedido</a>
                        <% } %>
                    </div>
                    <%
                        //creamos un dao para saber el esatdo de la mesa
                        //com.dao.MesaDao dao = new com.dao.MesaDao();
                        //buscamos la mesa con el id1
                        String estadoMesa6 = dao.listarMesas().get(5).getEstadoMesa();
                    %>
                    <div class="mesa <%= estadoMesa6%>">
                        <h3>Mesa 6</h3>
                        <p class="estado--mesa">Estado: <%= estadoMesa6.toUpperCase()%> </p>
                        <%if (estadoMesa6.equals("ocupada")) { %>
                        <a href="../CambiarEstadoMesa?idMesa=6&estado=pendiente_cobro" class=" btn-amarillo pendiente_cobro">Solicitar Cuenta</a>

                        <%} else if (estadoMesa6.equals("pendiente_cobro")) { %>
                        <span class="etiqueta-amarilla">Esperando en Caja....</span>

                        <% } else { %>
                        <a href="../MenuMeseroControlador?idMesa=6" class="btn btn--registrar-pedido btn-naranja">Registrar pedido</a>
                        <% } %>
                        <% if (estadoMesa6.equals("ocupada")) { %>
                        <a href="m-agregar-producto.jsp?idMesa=6" class="btn btn--agregar-producto btn-verde">Agregar producto</a>
                        <a href="m-editar-pedido.jsp?idMesa=6" class="btn btn--agregar-producto btn-verde">Editar Pedido</a>
                        <% } %>
                    </div>
                    <%
                        //creamos un dao para saber el esatdo de la mesa
                        //com.dao.MesaDao dao = new com.dao.MesaDao();
                        //buscamos la mesa con el id1
                        String estadoMesa7 = dao.listarMesas().get(6).getEstadoMesa();
                    %>
                    <div class="mesa <%= estadoMesa7%>">
                        <h3>Mesa 7</h3>
                        <p class="estado--mesa">Estado: <%= estadoMesa7.toUpperCase()%> </p>
                        <%if (estadoMesa7.equals("ocupada")) { %>
                        <a href="../CambiarEstadoMesa?idMesa=7&estado=pendiente_cobro" class=" btn-amarillo pendiente_cobro">Solicitar Cuenta</a>

                        <%} else if (estadoMesa7.equals("pendiente_cobro")) { %>
                        <span class="etiqueta-amarilla">Esperando en Caja....</span>

                        <% } else { %>
                        <a href="../MenuMeseroControlador?idMesa=7" class="btn btn--registrar-pedido btn-naranja">Registrar pedido</a>
                        <% } %>
                        <% if (estadoMesa7.equals("ocupada")) { %>
                        <a href="m-agregar-producto.jsp?idMesa=7" class="btn btn--agregar-producto btn-verde">Agregar producto</a>
                        <a href="m-editar-pedido.jsp?idMesa=7" class="btn btn--agregar-producto btn-verde">Editar Pedido</a>
                        <% } %>
                    </div>
                    <%
                        //creamos un dao para saber el esatdo de la mesa
                        //com.dao.MesaDao dao = new com.dao.MesaDao();
                        //buscamos la mesa con el id1
                        String estadoMesa8 = dao.listarMesas().get(7).getEstadoMesa();
                    %>
                    <div class="mesa <%= estadoMesa8%>">
                        <h3>Mesa 8</h3>
                        <p class="estado--mesa">Estado: <%= estadoMesa8.toUpperCase()%> </p>
                        <%if (estadoMesa8.equals("ocupada")) { %>
                        <a href="../CambiarEstadoMesa?idMesa=8&estado=pendiente_cobro" class=" btn-amarillo pendiente_cobro">Solicitar Cuenta</a>

                        <%} else if (estadoMesa8.equals("pendiente_cobro")) { %>
                        <span class="etiqueta-amarilla">Esperando en Caja....</span>

                        <% } else { %>
                        <a href="../MenuMeseroControlador?idMesa=8" class="btn btn--registrar-pedido btn-naranja">Registrar pedido</a>
                        <% } %>
                        <% if (estadoMesa8.equals("ocupada")) { %>
                        <a href="m-agregar-producto.jsp?idMesa=8" class="btn btn--agregar-producto btn-verde">Agregar producto</a>
                        <a href="m-editar-pedido.jsp?idMesa=8" class="btn btn--agregar-producto btn-verde">Editar Pedido</a>
                        <% } %>
                    </div>
                </section>         
            </main>
        </div>
        <!-- Pie de pÃ¡gina -->
        <footer class="footer">
            <p>&copy; 2025 Labur-Ga. Todos los derechos reservados.</p>
        </footer>

        <% if ("exitoso".equals(estado)) {%>

        <script>
            alert("Cliente registrado con éxito");
        </script>

        <% }%>

        <% if ("exitoso".equals(pedido)) { %>

        <script>
            alert("Pedido creado exitosamente");
        </script>

        <% }%>

        <script>
            <%
                // Obtenemos el parámetro enviado por el controlador
                String exito = request.getParameter("exito");
                if ("true".equals(exito)) {
            %>
            alert("¡La reserva se ha realizado con éxito!");
            <%
                }
            %>
        </script>
        <%
            String pedidoCerrado = request.getParameter("pedidoCerrado");
        %>

        <% if ("true".equals(pedidoCerrado)) { %>

        <script>
            alert("Pedido cerrado correctamente. La mesa quedó disponible nuevamente.");
        </script>

        <% }%>
        
          <%
            String finalizado = request.getParameter("finalizado");
        %>

        <% if ("true".equals(finalizado)) { %>

        <script>
            alert("Reserva finalizada correctamente.");
        </script>

        <% }%>

    </body>

</html>