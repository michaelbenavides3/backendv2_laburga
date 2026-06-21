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

        String accion = request.getParameter("accion");
        ProductosDao productosDao = new ProductosDao();

        if (accion == null) {
            accion = "listar";
        }

        switch (accion) {

            case "irARegistrar":
                request.getRequestDispatcher("/html/a-registrar-productos.jsp")
                       .forward(request, response);
                break;

            // ACCIÓN NUEVA — activa o desactiva un producto
            case "cambiarDisponibilidad":
                String idParam     = request.getParameter("idProducto");
                String estadoParam = request.getParameter("disponible");

                if (idParam != null && estadoParam != null) {
                    int idProducto      = Integer.parseInt(idParam);
                    boolean nuevoEstado = Boolean.parseBoolean(estadoParam);

                    boolean exito = productosDao.cambiarDisponibilidadProducto(idProducto, nuevoEstado);

                    if (exito) {
                        System.out.println("DEBUG: Producto " + idProducto 
                            + " cambió disponibilidad a " + nuevoEstado);
                    } else {
                        System.out.println("WARN: No se pudo cambiar disponibilidad del producto " + idProducto);
                    }
                }
                // Siempre volvemos al listado después de cambiar
                response.sendRedirect(request.getContextPath() + "/ProductosControlador?accion=listar");
                break;

            case "listar":
            default:
                List<Productos> listaProductos = productosDao.obtenerListaTodosLosProductos();
                request.setAttribute("listaProductos", listaProductos);
                request.getRequestDispatcher("/html/a-listar-productos.jsp")
                       .forward(request, response);
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { processRequest(request, response); }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { processRequest(request, response); }
}     