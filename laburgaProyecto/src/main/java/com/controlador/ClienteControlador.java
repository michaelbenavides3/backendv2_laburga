package com.controlador;

// Importamos el DAO y el Modelo de Labur-GA correspondientes
import com.dao.ClienteDao;
import com.modelo.Cliente;

// Importamos las librerías web modernas oficiales de Jakarta (Compatibles con Tomcat 10+)
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * CONTROLADOR DE CLIENTES - LABUR-GA
 * Actúa como el intermediario oficial entre la vista del formulario web 
 * y la persistencia de datos en MySQL (ClienteDao). Mapeado en la ruta /registrarCliente.
 */
@WebServlet("/registrarCliente")
public class ClienteControlador extends HttpServlet {

    // Capta las peticiones de envío seguro de datos (Método POST del formulario)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // CODIFICACIÓN DE SEGURIDAD: Forzamos la lectura en UTF-8 para que las tildes y eñes de Bucaramanga viajen sin romperse
        request.setCharacterEncoding("UTF-8");

        // RECOLECCIÓN DE DATOS: Atrapamos los textos mapeados en los atributos 'name' del formulario web
        String nombreFormulario = request.getParameter("nombre");
        String documentoFormulario = request.getParameter("documento");
        String telefonoFormulario = request.getParameter("telefono");
        String correoFormulario = request.getParameter("correo");
        
        // INVOCACIÓN DE LA CAPA MODELO: Instanciamos el DAO para ejecutar los procesos transaccionales
        ClienteDao dao = new ClienteDao();

        // VALIDACIÓN 1: Verifica que el número telefónico tenga exactamente 10 dígitos numéricos
        if (telefonoFormulario == null || telefonoFormulario.length() != 10 || !telefonoFormulario.matches("\\d+")) {
            System.out.println("=== Error: El teléfono ingresado no cuenta con el formato correcto ===");
            // Si falla, regresamos al formulario pasándole un parámetro para pintar la alerta visual
            response.sendRedirect("html/m-formulario-clientenuevo-mesero.jsp?estado=telefono_invalido");
            return; // Rompemos el flujo para evitar que intente registrar en la BD
        }
        
        // VALIDACIÓN 2: Consultamos si la cédula ya existe en la base de datos
        if (dao.existeCliente(documentoFormulario)) {
            System.out.println("=== Registro denegado: La CC " + documentoFormulario + " ya existe en MySQL ===");
            // Redirige al panel informando que el cliente ya estaba registrado
            response.sendRedirect("html/m-meserocopy.jsp?estado=duplicado");
            return; // Rompemos el flujo de ejecución
        }

        // EMPAQUETADO DEL OBJETO: Creamos una instancia del modelo Cliente con los datos limpios y validados
        Cliente nuevoCliente = new Cliente(nombreFormulario, documentoFormulario, telefonoFormulario, correoFormulario);
        
        // Ejecutamos la inserción en cascada (Tablas: clientes, telefonoUsuarios, correoUsuario)
        boolean operacionActualizacionExitosa = dao.registrarClienteCompleto(nuevoCliente);

        // ENRUTAMIENTO INTELIGENTE: Evaluamos el resultado de MySQL para decidir la respuesta visual
        if (operacionActualizacionExitosa) {
            // Caso de Éxito total
            System.out.println("=== Cliente registrado con éxito en la base de datos de Labur-GA. ===");
            response.sendRedirect("html/m-meserocopy.jsp?estado=exitoso");
        } else {
            // Caso de error imprevisto (Caída de conexión, error de sintaxis, etc.)
            System.out.println("=== Error crítico: No se pudo procesar el registro del cliente ===");
            response.sendRedirect("html/m-meserocopy.jsp?estado=error");
        }
    }
}