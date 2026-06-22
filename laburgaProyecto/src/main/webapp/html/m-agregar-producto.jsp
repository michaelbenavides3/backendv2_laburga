<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/m-agregar-producto.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/variables.css">
    <title>Agregar Producto</title>
</head>
<body>

    <header class="header">
        <h1>Agregar Producto — Mesa ${idMesa}</h1>
    </header>

    <div class="layout">
        <aside class="sidebar">
            <h2>Panel Menú Mesero</h2>
            <nav>
                <img src="${pageContext.request.contextPath}/recurso/logo-burguer.png" alt="logo">
                <a href="${pageContext.request.contextPath}/html/m-meserocopy.jsp" class="btn">Mesas</a>
                <a href="${pageContext.request.contextPath}/html/m-formulario-reserva.jsp" class="btn">Realizar Reserva</a>
                <a href="${pageContext.request.contextPath}/html/m-formulario-clientenuevo-mesero.jsp" class="btn">Nuevo Cliente</a>
                <a href="${pageContext.request.contextPath}/cerrarSesion" class="btn">Cerrar Sesión</a>
            </nav>
        </aside>

        <main class="contenido">
            <form action="${pageContext.request.contextPath}/AgregarProductoPedidoControlador" method="POST">
                <input type="hidden" name="txtIdMesa" value="${idMesa}">

                <div class="productos-grid">

                    <c:forEach var="entrada" items="${menuPorCategoria}">
                        <h2 class="titulo__categoria">${entrada.key}</h2>

                        <c:forEach var="producto" items="${entrada.value}">
                            <div class="producto-card">

                                <c:choose>
                                    <c:when test="${not empty imagenesProductos[producto.idProducto]}">
                                        <img src="${pageContext.request.contextPath}/${imagenesProductos[producto.idProducto]}"
                                             alt="${producto.nombreProducto}" class="producto-img">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${pageContext.request.contextPath}/img-productos/sin-imagen.png"
                                             alt="${producto.nombreProducto}" class="producto-img">
                                    </c:otherwise>
                                </c:choose>

                                <h3 class="producto-nombre">${producto.nombreProducto}</h3>
                                <p class="producto-descripcion">${producto.descripcionProducto}</p>
                                <p class="producto-precio">$<c:out value="${producto.precioBaseProducto}"/></p>
                                <label>Cantidad:
                                    <input type="number"
                                           name="prod_${producto.idProducto}"
                                           class="producto-cantidad"
                                           value="0" min="0">
                                </label>
                            </div>
                        </c:forEach>
                    </c:forEach>

                    <c:if test="${empty menuPorCategoria}">
                        <p class="sin-productos">No hay productos disponibles.</p>
                    </c:if>

                </div>

                <div class="observaciones">
                    <label class="pedido__label">Observaciones:</label>
                    <textarea name="txtObservaciones" class="pedido__observacion-general"
                              placeholder="Observaciones Generales"></textarea>
                    <button type="submit" class="pedido__boton pedido__boton--guardar">
                        Agregar al Pedido
                    </button>
                    <button type="button"
                            class="btn--cancelar pedido__boton pedido__boton--cancelar"
                            onclick="window.location.href='${pageContext.request.contextPath}/html/m-meserocopy.jsp'">
                        Cancelar
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