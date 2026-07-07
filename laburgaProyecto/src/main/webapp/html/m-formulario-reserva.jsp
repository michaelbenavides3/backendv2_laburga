<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.modelo.Cliente" %>

<%
    // recuperamos el cliente encontrado si viene del servlet de búsqueda
    Cliente clienteEncontrado = (Cliente) request.getAttribute("clienteEncontrado");

    // recuperamos los datos del formulario para no perderlos al buscar
    String telefonoBuscado = request.getParameter("telefonoBuscado") != null
            ? request.getParameter("telefonoBuscado")
            : (request.getAttribute("telefonoBuscado") != null
            ? (String) request.getAttribute("telefonoBuscado") : "");

    String nombreCliente = clienteEncontrado != null
            ? clienteEncontrado.getNombreCompleto() : "";

    String documentoCliente = "";
    if (clienteEncontrado != null && clienteEncontrado.getDocumentoIdentidad() != null) {
        documentoCliente = clienteEncontrado.getDocumentoIdentidad();
    }

    String mesaValor = request.getAttribute("mesa") != null ? (String) request.getAttribute("mesa") : "";
    String fechaValor = request.getAttribute("fecha") != null ? (String) request.getAttribute("fecha") : "";
    String horaValor = request.getAttribute("hora") != null ? (String) request.getAttribute("hora") : "";
    String personasValor = request.getAttribute("personas") != null ? (String) request.getAttribute("personas") : "";
    String ocasionValor = request.getAttribute("ocasion") != null ? (String) request.getAttribute("ocasion") : "";

    // Lógica para bloquear el campo si el cliente ya existe
    boolean tieneDocumento = clienteEncontrado != null && !documentoCliente.isEmpty();

    // ← NUEVA VARIABLE: Saber si el mesero ya presionó el botón buscar
    boolean seHaBuscado = !telefonoBuscado.isEmpty();


