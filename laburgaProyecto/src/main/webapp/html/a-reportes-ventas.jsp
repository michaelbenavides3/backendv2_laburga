<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="com.modelo.Usuario"%>
<%@ page import="com.modelo.ResumenVentas"%>
<%@ page import="com.modelo.ProductoMasVendido"%>
<%@ page import="com.modelo.VentasCategoria"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>

<%
    /* 
       RECUPERAR DATOS DE SESIÓN Y ATRIBUTOS
       El controlador VentasControlador ya preparó
       todos estos objetos antes de hacer el forward.
     */
    Usuario usuarioSesion = (Usuario) session.getAttribute("usuarioLogeadoObjeto");

    ResumenVentas ventasDia = (ResumenVentas) request.getAttribute("ventasDia");
    ResumenVentas totalFacturado = (ResumenVentas) request.getAttribute("totalFacturado");

    List<ProductoMasVendido> listaProductosMasVendidos
            = (List<ProductoMasVendido>) request.getAttribute("listaProductosMasVendidos");

    List<VentasCategoria> listaCategorias
            = (List<VentasCategoria>) request.getAttribute("listaCategorias");
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Control de Ventas - Labur-Ga</title>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/variables.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/panel-administrador.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reportes-ventas.css">
    </head>
    <body>

        <%-- 
             HEADER
        --%>
        <header class="header">
            <h1>Control de Ventas - Panel Administrador</h1>
            <p>
                Administrador:
                <strong><%= usuarioSesion != null ? usuarioSesion.getNombreCompleto() : "—"%></strong>
            </p>
        </header>

        <div class="layout">

            <%-- =============================================
                 SIDEBAR — igual al panel principal
                 ============================================= --%>


            <%-- 
                 CONTENIDO PRINCIPAL
                 = --%>
            <main class="contenido">

                <h2 class="titulo-seccion">Resumen de Ventas</h2>

                <%-- 
                     TARJETAS DE RESUMEN — ventas del día y total
                --%>
                <div class="tarjetas-resumen">

                    <%-- VENTAS DEL DÍA --%>
                    <div class="tarjeta-resumen">
                        <p class="tarjeta-etiqueta">Ventas del día</p>
                        <p class="tarjeta-valor">
                            $<%= ventasDia != null ? String.format("%,.0f", ventasDia.getTotal()) : "0"%>
                        </p>
                        <p class="tarjeta-detalle">
                            Facturas: <%= ventasDia != null ? ventasDia.getNumeroFacturas() : 0%>
                            &nbsp;|&nbsp;
                            IVA: $<%= ventasDia != null ? String.format("%,.0f", ventasDia.getIva()) : "0"%>
                        </p>
                    </div>

                    <%-- RESULTADO FILTRO POR FECHAS --%>
                    <% if (request.getAttribute("resultadoFechas") != null) {
                            ResumenVentas resultadoFechas = (ResumenVentas) request.getAttribute("resultadoFechas");
                    %>
                    <div class="tarjeta-resultado">
                        <p class="tarjeta-etiqueta">
                            Ventas del <%= request.getAttribute("fechaInicio")%> al <%= request.getAttribute("fechaFin")%>
                        </p>
                        <p class="tarjeta-valor">$<%= String.format("%,.0f", resultadoFechas.getTotal())%></p>
                        <p class="tarjeta-detalle">
                            Facturas: <%= resultadoFechas.getNumeroFacturas()%>
                            &nbsp;|&nbsp;
                            Subtotal: $<%= String.format("%,.0f", resultadoFechas.getSubtotal())%>
                            &nbsp;|&nbsp;
                            IVA: $<%= String.format("%,.0f", resultadoFechas.getIva())%>
                        </p>
                    </div>
                    <% } %>

                    <%-- RESULTADO FILTRO POR MES --%>
                    <% if (request.getAttribute("resultadoMes") != null) {
                            ResumenVentas resultadoMes = (ResumenVentas) request.getAttribute("resultadoMes");
                    %>
                    <div class="tarjeta-resultado">
                        <p class="tarjeta-etiqueta">
                            Ventas del mes <%= request.getAttribute("mesBuscado")%> / <%= request.getAttribute("anioBuscado")%>
                        </p>
                        <p class="tarjeta-valor">$<%= String.format("%,.0f", resultadoMes.getTotal())%></p>
                        <p class="tarjeta-detalle">
                            Facturas: <%= resultadoMes.getNumeroFacturas()%>
                            &nbsp;|&nbsp;
                            Subtotal: $<%= String.format("%,.0f", resultadoMes.getSubtotal())%>
                            &nbsp;|&nbsp;
                            IVA: $<%= String.format("%,.0f", resultadoMes.getIva())%>
                        </p>
                    </div>
                    <% }%>

                    <%-- TOTAL FACTURADO AÑO ACTUAL --%>
                    <div class="tarjeta-resumen tarjeta-destacada">
                        <p class="tarjeta-etiqueta">Total facturado <%= java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%></p>
                        <p class="tarjeta-valor">
                            $<%= totalFacturado != null ? String.format("%,.0f", totalFacturado.getTotal()) : "0"%>
                        </p>
                        <p class="tarjeta-detalle">
                            Facturas: <%= totalFacturado != null ? totalFacturado.getNumeroFacturas() : 0%>
                        </p>
                    </div>


                </div>

                <%-- 
                     FILTRO POR FECHAS — llama al controlador con parámetros
                --%>
                <section class="seccion-filtro">
                    <h3>Consultar ventas por rango de fechas</h3>
                    <form action="${pageContext.request.contextPath}/VentasPorFechasControlador" method="get" class="form-filtro">
                        <label>Desde:</label>
                        <input type="date" name="fechaInicio" required>
                        <label>Hasta:</label>
                        <input type="date" name="fechaFin" required>
                        <button type="submit" class="btn-filtrar">Consultar</button>
                    </form>
                </section>

                <%-- 
                     FILTRO POR MES
                --%>
                <section class="seccion-filtro">
                    <h3>Consultar ventas por mes</h3>
                    <form action="${pageContext.request.contextPath}/VentasPorMesControlador" method="get" class="form-filtro">
                        <label>Mes:</label>
                        <select name="mes">
                            <option value="1">Enero</option>
                            <option value="2">Febrero</option>
                            <option value="3">Marzo</option>
                            <option value="4">Abril</option>
                            <option value="5">Mayo</option>
                            <option value="6">Junio</option>
                            <option value="7" selected>Julio</option>
                            <option value="8">Agosto</option>
                            <option value="9">Septiembre</option>
                            <option value="10">Octubre</option>
                            <option value="11">Noviembre</option>
                            <option value="12">Diciembre</option>
                        </select>
                        <label>Año:</label>
                        <input type="number" name="anio" value="2026" min="2020" max="2099" style="width:90px">
                        <button type="submit" class="btn-filtrar">Consultar</button>
                    </form>
                </section>

                <%-- 
                     PRODUCTOS MÁS VENDIDOS
                --%>
                <%-- SECCIÓN PRODUCTOS MÁS VENDIDOS CON FILTROS --%>
                <section class="seccion-tabla">

                    <h3>Productos más vendidos</h3>

                    <%-- FORMULARIO DE FILTROS — mismo form, dos campos --%>
                    <form action="${pageContext.request.contextPath}/VentasControlador" method="get" class="form-filtro" style="margin-bottom:16px;">

                    <%--    <input type="text"
                               name="buscarProducto"
                               placeholder="Buscar por nombre..."
                               value="<%= request.getAttribute("filtroBusqueda") != null ? request.getAttribute("filtroBusqueda") : ""%>"
                               style="padding:7px 10px; border:1px solid #ccc; border-radius:6px; font-size:14px;"> --%>

                        <select name="filtrarCategoria" style="padding:7px 10px; border:1px solid #ccc; border-radius:6px; font-size:14px;">
                            <option value="">Todas las categorías</option>
                            <% for (VentasCategoria cat : listaCategorias) {%>
                            <option value="<%= cat.getCategoriaProducto()%>"
                                    <%= cat.getCategoriaProducto().equals(request.getAttribute("filtroCategoria") != null ? request.getAttribute("filtroCategoria") : "") ? "selected" : ""%>>
                                <%= cat.getCategoriaProducto()%>
                            </option>
                            <% } %>
                        </select>

                        <button type="submit" class="btn-filtrar">Filtrar</button>
                        <a href="${pageContext.request.contextPath}/VentasControlador" class="btn-limpiar">Ver Top 5</a>

                    </form>

                    <%-- ETIQUETA DE QUÉ SE ESTÁ MOSTRANDO --%>
                    <% if (request.getAttribute("filtroBusqueda") != null) {%>
                    <p class="etiqueta-filtro">Resultados para: "<%= request.getAttribute("filtroBusqueda")%>"</p>
                    <% } else if (request.getAttribute("filtroCategoria") != null) {%>
                    <p class="etiqueta-filtro">Categoría: "<%= request.getAttribute("filtroCategoria")%>"</p>
                    <% } else { %>
                    <p class="etiqueta-filtro">Mostrando Top 5 productos más vendidos</p>
                    <% } %>

                    <%-- TABLA --%>
                    <% if (listaProductosMasVendidos == null || listaProductosMasVendidos.isEmpty()) { %>
                    <p class="sin-datos">No hay datos para el filtro aplicado.</p>
                    <% } else { %>
                    <table class="tabla-reporte">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Producto</th>
                                <th>Cantidad vendida</th>
                                <th>Total generado</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                int posicion = 1;
                                for (ProductoMasVendido productoActual : listaProductosMasVendidos) {
                            %>
                            <tr>
                                <td class="posicion"><%= posicion++%></td>
                                <td><%= productoActual.getNombreProducto()%></td>
                                <td class="numero"><%= productoActual.getCantidadVendida()%></td>
                                <td class="numero">$<%= String.format("%,.0f", productoActual.getTotalVendido())%></td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                    <% } %>

                </section>

                <%-- 
                     VENTAS POR CATEGORÍA
                --%>
                <section class="seccion-tabla">
                    <h3>Ventas por categoría</h3>

                    <% if (listaCategorias == null || listaCategorias.isEmpty()) { %>
                    <p class="sin-datos">No hay datos de categorías aún.</p>
                    <% } else { %>
                    <table class="tabla-reporte">
                        <thead>
                            <tr>
                                <th>Categoría</th>
                                <th>Cantidad vendida</th>
                                <th>Total generado</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (VentasCategoria categoriaActual : listaCategorias) {%>
                            <tr>
                                <td class="categoria-nombre"><%= categoriaActual.getCategoriaProducto()%></td>
                                <td class="numero"><%= categoriaActual.getCantidadVendida()%></td>
                                <td class="numero">$<%= String.format("%,.0f", categoriaActual.getTotalVendido())%></td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                    <% }%>
                </section>
        </div>

        <%-- BOTÓN REGRESAR --%>
        <div class="contenedor-boton-regresar">
            <button type="button" class="btn-regresar"
                    onclick="window.location.href = '${pageContext.request.contextPath}/html/a-panel-principal-admin.jsp'">
                Regresar al Panel
            </button>
        </div>    

    </main>

    <footer class="footer">
        <p>&copy; 2025 Labur-Ga. Todos los derechos reservados.</p>
    </footer>

</body>
</html>
