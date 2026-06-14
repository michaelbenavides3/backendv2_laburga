package com.controlador;

import com.dao.DetallePedidoDao;
import com.dao.PedidoDao;
import com.modelo.DetallePedido;

import java.io.IOException;
import java.util.Enumeration;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(
        name = "AgregarProductoPedidoControlador",
        urlPatterns = {"/AgregarProductoPedidoControlador"}
)
public class AgregarProductoPedidoControlador extends HttpServlet {

    protected void processRequest( HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

        /*
        
        1. RECUPERAR LA MESA
        
         */
        int identificadorMesa = Integer.parseInt(request.getParameter("txtIdMesa"));

        /*
        
        2. BUSCAR EL PEDIDO ACTIVO DE ESA MESA
        
           si la mesa ya tiene una orden abierta, obtenemos el id del pedido.
        
         */
        PedidoDao pedidoDao = new PedidoDao();

        int identificadorPedido = pedidoDao.obtenerPedidoActivoPorMesa( identificadorMesa);

        /*
        
        3. VALIDAR QUE EXISTA UN PEDIDO ACTIVO
        
         */
        if (identificadorPedido > 0) {

            DetallePedidoDao detallePedidoDao = new DetallePedidoDao();

            /*
            
            PRECIOS DE LOS PRODUCTOS
            
             */
            double[] preciosProductos = {
                0.0,
                19000.0,
                25000.0,
                22000.0,
                35000.0,
                19000.0,
                19000.0,
                19000.0,
                19000.0,
                25000.0,
                40000.0,
                5000.0,
                5000.0,
                7000.0,
                12000.0
            };

            /*
            
            4. RECORRER TODOS LOS CAMPOS DEL FORMULARIO
            
             */
            Enumeration<String> nombresCampos = request.getParameterNames();

            while (nombresCampos.hasMoreElements()) {

                String nombreCampo = nombresCampos.nextElement();

                /*
                
                SOLO PROCESAMOS LOS CAMPOS prod_
                
                 */
                if (nombreCampo.startsWith("prod_")) {

                    String valorCantidad = request.getParameter( nombreCampo);

                    if (valorCantidad != null&& !valorCantidad.isEmpty()) {

                        int cantidadProducto = Integer.parseInt( valorCantidad);

                        /*
                        
                        SOLO GUARDAMOS SI LA CANTIDAD ES MAYOR A 0
                        
                         */
                        if (cantidadProducto > 0) {

                            int identificadorProducto = Integer.parseInt( nombreCampo.replace( "prod_", ""));

                            /*
                            
                            5. CREAR OBJETO DETALLEPEDIDO
                            
                             */
                            DetallePedido detallePedidoNuevo = new DetallePedido();

                            detallePedidoNuevo.setIdPedido(identificadorPedido);

                            detallePedidoNuevo.setIdProducto( identificadorProducto);

                            detallePedidoNuevo.setCantidad(cantidadProducto);

                            detallePedidoNuevo.setPrecioVenta( preciosProductos[ identificadorProducto]);

                            detallePedidoNuevo.setObservaciones("");

                            /*
                            
                            6. GUARDAR EL PRODUCTO
                            
                             */
                            detallePedidoDao.registrarDetalle( detallePedidoNuevo);
                        }
                    }
                }
            }

            /*
            
            7. REDIRECCIONAR AL PANEL
            
             */
            response.sendRedirect( "html/m-meserocopy.jsp?productoAgregado=true");

        } else {

            /*
            
            SI NO EXISTE PEDIDO ACTIVO
            
             */
            response.sendRedirect("html/m-meserocopy.jsp?error=noPedido");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

        processRequest(request, response);
    }

    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        processRequest(request, response);
    }
}