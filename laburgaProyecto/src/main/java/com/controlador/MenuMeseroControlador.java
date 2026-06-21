
package com.controlador;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.dao.ProductosDao;
import com.modelo.Productos;
import java.util.List;

@WebServlet("/MenuMeseroControlador")
public class MenuMeseroControlador extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductosDao dao = new ProductosDao();

        List<Productos> lista = dao.obtenerProductosDisponibles();

        request.setAttribute("listaProductos", lista);

        request.getRequestDispatcher("html/menu-mesero.jsp")
               .forward(request, response);
    }
}