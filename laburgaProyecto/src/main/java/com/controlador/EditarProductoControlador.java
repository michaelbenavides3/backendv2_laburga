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

@WebServlet(
        name = "EditarProductoControlador",
        urlPatterns = {"/EditarProductoControlador"})
public class EditarProductoControlador extends HttpServlet {

    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        ProductosDao productosDao = new ProductosDao();

        String accion = request.getParameter("accion");

        /*
        SI LA ACCIÓN ES "guardar"
        el administrador terminó de editar y desea guardar los cambios
        */
        if ("guardar".equals(accion)) {

            /*
            PASO 1 — RECUPERAR DATOS DEL FORMULARIO
            */
            int idProducto = Integer.parseInt(request.getParameter("idProducto"));

            String nombreProducto      = request.getParameter("nombreProducto");
            String descripcionProducto = request.getParameter("descripcionProducto");
            double precioProducto      = Double.parseDouble(request.getParameter("precioProducto"));
            String categoriaProducto   = request.getParameter("categoriaProducto");

            /*
            PASO 2 — NORMALIZAR CATEGORÍA
            sin importar si viene en mayúsculas o minúsculas, siempre se guarda en minúsculas
            ejemplo: "SOPAS" → "sopas" / "Hamburguesas" → "hamburguesas"
            */
            if (categoriaProducto != null) {
                categoriaProducto = categoriaProducto.trim().toLowerCase();
            }

            /*
            PASO 3 — CREAR OBJETO CON LOS NUEVOS DATOS
            */
            Productos productoActualizado = new Productos();

            productoActualizado.setIdProducto(idProducto);
            productoActualizado.setNombreProducto(nombreProducto);
            productoActualizado.setDescripcionProducto(descripcionProducto);
            productoActualizado.setPrecioBaseProducto(precioProducto);
            productoActualizado.setCategoriaProducto(categoriaProducto);

            /*
            PASO 4 — ACTUALIZAR EN BASE DE DATOS ejecuta el UPDATE sobre la tabla productos
            */
            productosDao.actualizarProducto(productoActualizado);

            /*
            PASO 5 — REDIRECCIONAR AL LISTADO permite visualizar inmediatamente los cambios realizados
            */
            response.sendRedirect(
                    request.getContextPath() + "/ListarProductosControlador?producto=editado");

        } else {

            /*
            SI NO ES "guardar" el administrador apenas abrió el formulario para editar buscamos la información existente del producto y la enviamos al JSP
            */

            /*
            PASO 1 — RECUPERAR ID DEL PRODUCTO
            */
            int idProducto = Integer.parseInt(request.getParameter("idProducto"));

            /*
            PASO 2 — CONSULTAR PRODUCTO EN BD
            */
            Productos producto = productosDao.obtenerProductoPorId(idProducto);

            /*
            PASO 3 — ENVIAR AL JSP  el formulario usará esta información para llenar automáticamente los campos
            */
            request.setAttribute("productoSeleccionado", producto);

            /*
            PASO 4 — ABRIR FORMULARIO DE EDICIÓN
            */
            request.getRequestDispatcher("/html/a-editar-productos.jsp")
                   .forward(request, response);
        }
    }

    /*
    Atiende solicitudes GET se usa cuando el administrador hace clic en "Editar"
    */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /*
    Atiende solicitudes POST se usa cuando el administrador presiona el botón "Guardar"
    */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}