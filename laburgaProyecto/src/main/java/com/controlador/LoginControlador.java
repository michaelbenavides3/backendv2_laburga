package com.controlador;

import com.dao.UsuarioDao;
import com.modelo.Usuario;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "LoginControlador", urlPatterns = {"/LoginControlador"})
public class LoginControlador extends HttpServlet {

    protected void processRequest(HttpServletRequest peticionWeb, HttpServletResponse respuestaWeb) 
            throws ServletException, IOException {
        
        String identificacionDigitada = peticionWeb.getParameter("txtUsuario");
        String claveDigitada = peticionWeb.getParameter("txtClave");

        UsuarioDao administradorUsuarios = new UsuarioDao();
        Usuario empleadoLogeado = administradorUsuarios.verificarCredencialesIngreso(identificacionDigitada, claveDigitada);

        if (empleadoLogeado != null) {
            // 1. INVALIDAMOS SESIÓN PREVIA: Por seguridad, si ya había alguien, la matamos primero
            HttpSession sesionVieja = peticionWeb.getSession(false);
            if (sesionVieja != null) {
                sesionVieja.invalidate();
            }

            // 2. CREAMOS SESIÓN NUEVA Y LIMPIA
            HttpSession sesionActivaRestaurante = peticionWeb.getSession(true);
            
            // 3. GUARDAMOS EL OBJETO (Esta llave debe ser idéntica a la del PedidoControlador)
            sesionActivaRestaurante.setAttribute("usuarioLogeadoObjeto", empleadoLogeado);
            
            // Log de control
            System.out.println("DEBUG: Sesión iniciada para Usuario ID: " + empleadoLogeado.getIdUsuario());

            // Redirecciones
            if (empleadoLogeado.getIdRol() == 4) {
                respuestaWeb.sendRedirect("html/a-panel-principal-admin.html");
            } else if (empleadoLogeado.getIdRol() == 1) {
                respuestaWeb.sendRedirect("html/m-meserocopy.jsp");
            } else if (empleadoLogeado.getIdRol() == 2) {
                respuestaWeb.sendRedirect("html/c-cajero.jsp");
            } else {
                respuestaWeb.sendRedirect("index.html");
            }

        } else {
            respuestaWeb.sendRedirect("html/t-login.jsp?error=1");
        }
    }

    @Override
    protected void doGet(HttpServletRequest peticionWeb, HttpServletResponse respuestaWeb)
            throws ServletException, IOException { processRequest(peticionWeb, respuestaWeb); }

    @Override
    protected void doPost(HttpServletRequest peticionWeb, HttpServletResponse respuestaWeb)
            throws ServletException, IOException { processRequest(peticionWeb, respuestaWeb); }
}