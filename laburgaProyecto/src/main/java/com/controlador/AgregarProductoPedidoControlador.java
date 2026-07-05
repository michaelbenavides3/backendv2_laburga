/*
    ESTE CONTROLADOR ES EL ENCARGADO DE AGREGAR PRODUCTOS A UN PEDIDO ABIERTO

*/


package com.controlador;

import com.dao.DetallePedidoDao;
import com.dao.PedidoDao;
import com.dao.ProductosDao;
import com.modelo.DetallePedido;
import com.modelo.Productos;

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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /*
        1. RECUPERAR LA MESA
        se convierte a int, por medio (Integer.parseInt), cuando se toma del formulario viaja como tipo texto
         */
        int identificadorMesa = Integer.parseInt(request.getParameter("txtIdMesa"));

        // Recuperar observaciones del formulario
        String observacionesPedido = request.getParameter("txtObservaciones");
        
        /*
        2. BUSCAR EL PEDIDO ACTIVO DE ESA MESA
         */
        PedidoDao pedidoDao = new PedidoDao();
        int identificadorPedido = pedidoDao.obtenerPedidoActivoPorMesa(identificadorMesa);

        /*
        3. VALIDAR QUE EXISTA UN PEDIDO ACTIVO
         */
        if (identificadorPedido > 0) {
            
            /*
            con este dao es el encargado de guardar el nuevo producto dentro del pedido
            con este objeto se llamara mas adelante para insertar los productos en la tabla
            */
            DetallePedidoDao detallePedidoDao = new DetallePedidoDao();

            // Precio real desde BD, ya no hay array hardcodeado
            /*
            aca se consulta el valor del producto en la base de datos
            se necesita concocer priemro el valor real del producto
            */
            ProductosDao productosDao = new ProductosDao();

            /*
            4. RECORRER TODOS LOS CAMPOS DEL FORMULARIO
             */
            /*
            (request)contiene todos los datos que se recolecta del formulario del jsp
            (getParameterNames) obtiene los nombre de los parametros ejemplo : (txtIdMesa, txtObservaciones, prod_3, prod_5, prod_8)
            */
            Enumeration<String> nombresCampos = request.getParameterNames();
            /*
            con el while vamos a recorres nombrecampos, cuando ya no tenga mas campo que recorrer finaliza el ciclo
            */
            while (nombresCampos.hasMoreElements()) {
                
                /*
                aca se almacena los valores de los parametros
                */
                String nombreCampo = nombresCampos.nextElement();

                /*
                SOLO PROCESAMOS LOS CAMPOS prod_
                asi se puede filtar para que solo lleguen los prod y no lleguen los ddemas datos
                 */
                if (nombreCampo.startsWith("prod_")) {
                    /*
                    
                    pero con una condicion que hace el if: que el producto empieze por "prod_"
                    aquie solo se guarda el valor del prod_ ejmeplo prod_5, prod_8
                    como cada producto tiene un id unico
                    */
                    String valorCantidad = request.getParameter(nombreCampo);
                    /*
                    Solo entra al if si la variable existe (no es null) y además contiene un valor, es decir, no viene vacía.
                    */
                    if (valorCantidad != null && !valorCantidad.isEmpty()) {
                        
                        /*
                        aqui valorcantidad convierte el valor recibido desde el formulario que llega como texto, a un numero entero 
                        */
                        int cantidadProducto = Integer.parseInt(valorCantidad);

                        /*
                        SOLO GUARDAMOS SI LA CANTIDAD ES MAYOR A 0
                         */
                        /*
                        si cantidadproducto es mayor que 0 entra en la condificoanl if,
                        */
                        if (cantidadProducto > 0) {
                            /*
                            Obtiene el ID del producto eliminando el prefijo "prod_"
                            del nombre del campo y convirtiéndolo a número entero.

                            Ejemplo: nombreCampo = "prod_8"

                            replace("prod_", "") → "8"

                            Integer.parseInt("8") → 8
                            */
                            int identificadorProducto = Integer.parseInt(nombreCampo.replace("prod_", ""));

                            /*
                            5. CONSULTAR PRECIO REAL DESDE BD
                            Se consulta el producto utilizando su ID para obtener toda su información, especialmente el precio oficial almacenado en MySQL.
                             */
                            Productos producto = productosDao.obtenerProductoPorId(identificadorProducto);
                            /*
                            Se verifica que el producto sí exista en la base de datos.
                            Si el método devuelve null significa que el producto no fue encontrado, por lo tanto no se puede agregar al pedido.
                            */

                            if (producto != null) {

                                /*
                                6. CREAR OBJETO DETALLEPEDIDO
                                Se crea un nuevo objeto que representa un producto dentro del pedido. 
                                Cada producto agregado genera un registro diferente en la tabla detalle_pedido.
                                 */
                                DetallePedido detallePedidoNuevo = new DetallePedido();
                                /*
                                Se asigna el ID del pedido al que pertenece este producto. Así MySQL sabe en qué pedido debe guardar el detalle.
                                */  
                                detallePedidoNuevo.setIdPedido(identificadorPedido);
                                /*
                                Se asigna el ID del producto seleccionado.
                                */
                                detallePedidoNuevo.setIdProducto(identificadorProducto);
                                /*
                                se alamcena la cantidad solicitada por el cliente
                                */
                                detallePedidoNuevo.setCantidad(cantidadProducto);
                                detallePedidoNuevo.setPrecioVenta(producto.getPrecioBaseProducto());
                                /*
                                DESPUÉS — guardamos lo que escribió el mesero
                                */
                                detallePedidoNuevo.setObservaciones(observacionesPedido != null ? observacionesPedido : "");

                                /*
                                7. GUARDAR EL PRODUCTO
                                
                                Se envía el objeto completo al DAO para registrar el producto dentro de la tabla detalle_pedido.
                                 */
                                detallePedidoDao.registrarDetalle(detallePedidoNuevo);

                                System.out.println("DEBUG: Agregado " + producto.getNombreProducto()
                                        + " x" + cantidadProducto
                                        + " a $" + producto.getPrecioBaseProducto());
                            } else {
                                System.out.println("WARN: Producto id=" + identificadorProducto + " no encontrado.");
                            }
                        }
                    }
                }
            }

            /*
            8. REDIRECCIONAR AL PANEL
             */
            response.sendRedirect("html/m-meserocopy.jsp?productoAgregado=true");

        } else {

            /*
            SI NO EXISTE PEDIDO ACTIVO
             */
            response.sendRedirect("html/m-meserocopy.jsp?error=noPedido");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}