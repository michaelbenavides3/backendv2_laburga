/*

RESPONSABILIDAD

    - Consultar todos los usuarios registrados en el sistema.
    - Enviar la lista de usuarios al JSP.
    - Mostrar la información en la tabla de administración.

------------------------------------------------------------

METODO DAO UTILIZADO

UsuarioDao

1. obtenerListaUsuariosConRol()

Consulta todos los usuarios registrados junto con
el rol que tiene asignado cada uno.

*/

package com.controlador;

import com.dao.UsuarioDao;
import com.modelo.Usuario;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ListarUsuariosControlador")
public class ListarUsuariosControlador extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest peticionWeb,
            HttpServletResponse respuestaWeb)
            throws ServletException, IOException {

        /*
      
        PASO 1 MENSAJE DE CONTROL
        
        Se imprime un mensaje en consola para verificar que el controlador fue ejecutado correctamente.
        */
        System.out.println("iniciando consulta de usuarios");

        /*
       
        PASO 2 CREAR EL DAO
        

        Se crea el objeto UsuarioDao para poder acceder a los métodos que consultan la base de datos.
        */
        UsuarioDao administradorUsuarios = new UsuarioDao();

        /*
        
        PASO 3 CONSULTAR LOS USUARIOS
        

        Se llama al método:

        obtenerListaUsuariosConRol()

        Este método consulta MySQL y devuelve una lista con todos los usuarios registrados y su rol.
        */
        List<Usuario> listaDeUsuariosEncontrados = administradorUsuarios.obtenerListaUsuariosConRol();

        /*
        Muestra en consola cuántos usuarios fueron encontrados.
        Esto sirve únicamente para verificar que la consulta funcionó correctamente.
        */
        System.out.println("cantidad de usuarios encontrados: "  + listaDeUsuariosEncontrados.size());

        /*
       
        PASO 4 ENVIAR LA LISTA AL JSP
        

        setAttribute() guarda la lista dentro del objeto request utilizando el nombre "listaUsuarios".

        Posteriormente el JSP podrá recuperar la lista con: request.getAttribute("listaUsuarios");
        */
        peticionWeb.setAttribute("listaUsuarios",listaDeUsuariosEncontrados);

        /*
        Se imprime en consola el atributo enviado al JSP.

        Solo se utiliza para verificar durante el desarrollo que el atributo fue almacenado correctamente.
        */
        System.out.println( "ATRIBUTO ENVIADO -> " + peticionWeb.getAttribute("listaUsuarios"));

        /*
       
        PASO 5 ABRIR EL JSP
       

        getRequestDispatcher()

        Busca el archivo JSP que mostrará la lista
        de usuarios.

        En este caso:

        /html/a-listar-usuarios.jsp
        */
        peticionWeb.getRequestDispatcher(
                "/html/a-listar-usuarios.jsp")

                /*
                forward()

                Transfiere el control hacia el JSP utilizando
                el mismo request y response.

                Gracias al forward():

                • El JSP recibe el atributo listaUsuarios.

                • No se crea una nueva petición HTTP.

                • El JSP puede mostrar inmediatamente
                  la información obtenida de la base de datos.
                */
                .forward(peticionWeb, respuestaWeb);
    }
}