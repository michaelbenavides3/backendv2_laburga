/*

    RESPONSABILIDAD DEL CONTROLADOR

    Este controlador registra un nuevo producto en el sistema.

    Su trabajo consiste en:

    1. Recibir los datos enviados desde el formulario.
    2. Validar que la información sea correcta.
    3. Registrar el producto en la base de datos.
    4. Guardar la imagen físicamente en el servidor.
    5. Registrar la ruta de la imagen en la base de datos.
    6. Redireccionar al listado de productos.

    --------------------------------------------------

    MÉTODOS DAO UTILIZADOS

    ProductosDao

    -> registrarProductoRetornandoId()
       Inserta el producto en la base de datos
       y devuelve el ID generado automáticamente.

    ProductoImagenDao

    -> registrarImagenProducto()
       Guarda la ruta de la imagen asociada al producto.

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


/*
    @MultipartConfig

    Permite que el controlador pueda recibir archivos,
    por ejemplo imágenes enviadas desde un formulario.
 */
@WebServlet(
        name = "RegistrarProductoControlador",
        urlPatterns = {"/RegistrarProductoControlador"})
@MultipartConfig
public class RegistrarProductoControlador extends HttpServlet {

    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        /*
     
        PASO 1  RECUPERAR DATOS DEL FORMULARIO
       

        request.getParameter()

        Obtiene los datos escritos por el usuario
        dentro del formulario HTML.

        request.getPart()

        Obtiene el archivo de imagen enviado.
         */
 /*
        request--> representa la peticion que hizo el navegador al servidor
        getParameter--> es el metodo, busca el formulario el valor del atributo, (name="txtNombreProducto")
         */
        String nombreProducto = request.getParameter("txtNombreProducto");

        String descripcionProducto = request.getParameter("txtDescripcionProducto");

        String precioTexto = request.getParameter("txtPrecioProducto");

        String categoriaProducto = request.getParameter("txtCategoriaProducto");

        // Normalizar categoría: eliminar espacios y convertir a minúsculas
        // Ejemplo: "  SOPAS  " → "sopas"
        // Ejemplo: "HaMbUrGuEsAs" → "hamburguesas"
        // Ejemplo: "  Papas Locas  " → "papas locas"
        if (categoriaProducto != null) {
            categoriaProducto = categoriaProducto.trim().toLowerCase();
        }

        Part archivoImagen = request.getPart("imagenProducto");

        /*
       
        PASO 2 VALIDACIONES
        

        Antes de guardar cualquier dato, primero se valida que toda la información sea correcta.
         */
 /*
        Valida que el nombre solamente tenga letras.
         */
        if (nombreProducto == null || !nombreProducto.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+")) {

            response.sendRedirect(request.getContextPath() + "/html/a-registrar-productos.jsp?error=nombre");

            return;

        }

        /*
        ¿Para qué sirve el if?

        Permite tomar una decisión.

        Si el nombre es incorrecto, no continúa el registro.

        return

        Finaliza inmediatamente el método para impedir que continúe guardando información incorrecta.
         */
 /*
        Validar longitud mínima.
        trim, es un metodo que elimina los espacios en blanco inciando y finalizadno el texto
         */
        if (nombreProducto.trim().length() < 3) {

            response.sendRedirect(request.getContextPath() + "/html/a-registrar-productos.jsp?error=nombreCorto");

            return;

        }


        /*
        Validar descripción.
        la descripcion no puede venir vacia 
         */
        if (descripcionProducto == null || descripcionProducto.trim().isEmpty()) {

            response.sendRedirect(request.getContextPath() + "/html/a-registrar-productos.jsp?error=descripcion");

            return;

        }

        /*
        la descipcion ademas debe contener al menos una letra
         */
        if (!descripcionProducto.matches(".*[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ].*")) {

            response.sendRedirect(request.getContextPath() + "/html/a-registrar-productos.jsp?error=descripcion");

            return;
        }


        /*
        Validar categoría.
         */
        // Validación categoría — va aquí, después de normalizar
        if (categoriaProducto == null || !categoriaProducto.matches("[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+")) {
            response.sendRedirect(request.getContextPath() + "/html/a-registrar-productos.jsp?error=categoria");
            return;
        }

        if (categoriaProducto.length() < 3) {
            response.sendRedirect(request.getContextPath() + "/html/a-registrar-productos.jsp?error=categoriaCorta");
            return;
        }


