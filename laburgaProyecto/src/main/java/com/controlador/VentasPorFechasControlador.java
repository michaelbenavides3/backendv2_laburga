
/*


    ENCARGADO DE MOSTRAR LOS PRODUCTOS VENDIDOS POR FECHA
*/



package com.controlador;

import com.dao.VentasDao;
import com.modelo.ResumenVentas;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/VentasPorFechasControlador")
public class VentasPorFechasControlador extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /*
        1. RECUPERAR LAS FECHAS DEL FORMULARIO
        */
        String fechaInicio = request.getParameter("fechaInicio");
        String fechaFin    = request.getParameter("fechaFin");

        /*
        2. CONSULTAR VENTAS EN ESE RANGO
        */
        VentasDao ventasDao = new VentasDao();
        ResumenVentas resultadoFechas = ventasDao.obtenerVentasPorFechas(fechaInicio, fechaFin);

        /*
        3. TAMBIÉN CARGAMOS LOS DATOS BASE PARA NO PERDER EL PANEL
        */
        ResumenVentas ventasDia      = ventasDao.obtenerVentasDelDia();
        ResumenVentas totalFacturado = ventasDao.obtenerTotalFacturado();

        /*
        4. MANDAMOS TODO AL JSP
        */
        request.setAttribute("ventasDia",        ventasDia);
        request.setAttribute("totalFacturado",    totalFacturado);
        request.setAttribute("resultadoFechas",   resultadoFechas);
        request.setAttribute("fechaInicio",        fechaInicio);
        request.setAttribute("fechaFin",           fechaFin);
        request.setAttribute("listaProductosMasVendidos",ventasDao.obtenerTop5ProductosMasVendidos());
        request.setAttribute("listaCategorias",    ventasDao.obtenerVentasPorCategoria());

        request.getRequestDispatcher("/html/a-reportes-ventas.jsp")
               .forward(request, response);
    }
}