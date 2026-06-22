/*

    CONTROLADOR CAJERO

    RESPONSABILIDAD:

        - Recibir la mesa seleccionada por el cajero.

        - Consultar todos los productos consumidos en esa mesa.

        - Obtener el detalle completo del pedido desde la base de datos.

        - Enviar la información al JSP del cajero.

        - Permitir que el cajero visualice la cuenta antes de generar la factura.


        METODOS:  detallePedidoDao.listarDetallesPorMesa -> Obtiene todos los productos consumidos por una mesa específica.

*/




package com.controlador;

import com.dao.DetallePedidoDao;
import com.modelo.DetallePedido;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


//basicamente este es el puente o el conector lo que no dice cuando el cjaeor hace click 

@WebServlet("/CajeroControlador")
public class CajeroControlador extends HttpServlet {
    //// cuando el cajero le da clic a una mesa, entra por aqui (doGet)
    
    
     /*
    
    CUANDO EL CAJERO HACE CLICK SOBRE UNA MESA
    
    EJEMPLO:
    
    Mesa 1
    Mesa 2
    Mesa 3
    
    El sistema envía: ?idMesa=3
    
    Este controlador recibe ese dato,consulta la base de datosy carga todos los productos consumidos.
    
    */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        /*
        
        1. OBTENER ID DE LA MESA
        
         */
        String idMesa = request.getParameter("idMesa");
        
        
        /*
        
        2. CREAR DAO
        
         */
        
        // Usamos el DAO que ya tienes para traer los datos
        DetallePedidoDao dao = new DetallePedidoDao();
        
        
         /*
        
        3. CONSULTAR CONSUMO DE LA MESA
        
        Convierte el texto recibido a entero para poder consultar la base de datos.
        
         */
       
        // pido la lista de lo que se ha consumido en  esa mesa en especifico
        // uso Integer.parseInt porque el id viene como texto y necesito un numero
        /*
        
        4. ENVIAR INFORMACIÓN AL JSP
        
         */
        java.util.List<DetallePedido> lista = dao.listarDetallesPorMesa(Integer.parseInt(idMesa));
        //// guardo la lista en el request para que el archivo jsp pueda mostrarla
        request.setAttribute("listaDetalles", lista);
        /*
        
        5. ABRIR PANTALLA DEL CAJERO
        
         */
        //// mando todo al archivo jsp que se encarga de pintar la pantalla de cobro
        request.getRequestDispatcher("html/c-cajero.jsp").forward(request, response);
    }
}
