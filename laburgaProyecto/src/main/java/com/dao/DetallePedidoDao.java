package com.dao;

import com.conexion.claseConexion;
import com.modelo.DetallePedido;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DetallePedidoDao {

    // Método para registrar un nuevo detalle de pedido
    public boolean registrarDetalle(DetallePedido detalle) {
        String sql = "INSERT INTO detallePedido (id_pedido, id_producto, cantidad_producto, precio_unitarioventa, observaciones) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = claseConexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, detalle.getIdPedido());
            ps.setInt(2, detalle.getIdProducto());
            ps.setInt(3, detalle.getCantidad());
            ps.setDouble(4, detalle.getPrecioVenta());
            ps.setString(5, detalle.getObservaciones());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar DetallePedido: " + e.getMessage());
            return false;
        }
    }

    public java.util.List<DetallePedido> listarDetallesPorPedido(int idPedido) {
        java.util.List<DetallePedido> lista = new java.util.ArrayList<>();
        String sql = "SELECT * FROM detallePedido WHERE id_pedido = ?";

        try (java.sql.Connection con = com.conexion.claseConexion.getConexion(); java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPedido);
            java.sql.ResultSet rs = ps.executeQuery();

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
            System.out.println("Error al listar: " + e.getMessage());
        }
        return lista;
    }

    public java.util.List<DetallePedido> listarDetallesPorMesa(int idMesa) {
        java.util.List<DetallePedido> lista = new java.util.ArrayList<>();
        // Aquí hacemos el JOIN para conectar la mesa con los detalles
        String sql = "SELECT d.* FROM detallepedido d "
                + "INNER JOIN pedidos p ON d.id_pedido = p.id_pedido "
                + "WHERE p.id_mesa = ? AND p.estado_pedido = 'activo'";

        try (java.sql.Connection con = com.conexion.claseConexion.getConexion(); java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idMesa);
            java.sql.ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DetallePedido d = new DetallePedido();
                d.setIdDetalle(rs.getInt("id_detallepedido"));
                d.setIdPedido(rs.getInt("id_pedido")); // Guardamos el ID del pedido
                d.setIdProducto(rs.getInt("id_producto"));
                d.setCantidad(rs.getInt("cantidad_producto"));
                d.setPrecioVenta(rs.getDouble("precio_unitarioventa"));
                lista.add(d);
            }
        } catch (java.sql.SQLException e) {
            System.out.println("Error al listar por mesa: " + e.getMessage());
        }
        return lista;
    }
}
