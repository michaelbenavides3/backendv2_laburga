package com.controlador;

import com.dao.ProductosDao;
import com.modelo.Productos;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "MenuMeseroControlador", urlPatterns = {"/MenuMeseroControlador"})
public class MenuMeseroControlador extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idMesa = request.getParameter("idMesa");

        // Traemos solo los productos disponibles desde la BD
        ProductosDao dao = new ProductosDao();
        List<Productos> productosDisponibles = dao.obtenerProductosDisponibles();

        // Agrupamos por categoría para mostrarlos en secciones
        Map<String, List<Productos>> menuPorCategoria = new LinkedHashMap<>();
        for (Productos p : productosDisponibles) {
            String cat = p.getCategoriaProducto();
            menuPorCategoria.computeIfAbsent(cat, k -> new java.util.ArrayList<>()).add(p);
        }

        // Mandamos los datos al JSP
        request.setAttribute("menuPorCategoria", menuPorCategoria);
        request.setAttribute("idMesa", idMesa);

        request.getRequestDispatcher("/html/m-registrar-pedido.jsp")
               .forward(request, response);
    }
}