/*
        

    SOLO PARA MESERO


*/



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

@WebServlet(name = "PedidoControladorMesero", urlPatterns = {"/PedidoControladorMesero"})
public class PedidoControladorMesero extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductosDao dao = new ProductosDao();

        // 🔥 SOLO PRODUCTOS DISPONIBLES (DINÁMICO)
        List<Productos> lista = dao.obtenerProductosDisponibles();

        request.setAttribute("listaProductos", lista);

        request.getRequestDispatcher("menu-mesero.jsp")
               .forward(request, response);
    }
}