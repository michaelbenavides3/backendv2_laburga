/*
    
    ProductosDao-> obtenerListaTodosLosProductos

    el objetovio es listar todos lo productos qe se encuntra disponibles


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

@WebServlet(
        name = "ListarProductosControlador",
        urlPatterns = {"/ListarProductosControlador"})
public class ListarProductosControlador extends HttpServlet {

    protected void processRequest( HttpServletRequest request,HttpServletResponse response)
            throws ServletException, IOException {

        /*
        
        1. CREAR DAO
        
         */
        ProductosDao productosDao =new ProductosDao();

        /*
        
        2. OBTENER LISTA DE PRODUCTOS
        
         */
        List<Productos> listaProductos = productosDao.obtenerListaTodosLosProductos();

        /*
        
        3. ENVIAR LISTA AL JSP
        
         */
        request.setAttribute( "listaProductos", listaProductos);

        /*
        
        4. ABRIR JSP
        
         */
        request.getRequestDispatcher( "html/a-listar-productos.jsp") .forward( request,response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {processRequest( request, response);
    }

    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { processRequest( request, response);
    }
}