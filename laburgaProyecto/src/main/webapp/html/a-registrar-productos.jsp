<%@page import="com.modelo.Usuario"%>





<%
    Usuario usuarioSesion
            = (Usuario) session.getAttribute("usuarioLogeadoObjeto");
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>

        <meta charset="UTF-8">

        <title>Registrar Producto</title>

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/css/variables.css">

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/css/style.css">

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/css/a-registrar-productos.css">

    </head>

    <body>

        <!-- HEADER -->

        <header class="encabezado">

            <p>Administración de productos</p>

            <p>
                Administrador:
                <strong>
                    <%= usuarioSesion.getNombreCompleto()%>
                </strong>
            </p>

        </header>

        <!-- MAIN -->

        <main class="contenedor-principal">

            <section class="contenedor-formulario formulario-producto">

                <h2>Registrar Nuevo Producto</h2>


                <form action="../RegistrarProductoControlador"method="post" enctype="multipart/form-data">

                    <div class="grupo-campo">

                        <label>Nombre Producto</label>

                        <input type="text" name="txtNombreProducto" required>

                    </div>

                    <div class="grupo-campo">

                        <label>Descripción</label>

                        <textarea name="txtDescripcionProducto"rows="4"></textarea>

                    </div>

                    <div class="grupo-campo">

                        <label>Precio</label>

                        <input type="number"step="0.01" min="1" name="txtPrecioProducto"required>

                    </div>

                    <div class="grupo-campo">

                        <label>Categoría</label>

                        <input type="text"  name="txtCategoriaProducto" required>

                    </div>

                    <div class="grupo-campo">

                        <label>
                            Imagen Producto
                        </label>

                        <input type="file" name="imagenProducto"accept=".jpg,.jpeg,.png, image/*" required>

                    </div>

                    <div class="contenedor-botones">

                        <button type="submit" class="btn-guardar"> Registrar Producto</button>
                        <a href="${pageContext.request.contextPath}/ProductosControlador?accion=listar" class="btn-cancelar">Cancelar</a>

                    </div>

                </form>

            </section>


            <a href="${pageContext.request.contextPath}/ProductosControlador?accion=listar" class="btn-regresar">Regresar</a>

        </main>

        <!-- FOOTER -->

        <footer class="footer">
            <p>&copy; 2025 Labur-Ga. Todos los derechos reservados.</p>
        </footer>
        
        
                    <%
                String error = request.getParameter("error");
            %>

            <% if ("nombre".equals(error)) { %>
                <script>alert("El nombre solo puede contener letras, sin números.");</script>
            <% } else if ("nombreCorto".equals(error)) { %>
                <script>alert("El nombre debe tener al menos 3 caracteres.");</script>
            <% } else if ("descripcion".equals(error)) { %>
                <script>alert("La descripción debe contener al menos una letra.");</script>
            <% } else if ("categoria".equals(error)) { %>
                <script>alert("La categoría solo puede contener letras, sin números.");</script>
            <% } else if ("imagen".equals(error)) { %>
                <script>alert("Debe seleccionar una imagen para el producto.");</script>
            <% } else if ("registro".equals(error)) { %>
                <script>alert("Error al guardar el producto. Intente de nuevo.");</script>
            <% } %>

    </body>
</html>