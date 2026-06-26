/*

RESPONSABILIDAD DEL CONTROLADOR

    - Eliminar un producto específico de un pedido.
    - Verificar si después de eliminarlo aún quedan productos.
    - Si el pedido queda vacío:
        * cerrar el pedido.
        * liberar la mesa.
    - Redireccionar nuevamente al panel del mesero.


METODOS DAO UTILIZADOS

1. eliminarDetallePedido()
   → Elimina un producto específico del pedido.

2. contarDetallesPorPedido()
   → Cuenta cuántos productos siguen perteneciendo al pedido.

3. obtenerMesaPorPedido()
   → Obtiene la mesa asociada a ese pedido.

4. actualizarEstadoPedido()
   → Cambia el estado del pedido a "cerrada".

5. cambiarEstado()
   → Cambia el estado de la mesa a "disponible".

*/

package com.controlador;

import com.dao.DetallePedidoDao;
import com.dao.MesaDao;
import com.dao.PedidoDao;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(
        name = "EliminarDetallePedidoControlador",
        urlPatterns = {"/EliminarDetallePedidoControlador"}
)
public class EliminarDetallePedidoControlador extends HttpServlet {

    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        /*
        
        PASO 1. RECIBIR LOS DATOS DESDE EL JSP
        

        El JSP envía:

        - idDetalle → producto que se quiere eliminar.
        - idPedido  → pedido al cual pertenece ese producto.

        */

        int identificadorDetallePedido = Integer.parseInt(request.getParameter("idDetalle"));

        int identificadorPedido = Integer.parseInt(request.getParameter("idPedido"));

        /*
        
        PASO 2. ELIMINAR EL PRODUCTO DEL PEDIDO
        

        Se crea el DAO encargado del detalle del pedido.

        Se llama el método:

            eliminarDetallePedido()

        Este método elimina físicamente el registro del producto dentro de la tabla detallepedido.

        Devuelve:

            true  -> si logró eliminarlo.
            false -> si ocurrió algún error.

        */

        DetallePedidoDao detallePedidoDao = new DetallePedidoDao();

        boolean detalleEliminadoCorrectamente = detallePedidoDao.eliminarDetallePedido(identificadorDetallePedido);

        /*
        Solo si el producto realmente fue eliminado continúa el flujo.
        */

        if (detalleEliminadoCorrectamente) {

            /*
            
            PASO 3. CONTAR CUÁNTOS PRODUCTOS QUEDAN
            

            Ahora se consulta nuevamente la base de datos.

            Método utilizado:

                contarDetallesPorPedido()

            Este método hace un COUNT(*) para saber cuántos productos siguen perteneciendo al pedido.

            Devuelve un entero.

            */

            int cantidadProductosRestantes = detallePedidoDao.contarDetallesPorPedido( identificadorPedido);

            System.out.println("Cantidad productos restantes: " + cantidadProductosRestantes);

            System.out.println("Pedido evaluado: " + identificadorPedido);

            /*
            
            PASO 4. ¿EL PEDIDO QUEDÓ VACÍO?
            

            Si ya no queda ningún producto significa que el pedido dejó de existir.

            */

            if (cantidadProductosRestantes == 0) {

                System.out.println( "NO QUEDAN PRODUCTOS EN EL PEDIDO");

                /*
                
                PASO 5. BUSCAR LA MESA DEL PEDIDO
                

                Se crea el DAO de pedidos.

                Método utilizado:

                    obtenerMesaPorPedido()

                Busca en la base de datos cuál es la mesa asociada a ese pedido.

                Devuelve:

                    idMesa

                */

                PedidoDao pedidoDao = new PedidoDao();

                int identificadorMesa =  pedidoDao.obtenerMesaPorPedido( identificadorPedido);

                System.out.println("Mesa encontrada: " + identificadorMesa);

                /*
                
                PASO 6. CERRAR EL PEDIDO
                

                Método utilizado:

                    actualizarEstadoPedido()

                Cambia el estado del pedido a:

                    cerrada

                Esto indica que ya no puede seguir recibiendo productos.

                */

                pedidoDao.actualizarEstadoPedido( identificadorPedido, "cerrada");

                /*
                
                PASO 7. LIBERAR LA MESA
                

                Se crea el DAO de mesas.

                Método utilizado:

                    cambiarEstado()

                Cambia la mesa nuevamente a: disponible

                Así otro cliente podrá ocuparla.

                */

                MesaDao mesaDao = new MesaDao();

                mesaDao.cambiarEstado( identificadorMesa,"disponible");

                /*
                
                PASO 8. REGRESAR AL PANEL
                

                Se recarga el panel del mesero indicando que el pedido fue cerrado automáticamente.

                */

                response.sendRedirect("html/m-meserocopy.jsp?pedidoCerrado=true");

                return;
            }
        }

        /*
        
        PASO 9. SI TODAVÍA QUEDAN PRODUCTOS
        

        No se libera la mesa.

        No se cierra el pedido.

        Simplemente se regresa a la pantalla anterior.

        */

        response.sendRedirect(request.getHeader("referer"));
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }
}