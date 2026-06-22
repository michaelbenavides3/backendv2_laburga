

/*

        Buscar la información del detalle seleccionado y enviarla a la vista para mostrarla.

        METODO DetallePedidoDao->   obtenerDetallePorId

 */
package com.controlador;

import com.dao.DetallePedidoDao;
import com.modelo.DetallePedido;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/EditarDetallePedidoControlador")
public class EditarDetallePedidoControlador extends HttpServlet {

    /*
    
        CONTROLADOR EDITAR DETALLE PEDIDO
        
        OBJETIVO:
        
        obtener la informacion de una linea
        del pedido y enviarla a la vista
        para que el mesero la pueda modificar.
    
     */
    @Override
    protected void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {

        /*
        
        1. RECIBIR ID DEL DETALLE
        
         */
        String parametroIdDetalle = request.getParameter("idDetalle");

        int identificadorDetallePedido = Integer.parseInt(parametroIdDetalle);

        /*
        
        2. BUSCAR EL DETALLE
        
         */
        DetallePedidoDao detallePedidoDao = new DetallePedidoDao();

        DetallePedido detallePedidoEncontrado = detallePedidoDao.obtenerDetallePorId(identificadorDetallePedido);

        /*
        
        3. ENVIAR OBJETO A LA VISTA
        
         */
        request.setAttribute("detallePedidoSeleccionado",detallePedidoEncontrado);

        /*
        
        4. ABRIR FORMULARIO DE EDICION
        
         */
        request.getRequestDispatcher("/html/m-editar-cantidad.jsp").forward(request, response);
    }
}
