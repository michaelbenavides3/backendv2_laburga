package com.controlador;

import com.dao.ProductosDao;
import com.modelo.Productos;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "EditarProductoControlador",
        urlPatterns = {"/EditarProductoControlador"})
public class EditarProductoControlador extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductosDao productosDao = new ProductosDao();

        String accion = request.getParameter("accion");

        if ("guardar".equals(accion)) {

            int idProducto= Integer.parseInt( request.getParameter("idProducto"));

            String nombreProducto = request.getParameter("nombreProducto");

            String descripcionProducto = request.getParameter("descripcionProducto");

            double precioProducto  = Double.parseDouble( request.getParameter("precioProducto"));

            String categoriaProducto = request.getParameter("categoriaProducto");

            Productos productoActualizado = new Productos();

            productoActualizado.setIdProducto(idProducto);
            productoActualizado.setNombreProducto(nombreProducto);
            productoActualizado.setDescripcionProducto(descripcionProducto);
            productoActualizado.setPrecioBaseProducto(precioProducto);
            productoActualizado.setCategoriaProducto(categoriaProducto);

            productosDao.actualizarProducto(productoActualizado);

            response.sendRedirect("html/a-listar-productos.jsp?producto=editado");

        } else {

            int idProducto = Integer.parseInt( request.getParameter("idProducto"));

            Productos producto = productosDao.obtenerProductoPorId( idProducto);

            request.setAttribute( "productoSeleccionado",producto);

            request.getRequestDispatcher(  "html/a-editar-producto.jsp") .forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }
}
