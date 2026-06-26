package com.controlador;

// DAO encargado de consultar los detalles del pedido
import com.dao.DetallePedidoDao;

// Modelo que representa un detalle del pedido
import com.modelo.DetallePedido;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*

RESPONSABILIDAD DEL CONTROLADOR

- Recibir el id del detalle seleccionado.
- Consultar ese detalle en la base de datos.
- Enviar el objeto encontrado al JSP.
- Abrir el formulario de edición.

METODO DAO UTILIZADO

1. obtenerDetallePorId()

   Busca un detalle específico del pedido
   utilizando su identificador.

*/

@WebServlet("/EditarDetallePedidoControlador")
public class EditarDetallePedidoControlador extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        /*
        
        PASO 1 RECIBIR EL ID DEL DETALLE
        
         */

        /*
            Recuperamos el id enviado desde el botón Editar.

            El parámetro llega como texto.
         */
        String parametroIdDetalle = request.getParameter("idDetalle");

        /*
            Convertimos el texto a entero porque el DAO necesita trabajar con un número.
         */
        int identificadorDetallePedido =  Integer.parseInt(parametroIdDetalle);

        /*
        
        PASO 2 BUSCAR EL DETALLE
       
         */

        /*
            Creamos el DAO encargado de consultar la base de datos.
         */
        DetallePedidoDao detallePedidoDao = new DetallePedidoDao();

        /*
            Consultamos el detalle utilizando el id recibido.

            El método devuelve un objeto DetallePedido.
         */
        DetallePedido detallePedidoEncontrado = detallePedidoDao.obtenerDetallePorId( identificadorDetallePedido);

        /*
        
        PASO 3 ENVIAR EL OBJETO AL JSP
        
         */

        /*
            Guardamos el objeto encontrado dentro del request.

            El JSP podrá acceder a esta información mediante el atributo "detallePedidoSeleccionado".
         */
        request.setAttribute( "detallePedidoSeleccionado", detallePedidoEncontrado);

        /*
        
        PASO 4 ABRIR EL FORMULARIO DE EDICIÓN
        
         */

        /*
            Enviamos el request al JSP para que cargue automáticamente la información del detalle y permita modificarla.
         */
        request.getRequestDispatcher( "/html/m-editar-cantidad.jsp").forward(request, response);
    }
}