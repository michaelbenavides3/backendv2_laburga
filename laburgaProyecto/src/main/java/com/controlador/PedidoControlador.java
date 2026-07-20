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
        int idMesa = (paramIdMesa != null && !paramIdMesa.isEmpty()) ? Integer.parseInt(paramIdMesa) : 0;
        String observaciones = request.getParameter("txtObservaciones");

        // 
        // NUEVA VALIDACIÓN ANTES DE INSERTAR: Verificar que el pedido no vaya vacío
        // 
        int cantidadProductosSeleccionados = 0;
        Enumeration<String> nombresCamposVerificacion = request.getParameterNames();

        while (nombresCamposVerificacion.hasMoreElements()) {
            String nombreCampo = nombresCamposVerificacion.nextElement();
            
            // Si el campo pertenece a un producto
            if (nombreCampo.startsWith("prod_")) {
                String valorCantidad = request.getParameter(nombreCampo);
                
                if (valorCantidad != null && !valorCantidad.trim().isEmpty()) {
                    int cantidad = Integer.parseInt(valorCantidad);
                    if (cantidad > 0) {
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
        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setIdPedido(0);
        nuevoPedido.setIdMesa(idMesa);
        nuevoPedido.setIdMesero(idMesero);
        nuevoPedido.setEstadoPedido("activo");

        // PASO 4. REGISTRAR EL PEDIDO EN LA BASE DE DATOS
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
