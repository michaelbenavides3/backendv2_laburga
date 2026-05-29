package com.dao;

import com.conexion.claseConexion; // Tu clase oficial de conectar a MySQL
import com.modelo.Pedido;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

public class PedidoDao {

    // MÉTODO 1: Para crear el pedido 
    public int registrarNuevoPedido(Pedido nuevoPedido) {
        java.sql.Connection accesoBD = com.conexion.claseConexion.getConexion();
        java.sql.PreparedStatement operacion = null;
        java.sql.PreparedStatement operacionMesa = null;
        java.sql.ResultSet resultadoClave = null;

        String sqlPedido = "INSERT INTO pedidos (id_mesa, id_mesero, estado_pedido) VALUES (?, ?, ?)";
        String sqlMesa = "UPDATE mesas SET estado_mesa = 'ocupada' WHERE id_mesas = ?";

        try {
            //insertamos el pedido
            operacion = accesoBD.prepareStatement(sqlPedido, java.sql.Statement.RETURN_GENERATED_KEYS);
            operacion.setInt(1, nuevoPedido.getIdMesa());
            operacion.setInt(2, nuevoPedido.getIdMesero());
            operacion.setString(3, nuevoPedido.getEstadoPedido());

            int filasInsertadas = operacion.executeUpdate();

            if (filasInsertadas > 0) {
                resultadoClave = operacion.getGeneratedKeys();
                if (resultadoClave.next()) {
                    int idPedidioGenerado = resultadoClave.getInt(1); //se guarad el id el pedido
                    // return resultadoClave.getInt(1); // Éxito total: devuelve el ID generado

                    operacionMesa = accesoBD.prepareStatement(sqlMesa);
                    operacionMesa.setInt(1, nuevoPedido.getIdMesa());
                    operacionMesa.executeUpdate();

                    return idPedidioGenerado;
                }
            }
        } catch (Exception error) {
            System.out.println("Error al guardar pedido en MySQL: " + error.getMessage());
        } finally {
            // Cerramos recursos adicionales de forma segura
            try {
                if (resultadoClave != null) {
                    resultadoClave.close();
                }
            } catch (Exception e) {
            }
            try {
                if (operacionMesa != null) {
                    operacionMesa.close();
                }
            } catch (Exception e) {
            }
            try {
                if (operacion != null) {
                    operacion.close();
                }
            } catch (Exception e) {
            }
        }

        return 0; // Si falla devuelve 0
    }

    // MÉTODO 2:para guardar los productos del pedido
    public boolean registrarDetallePedido(int idPedido, int idProducto, int cantidad, double precioVenta) {
        Connection accesoBD = claseConexion.getConexion();
        PreparedStatement operacion;

        // SQL limpio apuntando a tu tabla de detalles (Ajusta los nombres si cambian en tu BD)
        String sqlQuery = "INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";

        try {
            operacion = accesoBD.prepareStatement(sqlQuery);
            operacion.setInt(1, idPedido);
            operacion.setInt(2, idProducto);
            operacion.setInt(3, cantidad);
            operacion.setDouble(4, precioVenta);

            int filasInsertadas = operacion.executeUpdate();
            return filasInsertadas > 0; // Devuelve true si se guardó el producto con éxito
        } catch (Exception error) {
            System.out.println("Error en el detalle del pedido DAO: " + error.getMessage());
            return false;
        }
    }

    public void actualizarEstadoPedido(int idPedido, String nuevoEstado) {
        String sql = "UPDATE pedidos SET estado_pedido = ? WHERE id_pedido = ?";
        try (Connection con = claseConexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idPedido);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error al actualizar estado del pedido: " + e.getMessage());
        }
    }

    public List<Pedido> listarPedidosPendientes() {
        List<Pedido> lista = new ArrayList<>();
        // Usamos SUM para calcular el total y GROUP_CONCAT para mostrar los productos
        String sql = "SELECT p.id_pedido, p.id_mesa, "
                + "GROUP_CONCAT(prod.nombre_producto SEPARATOR ', ') as detalle, "
                + "SUM(dp.cantidad_producto * dp.precio_unitarioventa) as total "
                + "FROM pedidos p "
                + "JOIN detallePedido dp ON p.id_pedido = dp.id_pedido "
                + "JOIN productos prod ON dp.id_producto = prod.id_producto "
                + "WHERE p.estado_pedido = 'activo' "
                + // O el estado que uses para pendiente de pago
                "GROUP BY p.id_pedido";

        try (Connection con = claseConexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pedido p = new Pedido();
                p.setIdPedido(rs.getInt("id_pedido"));
                p.setIdMesa(rs.getInt("id_mesa"));
                p.setDetalle(rs.getString("detalle")); // Ahora esto trae los nombres de los productos
                p.setTotal(rs.getDouble("total"));     // Esto trae la suma calculada
                lista.add(p);
            }
        } catch (Exception e) {
            System.out.println("Error al listar pedidos: " + e.getMessage());
        }
        return lista;
    }

    public boolean solicitarCuenta(int idPedido) {

        String sql = "UPDATE pedidos SET estado_pedido='pendiente_cobro' WHERE id_pedido=?";

        try (
                Connection con = claseConexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql);) {

            ps.setInt(1, idPedido);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }
}
