/*

RESPONSABILIDAD

- Buscar un producto por su ID.
- Enviar la información del producto al formulario de edición.
- Recibir los nuevos datos modificados por el administrador.
- Actualizar la información del producto en la base de datos.
- Redireccionar nuevamente al listado de productos.

METODOS DAO UTILIZADOS

1. obtenerProductoPorId()
   Busca un producto específico utilizando su identificador.

2. actualizarProducto()
   Actualiza en la base de datos la información modificada del producto.

*/

package com.controlador;

import com.dao.ProductosDao;
import com.modelo.Productos;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
Este controlador atiende todas las peticiones relacionadas con la edición de productos.
*/
@WebServlet(
        name = "EditarProductoControlador",
        urlPatterns = {"/EditarProductoControlador"})
public class EditarProductoControlador extends HttpServlet {

    /*
    processRequest contiene toda la lógica del controlador.
    Tanto doGet como doPost llaman este mismo método.
    */
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        /*
        Se crea el DAO que permitirá consultar y actualizar
        productos en la base de datos.
        */
        ProductosDao productosDao = new ProductosDao();

        /*
        Se captura el parámetro "accion".

        Este parámetro determina qué operación realizará
        el controlador.

        Puede venir:

        accion = guardar

        o

        accion = null

        dependiendo desde dónde fue llamado.
        */
        String accion = request.getParameter("accion");

        /*
        SI LA ACCIÓN ES "guardar"

        significa que el administrador ya terminó de editar el formulario y ahora desea guardar los cambios.
        */
        if ("guardar".equals(accion)) {

            /*
            Se reciben todos los datos enviados por el formulario.
            */

            int idProducto = Integer.parseInt( request.getParameter("idProducto"));

            String nombreProducto = request.getParameter("nombreProducto");

            String descripcionProducto = request.getParameter("descripcionProducto");

            double precioProducto = Double.parseDouble( request.getParameter("precioProducto"));

            String categoriaProducto = request.getParameter("categoriaProducto");

            /*
            Se crea un objeto Producto.

            Este objeto almacenará toda la información nueva antes de enviarla al DAO.
            */
            Productos productoActualizado = new Productos();

            /*
            Se cargan todos los nuevos valores dentro del objeto.
            */
            productoActualizado.setIdProducto(idProducto);

            productoActualizado.setNombreProducto(nombreProducto);

            productoActualizado.setDescripcionProducto(descripcionProducto);

            productoActualizado.setPrecioBaseProducto(precioProducto);

            productoActualizado.setCategoriaProducto(categoriaProducto);

            /*
            Se llama al método actualizarProducto().

            Este método ejecuta el UPDATE sobre la base de datos.
            */
            productosDao.actualizarProducto(productoActualizado);

            /*
            Una vez actualizado el producto, se vuelve a cargar el listado de productos.

            Esto permite visualizar inmediatamente los cambios realizados.
            */
            response.sendRedirect( request.getContextPath() + "/ListarProductosControlador?producto=editado");

        } else {

            /*
            SI NO ES "guardar"

            significa que apenas se abrió el formulario para editar el producto.

            En este caso debemos buscar la información existente del producto.
            */

            int idProducto =
                    Integer.parseInt( request.getParameter("idProducto"));

            /*
            Se consulta el producto por su ID.
            */
            Productos producto = productosDao.obtenerProductoPorId(idProducto);

            /*
            Se envía el objeto encontrado al JSP.

            El formulario utilizará esta información para llenar automáticamente los campos.
            */
            request.setAttribute( "productoSeleccionado", producto);

            /*
            Se abre el formulario de edición.
            */
            request.getRequestDispatcher( "/html/a-editar-productos.jsp") .forward(request, response);
        }
    }

    /*
    Atiende las solicitudes GET.

    Normalmente se utiliza cuando el administrador hace clic en "Editar".
    */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    /*
    Atiende las solicitudes POST.

    Normalmente se utiliza cuando el administrador presiona el botón Guardar.
    */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }
}