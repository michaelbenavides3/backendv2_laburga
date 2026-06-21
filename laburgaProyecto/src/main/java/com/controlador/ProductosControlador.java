package com.controlador;

import com.dao.ProductosDao;
import com.modelo.Productos;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ProductosControlador", urlPatterns = {"/ProductosControlador"})
public class ProductosControlador extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Obtenemos qué acción quiere el usuario
        String accion = request.getParameter("accion");
        ProductosDao productosDao = new ProductosDao();

        // 2. Si no hay acción, por defecto listamos los productos
        if (accion == null) {
            accion = "listar";
        }

        // 3. El "cerebro" decide qué hacer
        switch (accion) {
            case "irARegistrar":
                request.getRequestDispatcher("/html/a-registrar-productos.jsp").forward(request, response);
                break;

            case "listar":
            default:
                List<Productos> listaProductos = productosDao.obtenerListaTodosLosProductos();
                request.setAttribute("listaProductos", listaProductos);
                request.getRequestDispatcher("/html/a-listar-productos.jsp").forward(request, response);
                break;
        }
    }
    
    // doGet y doPost se mantienen igual llamando a processRequest
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { processRequest(request, response); }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { processRequest(request, response); }
}
