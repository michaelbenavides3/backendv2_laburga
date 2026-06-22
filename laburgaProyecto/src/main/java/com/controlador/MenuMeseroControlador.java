/*

    el mesero pueda tomar un pedido rápidamente. Su complejidad radica en que no solo trae una lista plana de productos, 
    sino que organiza la información para que la vista sea más profesional y eficiente.


    ProductosDao-> obtenerProductosDisponibles
    ProductoImagenDao->	obtenerRutaImagenProducto


*/

package com.controlador;

import com.dao.ProductosDao;
import com.dao.ProductoImagenDao;
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

        ProductosDao productosDao       = new ProductosDao();
        ProductoImagenDao imagenDao     = new ProductoImagenDao();

        List<Productos> productosDisponibles = productosDao.obtenerProductosDisponibles();

        // Agrupamos por categoría
        Map<String, List<Productos>> menuPorCategoria = new LinkedHashMap<>();
        for (Productos p : productosDisponibles) {
            String cat = p.getCategoriaProducto();
            menuPorCategoria.computeIfAbsent(cat, k -> new java.util.ArrayList<>()).add(p);
        }

        // ✅ Mapa idProducto → ruta de imagen
        Map<Integer, String> imagenesProductos = new LinkedHashMap<>();
        for (Productos p : productosDisponibles) {
            String ruta = imagenDao.obtenerRutaImagenProducto(p.getIdProducto());
            imagenesProductos.put(p.getIdProducto(), ruta);
        }

        request.setAttribute("menuPorCategoria", menuPorCategoria);
        request.setAttribute("imagenesProductos", imagenesProductos);
        request.setAttribute("idMesa", idMesa);

        request.getRequestDispatcher("/html/m-registrar-pedido.jsp").forward(request, response);
    }
}