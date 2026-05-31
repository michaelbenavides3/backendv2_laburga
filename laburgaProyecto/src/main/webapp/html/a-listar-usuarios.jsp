<%@page import="java.util.List"%>
<%@page import="com.modelo.Usuario"%>



<%
    List<Usuario> listaUsuarios
            = (List<Usuario>) request.getAttribute("listaUsuarios");

    System.out.println("DEBUG JSP -> " + listaUsuarios);
%>

<!DOCTYPE html>
<html lang="es">

    <head>

        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/css/variables.css">

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/css/style.css">

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/css/a-listar-usuarios.css">

        <title>Listado de Usuarios</title>

    </head>

    <body>

        <!-- 
             ENCABEZADO DEL MODULO
         -->

        <header class="header encabezado">
            <h2>Listado General de Usuarios</h2>
        </header>

        <!-- 
             CONTENIDO PRINCIPAL
         -->

        <main class="contenedor-principal">

            <div class="contenedor-tabla">

                <h3>Total usuarios encontrados:
                    <%= listaUsuarios.size()%>
                </h3>

                <table class="tabla-usuarios">

                    <thead>

                        <tr>
                            <th>ID</th>
                            <th>Nombre Completo</th>
                            <th>Usuario</th>
                            <th>Rol</th>
                            <th>Estado</th>
                            <th>Activar/Desactivar</th>
                        </tr>

                    </thead>

                    <tbody>

                        <%
                            for (Usuario usuarioActual : listaUsuarios) {
                        %>

                        <tr>

                            <td><%= usuarioActual.getIdUsuario()%></td>

                            <td><%= usuarioActual.getNombreCompleto()%></td>

                            <td><%= usuarioActual.getNombreUsuario()%></td>

                            <td><%= usuarioActual.getNombreRol()%></td>

                            <td>

                                <%
                                    if (usuarioActual.getEstadoUsuario().equalsIgnoreCase("activo")) {
                                %>

                                <span class="estado-activo">
                                   Activo
                                </span>

                                <%
                                } else {
                                %>

                                <span class="estado-inactivo">
                                 
                                    Desactivado
                                </span>

                                <%
                                    }
                                %>

                            </td>

                            <td>

                                <%
                                    if (usuarioActual.getEstadoUsuario().equalsIgnoreCase("activo")) {
                                %>

                               <!-- <a href="../CambiarEstadoUsuarioControlador?idUsuario=<%= usuarioActual.getIdUsuario()%>&estado=inactivo"
                                   class="btn-desactivar">
                                    Desactivar
                                </a>-->
                                <a href="${pageContext.request.contextPath}/CambiarEstadoUsuarioControlador?idUsuario=<%= usuarioActual.getIdUsuario()%>&estado=desactivo" class="btn--accion btn-desactivar ">
                                    Desactivar
                                </a>
                                <!-- <a href="../CambiarEstadoUsuarioControlador?idUsuario=<%= usuarioActual.getIdUsuario()%>&estado=inactivo">
                                    Desactivar
                                </a>-->

                                <%
                                } else {
                                %>

                                <a href="${pageContext.request.contextPath}/CambiarEstadoUsuarioControlador?idUsuario=<%= usuarioActual.getIdUsuario()%>&estado=activo" class="btn--accion btn-activar">
                                    Activar
                                </a>

                                <%
                                    }
                                %>

                            </td>

                        </tr>

                        <%
                            }
                        %>

                    </tbody>    

                </table>

            </div>

                       
                        <a href="html/a-panel-principal-admin.jsp" class="btn-regresar">Regresar</a>
                        
        </main>
                        

        <!-- 
             PIE DE PAGINA
         -->

        <footer class="footer">
            <p>&copy; 2025 Labur-Ga. Todos los derechos reservados.</p>
        </footer>

    </body>

</html>