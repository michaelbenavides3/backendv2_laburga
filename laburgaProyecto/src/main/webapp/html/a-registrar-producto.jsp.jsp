<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Administrar Productos</title>
    <link rel="stylesheet" href="css/admin.css">
</head>
<body>
    <h1>Productos de la categoría</h1>

    <form action="productos" method="post">
        <input type="hidden" name="accion" value="crear">
        <input type="hidden" name="categoriaId" value="${categoriaId}">
        <input type="text" name="nombre" placeholder="Nombre del producto" required>
        <input type="number" step="0.01" name="precio" placeholder="Precio" required>
        <button type="submit">Agregar</button>
    </form>

    <table>
        <tr>
            <th>ID</th><th>Nombre</th><th>Precio</th><th>Estado</th><th>Acciones</th>
        </tr>
        <c:forEach var="p" items="${productos}">
            <tr>
                <td>${p.id}</td>
                <td>${p.nombre}</td>
                <td>$${p.precio}</td>
                <td>${p.estado ? "Activo" : "Inactivo"}</td>
                <td>
                    <c:if test="${p.estado}">
                        <a href="productos?categoriaId=${categoriaId}&accion=desactivar&id=${p.id}">Desactivar</a>
                    </c:if>
                    <c:if test="${!p.estado}">
                        <a href="productos?categoriaId=${categoriaId}&accion=activar&id=${p.id}">Activar</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>

    <a href="categorias">Volver a categorías</a>
</body>
</html>