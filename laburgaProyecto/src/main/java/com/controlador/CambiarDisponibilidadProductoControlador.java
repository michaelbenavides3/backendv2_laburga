package com.controlador;

import com.dao.ProductosDao;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "CambiarDisponibilidadProductoControlador",
        urlPatterns = {"/CambiarDisponibilidadProductoControlador"})
public class CambiarDisponibilidadProductoControlador
        extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("🔥 ENTRÓ AL CONTROLADOR");

        System.out.println("idProducto: " + request.getParameter("idProducto"));
        System.out.println("estado: " + request.getParameter("estado"));

        int idProducto = Integer.parseInt(request.getParameter("idProducto"));

        boolean estadoNuevo = Boolean.parseBoolean(request.getParameter("estado"));

        ProductosDao productosDao = new ProductosDao();

        productosDao.cambiarDisponibilidadProducto(idProducto, estadoNuevo);

        if (estadoNuevo) {

            response.sendRedirect(request.getContextPath() + "/ProductosControlador?producto=activado");

        } else {

            response.sendRedirect(request.getContextPath() + "/ProductosControlador?mensaje=desactivado");
        }

        String msg = request.getParameter("msg");

        request.setAttribute("msg", msg);
        request.getRequestDispatcher("html/a-listar-productos.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }
}
