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
@MultipartConfig
public class RegistrarProductoControlador extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /*
        1. RECUPERAR DATOS DEL FORMULARIO
         */
        String nombreProducto      = request.getParameter("txtNombreProducto");
        String descripcionProducto = request.getParameter("txtDescripcionProducto");
        String precioTexto         = request.getParameter("txtPrecioProducto");
        String categoriaProducto   = request.getParameter("txtCategoriaProducto");
        Part archivoImagen         = request.getPart("imagenProducto");

        /*
        2. VALIDACIONES
         */
        // Nombre: solo letras y espacios
        if (nombreProducto == null || !nombreProducto.trim()
                .matches("[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+")) {
            response.sendRedirect(request.getContextPath()
                    + "/html/a-registrar-productos.jsp?error=nombre");
            return;
        }

        // Nombre mínimo 3 caracteres
        if (nombreProducto.trim().length() < 3) {
            response.sendRedirect(request.getContextPath()
                    + "/html/a-registrar-productos.jsp?error=nombreCorto");
            return;
        }

        // Descripción: si se llena, debe tener al menos una letra
        if (descripcionProducto != null
                && !descripcionProducto.trim().isEmpty()
                && !descripcionProducto.matches(".*[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ].*")) {
            response.sendRedirect(request.getContextPath()
                    + "/html/a-registrar-productos.jsp?error=descripcion");
            return;
        }

        // Categoría: solo letras y espacios
        if (categoriaProducto == null || !categoriaProducto.trim()
                .matches("[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+")) {
            response.sendRedirect(request.getContextPath()
                    + "/html/a-registrar-productos.jsp?error=categoria");
            return;
        }

        // Imagen obligatoria
        if (archivoImagen == null || archivoImagen.getSize() == 0) {
            response.sendRedirect(request.getContextPath()
                    + "/html/a-registrar-productos.jsp?error=imagen");
            return;
        }

        /*
        3. CREAR OBJETO PRODUCTO
         */
        double precioProducto = Double.parseDouble(precioTexto);

        Productos nuevoProducto = new Productos();
        nuevoProducto.setNombreProducto(nombreProducto.trim());
        nuevoProducto.setDescripcionProducto(descripcionProducto.trim());
        nuevoProducto.setPrecioBaseProducto(precioProducto);
        nuevoProducto.setCategoriaProducto(categoriaProducto.trim());
        nuevoProducto.setDisponibleProducto(true);

        /*
        4. GUARDAR PRODUCTO
         */
        ProductosDao productosDao = new ProductosDao();
        int idProductoGenerado = productosDao.registrarProductoRetornandoId(nuevoProducto);

        /*
        5. SI EL PRODUCTO SE GUARDÓ
         */
        if (idProductoGenerado > 0) {

            String nombreArchivo = System.currentTimeMillis()
                    + "_"
                    + Paths.get(archivoImagen.getSubmittedFileName())
                            .getFileName().toString();

            String rutaProyecto   = getServletContext().getRealPath("/");
            String carpetaImagenes = rutaProyecto + File.separator
                    + "img" + File.separator + "productos";

            File carpeta = new File(carpetaImagenes);
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            archivoImagen.write(carpetaImagenes + File.separator + nombreArchivo);

            String rutaImagenBaseDatos = "img/productos/" + nombreArchivo;

            ProductoImagen nuevaImagen = new ProductoImagen();
            nuevaImagen.setIdProducto(idProductoGenerado);
            nuevaImagen.setRutaArchivoImagen(rutaImagenBaseDatos);
            nuevaImagen.setImagenActualizada(true);

            ProductoImagenDao productoImagenDao = new ProductoImagenDao();
            productoImagenDao.registrarImagenProducto(nuevaImagen);

            response.sendRedirect("ListarProductosControlador?producto=registrado");

        } else {
            response.sendRedirect(request.getContextPath()
                    + "/html/a-registrar-productos.jsp?error=registro");
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