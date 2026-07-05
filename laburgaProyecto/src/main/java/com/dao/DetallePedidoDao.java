/*
responsabilidad administrar todos los productos asociado con cada pedido


    - 1. METODO PARA REGISTRAR DETALLES --> se utiliza por si el productos lleva detalles en especifico, sin cebolla

    - 2. METODO PARA LISTA DETALLE POR PEDIDO --> obtener todos los productos de un pedido en especifico

    - 3. METODO PARA LISTAR DETALLES POR MESAS --> obtenemos todos los productos activos de una mesa

    - 4. METODO ACTUALIZAR CANTIDAD DETALLE --> permite modificar la cantidad de un producto ya registrado dentro de un pedido

    - 5. METODO ELIMINAR DETALLE PEDIDO  --> permite elminar un producto al pedido asociado

    - 6. MEOTODO OBTENER DETALLE POR ID --> obtener toda la informacion de una linea especifica del pedido para poder editarla.

    - 7.  MRTODO CONTAR DETALLES POR PEDIDO -->  este metodo se utiliza con el fin que si el pedido se queda sin proeuctos se cierre autocatimanete y pase a cerrado
 */
package com.dao;

import com.conexion.claseConexion;
import com.modelo.DetallePedido;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;

public class DetallePedidoDao {

