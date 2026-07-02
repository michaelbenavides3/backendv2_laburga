
/*

responsabilidad; administar todas las operaciones rekacuibasd cib oedudism detalles pedidos, solictidu de cuenta y cobros dentro del restaurante


    - MÉTODO 1: registrarNuevoPedidoo   --> crear un nuevo pedido y ocupa la mesa
 
    - MÉTODO 2: registrarDetallePedido --> guarda los productos asociado a un pedido

    - METODO 3. ACTUALIZAR EL ESTADO DEL PEDIDO --> cambia el estado actual del pedido

    - METODO 4. LISTAR PEDIDOS PENDIENTES --> lista los pedidos listos para cobrar

    - METODO 5. SOLICITAR CUENTA --> marca un pedido como pendiente de cobro

    - METODO 6. OBTENER PEDIDOS ACTIVOS POR MESA  --> busca el pedido activo de una mesa

    - METODO 7. OBTENER PEDIDOS POR ID --> obtiene el resumen completo de un pedido

    - METODO 8. OBTENER ID MESA POR ID PEDIDO -->  lo usamos cuando el pedido queda vacio y necesitamos liberar la mesa


 */
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
        /*
        se declaran nulas porque todavia no existen, esta variable existe pero aun no apunta a ningun lado.
        */
        java.sql.Connection accesoBD = com.conexion.claseConexion.getConexion();
        java.sql.PreparedStatement operacion = null;
        java.sql.PreparedStatement operacionMesa = null;
        java.sql.ResultSet resultadoClave = null;
        /*
        Inserta el encabezado del pedido.

        Se guarda:
        - la mesa donde se realiza el pedido
        - el mesero que lo atiende
        - el estado inicial del pedido (activo)

        MySQL genera automáticamente el id_pedido.
        */
        String sqlPedido = "INSERT INTO pedidos (id_mesa, id_mesero, estado_pedido) VALUES (?, ?, ?)";
        /*
        Actualiza la mesa asociada al pedido.

        Cuando un pedido se registra correctamente, la mesa pasa a estado 'ocupada'.

        Esto evita que otro mesero tome la misma mesa y registre otro pedido sobre ella.
        */
        String sqlMesa = "UPDATE mesas SET estado_mesa = 'ocupada' WHERE id_mesas = ?";

        try {
            //insertamos el pedido
            /*
            aqui se prepara la consulta, utilizando  (prepareStatement)
            (RETURN_GENERATED_KEYS), despues de insertar el pedido, devuelve el id, que se caba de generar automaticamente, 
            */
            operacion = accesoBD.prepareStatement(sqlPedido, java.sql.Statement.RETURN_GENERATED_KEYS);
            /*
            se reemplaza el primer ?
            */
            operacion.setInt(1, nuevoPedido.getIdMesa());
            operacion.setInt(2, nuevoPedido.getIdMesero());
            operacion.setString(3, nuevoPedido.getEstadoPedido());
            /*
            aquie envia la consulta a la base de datos
            */
            int filasInsertadas = operacion.executeUpdate();
            /*
            si la fila se inserta exitosamente, o es mayor que 0 entra dentro del if, 
            */
            if (filasInsertadas > 0) {
                resultadoClave = operacion.getGeneratedKeys();
                /*
                el resultado quedara guardado en resultadoclave,  (ResultSet resultadoClave)
                */
                if (resultadoClave.next()) {
                    int idPedidioGenerado = resultadoClave.getInt(1); //se guarad el id el pedido
                    // return resultadoClave.getInt(1); // Éxito total: devuelve el ID generado
                    
                    /*
                    ahora se prepara la consulta del update
                    */
                    operacionMesa = accesoBD.prepareStatement(sqlMesa);
                    
                    operacionMesa.setInt(1, nuevoPedido.getIdMesa());
                    /*
                    aca se procede a cambiar el estado de la mesa
                    */
                    operacionMesa.executeUpdate();
                    /*
                    el metodo termina regresando el idpedidogenrado
                    */
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
        //insertameos en detallepedido los valroes, esto lo captura al momento de hacer el pedido
        String sqlQuery = "INSERT INTO detallePedido (id_pedido, id_producto, cantidad_producto, precio_unitarioventa) VALUES (?, ?, ?, ?)";

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

    /*
    METODO 3. ACTUALIZAR EL ESTADO DEL PEDIDO
     */
    //cambia el estado del pedido (ej. de activo a pendiente_cobro)
    public void actualizarEstadoPedido(int idPedido, String nuevoEstado) {
        //con el update le dceimos a la bd modificar pedidos, estadopedido, con el where se le aplica el cambio al idpedido
        String sql = "UPDATE pedidos SET estado_pedido = ? WHERE id_pedido = ?";
        try (Connection con = claseConexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idPedido);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error al actualizar estado del pedido: " + e.getMessage());
        }
    }

    /*
    METODO 4. LISTAR PEDIDOS PENDIENTES
    
     */
    //lista pedidos pendientes para el cajero
    public List<Pedido> listarPedidosPendientes() {
        List<Pedido> lista = new ArrayList<>();
        // Usamos SUM para calcular el total y GROUP_CONCAT para mostrar los productos
        //aquie unimos 3 tablas,, es para armar el ticket paa cobrar
        String sql = "SELECT p.id_pedido, p.id_mesa, "
                + "GROUP_CONCAT(prod.nombre_producto SEPARATOR ', ') as detalle, "
                + "SUM(dp.cantidad_producto * dp.precio_unitarioventa) as total "
                + "FROM pedidos p "
                + "JOIN detallePedido dp ON p.id_pedido = dp.id_pedido " //con el join unimos cada pedido para saber los pedidos qeue se encuenta ordenados
                + "JOIN productos prod ON dp.id_producto = prod.id_producto "
                + "WHERE p.estado_pedido = 'pendiente_cobro' "//con where se filtran para agrupar ls pedidos que ya se encutran listo para para pagar
                + "GROUP BY p.id_pedido";

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

    /*
    
    METODO 5. SOLICITAR CUENTA
     */
    //marca el pedido para que el cajero sepa que debe cobrarlo
    public boolean solicitarCuenta(int idPedido) {
        //update es para modificar la tabla pedidos, set cambia la columna estado y se activa como pendietnecobro con el where solo afecta wl idpedido que se llama 
        String sql = "UPDATE pedidos SET estado_pedido='pendiente_cobro' WHERE id_pedido=?";
        
        //// Uso de try-with-resources para asegurar el cierre automático de la conexión y el statement
        try (
                Connection con = claseConexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql);) {
            // Vinculación del parámetro ID al placeholder '?'
            ps.setInt(1, idPedido);
            // executeUpdate devuelve el número de filas afectadas; si es > 0, la operación fue un éxito
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    /*
    
    
    METODO 6. OBTENER PEDIDOS ACTIVOS POR MESA
     */
    //busca el pedido que esta abierto en una mesa
    public int obtenerPedidoActivoPorMesa(int idMesa) {

        //con select utilizamos para encontrar el pedido activo de una mesa
        //con where filtraos solo los pedidos activos
        //order by es para odenar los pedidos del ms nuevo al antiguo
        String sql = """
        SELECT id_pedido
        FROM pedidos
        WHERE id_mesa = ?
        AND estado_pedido = 'activo'
        ORDER BY id_pedido DESC
        LIMIT 1
    """;

        try (
                Connection con = claseConexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql);) {

            ps.setInt(1, idMesa);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("id_pedido");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return 0;
    }

    /*
    
    METODO 7. OBTENER PEDIDOS POR ID
    
     */
    //obtiene un pedido completo por su ID para mostrar el resumen
    public Pedido obtenerPedidoPorId(int idPedido) {

        Pedido p = null;

        String sql = "SELECT p.id_pedido, p.id_mesa, "
                + "GROUP_CONCAT(prod.nombre_producto SEPARATOR ', ') as detalle, "
                + "SUM(dp.cantidad_producto * dp.precio_unitarioventa) as total "
                + "FROM pedidos p "
                + "JOIN detallePedido dp ON p.id_pedido = dp.id_pedido "
                + "JOIN productos prod ON dp.id_producto = prod.id_producto "
                + "WHERE p.id_pedido = ? "
                + "GROUP BY p.id_pedido";

        try (
                Connection con = claseConexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPedido);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                p = new Pedido();

                p.setIdPedido(rs.getInt("id_pedido"));
                p.setIdMesa(rs.getInt("id_mesa"));
                p.setDetalle(rs.getString("detalle"));
                p.setTotal(rs.getDouble("total"));
            }

        } catch (Exception e) {
            System.out.println("Error al obtener pedido: " + e.getMessage());
        }

        return p;
    }

    /*
--------------------------------------------------------
METODO 8. OBTENER ID MESA POR ID PEDIDO
--------------------------------------------------------
OBJETIVO:
Obtener la mesa asociada a un pedido.
Se usa cuando el pedido quedó vacío y necesitamos
liberar la mesa.
     */
    public int obtenerMesaPorPedido(int identificadorPedido) {

        String consultaSql
                = "SELECT id_mesa "
                + "FROM pedidos "
                + "WHERE id_pedido = ?";

        try (
                Connection conexion = claseConexion.getConexion(); PreparedStatement sentencia = conexion.prepareStatement(consultaSql)) {

            sentencia.setInt(1, identificadorPedido);

            ResultSet resultado = sentencia.executeQuery();

            if (resultado.next()) {

                return resultado.getInt("id_mesa");
            }

        } catch (Exception error) {

            System.out.println( "Error al obtener mesa del pedido: " + error.getMessage());
        }

        return 0;
    }

}
