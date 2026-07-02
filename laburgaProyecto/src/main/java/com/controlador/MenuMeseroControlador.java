/*

    el mesero pueda tomar un pedido rápidamente. Su complejidad radica en que no solo trae una lista plana de productos, 
    sino que organiza la información para que la vista sea más profesional y eficiente.

   ---- este controlador es el encargado de mostrar el menu al mesero, mas no de tomas el pedido o registrar el pedido ----

    
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

        // 1.  Obtenemos el ID de la mesa para saber dónde se está tomando el pedido.
        String idMesa = request.getParameter("idMesa");

        // 2.Instanciamos los DAOs para acceder a los datos.
        ProductosDao productosDao = new ProductosDao();
        ProductoImagenDao imagenDao = new ProductoImagenDao();

        // 3. Traemos todos los productos disponibles (SELECT * FROM... WHERE disponible=true).
        List<Productos> productosDisponibles = productosDao.obtenerProductosDisponibles();

        /*
         * 4. LÓGICA DE AGRUPACIÓN (Aquí es donde se organiza el menú):
         * Creamos un mapa donde la clave es el nombre de la categoría (String) 
         * y el valor es la lista de productos de esa categoría.
         */
        /*
        map es como un diccionario, donde guarda una clave y un valor
        string es la categoria texto : el valor sera list<productos> porque una categoria tiene muchos productos
        LinkedHashMap --> mantiene el orden que se agregan las categorias
        */
        Map<String, List<Productos>> menuPorCategoria = new LinkedHashMap<>();
        /*
        productos p = hamburguesa doble, lasagna, agua
        recorre porductos disponibles y los almacena en p
        */
        for (Productos p : productosDisponibles) {
            /*
            string cat, obtiene la categoria del prodcutos actual
            lasgnas = pastas
            agua = otros
            */
            String cat = p.getCategoriaProducto();
            // Si la categoría aún no existe en el mapa, crea una lista vacía y agrega el producto.
            menuPorCategoria.computeIfAbsent(cat, k -> new java.util.ArrayList<>()).add(p);
        }

        /*
         * 5. VINCULACIÓN DE IMÁGENES:
         * Relacionamos cada ID de producto con su ruta de imagen única.
         */
        Map<Integer, String> imagenesProductos = new LinkedHashMap<>();
        for (Productos p : productosDisponibles) {
            String ruta = imagenDao.obtenerRutaImagenProducto(p.getIdProducto());
            imagenesProductos.put(p.getIdProducto(), ruta);
        }

        // 6. TRANSPORTE: Enviamos los mapas y el idMesa al JSP para que los pueda mostrar.
        request.setAttribute("menuPorCategoria", menuPorCategoria);
        request.setAttribute("imagenesProductos", imagenesProductos);
        request.setAttribute("idMesa", idMesa);

        // 7. VISTA: Redirigimos al archivo JSP que mostrará la interfaz al mesero.
        request.getRequestDispatcher("/html/m-registrar-pedido.jsp").forward(request, response);
    }
}