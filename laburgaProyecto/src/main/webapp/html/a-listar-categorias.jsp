<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Administrar Categorías</title>
    <link rel="stylesheet" href="css/admin.css">
</head>
<body>
    <h1>Categorías</h1>

    <form action="categorias" method="post">
        <input type="hidden" name="accion" value="crear">
        <input type="text" name="nombre" placeholder="Nombre de la categoría" required>
        <button type="submit">Agregar</button>
    </form>

    <table>
        <tr>
            <th>ID</th><th>Nombre</th><th>Estado</th><th>Acciones</th>
        </tr>
        <c:forEach var="cat" items="${categorias}">
            <tr>
                <td>${cat.id}</td>
                <td>${cat.nombre}</td>
                <td>${cat.estado ? "Activo" : "Inactivo"}</td>
                <td>
                    <a href="productos?categoriaId=${cat.id}">Ver productos</a>
                    <c:if test="${cat.estado}">
                        <a href="categorias?accion=desactivar&id=${cat.id}">Desactivar</a>
                    </c:if>
                    <c:if test="${!cat.estado}">
                        <a href="categorias?accion=activar&id=${cat.id}">Activar</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>