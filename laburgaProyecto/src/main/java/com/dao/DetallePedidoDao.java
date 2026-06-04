/*
responsabilidad administrar todos los productos asociado con cada pedido


    - 1. METODO PARA REGISTRAR DETALLES --> se utiliza por si el productos lleva detalles en especifico, sin cebolla

    - 2. METODO PARA LISTA DETALLE POR PEDIDO --> obtener todos los productos de un pedido en especifico

    - 3. METODO PARA LISTAR DETALLES POR MESAS --> obtenemos todos los productos activos de una mesa
*/



package com.dao;

import com.conexion.claseConexion;
import com.modelo.DetallePedido;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
    public java.util.List<DetallePedido> listarDetallesPorPedido(int idPedido) {
        java.util.List<DetallePedido> lista = new java.util.ArrayList<>();
        // aqui nomas pido todo de la tabla donde el id del pedido coincida con el que busco
        String sql = "SELECT * FROM detallePedido WHERE id_pedido = ?";

        try (java.sql.Connection con = com.conexion.claseConexion.getConexion(); java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPedido);
            java.sql.ResultSet rs = ps.executeQuery();

            // voy recorriendo fila por fila lo que trajo el select y lo guardo en la lista
            while (rs.next()) {
                DetallePedido d = new DetallePedido();
                d.setIdDetalle(rs.getInt("id_detallepedido"));
                d.setIdPedido(rs.getInt("id_pedido"));
                d.setIdProducto(rs.getInt("id_producto"));
                d.setCantidad(rs.getInt("cantidad_producto"));
                d.setPrecioVenta(rs.getDouble("precio_unitarioventa"));
                lista.add(d);
            }
        } catch (java.sql.SQLException e) {
            System.out.println("error al listar: " + e.getMessage());
        }
        return lista;
    }

    
    /*
    
    
    3. METODO PARA LISTAR DETALLES POR MESAS
    */
    // este es el importante para las mesas
    //este metodo tambien se utiliza en en solicitarcuentacontrolador
    public java.util.List<DetallePedido> listarDetallesPorMesa(int idMesa) {
        java.util.List<DetallePedido> lista = new java.util.ArrayList<>();

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
        
        String sql = "SELECT d.* FROM detallepedido d "
                + "INNER JOIN pedidos p ON d.id_pedido = p.id_pedido "
                + "WHERE p.id_mesa = ? AND p.estado_pedido = 'activo'";

        try (java.sql.Connection con = com.conexion.claseConexion.getConexion(); java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idMesa);
            java.sql.ResultSet rs = ps.executeQuery();

            //d vinee de la viarble detalle asi se le coloc en la consutla sql
            while (rs.next()) {
                DetallePedido d = new DetallePedido();
                d.setIdDetalle(rs.getInt("id_detallepedido"));
                d.setIdPedido(rs.getInt("id_pedido"));
                d.setIdProducto(rs.getInt("id_producto"));
                d.setCantidad(rs.getInt("cantidad_producto"));
                d.setPrecioVenta(rs.getDouble("precio_unitarioventa"));
                lista.add(d);
            }
        } catch (java.sql.SQLException e) {
            System.out.println("error al listar por mesa: " + e.getMessage());
        }
        return lista;
    }
}