%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Formulario Reserva Mesa</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/formulario-reserva.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/variables.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <main>
            <header>
                <h1>Panel Mesero - Reserva Mesas</h1>
            </header>

            <section class="form-reserva">
                <h2>Realizar Reserva</h2>

                <%-- 
                     BLOQUE 1: BUSCAR CLIENTE POR TELÉFONO
                     Formulario separado que solo busca el cliente
                --%>
                <form action="${pageContext.request.contextPath}/BuscarClientePorTelefono" method="get">

                    <label for="telefono">Teléfono del cliente:</label>
                    <div class="campo-telfono">
                        <input type="text"
                               id="telefono"
                               name="telefono"
                               value="<%= telefonoBuscado%>"
                               maxlength="10"
                               placeholder="Ingrese el teléfono y busque"
                               required>

                        <button type="submit" class="btn--buscar--cliente">Buscar Cliente</button>
                    </div>

                    <%-- conservamos los datos ya ingresados para no perderlos al buscar --%>
                    <input type="hidden" name="mesa"     value="<%= mesaValor%>">
                    <input type="hidden" name="fecha"    value="<%= fechaValor%>">
                    <input type="hidden" name="hora"     value="<%= horaValor%>">
                    <input type="hidden" name="personas" value="<%= personasValor%>">
                    <input type="hidden" name="ocasion"  value="<%= ocasionValor%>">

                </form>

                <%-- Mensaje si el cliente fue encontrado --%>
                <% if (clienteEncontrado != null) {%>
                <p class="mensaje-exito">
                    Cliente encontrado: <strong><%= clienteEncontrado.getNombreCompleto()%></strong>
                </p>
                <% } else if (!telefonoBuscado.isEmpty()) { %>
                <p class="mensaje-advertencia">
                    Cliente no encontrado. Se creará automáticamente al confirmar la reserva.
                </p>
                <% }%>



                <%-- 
                     BLOQUE 2: FORMULARIO DE RESERVA
                --%>
                <form action="${pageContext.request.contextPath}/registrarReserva" method="post">

                    <label for="nombre">Nombre del cliente:</label>
                    <input type="text"
                           id="nombre"
                           name="nombre"
                           value="<%= nombreCliente%>"
                           placeholder="Se autocompleta al buscar el teléfono"
                           required>

                    <%-- teléfono oculto para que llegue al controlador de reserva --%>
                    <input type="hidden" name="telefono" value="<%= telefonoBuscado%>">

                    <%-- 
                    CAMPO DOCUMENTO INTELIGENTE: Solo se renderiza en el HTML SI seHaBuscado es true. Si no se ha buscado, el campo simplemente NO EXISTE en la página.
                    --%>
                    <% if (seHaBuscado) {%>
                    <label for="documentoIdentidad">Documento de identidad:</label>
                    <input type="text"
                           id="documentoIdentidad"
                           name="documentoIdentidad"
                           value="<%= documentoCliente%>"
                           placeholder="Solo números (6 a 10 dígitos)"
                           pattern="[0-9]{6,10}"
                           maxlength="10"
                           minlength="6"
                           title="Ingrese solo números, entre 6 y 10 dígitos."
                           oninput="this.value = this.value.replace(/[^0-9]/g, '')"
                           <%= tieneDocumento ? "readonly" : ""%>
                           required>
                    <% }%>



                    <label for="ocasion">Ocasión especial:</label>
                    <input type="text"
                           id="ocasion"
                           name="ocasion"
                           value="<%= ocasionValor%>"
                           required>

                    <label for="fecha">Fecha:</label>
                    <input type="date"
                           id="fecha"
                           name="fecha"
                           value="<%= fechaValor%>"
                           required>

                    <label for="hora">Hora:</label>
                    <input type="time"
                           id="hora"
                           name="hora"
                           value="<%= horaValor%>"
                           required>

                    <label for="personas">Número de personas:</label>
                    <input type="number"
                           id="personas"
                           name="personas"
                           value="<%= personasValor%>"
                           min="1" max="8"
                           required>

                    <label for="mesa">Mesa:</label>
                    <select id="mesa" name="mesa" required>
                        <option value="">Seleccione una mesa</option>
                        <option value="1" <%= "1".equals(mesaValor) ? "selected" : ""%>>Mesa 1</option>
                        <option value="2" <%= "2".equals(mesaValor) ? "selected" : ""%>>Mesa 2</option>
                        <option value="3" <%= "3".equals(mesaValor) ? "selected" : ""%>>Mesa 3</option>
                        <option value="4" <%= "4".equals(mesaValor) ? "selected" : ""%>>Mesa 4</option>
                        <option value="5" <%= "5".equals(mesaValor) ? "selected" : ""%>>Mesa 5</option>
                        <option value="6" <%= "6".equals(mesaValor) ? "selected" : ""%>>Mesa 6</option>
                        <option value="7" <%= "7".equals(mesaValor) ? "selected" : ""%>>Mesa 7</option>
                        <option value="8" <%= "8".equals(mesaValor) ? "selected" : ""%>>Mesa 8</option>
                    </select>

                    <button type="submit" class="btn--envio">Confirmar Reserva</button>
                    <button type="button"
                            class="btn--cancelar"
                            onclick="window.location.href = '${pageContext.request.contextPath}/html/m-meserocopy.jsp'">
                        Cancelar Reserva
                    </button>
                </form>
            </section>
        </main>

        <%-- ALERTAS --%>
        <%
            String finalizado = request.getParameter("finalizado");
            String error = request.getParameter("error");
        %>
        <% if ("true".equals(finalizado)) { %>
        <script>alert("Reserva creada correctamente.");</script>
        <% } %>
        <% if ("mesaReservada".equals(error)) { %>
        <script>alert("Esta mesa ya tiene una reserva activa.");</script>
        <% } %>

        <% if ("documentoInvalido".equals(error)) { %>
        <script>alert("El documento de identidad debe contener solo números, entre 6 y 10 dígitos.");</script>
        <% }%>

        <footer class="footer">
            <p>&copy; 2025 Labur-Ga. Todos los derechos reservados.</p>
        </footer>
    </body>
</html>