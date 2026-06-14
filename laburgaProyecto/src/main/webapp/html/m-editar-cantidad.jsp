<%@page import="com.modelo.DetallePedido"%>
<%@page import="com.modelo.Usuario"%>

<%
    Usuario usuarioSesion = (Usuario) session.getAttribute(
                    "usuarioLogeadoObjeto");

    DetallePedido detallePedidoSeleccionado = (DetallePedido) request.getAttribute( "detallePedidoSeleccionado");
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>

        <meta charset="UTF-8">

        <title>Editar Pedido</title>

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/css/variables.css">

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/css/style.css">

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/css/m-editar-cantidad.css">

    </head>

    <body>

       
        <!-- ENCABEZADO -->
        

        <header class="encabezado">

            <p>Gestión de mesas y pedidos</p>

            <p>
                Mesero:
                <strong>
                    <%= usuarioSesion.getNombreCompleto()%>
                </strong>
            </p>

        </header>

       
        <!-- CONTENIDO PRINCIPAL -->
      

        <main class="contenedor-principal contenedor-editar-cantidad">

            <section class="contenedor-formulario tarjeta-editar">

                <h2>
                    Editar Producto del Pedido
                </h2>

                <form action="${pageContext.request.contextPath}/ActualizarDetallePedidoControlador" method="post" class="formulario-editar">

                    <!-- id oculto -->

                    <input type="hidden" name="idDetalle" value="<%= detallePedidoSeleccionado.getIdDetalle()%>">

                    <!-- producto -->

                    <div class="grupo-campo">

                        <label>
                            Producto
                        </label>

                        <input type="text" value="<%= detallePedidoSeleccionado.getNombreProducto()%>"  readonly>

                    </div>

                    <!-- precio -->

                    <div class="grupo-campo">

                        <label>
                            Precio Unitario
                        </label>

                        <input type="text" value="$ <%= detallePedidoSeleccionado.getPrecioVenta()%>" readonly>

                    </div>

                    <!-- cantidad -->

                    <div class="grupo-campo">

                        <label>
                            Cantidad
                        </label>

                        <input type="number" name="cantidadNueva" min="1" value="<%= detallePedidoSeleccionado.getCantidad()%>" required>

                    </div>

                    <!-- observaciones -->

                    <div class="grupo-campo">

                        <label>
                            Observaciones
                        </label>

                        <textarea readonly><%= detallePedidoSeleccionado.getObservaciones()%></textarea>

                    </div>

                    <!-- botones -->

                    <div class="contenedor-botones">

                        <button type="submit" class="btn-guardar">
                            Guardar Cambios
                        </button>

                        <a href="javascript:history.back()"  class="btn-cancelar">
                            Cancelar
                        </a>

                    </div>

                </form>

            </section>

        </main>

        
        <!-- FOOTER -->
        

       <footer class="footer">
            <p>&copy; 2025 Labur-Ga. Todos los derechos reservados.</p>
        </footer>

    </body>
</html>