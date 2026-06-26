/*

    RESPONSABILIDAD DEL CONTROLADOR

    Este controlador se encarga de cerrar la sesión
    del usuario que inició sesión en el sistema.

    Su función consiste en:

    1. Obtener la sesión actual.
    2. Destruir la sesión.
    3. Eliminar la caché del navegador.
    4. Redireccionar nuevamente al login.

*/

package com.controlador;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/*
    Este servlet responderá cuando el navegador invoque la URL:

    /cerrarSesion
*/
@WebServlet(
        name = "cerrarSesion",
        urlPatterns = {"/cerrarSesion"})
public class cerrarSesion extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        /*
        
        PASO 1  OBTENER LA SESIÓN ACTUAL
       

        getSession(false)

        Busca una sesión existente.

        false significa:

        "No crear una sesión nueva si el usuario ya no tiene una."

        Devuelve un objeto HttpSession o null si no existe.
        */

        HttpSession sesion = request.getSession(false);

        /*
    
        PASO 2 VERIFICAR SI EXISTE SESIÓN
        

        if

        Sirve para tomar una decisión.

        En este caso pregunta:

        ¿Existe una sesión activa?

        Si existe, la elimina.
        */

        if (sesion != null) {

            /*
            invalidate()

            Destruye completamente la sesión.

            Elimina todos los atributos guardados.

            Por ejemplo:

            usuarioLogeadoObjeto

            Después de ejecutar invalidate(), el usuario ya no está autenticado.
            */

            sesion.invalidate();

        }

        /*
       
        PASO 3 ELIMINAR LA CACHÉ DEL NAVEGADOR
        

        Estos encabezados HTTP impiden que el navegador permita regresar con el botón "Atrás" después de cerrar sesión.
        */

        /*
        Cache-Control

        Indica al navegador que no guarde ninguna copia de la página.
        */

        response.setHeader( "Cache-Control", "no-cache, no-store, must-revalidate");

        /*
        Pragma

        Compatibilidad con navegadores antiguos.
        */

        response.setHeader( "Pragma", "no-cache");

        /*
        Expires

        Indica que la página expiró inmediatamente.
        */

        response.setDateHeader( "Expires",0);

        /*
     
        PASO 4 REDIRECCIONAR AL LOGIN
        

        sendRedirect()

        Envía al usuario nuevamente
        a la página principal.

        En este caso:

        index.html
        */

        response.sendRedirect("index.html");
    }

    /*
    Información descriptiva del servlet.

    No participa directamente en la lógica del programa.
    */

    @Override
    public String getServletInfo() {

        return "Cerrar sesión";

    }

}