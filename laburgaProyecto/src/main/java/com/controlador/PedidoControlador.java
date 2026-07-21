/*

    CONTROLADOR ENCARGADO POR PARTE DEL MESERO PARA CREAR UN PEDIDO NUEVO


*/


package com.controlador;

import com.dao.PedidoDao;
import com.dao.MesaDao;
import com.dao.DetallePedidoDao;
import com.modelo.DetallePedido;
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
        String paramIdMesa = request.getParameter("txtIdMesa");
        /*
        por medio del operador ternatio nos esta diciendo que (paramIdMesa) debe traer un dato y que no este vacio para continuar
        de lo contrario por medio del else que es :0, se le aplica 0 para que no rompra el programa
        */
        int idMesa = (paramIdMesa != null && !paramIdMesa.isEmpty()) ? Integer.parseInt(paramIdMesa) : 0;
        /*
        aca se capturas las observaciones que viajan desde el formulario por el metodo post desde el jsp
        */
        String observaciones = request.getParameter("txtObservaciones");

        // 
        // NUEVA VALIDACIÓN ANTES DE INSERTAR: Verificar que el pedido no vaya vacío
        // 
        /*
        se inicializa la variable en 0 o contador
        */
        int cantidadProductosSeleccionados = 0;
        /*
        se captura todos los datos que viene del formulario por medio del request.getParameterNames
        */
        Enumeration<String> nombresCamposVerificacion = request.getParameterNames();
        /*
        con el ciclo while corrobora los campos de la variable nombrecamposverificado, que se encunetran en una lista o variable de tippo enumeration
        lo que hace el hasmoreelements actua con false o true, pregutnado al while si existen mas elemnetnos de la lista por recorrer si es true sigue reccoeriendo
        si es false se detiene
        */
        while (nombresCamposVerificacion.hasMoreElements()) {
            String nombreCampo = nombresCamposVerificacion.nextElement();
            
            // Si el campo pertenece a un producto
            /*
            por medio del metodo (startsWith) se le esta diciendo capture todo lo que empeize por estas iniciales ("prod_")
            aca le dice que nombrecampo solo le interesa los que empiza por prod_
            */
            if (nombreCampo.startsWith("prod_")) {
                /*
                aca almacena la cantidad obetneida de cada prod_ en la vairable valorcantidad
                */
                String valorCantidad = request.getParameter(nombreCampo);
                
                if (valorCantidad != null && !valorCantidad.trim().isEmpty()) {
                    /*
                    valor cantidad lo convertimos en tipo numerico, y se almacena en cantidad
                    */
                    int cantidad = Integer.parseInt(valorCantidad);
                    if (cantidad > 0) {
                        /*
                        aca se le va aumentado al contado inicial o la variable que se declaro 
                        */
                        cantidadProductosSeleccionados++; // Encontramos un producto válido
                    }
                }
            }
        }

        // Si el mesero no digitó cantidades mayores a 0 en ningún producto, cancelamos
        if (cantidadProductosSeleccionados == 0) {
            // Redireccionamos enviando error=vacio para que el JSP lo capture y muestre la alerta
            response.sendRedirect("html/m-registrar-pedido.jsp?error=vacio&idMesa=" + idMesa);
            return; // Detiene la ejecución del Servlet por completo
        }
        // ------------------------------------------------------------------------

        // PASO 2. IDENTIFICAR AL MESERO QUE ESTÁ HACIENDO EL PEDIDO
        HttpSession sesion = request.getSession(false);
        Usuario usuarioLogeado = null;

        if (sesion != null) {
            usuarioLogeado = (Usuario) sesion.getAttribute("usuarioLogeadoObjeto");
        }

        int idMesero = (usuarioLogeado != null) ? usuarioLogeado.getIdUsuario() : 3;

        // PASO 3. CREAR EL ENCABEZADO DEL PEDIDO
        /*
        se crea un objeto del modelo pedido
        su funcion es guardar la informacion antes de enviarla a la bd
        solamente queda guardado en la memoria todavia no envia nada
        */
        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setIdPedido(0);
        nuevoPedido.setIdMesa(idMesa);
        nuevoPedido.setIdMesero(idMesero);
        nuevoPedido.setEstadoPedido("activo");

        // PASO 4. REGISTRAR EL PEDIDO EN LA BASE DE DATOS
        /*
        
        */
        PedidoDao pedidoDao = new PedidoDao();
        int idPedidoGenerado = pedidoDao.registrarNuevoPedido(nuevoPedido);

        // PASO 5. VERIFICAR QUE EL PEDIDO SE HAYA CREADO
        if (idPedidoGenerado > 0) {

            ProductosDao productosDao = new ProductosDao();
            Enumeration<String> nombresCampos = request.getParameterNames();

            // Recorre todos los parámetros enviados para guardar los detalles
            while (nombresCampos.hasMoreElements()) {
                String nombreCampo = nombresCampos.nextElement();

                if (nombreCampo.startsWith("prod_")) {
                    String valorCantidad = request.getParameter(nombreCampo);

                    if (valorCantidad != null && !valorCantidad.trim().isEmpty()) {
                        int cantidad = Integer.parseInt(valorCantidad);

                        if (cantidad > 0) {
                            int idProducto = Integer.parseInt(nombreCampo.replace("prod_", ""));

                            // Busca el producto para obtener su información.
                            Productos producto = productosDao.obtenerProductoPorId(idProducto);

                            if (producto != null) {
                                double precioVenta = producto.getPrecioBaseProducto();
                                // Guarda el producto dentro del detalle del pedido.
                                pedidoDao.registrarDetallePedido(idPedidoGenerado, idProducto, cantidad, precioVenta, observaciones);
                            }
                        }
                    }
                }
            }

            // PASO 6. CAMBIAR EL ESTADO DE LA MESA
            MesaDao mesaDao = new MesaDao();
            mesaDao.cambiarEstado(idMesa, "ocupada");

            // PASO 7. REDIRECCIONAR AL PANEL DEL MESERO
            response.sendRedirect("html/m-meserocopy.jsp?pedido=exitoso");

        } else {
            // SI EL PEDIDO NO PUDO CREARSE EN LA BD
            response.sendRedirect("html/m-registrar-pedido.jsp?error=3&idMesa=" + idMesa);
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
