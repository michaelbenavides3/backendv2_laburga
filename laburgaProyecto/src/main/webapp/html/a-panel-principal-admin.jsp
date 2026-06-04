<%@page contentType="text/html" pageEncoding="UTF-8"%>    


<%@page import="com.modelo.Usuario"%>


<%
    String exito = request.getParameter("exito");
%>
<%
    Usuario usuarioSesion = (Usuario) session.getAttribute("usuarioLogeadoObjeto");
%>
<!DOCTYPE html>
<html lang="es">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Panel Administrador</title>
        <link rel="stylesheet" href="../css/panel-administrador.css">
    </head>

    <body>
        <!-- Header -->
        <header class="header">
            <h1>Gestionn del Sistema - Panel Administrador</h1>
            <p>
                Administrador:
                <strong><%= usuarioSesion.getNombreCompleto()%></strong>
            </p>
        </header>

        <!-- Layout principal -->
        <div class="layout">
            <!-- Sidebar -->
            <aside class="sidebar">
                <h2>Opciones Administrador</h2>
                <nav>
                    <a href="a-nuevoTrabajado.jsp" class="btn">Nuevo Usuario</a>
                    <!-- <a href="a-actualizar-menu.html" class="btn">Asignar Rol</a> -->
                    <!-- <a href="../ListarUsuariosControlador" class="btn">Desactivar Usuario</a> -->
                    <a href="${pageContext.request.contextPath}/ListarUsuariosControlador" class="btn">Desactivar Usuario</a>
                    <!-- <a href="a-listar-usuarios.jsp" class="btn">Desactivar Usuario</a> -->
                    <!-- <a href="a-gestionar-permisos.html" class="btn">Permisos</a> -->
                    <!-- <a href="a-restablecer-pwss.html" class="btn">Restablecer ContraseÃ±a</a> -->
                    <!-- <a href="a-actualizar-menu.html" class="btn">Actualizar MenÃº</a> -->
                    <a href="../cerrarSesion" class="btn">Cerrar Sesion</a>
                </nav>
            </aside>

            <!-- Contenido principal -->
            <main class="contenido">
                <div class="logo-container">
                    <img src="../recurso/logo-burguer.png" alt="Logo HamburgueserÃ­a">
                </div>
            </main>
        </div>

        <!-- Footer -->
        <footer class="footer">
            <p>&copy; 2025 Labur-Ga. Todos los derechos reservados.</p>
        </footer>



        <% if ("1".equals(exito)) { %>

        <script>
            alert("Usuario creado con éxito");
        </script>

        <% }%>
    </body>

</html>