package com.controlador;

import com.dao.PedidoDao;
import com.dao.MesaDao;
import com.modelo.Pedido;
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

    
    
    //es el encargado de procesar todo el formulario pedidos 
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //  RECUPERACIÓN DE DATOS (Variables activas y declaradas)
        String paramIdMesa = request.getParameter("txtIdMesa");
        int idMesa = (paramIdMesa != null && !paramIdMesa.isEmpty()) ? Integer.parseInt(paramIdMesa) : 0;
        String observaciones = request.getParameter("txtObservaciones");

        // verifico quien es el mesero que esta haciendo el pedido
        HttpSession sesion = request.getSession(false);
        Usuario usuarioLogeado = null;

        if (sesion != null) {
            usuarioLogeado = (Usuario) sesion.getAttribute("usuarioLogeadoObjeto");
        }
        
        // Log de diagnóstico
        if (usuarioLogeado == null) {
            System.out.println("DEBUG: Sesión inválida o usuario no logueado. Usando ID Mesero 3 por defecto.");
        } else {
            System.out.println("DEBUG: Usuario ID: " + usuarioLogeado.getIdUsuario() + " realizando pedido.");
        }

        int idMesero = (usuarioLogeado != null) ? usuarioLogeado.getIdUsuario() : 3;

       //  REGISTRO DE PEDIDO
        Pedido nuevoPedido = new Pedido(); // Usamos el constructor vacío
        nuevoPedido.setIdPedido(0);
        nuevoPedido.setIdMesa(idMesa);
        nuevoPedido.setIdMesero(idMesero);
        nuevoPedido.setEstadoPedido("activo");
        
        PedidoDao pedidoDao = new PedidoDao();
        int idPedidoGenerado = pedidoDao.registrarNuevoPedido(nuevoPedido);

        if (idPedidoGenerado > 0) {
            double[] preciosProductos = { 0.0, 19000.0, 25000.0, 22000.0, 35000.0, 19000.0, 19000.0, 19000.0, 19000.0, 25000.0, 40000.0, 5000.0, 5000.0, 7000.0, 12000.0 };
            
            Enumeration<String> nombresCampos = request.getParameterNames();
            // recorro todos los campos del formulario 
            while (nombresCampos.hasMoreElements()) {
                String nombreCampo = nombresCampos.nextElement();
                if (nombreCampo.startsWith("prod_")) {
                    String valorCantidad = request.getParameter(nombreCampo);
                    if (valorCantidad != null && !valorCantidad.trim().isEmpty()) { // aseguras que no venga vacío ni solo espacios.
                        int cantidad = Integer.parseInt(valorCantidad); //Convertir el texto "2" a número 2.
                        if (cantidad > 0) {
                            int idProducto = Integer.parseInt(nombreCampo.replace("prod_", ""));
                            double precioVenta = preciosProductos[idProducto];
                            pedidoDao.registrarDetallePedido(idPedidoGenerado, idProducto, cantidad, precioVenta);
                        }
                    }
                }
            }

            // Actualización de mesa
            MesaDao mesaDao = new MesaDao();
            mesaDao.cambiarEstado(idMesa, "ocupada");
            //response.sendRedirect("html/m-meserocopy.jsp");
            response.sendRedirect("html/m-meserocopy.jsp?pedido=exitoso");
        } else {
            // Aquí idMesa ya existe y puede ser usada en la redirección
            response.sendRedirect("html/m-registrar-pedido.jsp?error=3&idMesa=" + idMesa);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { processRequest(request, response); }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { processRequest(request, response); }
}