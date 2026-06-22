/*
    encargado de traer la lista de personas que trabajan en el restaurante (o que tienen acceso al sistema).

    Metodo Usuariodao-> obtenerListaUsuariosConRol


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
    protected void doGet(HttpServletRequest peticionWeb,HttpServletResponse respuestaWeb)
            throws ServletException, IOException {
        

        System.out.println("iniciando consulta de usuarios");

        // Creo el administrador de acceso a datos
        UsuarioDao administradorUsuarios = new UsuarioDao();

        // Solicito la lista completa de usuarios
        List<Usuario> listaDeUsuariosEncontrados = administradorUsuarios.obtenerListaUsuariosConRol();

        System.out.println("cantidad de usuarios encontrados: " + listaDeUsuariosEncontrados.size());
        // Guardo la lista dentro de la petición
        // para enviarla al JSP
        peticionWeb.setAttribute("listaUsuarios",listaDeUsuariosEncontrados);
        
        System.out.println(
        "ATRIBUTO ENVIADO -> "
        + peticionWeb.getAttribute("listaUsuarios"));

        // Redirecciono al JSP encargado de mostrar la tabla
        peticionWeb.getRequestDispatcher("/html/a-listar-usuarios.jsp").forward(peticionWeb,respuestaWeb);
    }
}