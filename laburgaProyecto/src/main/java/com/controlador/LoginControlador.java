/*

RESPONSABILIDAD

    - Recibir las credenciales digitadas por el usuario.
    - Verificar si el usuario existe.
    - Verificar que el rol seleccionado sea correcto.
    - Crear la sesión del usuario autenticado.
    - Redireccionar al panel correspondiente según el rol.

------------------------------------------------------------

METODO DAO UTILIZADO

UsuarioDao

1. verificarCredencialesIngreso()

Busca en la base de datos si existe un usuario
con la identificación y contraseña digitadas.

Si existe devuelve un objeto Usuario.

Si no existe devuelve null.

*/

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

@WebServlet(
        name = "LoginControlador",
        urlPatterns = {"/LoginControlador"})
public class LoginControlador extends HttpServlet {

    protected void processRequest(HttpServletRequest peticionWeb,
            HttpServletResponse respuestaWeb)
            throws ServletException, IOException {

        /*
       
        PASO 1 RECIBIR LOS DATOS DEL FORMULARIO
       
        */

        System.out.println("DEBUG [1]: Iniciando processRequest...");

        // Captura el usuario digitado.
        String identificacionDigitada = peticionWeb.getParameter("txtUsuario");

        // Captura la contraseña digitada.
        String claveDigitada = peticionWeb.getParameter("txtClave");

        // Captura el rol seleccionado por el usuario.
        String rolEnviado = peticionWeb.getParameter("rolSeleccionado");

        System.out.println(
                "DEBUG [2]: Datos recibidos -> Usuario: "
                + identificacionDigitada
                + ", Rol Enviado: "
                + rolEnviado);

        /*
        
        PASO 2 CONSULTAR EL USUARIO
       

        Se crea el DAO encargado de consultar la base de datos.
        */

        UsuarioDao administradorUsuarios =
                new UsuarioDao();

        /*
        Se llama al método:

        verificarCredencialesIngreso()

        Este método busca el usuario y la contraseña en MySQL.

        Puede devolver:

        • Un objeto Usuario.

        • null si no existe.
        */

        Usuario empleadoLogeado = administradorUsuarios.verificarCredencialesIngreso( identificacionDigitada,claveDigitada);

        /*
       
        PASO 3 VALIDAR SI EL USUARIO EXISTE
       
        */

        /*
        El if pregunta si el objeto Usuario es null.

        Si es null significa que el usuario o la contraseña son incorrectos.

        En ese caso termina el proceso.
        */

        if (empleadoLogeado == null) {

            System.out.println( "DEBUG [3]: Usuario no encontrado.");

            respuestaWeb.sendRedirect( peticionWeb.getContextPath() + "/html/t-login.jsp?error=1");

            return;
        }

        System.out.println( "DEBUG [3]: Usuario encontrado.");

        /*
        
        PASO 4 VALIDAR EL ROL
      
        */

        // Obtiene el rol real almacenado en la base de datos.
        String rolRealEnBD = String.valueOf(empleadoLogeado.getIdRol());

        // Copia el rol recibido del formulario.
        String rolComparar = rolEnviado;

        /*
        Convierte el nombre del rol en su identificador numérico.

        administrador -> 4

        mesero -> 1

        cajero -> 2
        */

        if ("administrador".equalsIgnoreCase(rolEnviado))
            rolComparar = "4";

        else if ("mesero".equalsIgnoreCase(rolEnviado))
            rolComparar = "1";

        else if ("cajero".equalsIgnoreCase(rolEnviado))
            rolComparar = "2";

        /*
        Este if compara ambos roles.

        Si son diferentes significa que el usuario intenta ingresar a unpanel que no le pertenece.

        Se cancela el ingreso.
        */

        if (!rolRealEnBD.equals(rolComparar)) {

            respuestaWeb.sendRedirect( peticionWeb.getContextPath() + "/html/t-login.jsp?error=2");

            return;
        }

        /*
        
        PASO 5 CREAR LA SESIÓN
        
        */

        /*
        Se crea una sesión para guardar la información del usuario autenticado.
        */

        HttpSession sesion = peticionWeb.getSession(true);

        /*
        Guarda el objeto Usuario dentro de la sesión.

        Gracias a esto cualquier JSP puede hacer:

        session.getAttribute("usuarioLogeadoObjeto");
        */

        sesion.setAttribute("usuarioLogeadoObjeto", empleadoLogeado);

        /*
       
        PASO 6 REDIRECCIONAR SEGÚN EL ROL
       
        */

        String context = peticionWeb.getContextPath();

        /*
        Cada if verifica el rol del usuario para abrir el panel correspondiente.
        */

        if (empleadoLogeado.getIdRol() == 4) {

            respuestaWeb.sendRedirect( context + "/html/a-panel-principal-admin.jsp");

        } else if (empleadoLogeado.getIdRol() == 1) {

            respuestaWeb.sendRedirect( context + "/html/m-meserocopy.jsp");

        } else if (empleadoLogeado.getIdRol() == 2) {

            respuestaWeb.sendRedirect( context + "/html/c-cajero.jsp");

        } else {

            /*
            Si el rol no existe, vuelve a la página principal.
            */

            respuestaWeb.sendRedirect(context + "/index.html");
        }
    }

    /*
    Si la petición llega por GET,reutiliza processRequest().
    */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    /*
    Si la petición llega por POST, también reutiliza processRequest().
    */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }
}