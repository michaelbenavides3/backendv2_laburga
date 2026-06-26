/*

RESPONSABILIDAD DEL CONTROLADOR

    - Finalizar el proceso de cobro.
    - Cerrar definitivamente el pedido.
    - Liberar la mesa para nuevos clientes.
    - Regresar al panel del cajero.


METODOS DAO UTILIZADOS

1. actualizarEstadoPedido()
   → Cambia el estado del pedido de "activo" a "cerrada".

2. cambiarEstado()
   → Cambia el estado de la mesa de "ocupada" a "disponible".

 */
package com.controlador;

import com.dao.MesaDao;
import com.dao.PedidoDao;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/FinalizarCobro")
public class FinalizarCobro extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        /*
        
        PASO 1. RECIBIR LOS DATOS ENVIADOS DESDE EL JSP
      

        El JSP envía:

            idPedido
            idMesa

        Ambos llegan como texto (String).

         */
        String parametroIdPedido = request.getParameter("idPedido");

        String parametroIdMesa = request.getParameter("idMesa");

        /*
        
        PASO 2. VALIDAR QUE LOS DATOS EXISTAN
        

        Si alguno llega vacío (null), significa que el formulario o la URL fue enviada incorrectamente.

        El OR (||) significa:

        Si falta cualquiera de los dos datos, no se puede continuar.

         */
        if (parametroIdPedido == null || parametroIdMesa == null) {

            response.sendRedirect("html/c-cajero.jsp?error=datos");

            return;

            /*
            return finaliza inmediatamente la ejecución del controlador.

             */
        }

        try {

            /*
           
            PASO 3. CONVERTIR LOS DATOS A ENTEROS
            
            Los parámetros llegan como texto.

            Integer.parseInt()

            los convierte para poder trabajar
            con ellos como IDs.

             */
            int idPedido = Integer.parseInt(parametroIdPedido);

            int idMesa = Integer.parseInt(parametroIdMesa);

            /*
            
            PASO 4. CREAR LOS DAO
           

            Cada DAO será responsable de trabajar con una tabla diferente.

             */
            PedidoDao pedidoDao = new PedidoDao();

            MesaDao mesaDao = new MesaDao();

            /*
            
            PASO 5. CERRAR EL PEDIDO
            

            Método DAO utilizado:

                actualizarEstadoPedido()

            ¿Por qué se llama?

            Porque el pedido ya fue pagado
            y debe quedar cerrado.

            ¿Qué recibe?

                idPedido
                nuevoEstado

            ¿Qué hace?

            Ejecuta un UPDATE sobre la tabla pedidos.

             */
            pedidoDao.actualizarEstadoPedido( idPedido, "cerrada");

            /*
            
            PASO 6. LIBERAR LA MESA
            

            Método DAO utilizado:

                cambiarEstado()

            ¿Por qué se llama?

            Porque ya terminó el servicio.

            La mesa queda disponible para
            nuevos clientes.

            ¿Qué recibe?

                idMesa
                nuevoEstado

            ¿Qué hace?

            Ejecuta un UPDATE sobre la tabla mesas.

             */
            mesaDao.cambiarEstado( idMesa,"disponible");

            /*
            Mensaje únicamente para verificar que el proceso terminó correctamente.

             */
            System.out.println(
                    "Pedido cerrado correctamente. "
                    + "ID Pedido: "
                    + idPedido
                    + " | Mesa liberada: "
                    + idMesa);

            /*
            
            PASO 7. REDIRECCIONAR AL CAJERO
            

            Se vuelve a cargar la pantalla del cajero mostrando un mensaje de éxito.

             */
            response.sendRedirect( "html/c-cajero.jsp?cobro=exitoso");

        } /*
        NumberFormatException ocurre cuando los parámetros no son números válidos.

         */ catch (NumberFormatException errorConversion) {

            System.out.println( "Error convirtiendo IDs: " + errorConversion.getMessage());

            response.sendRedirect( "html/c-cajero.jsp?error=formato");
        } /*
        Captura cualquier otro error inesperado.

         */ catch (Exception errorGeneral) {

            System.out.println( "Error finalizando cobro: " + errorGeneral.getMessage());

            response.sendRedirect( "html/c-cajero.jsp?error=sistema");
        }
    }
}
