package com.controlador;

import com.dao.ProductosDao;
import com.modelo.Productos;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(
        name = "RegistrarProductoControlador",
        urlPatterns = {"/RegistrarProductoControlador"}
)
public class RegistrarProductoControlador extends HttpServlet {

    protected void processRequest(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        /*
        
        1. RECUPERAR DATOS DEL FORMULARIO
        
         */
        String nombreProducto = request.getParameter("txtNombreProducto");

        String descripcionProducto = request.getParameter("txtDescripcionProducto");

        double precioProducto  = Double.parseDouble(  request.getParameter("txtPrecioProducto"));

        String categoriaProducto = request.getParameter("txtCategoriaProducto");

        /*
        
        2. CREAR OBJETO PRODUCTO
        
         */
        Productos nuevoProducto = new Productos();

        nuevoProducto.setNombreProducto(nombreProducto);

        nuevoProducto.setDescripcionProducto( descripcionProducto);

        nuevoProducto.setPrecioBaseProducto( precioProducto);

        nuevoProducto.setCategoriaProducto( categoriaProducto);

        nuevoProducto.setDisponibleProducto(true);

        /*
        
        3. GUARDAR PRODUCTO
        
         */
        ProductosDao productosDao = new ProductosDao();

        boolean productoGuardado = productosDao.registrarNuevoProducto( nuevoProducto);

        /*
        
        4. REDIRECCIONAR
        
         */
        if (productoGuardado) {

            response.sendRedirect( "html/a-listar-productos.jsp?producto=registrado");

        } else {

            response.sendRedirect("html/a-registrar-producto.jsp?error=registro");

        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }
}
