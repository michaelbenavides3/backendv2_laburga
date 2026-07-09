/*
    OBJETIVO DEL CONTROLADOR

    Permitir que el mesero modifique la cantidad de productos dentro de un pedido abierto sin necesidad de eliminar el pedido completo, 
    manteniendo actualizada la información almacenada en la base de datos.


    DetallePedidoDao: Métodos utilizados: actualizarCantidadDetalle(int idDetallePedido, int nuevaCantidad)

    FLUJO DE FUNCIONAMIENTO:

        Recibir los datos enviados por el formulario.
        Obtener el ID del detalle del pedido.
        Obtener la nueva cantidad ingresada por el usuario.
        Crear una instancia de DetallePedidoDao.
        Ejecutar la actualización en la base de datos.
        Validar el resultado de la operación.
        Redireccionar al panel del mesero mostrando el resultado.
*/






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
        /*
        DetallePedidoDao --> el nombre de la clase 
        detallePedidoDao --> nombre de la variable
        new DetallePedidoDao() --> es la llamada al constructor de la clase, aqui es donde se crea el objeto en la memoria
        */
        DetallePedidoDao detallePedidoDao = new DetallePedidoDao();
        /*
        pametros(argumento)  identificadorDetallePedido, nuevaCantidadProducto: que registro voy actualizar y cual es la nueva cantidad
        
        */
        boolean operacionActualizacionExitosa = detallePedidoDao.actualizarCantidadDetalle( identificadorDetallePedido, nuevaCantidadProducto);

        /*
        
        3. VALIDAR RESULTADO
        
         */
        /*
        si operacionActualizacionExitosa entra en el if, es true, de lo contrarioa salta al else
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
