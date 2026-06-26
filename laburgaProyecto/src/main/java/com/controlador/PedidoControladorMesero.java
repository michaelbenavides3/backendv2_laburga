/*

    RESPONSABILIDAD:

    - Mostrar al mesero únicamente los productos disponibles.
    - Consultar los productos activos registrados en la base de datos.
    - Enviar la lista de productos a la vista del mesero.
    - Abrir el menú donde el mesero podrá registrar pedidos.


    METODOS DAO UTILIZADOS:

    1. obtenerProductosDisponibles()
       -> Consulta todos los productos que tienen disponible_producto = true.

    VALOR RETORNADO:

    List<Productos>
    -> Devuelve una lista con todos los productos disponibles para la venta.


    FLUJO:

    1. El mesero ingresa al menú.
    2. El controlador se ejecuta.
    3. Consulta los productos disponibles.
    4. Guarda la lista en el request.
    5. Envía la información al JSP.
    6. El JSP muestra los productos.

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
        name = "PedidoControladorMesero",
        urlPatterns = {"/PedidoControladorMesero"}
)
public class PedidoControladorMesero extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        /*
            PASO 1

            Crear el objeto DAO.

            El DAO es el encargado de comunicarse con la base de datos.
        */
        ProductosDao dao = new ProductosDao();

        /*
            PASO 2

            Consultar únicamente los productos que se encuentran disponibles.

            Método utilizado: obtenerProductosDisponibles()

            Retorna:List<Productos>
        */
        List<Productos> lista = dao.obtenerProductosDisponibles();

        /*
            PASO 3

            Guardar la lista dentro del request.

            setAttribute()

            Permite enviar información desde elcontrolador hacia el JSP.

            Nombre atributo:listaProductos

            Valor: lista
        */
        request.setAttribute("listaProductos", lista);

        /*
            PASO 4

            Abrir la vista del mesero.

            getRequestDispatcher()

            Busca el archivo JSP indicado.

            forward()

            Envía la petición actual al JSP conservando todos los atributosalmacenados en el request.

            Gracias a esto el JSP puede accedera listaProductos y mostrar el menú.
        */
        request.getRequestDispatcher("menu-mesero.jsp")
               .forward(request, response);
    }
}