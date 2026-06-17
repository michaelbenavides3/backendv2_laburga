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

    protected void doGet(HttpServletRequest request,HttpServletResponse response)
            throws ServletException, IOException {

        /*
    
            RESPONSABILIDAD:

            1. VALIDAR DATOS RECIBIDOS
            2. CERRAR PEDIDO
            3. LIBERAR MESA
            4. REDIRECCIONAR AL CAJERO
    
         */
        String parametroIdPedido= request.getParameter("idPedido");

        String parametroIdMesa = request.getParameter("idMesa");

        /*
    
            VALIDAR QUE LOS PARAMETROS EXISTAN
    
         */
        if (parametroIdPedido == null || parametroIdMesa == null) {

            response.sendRedirect( "html/c-cajero.jsp?error=datos");

            return;
        }

        try {

            int idPedido = Integer.parseInt(parametroIdPedido);

            int idMesa= Integer.parseInt(parametroIdMesa);

            PedidoDao pedidoDao = new PedidoDao();

            MesaDao mesaDao = new MesaDao();

            /*
        
                PASO 1. CERRAR PEDIDO
        
             */
            pedidoDao.actualizarEstadoPedido( idPedido, "cerrada");

            /*
        
                 PASO 2. LIBERAR MESA
        
             */
            mesaDao.cambiarEstado(idMesa,"disponible");

            System.out.println(
                    "Pedido cerrado correctamente. "
                    + "ID Pedido: "
                    + idPedido
                    + " | Mesa liberada: "
                    + idMesa);

            /*
        
                PASO 3. VOLVER AL PANEL DEL CAJERO
        
             */
            response.sendRedirect( "html/c-cajero.jsp?cobro=exitoso");

        } catch (NumberFormatException errorConversion) {

            System.out.println( "Error convirtiendo IDs: " + errorConversion.getMessage());

            response.sendRedirect( "html/c-cajero.jsp?error=formato");

        } catch (Exception errorGeneral) {

            System.out.println( "Error finalizando cobro: "+ errorGeneral.getMessage());

            response.sendRedirect("html/c-cajero.jsp?error=sistema");
        }
    }

}