    /*
    
    1. METODO PARA REGISTRAR DETALLES
     */
    // metodo para meter un producto nuevo en un pedido
    public boolean registrarDetalle(DetallePedido detalle) {
        /* sql: metemos los datos en la tabla 'detallePedido'. 
         * usamos los '?' porque el objeto 'detalle' trae el id del pedido, del producto, 
         * la cantidad, el precio y cualquier observacion que el mesero haya escrito.
         */
        String sql = "INSERT INTO detallePedido (id_pedido, id_producto, cantidad_producto, precio_unitarioventa, observaciones) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = claseConexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            // mapeo de variables: asigno cada dato del objeto a su lugar en el sql
            ps.setInt(1, detalle.getIdPedido());          // el id de a qué pedido pertenece esto
            ps.setInt(2, detalle.getIdProducto());        // el producto que eligieron
            ps.setInt(3, detalle.getCantidad());          // cuantas unidades pidieron
            ps.setDouble(4, detalle.getPrecioVenta());    // a cuanto se cobra la unidad
            ps.setString(5, detalle.getObservaciones());  // por si piden sin cebolla o algo asi

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0; // si devolvio mas de 0, es que guardo bien

        } catch (SQLException e) {
            System.out.println("error al meter el detalle: " + e.getMessage());
            return false;
        }
    }

    /*
    2. METODO PARA LISTA DETALLE POR PEDIDO 
    
    
     */
    // metodo para traer todos los productos de un pedido especifico
    public List<DetallePedido> listarDetallesPorPedido(int identificadorPedido) {

        // lista vacia donde se van a guardar todos los productos del pedido
        List<DetallePedido> listaDetallesPedido = new java.util.ArrayList<>();

        // consultamos el detalle del pedido y hacemos JOIN con productos
        // para obtener el nombre del producto sin tener que hacer otra consulta
        String consultaDetalleConProducto
                = "SELECT " /* Especifica qué columnas queremos traer de la base de datos */
                + "detallePedido.id_detallepedido, " /* El identificador único de este renglón del pedido */
                + "detallePedido.id_pedido, " /* El número general de la orden (ej: Pedido #45) */
                + "detallePedido.id_producto, " /* El código del producto solicitado (ej: Producto #8) */
                + "detallePedido.cantidad_producto, " /* Cuántas unidades de ese producto pidió el cliente (ej: 2) */
                + "detallePedido.precio_unitarioventa, " /* El precio al que se vendió en ese momento (por seguridad histórica) */
                + "detallePedido.observaciones, "     /* Notas especiales del cliente (ej: "Sin cebolla", "Bien cocido") */
                + "productos.nombre_producto "  /* ¡La clave! Trae el nombre real del producto desde la otra tabla */
                + "FROM detallePedido "  /* Indica que la búsqueda principal inicia en la tabla de detalles */
                + "INNER JOIN productos "  /* Une la tabla de detalles con la tabla de productos */
                + "ON detallePedido.id_producto = productos.id_producto " /* Conecta ambas tablas haciendo coincidir el código del producto */
                + "WHERE detallePedido.id_pedido = ?"; /* FILTRO: Trae SOLO los productos que correspondan al número de pedido solicitado */

        try (
                Connection conexionBaseDatos = claseConexion.getConexion(); PreparedStatement sentenciaPreparada = conexionBaseDatos.prepareStatement(consultaDetalleConProducto)) {

            // le indicamos de cual pedido queremos los detalles
            sentenciaPreparada.setInt(1, identificadorPedido);

            ResultSet resultadoConsulta = sentenciaPreparada.executeQuery();

            // recorremos fila por fila el resultado de la consulta
            while (resultadoConsulta.next()) {

                // creamos un objeto DetallePedido por cada fila encontrada
                DetallePedido detallePedidoActual = new DetallePedido();

                // id único de esta línea del pedido
                detallePedidoActual.setIdDetalle( resultadoConsulta.getInt("id_detallepedido"));

                // id del pedido al que pertenece esta línea
                detallePedidoActual.setIdPedido( resultadoConsulta.getInt("id_pedido"));

                // id del producto pedido
                detallePedidoActual.setIdProducto( resultadoConsulta.getInt("id_producto"));

                // cuántas unidades pidió el cliente
                detallePedidoActual.setCantidad( resultadoConsulta.getInt("cantidad_producto"));

                // precio al que se vendió la unidad en ese momento
                detallePedidoActual.setPrecioVenta( resultadoConsulta.getDouble("precio_unitarioventa"));

                // observaciones del mesero para este producto
                detallePedidoActual.setObservaciones( resultadoConsulta.getString("observaciones"));

                // nombre del producto obtenido del JOIN con la tabla productos
                detallePedidoActual.setNombreProducto( resultadoConsulta.getString("nombre_producto"));

                // calculamos el subtotal de esta línea: cantidad x precio unitario
                // ejemplo: 2 hamburguesas x $19.000 = $38.000
                double subtotalLineaCalculado = detallePedidoActual.getCantidad() * detallePedidoActual.getPrecioVenta();

                detallePedidoActual.setSubtotalLinea(subtotalLineaCalculado);

                // agregamos el detalle lleno a la lista final
                listaDetallesPedido.add(detallePedidoActual);
            }

        } catch (SQLException errorConsulta) {
            System.out.println("Error al listar detalles por pedido: "
                    + errorConsulta.getMessage());
        }

        return listaDetallesPedido;
    }

    /*
    
    
    3. METODO PARA LISTAR DETALLES POR MESAS
     */
    // este es el importante para las mesas
    //este metodo tambien se utiliza en en solicitarcuentacontrolador
    public java.util.List<DetallePedido> listarDetallesPorMesa(int idMesa) {
        java.util.List<DetallePedido> listaDesatallePedidosMesas = new java.util.ArrayList<>();

        /* explicacion del join:
         * como el detalle no sabe en que mesa esta, tengo que unir (INNER JOIN) con la tabla 'pedidos'.
         * conecto 'd.id_pedido' con 'p.id_pedido'.
         * asi puedo filtrar por el 'id_mesa' y asegurar que solo traiga los que estan 'activo'.
         */
 /*
        detallepedido d: se le nombra d a la tabla detallepedido para no escribir el nombre completo cada vez.
        pedidos p: lo mismo, p es el apodo de pedidos.
        d.*: "tráeme todas las columnas (*) pero solo de la tabla con alias d", o sea, solo de detallepedido. 
        d.id_pedido = p.id_pedido: unes ambas tablas por esa columna.
        p.id_mesa = ?: aquí aparece el comodín. Ese ? es el único parámetro que tienes que llenar desde Java, y corresponde al id de la mesa.
         */
        String sql
                = "SELECT "
                + "detallePedido.id_detallepedido, "
                + "detallePedido.id_pedido, "
                + "detallePedido.id_producto, "
                + "detallePedido.cantidad_producto, "
                + "detallePedido.precio_unitarioventa, "
                + "detallePedido.observaciones, "
                + "productos.nombre_producto "
                + "FROM detallePedido "
                + "INNER JOIN pedidos "
                + "ON detallePedido.id_pedido = pedidos.id_pedido "
                + "INNER JOIN productos "
                + "ON detallePedido.id_producto = productos.id_producto "
                + "WHERE pedidos.id_mesa = ? "
                + "AND pedidos.estado_pedido = 'activo'";

        try (java.sql.Connection con = com.conexion.claseConexion.getConexion(); java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idMesa);
            java.sql.ResultSet rs = ps.executeQuery();

            //d vinee de la viarble detalle asi se le coloc en la consutla sql
            while (rs.next()) {

                DetallePedido detallePedidoActual = new DetallePedido();

                detallePedidoActual.setIdDetalle(rs.getInt("id_detallepedido"));

                detallePedidoActual.setIdPedido(rs.getInt("id_pedido"));

                detallePedidoActual.setIdProducto(rs.getInt("id_producto"));

                detallePedidoActual.setCantidad(rs.getInt("cantidad_producto"));

                detallePedidoActual.setPrecioVenta(rs.getDouble("precio_unitarioventa"));

                detallePedidoActual.setObservaciones(rs.getString("observaciones"));

                detallePedidoActual.setNombreProducto(rs.getString("nombre_producto"));

                /*
                    subtotal de la línea.

                    ejemplo: hamburguesa x 3 20.000 * 3 = 60.000
                
                 */
                double subtotalLineaCalculado = detallePedidoActual.getCantidad() * detallePedidoActual.getPrecioVenta();

                detallePedidoActual.setSubtotalLinea(subtotalLineaCalculado);

                listaDesatallePedidosMesas.add(detallePedidoActual);

            }
        } catch (java.sql.SQLException e) {
            System.out.println("error al listar por mesa: " + e.getMessage());
        }
        return listaDesatallePedidosMesas;
    }

    /*
    
        4. METODO ACTUALIZAR CANTIDAD DETALLE
            
            - controlador que se utiliza es editarpedidocontrolador
    
             OBJETIVO: Permitir modificar la cantidad de un producto ya registrado dentro de un pedido.
    
     */
    public boolean actualizarCantidadDetalle(int identificadorDetallePedido, int nuevaCantidadProducto) {

        Connection conexionFisicaBaseDatos = null;

        PreparedStatement sentenciaSqlPreparada = null;

        boolean operacionActualizacionExitosa = false;

        /*
        UPDATE detallePedido

        Modifica la cantidad de un producto específico dentro del pedido.
        
        SET, es como nuestra variable = se le dice que el valor anterior ahora pasara al nuevo valor actuliza o cambia o modifica
        
        WHERE id_detallepedido = ?

        Garantiza que solamente se modifique la línea seleccionada.
         */
        String consultaActualizarSql
                = "UPDATE detallePedido "
                + "SET cantidad_producto = ? "
                + "WHERE id_detallepedido = ?";

        try {

            conexionFisicaBaseDatos = claseConexion.getConexion();

            if (conexionFisicaBaseDatos != null) {

                sentenciaSqlPreparada = conexionFisicaBaseDatos.prepareStatement(consultaActualizarSql);

                sentenciaSqlPreparada.setInt(1, nuevaCantidadProducto);

                sentenciaSqlPreparada.setInt(2, identificadorDetallePedido);

                int cantidadFilasActualizadas = sentenciaSqlPreparada.executeUpdate();

                if (cantidadFilasActualizadas > 0) {

                    operacionActualizacionExitosa = true;

                    System.out.println("cantidad del producto actualizada correctamente");
                }
            }

        } catch (SQLException errorBaseDatos) {

            System.out.println("error al actualizar cantidad del detalle: " + errorBaseDatos.getMessage());

        } finally {

            try {

                if (sentenciaSqlPreparada != null) {
                    sentenciaSqlPreparada.close();
                }

                if (conexionFisicaBaseDatos != null) {
                    conexionFisicaBaseDatos.close();
                }

            } catch (SQLException errorCerrarRecursos) {

                System.out.println("error al cerrar recursos: " + errorCerrarRecursos.getMessage());
            }
        }

        return operacionActualizacionExitosa;
    }

    /*
    
    
        5. METODO ELIMINAR DETALLE PEDIDO
    
            objetivo. como objetivo principal es eliminar completamente un productos asociado a pedido
    
            controaldo que se utiliza es editarpedidocontrolador
    
     */
    public boolean eliminarDetallePedido(int identificadorDetallePedido) {

        Connection conexionFisicaBaseDatos = null;

        PreparedStatement sentenciaSqlPreparada = null;

        boolean operacionEliminacionExitosa = false;

        /*
        DELETE FROM detallePedido

        Elimina únicamente la línea seleccionada del pedido.

        WHERE id_detallepedido = ?

        asi evitamos eliminar registros adicionales.
        
         */
        String consultaEliminarSql
                = "DELETE FROM detallePedido "
                + "WHERE id_detallepedido = ?";

        try {

            conexionFisicaBaseDatos = claseConexion.getConexion();

            if (conexionFisicaBaseDatos != null) {

                sentenciaSqlPreparada = conexionFisicaBaseDatos.prepareStatement(consultaEliminarSql);

                sentenciaSqlPreparada.setInt(1, identificadorDetallePedido);

                int cantidadFilasEliminadas = sentenciaSqlPreparada.executeUpdate();

                if (cantidadFilasEliminadas > 0) {

                    operacionEliminacionExitosa = true;

                    System.out.println("detalle eliminado correctamente");
                }
            }

        } catch (SQLException errorBaseDatos) {

            System.out.println("error al eliminar detalle del pedido: " + errorBaseDatos.getMessage());

        } finally {

            try {

                if (sentenciaSqlPreparada != null) {
                    sentenciaSqlPreparada.close();
                }

                if (conexionFisicaBaseDatos != null) {
                    conexionFisicaBaseDatos.close();
                }

            } catch (SQLException errorCerrarRecursos) {

                System.out.println("error al cerrar recursos: " + errorCerrarRecursos.getMessage());
            }
        }

        return operacionEliminacionExitosa;
    }

    /*
    
    6. METODO OBTENER DETALLE POR ID
    
        - utilizado desde EditarDetallePedidoControlador
        
        OBJETIVO: obtener toda la informacion de una linea especifica del pedido para poder editarla.
        
     */
    public DetallePedido obtenerDetallePorId(int identificadorDetallePedido) {

        Connection conexionFisicaBaseDatos = null;

        PreparedStatement sentenciaSqlPreparada = null;

        ResultSet resultadoConsulta = null;

        DetallePedido detallePedidoEncontrado = null;

        /*
    
             buscamos una unica linea del pedido usando su llave primaria
    
         */
        String consultaBuscarSql
                = "SELECT " /*seeccionar o consultar*/
                + "detallePedido.id_detallepedido, "
                + "detallePedido.id_pedido, "
                + "detallePedido.id_producto, "
                + "detallePedido.cantidad_producto, "
                + "detallePedido.precio_unitarioventa, "
                + "detallePedido.observaciones, "
                + "productos.nombre_producto " /*trae el nombre dle producto esto evita que se trabaia el idproducto*/
                + "FROM detallePedido " /*mi tabla  principal donde se realiza la consulta, nobre de la vairable detallepeido*/
                + "INNER JOIN productos " /*con el inner join se le dice quiero fusionar o conectar con la tabla productos*/
                + "ON detallePedido.id_producto = productos.id_producto " /*se conecta solo cuando el idproducto en el detalle coincida con idproducto de tabla producto*/
                + "WHERE detallePedido.id_detallepedido = ?";
        /*con el where, es el filtro, le estamos diciiendo bsqueme el registro, que coincida con el id que se envia atrabavez del ?*/

        try {

            conexionFisicaBaseDatos
                    = claseConexion.getConexion();

            if (conexionFisicaBaseDatos != null) {

                sentenciaSqlPreparada = conexionFisicaBaseDatos.prepareStatement(consultaBuscarSql);

                sentenciaSqlPreparada.setInt(1, identificadorDetallePedido);

                resultadoConsulta = sentenciaSqlPreparada.executeQuery();

                if (resultadoConsulta.next()) {

                    detallePedidoEncontrado = new DetallePedido();

                    detallePedidoEncontrado.setIdDetalle(resultadoConsulta.getInt("id_detallepedido"));

                    detallePedidoEncontrado.setIdPedido(resultadoConsulta.getInt("id_pedido"));

                    detallePedidoEncontrado.setIdProducto(resultadoConsulta.getInt("id_producto"));

                    detallePedidoEncontrado.setCantidad(resultadoConsulta.getInt("cantidad_producto"));

                    detallePedidoEncontrado.setPrecioVenta(resultadoConsulta.getDouble("precio_unitarioventa"));

                    detallePedidoEncontrado.setObservaciones(resultadoConsulta.getString("observaciones"));

                    detallePedidoEncontrado.setNombreProducto(resultadoConsulta.getString("nombre_producto"));

                    /*
                
                calculamos subtotal de la linea
                
                     */
                    double subtotalLineaCalculado = detallePedidoEncontrado.getCantidad() * detallePedidoEncontrado.getPrecioVenta();

                    detallePedidoEncontrado.setSubtotalLinea(subtotalLineaCalculado);
                }
            }

        } catch (SQLException errorBaseDatos) {

            System.out.println("error al obtener detalle por id: " + errorBaseDatos.getMessage());

        } finally {

            try {

                if (resultadoConsulta != null) {
                    resultadoConsulta.close();
                }

                if (sentenciaSqlPreparada != null) {
                    sentenciaSqlPreparada.close();
                }

                if (conexionFisicaBaseDatos != null) {
                    conexionFisicaBaseDatos.close();
                }

            } catch (SQLException errorCerrarRecursos) {

                System.out.println("error al cerrar recursos: " + errorCerrarRecursos.getMessage());
            }
        }

        return detallePedidoEncontrado;
    }

    /*
    
        7. METODO CONTAR DETALLES POR PEDIDO
    
        - utilizado despues de eliminar productos
        - permite verificar si el pedido aun tiene productos
        - si devuelve 0, el pedido puede cerrarse automaticamente

     */
    public int contarDetallesPorPedido(int identificadorPedido) {

        Connection conexionFisicaBaseDatos = null;

        PreparedStatement sentenciaSqlPreparada = null;

        ResultSet resultadoConsulta = null;

        int cantidadDetallesPedido = 0;

        /*
    
            COUNT(*) cuenta cuantas filas existen del pedido recibido
    
         */
        String consultaConteoSql
                = "SELECT COUNT(*) AS cantidad_detalles " /*con select count nos dice cuanto registros cumplen la condicion*/
                + "FROM detallePedido " /*busaca dentro de la tabla detallepedido*/
                + "WHERE id_pedido = ?";

        try {

            conexionFisicaBaseDatos = claseConexion.getConexion();

            if (conexionFisicaBaseDatos != null) {

                sentenciaSqlPreparada = conexionFisicaBaseDatos.prepareStatement(consultaConteoSql);

                sentenciaSqlPreparada.setInt(1, identificadorPedido);

                resultadoConsulta = sentenciaSqlPreparada.executeQuery();

                if (resultadoConsulta.next()) {

                    cantidadDetallesPedido = resultadoConsulta.getInt("cantidad_detalles");
                }
            }

        } catch (SQLException errorBaseDatos) {

            System.out.println("error al contar detalles del pedido: " + errorBaseDatos.getMessage());

        } finally {

            try {

                if (resultadoConsulta != null) {
                    resultadoConsulta.close();
                }

                if (sentenciaSqlPreparada != null) {
                    sentenciaSqlPreparada.close();
                }

                if (conexionFisicaBaseDatos != null) {
                    conexionFisicaBaseDatos.close();
                }

            } catch (SQLException errorCerrarRecursos) {

                System.out.println("error al cerrar recursos: " + errorCerrarRecursos.getMessage());
            }
        }

        return cantidadDetallesPedido;
    }

}
