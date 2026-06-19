<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.dao.ReservaDao"%>
<%@page import="com.modelo.Reserva"%>

<%
    ReservaDao reservaDao = new ReservaDao();

    List<Reserva> listaReservas
            = reservaDao.obtenerListaReservas();
%>

<!DOCTYPE html>
<html lang="es">

    <head>

        <meta charset="UTF-8">

        <title>Listado Reservas</title>

        <link rel="stylesheet" href="../css/style.css">
        <link rel="stylesheet" href="../css/variables.css">
        <link rel="stylesheet" href="../css/listado-reservas.css">

    </head>

    <body>

        <header class="encabezado">

            <h1>Panel Mesero - Reservas</h1>

        </header>

        <main class="contenedor-principal">



            <section class="contenido">

                <h2>Listado de Reservas</h2>

                <table>

                    <thead>

                        <tr>

                            <th>ID</th>

                            <th>Mesa</th>

                            <th>Cliente</th>

                            <th>Fecha</th>

                            <th>Hora</th>

                            <th>Personas</th>

                            <th>Estado</th>

                            <th>Acciones</th>

                        </tr>

                    </thead>

                    <tbody>

                    <p>Total reservas encontradas:
                        <%= listaReservas.size()%>
                    </p>

                    <% for (Reserva reserva : listaReservas) {%>

                    <tr>

                        <td>
                            <%= reserva.getIdReserva()%>
                        </td>

                        <td>
                            Mesa <%= reserva.getNumeroMesa()%>
                        </td>

                        <td>
                            <%= reserva.getNombreCliente()%>
                        </td>

                        <td>
                            <%= reserva.getFechaReserva()%>
                        </td>

                        <td>
                            <%= reserva.getHoraReserva()%>
                        </td>

                        <td>
                            <%= reserva.getPersonasReserva()%>
                        </td>

                        <td>
                            <%= reserva.getEstadoReserva()%>
                        </td>

                        <td>

                            <% if (reserva.getEstadoReserva().equals("reservada")) {%>

                            <a href="../finalizarReserva?idReserva=<%= reserva.getIdReserva()%>"class="btn-finalizar">Finalizar</a>

                            <% } %>

                        </td>

                    </tr>

                    <% }%>

                    </tbody>

                </table>
                    
                    
                    
                    <button type="button" class="btn--cancelar btn-regresar" onclick="window.location.href = 'm-meserocopy.jsp'">Regresar</button>

            </section>

        </main>

        <footer class="footer">

            <p>
                &copy; 2025 Labur-Ga. Todos los derechos reservados.
            </p>

        </footer>

    </body>

</html>