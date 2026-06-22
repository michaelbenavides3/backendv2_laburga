/*

ProductosDao-> registrarProductoRetornandoId
ProductoImagenDao-> registrarImagenProducto

    OBJETIVO, ES CREAR UN PRODUCTO NUEVO, Y AGREGAR O ASGINARLE UNA IMAGEN COMO REFERENCIA DEL PRODUCTO



*/
package com.controlador;

import com.dao.ProductosDao;
import com.dao.ProductoImagenDao;

import com.modelo.Productos;
import com.modelo.ProductoImagen;

import java.io.IOException;
import java.io.File;

import java.nio.file.Paths;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet(
        name = "RegistrarProductoControlador",
        urlPatterns = {"/RegistrarProductoControlador"})
@MultipartConfig /*EL CONTROLADOR PUEDE LEER ARCHIVOS SUBIDOS*/
public class RegistrarProductoControlador extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /*
        1. RECUPERAR DATOS DEL FORMULARIO
         */
        String nombreProducto = request.getParameter("txtNombreProducto");

        String descripcionProducto = request.getParameter("txtDescripcionProducto");

        double precioProducto = Double.parseDouble(request.getParameter("txtPrecioProducto"));

        String categoriaProducto = request.getParameter("txtCategoriaProducto");

        Part archivoImagen = request.getPart("imagenProducto");

        if (archivoImagen == null || archivoImagen.getSize() == 0) {

            response.sendRedirect("html/a-registrar-producto.jsp?error=imagen");

            return;
        }

        /*
        2. CREAR OBJETO PRODUCTO
         */
        Productos nuevoProducto = new Productos();

        nuevoProducto.setNombreProducto(nombreProducto);

        nuevoProducto.setDescripcionProducto(descripcionProducto);

        nuevoProducto.setPrecioBaseProducto(precioProducto);

        nuevoProducto.setCategoriaProducto(categoriaProducto);

        nuevoProducto.setDisponibleProducto(true);

        /*
        3. GUARDAR PRODUCTO
         */
        ProductosDao productosDao = new ProductosDao();

        int idProductoGenerado = productosDao.registrarProductoRetornandoId(nuevoProducto);

        /*
        4. SI EL PRODUCTO SE GUARDÓ
         */
        if (idProductoGenerado > 0) {

            /*
            5. OBTENER NOMBRE DE LA IMAGEN
             */
 /*
            String nombreArchivo = Paths.get(archivoImagen.getSubmittedFileName()).getFileName().toString();
             */
            String nombreArchivo
                    = System.currentTimeMillis()
                    + "_"
                    + Paths.get(
                            archivoImagen.getSubmittedFileName())
                            .getFileName()
                            .toString();

            /*
            6. CREAR CARPETA SI NO EXISTE
             */
            /*String rutaProyecto = getServletContext().getRealPath("");*/
            String rutaProyecto = getServletContext().getRealPath("/");

            String carpetaImagenes
                    = rutaProyecto
                    + File.separator
                    + "img"
                    + File.separator
                    + "productos";

            File carpeta = new File(carpetaImagenes);

            if (!carpeta.exists()) {

                carpeta.mkdirs();
            }

            /*
            7. GUARDAR IMAGEN FÍSICAMENTE
             */
            archivoImagen.write(
                    carpetaImagenes
                    + File.separator
                    + nombreArchivo);

            /*
            8. CREAR RUTA PARA BASE DE DATOS
             */
            String rutaImagenBaseDatos
                    = "img/productos/"
                    + nombreArchivo;

            /*
            9. CREAR OBJETO IMAGEN
             */
            ProductoImagen nuevaImagen = new ProductoImagen();

            nuevaImagen.setIdProducto(idProductoGenerado);

            nuevaImagen.setRutaArchivoImagen(rutaImagenBaseDatos);

            nuevaImagen.setImagenActualizada(true);

            /*
            10. GUARDAR IMAGEN EN BASE DE DATOS
             */
            ProductoImagenDao productoImagenDao = new ProductoImagenDao();

            productoImagenDao.registrarImagenProducto(nuevaImagen);

            /*
            11. REDIRECCIONAR
             */
 /*
            response.sendRedirect( "html/a-listar-productos.jsp?producto=registrado");
             */
            response.sendRedirect("ListarProductosControlador?producto=registrado");

        } else {

            response.sendRedirect("html/a-registrar-producto.jsp?error=registro");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

}
