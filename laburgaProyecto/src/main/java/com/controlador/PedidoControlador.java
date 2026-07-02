/*

    ESTE ES EL CONTROLADOR ESPECIFIACAMENTE EL ENCARGADO DE CREAR UN NUEVO PEDIDO POR PARTE DEL MESERO


*/



package com.controlador;

import com.dao.PedidoDao;
import com.dao.MesaDao;
import com.dao.ProductosDao;
import com.modelo.Pedido;
import com.modelo.Productos;
import com.modelo.Usuario;

import java.io.IOException;
import java.util.Enumeration;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "PedidoControlador", urlPatterns = {"/PedidoControlador"})
public class PedidoControlador extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

       
        // PASO 1. RECUPERAR LOS DATOS QUE ENVÍA EL FORMULARIO
        

        // Obtiene el id de la mesa enviado desde el JSP.
        String paramIdMesa = request.getParameter("txtIdMesa");

        // Operador ternario.
        // Si el parámetro existe y no viene vacío, lo convierte a entero.
        // Si viene vacío, asigna 0 para evitar errores.
        /*
        por medio de un operardor ternario verifica que paramIdMesa exista y que no sea nula,
        con el operado && se exigue que se cumplan ambas condiciones, paramIdMesa (!),
        signgica negacion se debe interpretar que el texto no esete vacio
        */
        int idMesa = (paramIdMesa != null && !paramIdMesa.isEmpty())
                /*
                si el texto no viene vacio toma el texto = 5 y lo combierte en numero (int)
                */
                ? Integer.parseInt(paramIdMesa)
                : 0; /*si no hay datos se le asigna 0*/

        // Obtiene las observaciones escritas por el mesero.
        String observaciones = request.getParameter("txtObservaciones");

        
        // PASO 2. IDENTIFICAR AL MESERO QUE ESTÁ HACIENDO EL PEDIDO
        

        // Obtiene la sesión actual.
        // El false indica que NO cree una nueva sesión si no existe.
        HttpSession sesion = request.getSession(false);

        // Variable donde se almacenará el usuario autenticado.
        Usuario usuarioLogeado = null;

        // Si existe una sesión o no viene vacia entra en el if
        if (sesion != null) {

            // Recupera el objeto Usuario guardado durante el Login.
            usuarioLogeado =(Usuario) sesion.getAttribute("usuarioLogeadoObjeto");
        }

        // Operador ternario.
        // Si el usuario existe utiliza su id.
        // Si no existe utiliza el id 3 como respaldo.
        int idMesero = (usuarioLogeado != null)? usuarioLogeado.getIdUsuario() : 3;

        
        // PASO 3. CREAR EL ENCABEZADO DEL PEDIDO
     

        // Se crea un nuevo objeto Pedido.
        Pedido nuevoPedido = new Pedido();

        // El id queda en cero porque MySQL lo genera automáticamente.
        nuevoPedido.setIdPedido(0);

        // Se asigna la mesa donde se está realizando el pedido.
        nuevoPedido.setIdMesa(idMesa);

        // Se asigna el mesero responsable.
        nuevoPedido.setIdMesero(idMesero);

        // Todo pedido inicia como ACTIVO.
        nuevoPedido.setEstadoPedido("activo");

      
        // PASO 4. REGISTRAR EL PEDIDO EN LA BASE DE DATOS
     

        // Se crea el DAO encargado de trabajar con pedidos.
        PedidoDao pedidoDao = new PedidoDao();

        // Guarda el pedido en MySQL.
        // Este método devuelve el id generado automáticamente.
        int idPedidoGenerado = pedidoDao.registrarNuevoPedido(nuevoPedido);

        
        // PASO 5. VERIFICAR QUE EL PEDIDO SE HAYA CREADO
      
        // Si el id generado es mayor que cero significa que el pedido fue creado.
        if (idPedidoGenerado > 0) {

           
            // Ahora se registran todos los productos del pedido.
            

            ProductosDao productosDao = new ProductosDao();

            // Obtiene TODOS los nombres de campos enviados por el formulario.
            Enumeration<String> nombresCampos = request.getParameterNames();

            // Recorre todos los parámetros enviados.
            while (nombresCampos.hasMoreElements()) {

                // Obtiene el siguiente nombre del formulario.
                String nombreCampo = nombresCampos.nextElement();

                // Solo procesa los campos que empiezan por "prod_".
                if (nombreCampo.startsWith("prod_")) { /*aca captura el id del producto desde el jsp*/

                    // Obtiene la cantidad digitada para ese producto.
                    String valorCantidad = request.getParameter(nombreCampo);

                    // Valida que el campo no esté vacío.
                    if (valorCantidad != null && !valorCantidad.trim().isEmpty()) {

                        // Convierte la cantidad a entero.
                        int cantidad = Integer.parseInt(valorCantidad);

                        // Solo registra productos cuya cantidad sea mayor que cero.
                        if (cantidad > 0) {

                            // Extrae el id del producto.
                            // Ejemplo:
                            // prod_8 → 8
                            int idProducto = Integer.parseInt(  nombreCampo.replace("prod_", ""));

                            
                            // CONSULTA EL PRODUCTO EN LA BASE DE DATOS
                            

                            // Busca el producto para obtener su información.
                            Productos producto = productosDao.obtenerProductoPorId(idProducto);

                            // Si el producto existe...
                            if (producto != null) {

                                // Obtiene el precio oficial desde la BD.
                                // Nunca desde el formulario por seguridad.
                                double precioVenta = producto.getPrecioBaseProducto();

                                // Guarda el producto dentro del detalle del pedido.
                                pedidoDao.registrarDetallePedido( idPedidoGenerado, idProducto, cantidad, precioVenta);
                            }
                        }
                    }
                }
            }

            
            // PASO 6. CAMBIAR EL ESTADO DE LA MESA
         

            // Se crea el DAO encargado de administrar mesas.
            MesaDao mesaDao = new MesaDao();

            // Cambia la mesa de disponible a ocupada.
            mesaDao.cambiarEstado(idMesa, "ocupada");

        
            // PASO 7. REDIRECCIONAR AL PANEL DEL MESERO
           

            response.sendRedirect( "html/m-meserocopy.jsp?pedido=exitoso");

        } else {

          
            // SI EL PEDIDO NO PUDO CREARSE
            

            // Redirecciona nuevamente al formulario mostrando un mensaje de error.
            response.sendRedirect("html/m-registrar-pedido.jsp?error=3&idMesa=" + idMesa);
        }
    }

    
    // Ambos métodos llaman al mismo processRequest().
    // Así se evita repetir código.
    

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }
}