        /*
        Validar imagen.
         */
        if (archivoImagen == null || archivoImagen.getSize() == 0) {

            response.sendRedirect(request.getContextPath() + "/html/a-registrar-productos.jsp?error=imagen");

            return;

        }

        /*
            TODO LO ANTERIOR DEBE PASAR PARA LLEGAR A ESTE PASO DONDE SE CREA EL NUEVO PRODUCTOS Y VIAJAN LOS DATOS A MYSQL
        
        PASO 3  CREAR OBJETO PRODUCTO
        

        Se construye un objeto Productos con toda la información recibida.
         */
        double precioProducto = Double.parseDouble(precioTexto);

        Productos nuevoProducto = new Productos();
        /*por medio de set, se utiliza para asignale un valor a una variable*/
        nuevoProducto.setNombreProducto(nombreProducto.trim());

        nuevoProducto.setDescripcionProducto(descripcionProducto.trim());

        nuevoProducto.setPrecioBaseProducto(precioProducto);

        nuevoProducto.setCategoriaProducto(categoriaProducto.trim());

        nuevoProducto.setDisponibleProducto(true);

        /*
        
        PASO 4REGISTRAR PRODUCTO
        

        Se crea el DAO encargado de acceder a la base de datos.
         */
        ProductosDao productosDao = new ProductosDao();

        /*
        Método DAO

        registrarProductoRetornandoId()

        Inserta el producto  y devuelve el ID generado.
         */
        int idProductoGenerado = productosDao.registrarProductoRetornandoId(nuevoProducto);

        /*
        
        PASO 5 VERIFICAR SI EL PRODUCTO SE REGISTRÓ
        

        Si el ID es mayor que cero, significa que el registro fue exitoso.
        si idProductoGenerado es mayor que 0 entra en la condicional if 
         */
        if (idProductoGenerado > 0) {

            /*
            Crear un nombre único para evitar imágenes repetidas.
             */
            String nombreArchivo
                    = System.currentTimeMillis() /*regresa la fehca y hora actual*/
                    + "_"
                    + Paths.get(
                            archivoImagen.getSubmittedFileName()) /*obitne el nombre original del archivo que escogio el usuario ej(burguermarrano.png)*/
                            .getFileName() /*extrae unicamente el nombre del archivo obviando la ruta*/
                            .toString();


            /*
            Obtener la ruta física del proyecto.
             */
            String rutaProyecto = getServletContext().getRealPath("/");


            /*
            Crear carpeta destino.
             */
            String carpetaImagenes
                    = rutaProyecto
                    + File.separator
                    + "img"
                    + File.separator
                    + "productos";

            File carpeta = new File(carpetaImagenes);


            /*
            Si la carpeta no existe, la crea automáticamente.
             */
            if (!carpeta.exists()) {

                carpeta.mkdirs();

            }


            /*
            Guarda físicamente la imagen.
             */
            archivoImagen.write(
                    carpetaImagenes
                    + File.separator
                    + nombreArchivo);


            /*
            Ruta que quedará registrada dentro de la base de datos.
             */
            String rutaImagenBaseDatos = "img/productos/" + nombreArchivo;


            /*
            Crear objeto imagen. estos datos viene del modelo
             */
            ProductoImagen nuevaImagen = new ProductoImagen();

            nuevaImagen.setIdProducto(idProductoGenerado);

            nuevaImagen.setRutaArchivoImagen(rutaImagenBaseDatos);

            nuevaImagen.setImagenActualizada(true);


            /*
            Crear DAO de imágenes.
             */
            ProductoImagenDao productoImagenDao = new ProductoImagenDao();


            /*
            Método DAO

            registrarImagenProducto()

            Guarda la ruta de la imagen relacionada con el producto.
             */
            productoImagenDao.registrarImagenProducto(nuevaImagen);


            /*
            Registro exitoso.
             */
            response.sendRedirect("ListarProductosControlador?producto=registrado");

        } else {

            /*
            Si el producto no pudo registrarse, vuelve al formulario mostrando error.
             */
            response.sendRedirect(request.getContextPath() + "/html/a-registrar-productos.jsp?error=registro");

        }

    }

    /*
    GET Llama al mismo proceso principal.
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);

    }

    /*
    POST También llama al proceso principal.
     */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);

    }

}
