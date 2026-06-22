/*

    - Activar productos.
    - Desactivar productos.
    - Actualizar el estado en la base de datos.
    - Recargar el listado de productos.

        METODOS DAO UTILIZADOS:

            1. cambiarDisponibilidadProducto() -> Permite activar o desactivar un producto del menú.


*/






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
public class CambiarDisponibilidadProductoControlador extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("ENTRÓ AL CONTROLADOR");

        int idProducto = Integer.parseInt(request.getParameter("idProducto"));
        boolean estadoNuevo = Boolean.parseBoolean(request.getParameter("estado"));

        System.out.println("idProducto: " + idProducto);
        System.out.println("estado: " + estadoNuevo);

        ProductosDao productosDao = new ProductosDao();
        productosDao.cambiarDisponibilidadProducto(idProducto, estadoNuevo);

        // MENSAJE UNIFICADO
        String mensaje = estadoNuevo ? "activado" : "desactivado";

        response.sendRedirect(
                request.getContextPath()
                + "/ProductosControlador?producto=" + mensaje
        );
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}