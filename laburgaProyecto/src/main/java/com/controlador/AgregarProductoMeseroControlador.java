/*
    
    CONTROLADOR AGREGAR PRODUCTO, es el mapa que nos muestra los productos dentro del jsp
    
    RESPONSABILIDAD:
    
    - Obtener todos los productos disponibles para venta.
    
    - Agrupar los productos por categoría.
    
    - Obtener las imágenes asociadas a cada producto.
    
    - Enviar toda la información al JSP m-agregar-producto.jsp.
    
    - Permitir que el mesero visualice el menú antes de agregar
      productos a un pedido abierto.


      Métodos que utiliza:

        request.getParameter("idMesa"): Obtiene la mesa que tiene el pedido abierto.
        productosDao.obtenerProductosDisponibles(): Obtiene únicamente los productos activos.
        imagenDao.obtenerRutaImagenProducto(idProducto): Obtiene la imagen asociada al producto.
        request.setAttribute(...): Envía información al JSP.
        request.getRequestDispatcher(...).forward(...): Abre la vista m-agregar-producto.jsp.
    
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

@WebServlet(name = "AgregarProductoMeseroControlador", urlPatterns = {"/AgregarProductoMeseroControlador"})
public class AgregarProductoMeseroControlador extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        /*
        
        1. RECUPERAR ID DE LA MESA: Se recibe desde el botón: Agregar Producto del módulo de pedidos.
        
         */

        String idMesa = request.getParameter("idMesa");
        
        /*
        
        2. CREAR OBJETOS DAO
        
         */
        /*
        se instancia la clase productosdao e imagendao, 
        
        */
        ProductosDao productosDao     = new ProductosDao();
        ProductoImagenDao imagenDao   = new ProductoImagenDao();
        
         /*
        
        3. OBTENER TODOS LOS PRODUCTOS DISPONIBLES Solo trae productos activos.
        
         */
         /*
         productosDao--> es el resultado de la instacia del objeto dao
         */

        List<Productos> productosDisponibles = productosDao.obtenerProductosDisponibles();
        
        
         /*
        
        4. AGRUPAR PRODUCTOS POR CATEGORÍA
        
        Ejemplo:
        
        Hamburguesas
            - Clásica
            - Doble Carne
        
        Bebidas
            - Coca Cola
            - Jugo Natural
        
         */

        // Agrupamos por categoría
        /*
        SE ALMACENAN DE FORMA CATEGORIA Y VALOR (LinkedHashMap) --> MANTIENE EL ORDEN EN QUE SE VA INSERTANDO LAS CATEGORIAS
        
        */
        Map<String, List<Productos>> menuPorCategoria = new LinkedHashMap<>();
        /*
        CON EL FOR UTILIZAMOS PARA RECORRES TODOS LOS PRODUCTOS (P), SON LOS PRODUCTO QUE SE ESTAN ANALIZANDO
        P es la variable temporal que va aumentando cada vuelta (productosDisponibles) donde estan todos los productos es la variable de la lista donde se obtuvieron los productos disponibles
        */
        for (Productos p : productosDisponibles) {
            /*
            cat es una varibale que se llama categoria, donde guarda la respuesta (hamburguesas, otros, sopas)
            p que es el productos encontrar lo asigna dentro de cat, 
            */
            String cat = p.getCategoriaProducto();
            /*
            menuporcategoria, es el mapa donde se agruoan las categrias o el diccionario, 
            (computeIfAbsent)--> se obtiene la lista de productos guardados dentro de la categoria
            (cat, k -> new java.util.ArrayList<>()).add(p)--> si no existe la categoria creela una lista  nueva, y add, agregaa el producto nuevo dentro de la categoria,
            */
            menuPorCategoria.computeIfAbsent(cat, k -> new java.util.ArrayList<>()).add(p);
        }
        
        /*
        
        5. OBTENER IMÁGENES DE LOS PRODUCTOS
        
        La clave será: id_producto
        
        El valor será:ruta de la imagen
        
         */

        // Mapa de imágenes
        Map<Integer, String> imagenesProductos = new LinkedHashMap<>();
        for (Productos p : productosDisponibles) {
            String ruta = imagenDao.obtenerRutaImagenProducto(p.getIdProducto());
            if (ruta != null && !ruta.isEmpty()) {
                imagenesProductos.put(p.getIdProducto(), ruta);
            }
        }
        
        /*
        
        6. ENVIAR INFORMACIÓN AL JSP
        
         */
        
        
        request.setAttribute("menuPorCategoria", menuPorCategoria);
        request.setAttribute("imagenesProductos", imagenesProductos);
        request.setAttribute("idMesa", idMesa);

        request.getRequestDispatcher("/html/m-agregar-producto.jsp")
               .forward(request, response);
    }
}
