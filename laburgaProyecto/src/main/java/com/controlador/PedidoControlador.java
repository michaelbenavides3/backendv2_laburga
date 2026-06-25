/*
    Su función es crear una orden de compra completa. Recibe la solicitud de la mesa, 
    la vincula con el mesero que inició sesión y desglosa una lista variable de productos
    (cantidad y precio real) para guardarlos en la base de datos de una sola vez.

    PedidoDao -> cerrarPedidosActivosPorMesa  (NUEVO - evita pedidos huérfanos)
    PedidoDao -> registrarNuevoPedido
    PedidoDao -> registrarDetallePedido
    ProductosDao -> obtenerProductoPorId
    MesaDao -> cambiarEstado
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

        /*
        
        1. RECUPERAR DATOS DEL FORMULARIO
           - txtIdMesa: viene del input hidden del JSP, identifica qué mesa hizo el pedido
           - txtObservaciones: notas generales que el mesero escribe sobre el pedido
           - si idMesa viene vacío o nulo, se asigna 0 para evitar errores
        
        */
        String parametroIdMesa = request.getParameter("txtIdMesa");

        int identificadorMesa = (parametroIdMesa != null && !parametroIdMesa.isEmpty())
                ? Integer.parseInt(parametroIdMesa)
                : 0;

        String observacionesGeneralesPedido = request.getParameter("txtObservaciones");

        /*
       
        2. IDENTIFICAR AL MESERO QUE HACE EL PEDIDO
           - getSession(false): busca la sesión existente sin crear una nueva
           - getAttribute: extrae el objeto usuario que se guardó al hacer login
           - si no hay sesión activa, usamos id 3 por defecto (solo para pruebas)
        
        */
        HttpSession sesionActivaUsuario = request.getSession(false);
        Usuario usuarioMeseroLogeado = null;

        // verificamos si la sesión existe antes de intentar leer el usuario
        if (sesionActivaUsuario != null) {
            usuarioMeseroLogeado = (Usuario) sesionActivaUsuario.getAttribute("usuarioLogeadoObjeto");
        }

        // log de diagnóstico para saber quién está haciendo el pedido
        if (usuarioMeseroLogeado == null) {
            System.out.println("DEBUG: Sesión inválida o usuario no logueado. Usando ID Mesero 3 por defecto.");
        } else {
            System.out.println("DEBUG: Usuario ID: " + usuarioMeseroLogeado.getIdUsuario() + " realizando pedido.");
        }

        // si hay usuario logeado usamos su id, si no usamos 3 por defecto
        int identificadorMesero = (usuarioMeseroLogeado != null)
                ? usuarioMeseroLogeado.getIdUsuario()
                : 3;

        /*
        
        3. CERRAR PEDIDOS ACTIVOS ANTERIORES DE ESA MESA
           - antes de crear un pedido nuevo, cerramos cualquier pedido
             activo que haya quedado abierto en esa mesa
           - esto evita que queden pedidos huérfanos acumulados
           - garantiza que solo exista 1 pedido activo por mesa en todo momento
        
        */
        PedidoDao pedidoDao = new PedidoDao();
        pedidoDao.cerrarPedidosActivosPorMesa(identificadorMesa);

        /*
        
        4. CREAR EL OBJETO PEDIDO Y REGISTRARLO EN BD
           - construimos el objeto con los datos recuperados
           - estado siempre inicia como "activo" porque acaba de abrirse
           - registrarNuevoPedido devuelve el id generado por MySQL (AUTO_INCREMENT)
        
        */
        Pedido nuevoPedidoObjeto = new Pedido();
        nuevoPedidoObjeto.setIdPedido(0);
        nuevoPedidoObjeto.setIdMesa(identificadorMesa);
        nuevoPedidoObjeto.setIdMesero(identificadorMesero);
        nuevoPedidoObjeto.setEstadoPedido("activo");

        // registramos el pedido y guardamos el id que MySQL generó automáticamente
        int identificadorPedidoGenerado = pedidoDao.registrarNuevoPedido(nuevoPedidoObjeto);

        /*
       
        5. SI EL PEDIDO SE GUARDÓ CORRECTAMENTE
           - identificadorPedidoGenerado > 0 significa que MySQL insertó el registro
           - si es 0 o negativo, algo salió mal al guardar
       
        */
        if (identificadorPedidoGenerado > 0) {

            /*
            
            5.1 RECORRER TODOS LOS CAMPOS DEL FORMULARIO
                - getParameterNames() devuelve una lista con los nombres
                  de todos los campos que llegaron en el formulario
                - hasMoreElements() devuelve true mientras haya campos por revisar
                - nextElement() toma el siguiente nombre de campo de la lista
                - ejemplo de campos: txtIdMesa, txtObservaciones, prod_1, prod_3, prod_11
           
            */
            ProductosDao productosDao = new ProductosDao();
            Enumeration<String> nombresCamposFormulario = request.getParameterNames();

            /*
                recorremos uno por uno todos los campos del formulario
                hasta que no queden más por revisar
            */
            while (nombresCamposFormulario.hasMoreElements()) {

                String nombreCampoActual = nombresCamposFormulario.nextElement();

                /*
               
                5.2 FILTRAR SOLO LOS CAMPOS DE PRODUCTOS
                    - startsWith("prod_") filtra únicamente los campos de productos
                    - ignoramos txtIdMesa, txtObservaciones y cualquier otro campo
                    - ejemplo: "prod_3" pasa el filtro, "txtIdMesa" no pasa
                
                */
                if (nombreCampoActual.startsWith("prod_")) {

                    // obtenemos el valor del campo, ejemplo: "prod_3" tiene valor "2"
                    String valorCantidadTexto = request.getParameter(nombreCampoActual);

                    /*
                   
                    5.3 VALIDAR QUE LA CANTIDAD NO VENGA VACÍA
                        - trim() elimina espacios en blanco al inicio y al final
                        - isEmpty() verifica que el campo no esté vacío
                        - evitamos errores al intentar convertir "" a número
                    
                    */
                    if (valorCantidadTexto != null && !valorCantidadTexto.trim().isEmpty()) {

                        // convertimos el texto "2" al número entero 2
                        int cantidadProductoPedido = Integer.parseInt(valorCantidadTexto);

                        /*
                       
                        5.4 SOLO PROCESAMOS SI LA CANTIDAD ES MAYOR A 0
                            - si el mesero dejó el campo en 0, ese producto no se pidió
                            - solo guardamos los productos que realmente se seleccionaron
                            - ejemplo: prod_3=2 se guarda, prod_5=0 se ignora
                        
                        */
                        if (cantidadProductoPedido > 0) {

                            /*
                            extraemos el id del producto del nombre del campo
                            ejemplo: "prod_3" → replace("prod_", "") → "3" → parseInt → 3
                            */
                            int identificadorProducto = Integer.parseInt(
                                    nombreCampoActual.replace("prod_", ""));

                            /*
                            
                            5.5 CONSULTAR PRECIO REAL DESDE LA BASE DE DATOS
                                - ya no usamos array hardcodeado de precios
                                - consultamos el precio actual del producto en BD
                                - así si el admin cambia el precio, se refleja automáticamente
                            
                            */
                            Productos productoEncontrado = productosDao
                                    .obtenerProductoPorId(identificadorProducto);

                            /*
                            
                            5.6 SI EL PRODUCTO EXISTE EN BD, REGISTRAR EL DETALLE
                                - verificamos que el producto exista antes de guardarlo
                                - si no existe (fue eliminado de BD), lo omitimos con un log
                           
                            */
                            if (productoEncontrado != null) {

                                // tomamos el precio real del producto desde BD
                                double precioVentaProducto = productoEncontrado.getPrecioBaseProducto();

                                /*
                                registramos una línea de detalle por cada producto seleccionado
                                ejemplo: pedido 15, producto 3 (hamburguesa), cantidad 2, precio 19000
                                */
                                pedidoDao.registrarDetallePedido(
                                        identificadorPedidoGenerado,
                                        identificadorProducto,
                                        cantidadProductoPedido,
                                        precioVentaProducto);

                                System.out.println("DEBUG: Producto "
                                        + productoEncontrado.getNombreProducto()
                                        + " x" + cantidadProductoPedido
                                        + " a $" + precioVentaProducto);

                            } else {
                                // el producto no existe en BD, lo saltamos y avisamos en consola
                                System.out.println("WARN: Producto con id "
                                        + identificadorProducto
                                        + " no encontrado en BD, se omite.");
                            }
                        }
                    }
                }
            } // fin del while - ya revisamos todos los campos del formulario

            /*
            
            5.7 CAMBIAR ESTADO DE LA MESA A OCUPADA
                - ahora que el pedido está registrado, la mesa pasa a "ocupada"
                - esto se refleja en el panel del mesero en tiempo real
            
            */
            MesaDao mesaDao = new MesaDao();
            mesaDao.cambiarEstado(identificadorMesa, "ocupada");

            // redirigimos al panel de mesas con mensaje de éxito
            response.sendRedirect("html/m-meserocopy.jsp?pedido=exitoso");

        } else {

            /*
            
            6. SI EL PEDIDO NO SE PUDO GUARDAR
               - algo falló al insertar en BD
               - redirigimos de vuelta al formulario con código de error
               - mandamos el idMesa para que el mesero no pierda el contexto
           
            */
            response.sendRedirect("html/m-registrar-pedido.jsp?error=3&idMesa=" + identificadorMesa);
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