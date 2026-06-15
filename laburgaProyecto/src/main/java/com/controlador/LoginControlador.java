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

        System.out.println("DEBUG [1]: Iniciando processRequest...");

        // 1. Obtener datos
        String identificacionDigitada = peticionWeb.getParameter("txtUsuario");
        String claveDigitada = peticionWeb.getParameter("txtClave");
        String rolEnviado = peticionWeb.getParameter("rolSeleccionado");

        System.out.println("DEBUG [2]: Datos recibidos -> Usuario: " + identificacionDigitada + ", Rol Enviado: " + rolEnviado);

        UsuarioDao administradorUsuarios = new UsuarioDao();
        Usuario empleadoLogeado = administradorUsuarios.verificarCredencialesIngreso(identificacionDigitada, claveDigitada);

        // 2. Validación de existencia
        if (empleadoLogeado == null) {
            System.out.println("DEBUG [3]: Usuario no encontrado (null). Redirigiendo a error=1");
            respuestaWeb.sendRedirect(peticionWeb.getContextPath() + "/html/t-login.jsp?error=1");
            return;
        }
        
        System.out.println("DEBUG [3]: Usuario encontrado: " + empleadoLogeado.getIdUsuario() + " con Rol BD: " + empleadoLogeado.getIdRol());

        // 3. Validación de rol (Normalización)
        String rolRealEnBD = String.valueOf(empleadoLogeado.getIdRol());
        String rolComparar = rolEnviado;
        
        // Convertimos nombres a IDs si es necesario
        if ("administrador".equalsIgnoreCase(rolEnviado)) rolComparar = "4";
        else if ("mesero".equalsIgnoreCase(rolEnviado)) rolComparar = "1";
        else if ("cajero".equalsIgnoreCase(rolEnviado)) rolComparar = "2";

        System.out.println("DEBUG [4]: Comparando -> RolBD(" + rolRealEnBD + ") == RolComparar(" + rolComparar + ")");

        if (!rolRealEnBD.equals(rolComparar)) {
            System.out.println("DEBUG [5]: Roles no coinciden. Redirigiendo a error=2");
            respuestaWeb.sendRedirect(peticionWeb.getContextPath() + "/html/t-login.jsp?error=2");
            return;
        }

        // 4. Sesión
        System.out.println("DEBUG [6]: Roles coinciden. Creando sesión...");
        HttpSession sesion = peticionWeb.getSession(true);
        sesion.setAttribute("usuarioLogeadoObjeto", empleadoLogeado);

        // 5. Redirecciones
        String context = peticionWeb.getContextPath();
        System.out.println("DEBUG [7]: Redirigiendo a panel según rol: " + empleadoLogeado.getIdRol());
        
        if (empleadoLogeado.getIdRol() == 4) {
            respuestaWeb.sendRedirect(context + "/html/a-panel-principal-admin.jsp");
        } else if (empleadoLogeado.getIdRol() == 1) {
            respuestaWeb.sendRedirect(context + "/html/m-meserocopy.jsp");
        } else if (empleadoLogeado.getIdRol() == 2) {
            respuestaWeb.sendRedirect(context + "/html/c-cajero.jsp");
        } else {
            respuestaWeb.sendRedirect(context + "/index.html");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException { processRequest(request, response); }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException { processRequest(request, response); }
}