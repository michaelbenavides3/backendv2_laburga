/*
    
    CONTROLADOR: CambiarDisponibilidadProductoControlador
    
    RESPONSABILIDAD:
        Gestiona la activación y desactivación de productos del menú.
        Cuando el administrador hace clic en "Activar" o "Desactivar",
        este controlador recibe la solicitud, actualiza el estado en la
        base de datos y redirige al listado con un mensaje de confirmación.

    FLUJO:
        Admin hace clic en "Activar/Desactivar"
                ↓
        CambiarDisponibilidadProductoControlador (doGet)
                ↓
        ProductosDao.cambiarDisponibilidadProducto(id, estado)
                ↓
        UPDATE productos SET disponible_producto = ? WHERE id_producto = ?
                ↓
        Redirige a ProductosControlador con mensaje de éxito

    MÉTODOS DAO UTILIZADOS:
        ProductosDao.cambiarDisponibilidadProducto()
            → Actualiza el campo disponible_producto en MySQL
            → true  = producto visible en el menú del mesero
            → false = producto oculto en el menú del mesero
*/
package com.controlador;

import com.dao.ProductosDao;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(
        name = "CambiarDisponibilidadProductoControlador",
        urlPatterns = {"/CambiarDisponibilidadProductoControlador"})
public class CambiarDisponibilidadProductoControlador extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println(">>> ENTRÓ AL CONTROLADOR CambiarDisponibilidadProducto");

        /*
        
        1. RECUPERAR PARÁMETROS DE LA URL
           - idProducto: identifica qué producto se va a activar o desactivar
           - estado: el NUEVO estado que queremos asignar
               true  = activar el producto (aparece en el menú)
               false = desactivar el producto (desaparece del menú)

           Ejemplo de URL que llega:
           /CambiarDisponibilidadProductoControlador?idProducto=3&estado=false
        
        */
        int identificadorProducto = Integer.parseInt(
                request.getParameter("idProducto"));

        // parseBoolean convierte el texto "true" o "false" a boolean
        boolean nuevoEstadoDisponibilidad = Boolean.parseBoolean(
                request.getParameter("estado"));

        // logs de diagnóstico para verificar qué llegó
        System.out.println("DEBUG: idProducto recibido → " + identificadorProducto);
        System.out.println("DEBUG: nuevo estado recibido → " + nuevoEstadoDisponibilidad);

        /*
        
        2. ACTUALIZAR DISPONIBILIDAD EN LA BASE DE DATOS
           - llamamos al DAO que ejecuta el UPDATE en MySQL
           - si nuevoEstado = true  → disponible_producto = 1 (aparece en menú)
           - si nuevoEstado = false → disponible_producto = 0 (desaparece del menú)
        
        */
        ProductosDao productosDao = new ProductosDao();
        productosDao.cambiarDisponibilidadProducto( identificadorProducto, nuevoEstadoDisponibilidad);

        System.out.println("DEBUG: Disponibilidad actualizada correctamente.");

        /*
        
        3. CONSTRUIR MENSAJE DE CONFIRMACIÓN
           - si el nuevo estado es true  → mensaje "activado"
           - si el nuevo estado es false → mensaje "desactivado"
           - este mensaje lo recibe el JSP para mostrar el alert al admin
        
        */
        String mensajeConfirmacion = nuevoEstadoDisponibilidad ? "activado" : "desactivado";

        /*
        
        4. REDIRIGIR AL LISTADO DE PRODUCTOS
           - usamos contextPath para que funcione sin importar desde dónde se sirva el controlador
           - el parámetro "producto" activa el alert en a-listar-productos.jsp
           - ejemplo: /laburgaProyecto/ProductosControlador?producto=desactivado
       
        */
        response.sendRedirect(
                request.getContextPath()
                + "/ProductosControlador?producto=" + mensajeConfirmacion);
    }

    /*
    
    NOTA: Solo implementamos doGet porque el botón
    en el JSP es un enlace <a href="...">, no un
    formulario POST. Los enlaces siempre son GET.
   
    */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}