/*

    CONTROLADOR: ProductosControlador

    RESPONSABILIDAD:

    - Mostrar todos los productos registrados.
    - Abrir el formulario para registrar nuevos productos.
    - Activar o desactivar productos del inventario.

    MÉTODOS DAO UTILIZADOS

    ProductosDao

    1. obtenerListaTodosLosProductos()-> Consulta todos los productos registrados en la base de datos.

    2. cambiarDisponibilidadProducto(idProducto, estado)-> Cambia el estado del producto entre disponible y no disponible.

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
        name = "ProductosControlador",
        urlPatterns = {"/ProductosControlador"}
)
public class ProductosControlador extends HttpServlet {

    /*
        processRequest()

        Este método concentra toda la lógica del controlador.

        Tanto doGet() como doPost() llaman este método para evitar repetir código.
    */

    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        /*
            Recupera la acción enviada desde la URL.

            Ejemplos:

            ProductosControlador?accion=listar

            ProductosControlador?accion=irARegistrar

            ProductosControlador?accion=cambiarDisponibilidad
        */

        String accion = request.getParameter("accion");

        /*
            Se crea el DAO.

            ¿Qué significa?

            Se crea un objeto que permitirá comunicarse con la base de datos.

            Desde este momento el controlador ya puede consultar o modificar productos.
        */

        ProductosDao productosDao = new ProductosDao();

        /*
            IF

            ¿Para qué sirve?

            Verificar si el usuario NO envió ninguna acción.

            Si no viene ninguna acción, automáticamente se mostrará el listado de productos.

            Esto evita errores cuando el controlador se ejecuta sin parámetros.
        */

        if (accion == null) {

            accion = "listar";

        }

        /*
            SWITCH

            ¿Para qué sirve?

            Dependiendo del valor recibido en "accion", ejecuta un bloque diferente.

            Es equivalente a muchos if-else,pero mucho más organizado.
        */

        switch (accion) {

            /*
                CASO 1

                El administrador desea abrir el formulario para registrar  un nuevo producto.
            */

            case "irARegistrar":

                /*
                    getRequestDispatcher()

                    Envía el control al JSP.

                    No cambia de página mediante URL.

                    Simplemente entrega la petición al archivo JSP para que construya la vista.
                */

                request.getRequestDispatcher( "/html/a-registrar-productos.jsp").forward(request, response);

                break;

            /*
                CASO 2

                Activar o desactivar un producto.
            */

            case "cambiarDisponibilidad":

                /*
                    Recupera el ID del producto.

                    Llega como texto.
                */

                String idParam = request.getParameter("idProducto");

                /*
                    Recupera el nuevo estado.

                    true

                    false
                */

                String estadoParam = request.getParameter("disponible");

                /*
                    IF

                    Verifica que ambos parámetros existan.

                    Si alguno viene vacío, no se intenta actualizar la base de datos.
                */

                if (idParam != null && estadoParam != null) {

                    /*
                        Convierte el ID
                        de texto a entero.
                    */

                    int idProducto = Integer.parseInt(idParam);

                    /*
                        Convierte el texto

                        "true"

                        "false"

                        a un valor boolean.
                    */

                    boolean nuevoEstado = Boolean.parseBoolean(estadoParam);

                    /*
                        BOOLEAN

                        ¿Qué representa?

                        true

                        Producto disponible.

                        false

                        Producto desactivado.
                    */

                    /*
                        Se llama al DAO.

                        MÉTODO:

                        cambiarDisponibilidadProducto()

                        El DAO ejecuta el UPDATE
                        sobre la base de datos.
                    */

                    boolean exito = productosDao.cambiarDisponibilidadProducto( idProducto, nuevoEstado);

                    /*
                        IF

                        ¿Para qué sirve?

                        Saber si el UPDATE realmente funcionó.

                        true

                        Todo salió bien.

                        false

                        Hubo un problema durante la actualización.
                    */

                    if (exito) {

                        System.out.println( "Producto actualizado correctamente.");

                    } else {

                        System.out.println( "No fue posible actualizar el producto.");

                    }
                }

                /*
                    sendRedirect()

                    Una vez terminado el proceso, vuelve nuevamente al listado.

                    Se hace otra petición nueva al controlador.
                */

                response.sendRedirect( request.getContextPath() + "/ProductosControlador?accion=listar"

                );

                break;

            /*
                CASO 3

                Mostrar el listado de productos.

                También será el caso por defecto.
            */

            case "listar":

            default:

                /*
                    MÉTODO DAO

                    obtenerListaTodosLosProductos()

                    Consulta toda la tabla productos.
                */

                List<Productos> listaProductos = productosDao.obtenerListaTodosLosProductos();

                /*
                    setAttribute()

                    Guarda la lista dentro
                    del request.

                    El JSP podrá acceder
                    mediante:

                    ${listaProductos}
                */

                request.setAttribute( "listaProductos", listaProductos);

                /*
                    Se envía la petición al JSP encargado de construir la tabla.
                */

                request.getRequestDispatcher("/html/a-listar-productos.jsp").forward(request, response);

                break;
        }
    }

    /*
        GET

        Cuando el usuario entra mediante URL, este método simplemente delega toda la lógica al processRequest().
    */

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);

    }

    /*
        POST

        Cuando la petición llega desde  un formulario HTML, también delega todo al processRequest().
    */

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);

    }
}