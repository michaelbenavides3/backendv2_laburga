package com.controlador;

// DAO encargado de las operaciones relacionadas con los pedidos
import com.dao.PedidoDao;

// DAO encargado de las operaciones relacionadas con las mesas
import com.dao.MesaDao;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*

RESPONSABILIDAD DEL CONTROLADOR

- Recibir la solicitud del mesero cuando cambia el estado de una mesa.
- Cambiar el estado de la mesa en la base de datos.
- Si el estado cambia a "pendiente_cobro":
    • Buscar el pedido activo de esa mesa.
    • Enviar dicho pedido a caja.
- Finalmente regresar nuevamente al panel principal del mesero.

METODOS DAO UTILIZADOS

1. cambiarEstado()
   Cambia el estado actual de una mesa.

2. obtenerPedidoActivoPorMesa()
   Busca el pedido que actualmente está abierto para esa mesa.

3. solicitarCuenta()
   Cambia el estado del pedido para indicar que fue enviado a caja.

*/

@WebServlet(
        name = "CambiarEstadoMesa",
        urlPatterns = {"/CambiarEstadoMesa"}
)
public class CambiarEstadoMesa extends HttpServlet {

    /*
        processRequest()

        Este método contiene toda la lógica del controlador.

        Tanto doGet() como doPost() llaman este método para evitar
        escribir el mismo código dos veces.
     */
    protected void processRequest(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Mensaje de prueba para verificar que el servlet fue ejecutado
        System.out.println("===== ENTRE AL SERVLET =====");

        /*
            Recuperamos los parámetros enviados desde el JSP.

            idMesa -> Mesa seleccionada.
            estado -> Nuevo estado que tendrá la mesa.
         */
        String idMesa = request.getParameter("idMesa");

        String nuevoEstado = request.getParameter("estado");

        // Mostramos por consola los valores recibidos
        System.out.println("Mesa recibida: " + idMesa);

        System.out.println("Estado recibido: " + nuevoEstado);

        /*
            Validamos que ambos datos realmente hayan sido enviados.

            Si alguno viene vacío no se realiza ninguna operación.
         */
        if (idMesa != null && nuevoEstado != null) {

            /*
                Creamos el DAO encargado de modificar la información
                relacionada con las mesas.
             */
            MesaDao mesaDao = new MesaDao();

            /*
                Cambiamos el estado de la mesa.

                Ejemplos:

                libre
                ocupada
                pendiente_cobro
             */
            mesaDao.cambiarEstado(
                    Integer.parseInt(idMesa),
                    nuevoEstado
            );

            /*
                Si el nuevo estado corresponde a "pendiente_cobro"
                significa que el mesero solicitó enviar la cuenta
                al cajero.
             */
            if (nuevoEstado.equals("pendiente_cobro")) {

                /*
                    Creamos el DAO encargado de administrar
                    los pedidos.
                 */
                PedidoDao pedidoDao = new PedidoDao();

                /*
                    Buscamos cuál es el pedido activo
                    perteneciente a la mesa seleccionada.
                 */
                int idPedido = pedidoDao.obtenerPedidoActivoPorMesa(
                        Integer.parseInt(idMesa)
                );

                // Mostramos el pedido encontrado
                System.out.println("Pedido encontrado: " + idPedido);

                /*
                    Si existe un pedido activo,
                    se envía a caja.
                 */
                if (idPedido > 0) {

                    /*
                        Este método cambia el estado del pedido
                        para indicar que fue enviado al cajero.
                     */
                    pedidoDao.solicitarCuenta(idPedido);

                    System.out.println(
                            "Pedido actualizado a pendiente_cobro"
                    );
                }

                /*
                    Mensajes adicionales para verificar
                    que todo el proceso fue ejecutado.
                 */
                System.out.println("Mesa recibida: " + idMesa);

                System.out.println("Estado recibido: " + nuevoEstado);
            }
        }

        /*
            Finalizado todo el proceso,
            regresamos nuevamente al panel principal
            del mesero.
         */
        response.sendRedirect("html/m-meserocopy.jsp");
    }

    /*
        Si la solicitud llega mediante GET,
        delegamos toda la lógica al método processRequest().
     */
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    /*
        Si la solicitud llega mediante POST,
        también utilizamos processRequest().
     */
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }
}
