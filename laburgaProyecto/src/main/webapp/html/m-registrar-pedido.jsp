<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/registrar-pedido.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/variables.css">
        <title>Registrar Pedido</title>
    </head>
    <body>

        <header class="header">
            <h1>Gestión Menú - Panel Mesero | Mesa ${idMesa}</h1>
        </header>

        <div class="layout">
            <aside class="sidebar">
                <h2>Panel Menú Mesero</h2>
                <nav>
                    <img src="${pageContext.request.contextPath}/recurso/logo-burguer.png" alt="logo">

                    <a href="${pageContext.request.contextPath}/html/m-meserocopy.jsp" class="btn">Mesas</a>
                    <a href="${pageContext.request.contextPath}/html/m-listar-reservas.jsp" class="btn">Ver reservas</a>
                    <a href="${pageContext.request.contextPath}/html/m-formulario-reserva.jsp" class="btn">Realizar Reserva</a>
                    <a href="${pageContext.request.contextPath}/html/m-formulario-clientenuevo-mesero.jsp" class="btn">Nuevo Cliente</a>
                    <a href="${pageContext.request.contextPath}/cerrarSesion" class="btn">Cerrar Sesión</a>
                </nav>
            </aside>

            <main class="contenido">
                <form action="../PedidoControlador" method="POST">
                    <input type="hidden" name="txtIdMesa" value="${idMesa}">

                    <div class="productos-grid">

                        <%-- Iteramos por cada categoría --%>
                        <c:forEach var="entrada" items="${menuPorCategoria}">

                            <%-- Título de categoría --%>
                            <h2 class="titulo__categoria">${entrada.key}</h2>

                            <%-- Productos de esa categoría --%>
                            <c:forEach var="producto" items="${entrada.value}">
                                <div class="producto-card">

                                    <%-- Imagen del producto --%>
                                    <img src="${pageContext.request.contextPath}/img-productos/hamburguesa-clasica.png" 
                                         alt="${producto.nombreProducto}"
                                         class="producto-img">

                                    <h3 class="producto-nombre">${producto.nombreProducto}</h3>
                                    <p class="producto-descripcion">${producto.descripcionProducto}</p>
                                    <p class="producto-precio">
                                        $<c:out value="${producto.precioBaseProducto}"/>
                                    </p>
                                    <label>Cantidad:
                                        <input type="number"
                                               name="prod_${producto.idProducto}"
                                               class="producto-cantidad"
                                               value="0"
                                               min="0">
                                    </label>
                                </div>
                            </c:forEach>

                        </c:forEach>

                        <%-- Si no hay productos disponibles --%>
                        <c:if test="${empty menuPorCategoria}">
                            <p style="grid-column: 1/-1; text-align:center; padding: 2rem;">
                                No hay productos disponibles en este momento.
                            </p>
                        </c:if>

                    </div>

                    <div class="observaciones">
                        <label class="pedido__label">Observaciones generales del pedido:</label>
                        <textarea name="txtObservaciones" class="pedido__observacion-general"
                                  placeholder="Observaciones Generales"></textarea>
                        <button type="submit" class="pedido__boton pedido__boton--guardar">
                            Guardar Pedido
                        </button>
                        <button type="button"
                                class="btn--cancelar pedido__boton pedido__boton--cancelar"
                                onclick="window.location.href = '${pageContext.request.contextPath}/html/m-meserocopy.jsp'">
                            Cancelar Pedido
                        </button>
                    </div>
                </form>
            </main>
        </div>

        <footer class="footer">
            <p>&copy; 2025 Labur-Ga. Todos los derechos reservados.</p>
        </footer>
    </body>
</html>