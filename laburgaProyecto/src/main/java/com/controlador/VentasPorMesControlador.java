package com.controlador;

import com.dao.VentasDao;
import com.modelo.ResumenVentas;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/VentasPorMesControlador")
public class VentasPorMesControlador extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /*
        1. RECUPERAR MES Y AÑO DEL FORMULARIO
        */
        int mes  = Integer.parseInt(request.getParameter("mes"));
        int anio = Integer.parseInt(request.getParameter("anio"));

        /*
        2. CONSULTAR VENTAS DE ESE MES
        */
        VentasDao ventasDao = new VentasDao();
        ResumenVentas resultadoMes = ventasDao.obtenerVentasPorMes(mes, anio);

        /*
        3. TAMBIÉN CARGAMOS LOS DATOS BASE
        */
        ResumenVentas ventasDia      = ventasDao.obtenerVentasDelDia();
        ResumenVentas totalFacturado = ventasDao.obtenerTotalFacturado();

        /*
        4. MANDAMOS TODO AL JSP
        */
        request.setAttribute("ventasDia",        ventasDia);
        request.setAttribute("totalFacturado",    totalFacturado);
        request.setAttribute("resultadoMes",      resultadoMes);
        request.setAttribute("mesBuscado",        mes);
        request.setAttribute("anioBuscado",       anio);
        request.setAttribute("listaProductosMasVendidos",ventasDao.obtenerTop5ProductosMasVendidos());
        request.setAttribute("listaCategorias",    ventasDao.obtenerVentasPorCategoria());

        request.getRequestDispatcher("/html/a-reportes-ventas.jsp")
               .forward(request, response);
    }
}  