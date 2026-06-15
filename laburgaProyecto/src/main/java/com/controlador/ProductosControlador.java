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

@WebServlet(name = "ProductosControlador",
        urlPatterns = {"/ProductosControlador"})
public class ProductosControlador extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        ProductosDao productosDao = new ProductosDao();

        List<Productos> listaProductos = productosDao.obtenerListaTodosLosProductos();

        request.setAttribute("listaProductos",listaProductos);

        request.getRequestDispatcher( "/html/a-listar-productos.jsp") .forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException { processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { processRequest(request, response);
    }
}
