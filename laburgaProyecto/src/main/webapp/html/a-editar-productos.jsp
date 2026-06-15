<%@page import="com.modelo.Productos"%>
<%@page import="com.modelo.Usuario"%>

<%
    Usuario usuarioSesion
            = (Usuario) session.getAttribute(
                    "usuarioLogeadoObjeto");

    Productos productoSeleccionado
            = (Productos) request.getAttribute(
                    "productoSeleccionado");
%>

<%@page contentType="text/html"
        pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>

    <head>

        <meta charset="UTF-8">

        <title>Editar Producto</title>

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/css/variables.css">

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/css/style.css">

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/css/a-editar-producto.css">

    </head>

    <body>

        <!-- HEADER -->

        <header class="encabezado">

            <p>Administración de Productos</p>

            <p>
                Administrador:
                <strong>
                    <%= usuarioSesion.getNombreCompleto()%>
                </strong>
            </p>

        </header>

        <!-- MAIN -->

        <main class="contenedor-principal">

            <section class="contenedor-formulario">

                <h2>
                    Editar Producto
                </h2>

                <form action="../EditarProductoControlador"
                      method="post"
                      class="formulario-producto">

                    <input type="hidden"
                           name="accion"
                           value="guardar">

                    <input type="hidden"
                           name="idProducto"
                           value="<%= productoSeleccionado.getIdProducto()%>">

                    <div class="grupo-campo">

                        <label>
                            Nombre Producto
                        </label>

                        <input type="text"
                               name="nombreProducto"
                               value="<%= productoSeleccionado.getNombreProducto()%>"
                               required>

                    </div>

                    <div class="grupo-campo">

                        <label>
                            Descripción
                        </label>

                        <textarea name="descripcionProducto"
                                  rows="4"><%= productoSeleccionado.getDescripcionProducto()%></textarea>

                    </div>

                    <div class="grupo-campo">

                        <label>
                            Precio Base
                        </label>

                        <input type="number"
                               step="0.01"
                               min="1"
                               name="precioProducto"
                               value="<%= productoSeleccionado.getPrecioBaseProducto()%>"
                               required>

                    </div>

                    <div class="grupo-campo">

                        <label>
                            Categoría
                        </label>

                        <input type="text"
                               name="categoriaProducto"
                               value="<%= productoSeleccionado.getCategoriaProducto()%>"
                               required>

                    </div>

                    <div class="grupo-campo">

                        <label>
                            Disponible
                        </label>

                        <input type="text"
                               value="<%= productoSeleccionado.isDisponibleProducto() ? "SI" : "NO"%>"
                               readonly>

                    </div>

                    <div class="contenedor-botones">

                        <button type="submit"
                                class="btn-guardar">

                            Guardar Cambios

                        </button>

                        <a href="a-listar-productos.jsp"
                           class="btn-cancelar">

                            Cancelar

                        </a>

                    </div>

                </form>

            </section

            <a href="html/a-panel-principal-admin.jsp" class="btn-regresar">Regresar</a>

        </main>

        <!-- FOOTER -->

        <footer class="footer">
            <p>&copy; 2025 Labur-Ga. Todos los derechos reservados.</p>
        </footer>

    </body>

</html>