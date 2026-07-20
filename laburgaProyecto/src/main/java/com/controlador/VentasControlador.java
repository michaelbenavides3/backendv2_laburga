/*

EL ENCARGADO DE DAR LA VISUAL O MOSTRAR A TRAVEZ DEL JSP TODAS LAS VENTAS


RESPONSABILIDAD DEL CONTROLADOR

- Obtener todos los reportes del módulo de ventas.
- Consultar la información mediante VentasDao.
- Enviar los resultados al JSP.
- Mostrar el panel de estadísticas al administrador.

METODOS DAO UTILIZADOS

1. obtenerVentasDelDia()

2. obtenerVentasPorFechas()

3. obtenerVentasPorMes()

4. obtenerProductosMasVendidos()

5. obtenerVentasPorCategoria()

6. obtenerTotalFacturado()

 */
package com.controlador;

import com.dao.VentasDao;
import com.modelo.ProductoMasVendido;
import com.modelo.ResumenVentas;
import com.modelo.VentasCategoria;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(
        name = "VentasControlador",
        urlPatterns = {"/VentasControlador"}
)
public class VentasControlador extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        /*
        
        PASO 1  Crear el DAO encargado de consultar los reportes.
        
         */
        VentasDao ventasDao = new VentasDao();

        /*
        
        PASO 2  Obtener ventas del día.
      
         */
        ResumenVentas ventasDia = ventasDao.obtenerVentasDelDia();

        /*
       
        PASO 3  Obtener total facturado.
      
         */
        ResumenVentas totalFacturado = ventasDao.obtenerTotalFacturado();

        /*
      
        /*
        PASO 4 — OBTENER PRODUCTOS
        - si viene filtro de nombre → busca por nombre
        - si viene filtro de categoría → busca por categoría
        - si no hay filtro → muestra top 5
         */
        String filtroBusqueda = request.getParameter("buscarProducto");
        String filtroCategoria = request.getParameter("filtrarCategoria");

        List<ProductoMasVendido> listaProductosMasVendidos;

        if (filtroBusqueda != null && !filtroBusqueda.trim().isEmpty()) {
            // búsqueda por nombre de producto
            listaProductosMasVendidos = ventasDao.buscarProductosPorNombre(filtroBusqueda.trim());
            request.setAttribute("filtroBusqueda", filtroBusqueda);

        } else if (filtroCategoria != null && !filtroCategoria.trim().isEmpty()) {
            // búsqueda por categoría
            listaProductosMasVendidos = ventasDao.buscarProductosPorCategoria(filtroCategoria.trim());
            request.setAttribute("filtroCategoria", filtroCategoria);

        } else {
            // sin filtro → top 5 por defecto
            listaProductosMasVendidos = ventasDao.obtenerTop5ProductosMasVendidos();
        }

        request.setAttribute("listaProductosMasVendidos",listaProductosMasVendidos);

        /*
       
        PASO 5  Obtener ventas por categoría.
       
         */
        List<VentasCategoria> listaCategorias = ventasDao.obtenerVentasPorCategoria();

        /*
     
        PASO 6  Enviar la información al JSP.

        Cada atributo podrá utilizarse mediante Expression Language.

        Ejemplo:

        ${ventasDia.total}

        ${listaProductosMasVendidos}

        
         */
        request.setAttribute("ventasDia", ventasDia);

        request.setAttribute("totalFacturado", totalFacturado);

        request.setAttribute("listaProductosMasVendidos",listaProductosMasVendidos);

        request.setAttribute("listaCategorias", listaCategorias);

        /*
       
        PASO 7 Abrir la vista de reportes.

        forward()

        Conserva todos los atributos enviados mediante request.

       
         */
        request.getRequestDispatcher("/html/a-reportes-ventas.jsp").forward(request, response);

    }

}
