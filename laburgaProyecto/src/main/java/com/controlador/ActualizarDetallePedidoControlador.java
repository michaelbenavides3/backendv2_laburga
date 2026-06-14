package com.controlador;

import com.dao.DetallePedidoDao;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ActualizarDetallePedidoControlador")
public class ActualizarDetallePedidoControlador extends HttpServlet {

    /*
    
        CONTROLADOR ACTUALIZAR DETALLE PEDIDO
        
        OBJETIVO: recibir la nueva cantidad ingresada por el mesero y actualizarla en la base de datos.
    
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        /*
        
        1. RECUPERAR DATOS DEL FORMULARIO
        
         */
        int identificadorDetallePedido = Integer.parseInt( request.getParameter( "idDetalle"));

        int nuevaCantidadProducto = Integer.parseInt( request.getParameter( "cantidadNueva"));

        /*
        
        2. ACTUALIZAR DETALLE
        
         */
        DetallePedidoDao detallePedidoDao = new DetallePedidoDao();

        boolean operacionActualizacionExitosa = detallePedidoDao.actualizarCantidadDetalle( identificadorDetallePedido, nuevaCantidadProducto);

        /*
        
        3. VALIDAR RESULTADO
        
         */
        if (operacionActualizacionExitosa) {

            response.sendRedirect("html/m-meserocopy.jsp?actualizacion=exitosa");

        } else {

            response.sendRedirect( "html/m-meserocopy.jsp?actualizacion=error");
        }
    }

    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { doPost(request, response);
    }
}
