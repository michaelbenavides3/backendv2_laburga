<%@page import="com.modelo.Usuario"%>
<%@page import="java.util.List"%>
<%@page import="com.modelo.Productos"%>
<%@page import="com.dao.ProductoImagenDao"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    Usuario usuarioSesion
            = (Usuario) session.getAttribute("usuarioLogeadoObjeto");

    List<Productos> listaProductos
            = (List<Productos>) request.getAttribute("listaProductos");
%>

<!DOCTYPE html>

<html>

    <head>

        <meta charset="UTF-8">

        <title>Gestión Productos</title>

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/css/variables.css">

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/css/style.css">

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/css/a-listar-productos.css">

    </head>

    <body>

        <!-- ENCABEZADO -->

        <header class="encabezado">

            <p>
                Administración de Productos
            </p>

            <p>

                Administrador:

                <strong>
                    <%= usuarioSesion.getNombreCompleto()%>
                </strong>

            </p>

        </header>

        <!-- CONTENIDO -->

        <main class="contenedor-principal">

            <section class="contenedor-productos">

                <h2>
                    Lista de Productos
                </h2>

                <a href="a-registrar-productos.jsp" class="btn-nuevo-producto">

                    Nuevo Producto

                </a>

                <table class="tabla-productos">

                    <thead>

                        <tr>

                            <th>ID</th>

                            <th>Imagen</th>

                            <th>Nombre</th>

                            <th>Categoría</th>

                            <th>Precio</th>

                            <th>Disponible</th>

                            <th>Acciones</th>

                        </tr>

                    </thead>

                    <tbody>

                        <%

                            if (listaProductos != null) {

                                ProductoImagenDao productoImagenDao
                                        = new ProductoImagenDao();

                                for (Productos productoActual : listaProductos) {

                                    String rutaImagen
                                            = productoImagenDao.obtenerRutaImagenProducto(
                                                    productoActual.getIdProducto());

                                    if (rutaImagen == null
                                            || rutaImagen.isEmpty()) {

                                        rutaImagen
                                                = "img/productos/sin-imagen.png";
                                    }
                        %>

                        <tr>

                            <td>

                                <%= productoActual.getIdProducto()%>

                            </td>

                            <td>

                                <img src="${pageContext.request.contextPath}/<%= rutaImagen%>"
                                     width="80"
                                     height="80"
                                     alt="Imagen producto">

                            </td>

                            <td>

                                <%= productoActual.getNombreProducto()%>

                            </td>

                            <td>

                                <%= productoActual.getCategoriaProducto()%>

                            </td>

                            <td>

                                $ <%= productoActual.getPrecioBaseProducto()%>

                            </td>

                            <td>

                                <%

                                    if (productoActual.isDisponibleProducto()) {
                                %>

                                Disponible

                                <%
                                } else {
                                %>

                                No Disponible

                                <%
                                    }
                                %>

                            </td>

                            <td>

                                <a href="../EditarProductoControlador?idProducto=<%= productoActual.getIdProducto()%>"
                                   class="btn-editar">

                                    Editar

                                </a>

                                <a href="../CambiarDisponibilidadProductoControlador?idProducto=<%= productoActual.getIdProducto()%>"
                                   class="btn-estado">

                                    <%

                                        if (productoActual.isDisponibleProducto()) {
                                    %>

                                    Desactivar

                                    <%
                                    } else {
                                    %>

                                    Activar

                                    <%
                                        }
                                    %>

                                </a>

                            </td>

                        </tr>

                        <%
                                }
                            }
                        %>

                    </tbody>

                </table>

            </section>

            <a href="a-panel-principal-admin.jsp"
               class="btn-regresar">

                Regresar

            </a>

        </main>

        <!-- FOOTER -->

        <footer class="footer">

            <p>
                &copy; 2025 Labur-Ga. Todos los derechos reservados.
            </p>

        </footer>

    </body>

</html>