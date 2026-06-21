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

        // RECUPERACIÓN DE DATOS
        String paramIdMesa = request.getParameter("txtIdMesa");
        int idMesa = (paramIdMesa != null && !paramIdMesa.isEmpty()) ? Integer.parseInt(paramIdMesa) : 0;
        String observaciones = request.getParameter("txtObservaciones");

        // Verificamos quién es el mesero
        HttpSession sesion = request.getSession(false);
        Usuario usuarioLogeado = null;

        if (sesion != null) {
            usuarioLogeado = (Usuario) sesion.getAttribute("usuarioLogeadoObjeto");
        }

        if (usuarioLogeado == null) {
            System.out.println("DEBUG: Sesión inválida o usuario no logueado. Usando ID Mesero 3 por defecto.");
        } else {
            System.out.println("DEBUG: Usuario ID: " + usuarioLogeado.getIdUsuario() + " realizando pedido.");
        }

        int idMesero = (usuarioLogeado != null) ? usuarioLogeado.getIdUsuario() : 3;

        // REGISTRO DE PEDIDO
        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setIdPedido(0);
        nuevoPedido.setIdMesa(idMesa);
        nuevoPedido.setIdMesero(idMesero);
        nuevoPedido.setEstadoPedido("activo");

        PedidoDao pedidoDao = new PedidoDao();
        int idPedidoGenerado = pedidoDao.registrarNuevoPedido(nuevoPedido);

        if (idPedidoGenerado > 0) {

            // ✅ CAMBIO: ya no usamos el array hardcodeado de precios
            // Ahora consultamos el precio real de cada producto desde la BD
            ProductosDao productosDao = new ProductosDao();

            Enumeration<String> nombresCampos = request.getParameterNames();

            while (nombresCampos.hasMoreElements()) {
                String nombreCampo = nombresCampos.nextElement();

                if (nombreCampo.startsWith("prod_")) {
                    String valorCantidad = request.getParameter(nombreCampo);

                    if (valorCantidad != null && !valorCantidad.trim().isEmpty()) {
                        int cantidad = Integer.parseInt(valorCantidad);

                        if (cantidad > 0) {
                            int idProducto = Integer.parseInt(nombreCampo.replace("prod_", ""));

                            // ✅ Consultamos el precio real desde la BD
                            Productos producto = productosDao.obtenerProductoPorId(idProducto);

                            if (producto != null) {
                                double precioVenta = producto.getPrecioBaseProducto();
                                pedidoDao.registrarDetallePedido(idPedidoGenerado, idProducto, cantidad, precioVenta);
                                System.out.println("DEBUG: Producto " + producto.getNombreProducto() 
                                    + " x" + cantidad + " a $" + precioVenta);
                            } else {
                                System.out.println("WARN: Producto con id " + idProducto + " no encontrado en BD, se omite.");
                            }
                        }
                    }
                }
            }

            // Actualización de estado de mesa
            MesaDao mesaDao = new MesaDao();
            mesaDao.cambiarEstado(idMesa, "ocupada");
            response.sendRedirect("html/m-meserocopy.jsp?pedido=exitoso");

        } else {
            response.sendRedirect("html/m-registrar-pedido.jsp?error=3&idMesa=" + idMesa);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { processRequest(request, response); }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { processRequest(request, response); }
}