package com.dao;

import com.conexion.claseConexion;
import com.modelo.Factura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*   

## FACTURA DAO

METODO 1. REGISTRAR FACTURA

METODO 2. OBTENER FACTURA POR ID

## METODO 3. OBTENER FACTURA POR PEDIDO

 */
public class FacturaDao {


    /*

METODO 1. REGISTRAR FACTURA


Guarda una nueva factura en la base de datos.
     */
    public boolean registrarFactura(Factura nuevaFacturaObjeto) {

        String consultaSql = """
        INSERT INTO facturas
        (
            id_pedido,
            subtotal,
            iva,
            total,
            estado_pago
        )
        VALUES (?, ?, ?, ?, ?)
        """;

        try (
                Connection conexion = claseConexion.getConexion(); PreparedStatement sentenciaSql = conexion.prepareStatement(consultaSql)) {

            sentenciaSql.setInt(1, nuevaFacturaObjeto.getIdPedido());

            sentenciaSql.setDouble(2, nuevaFacturaObjeto.getSubtotal());

            sentenciaSql.setDouble(3, nuevaFacturaObjeto.getIva());

            sentenciaSql.setDouble(4, nuevaFacturaObjeto.getTotal());

            sentenciaSql.setString(5, nuevaFacturaObjeto.getEstadoPago());

            return sentenciaSql.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println("Error registrando factura: " + e.getMessage());
        }

        return false;
    }

    /*

METODO 2. OBTENER FACTURA POR ID

     */
    public Factura obtenerFacturaPorId(
            int idFactura) {

        Factura facturaEncontrada = null;

        String consultaSql = """
        SELECT *
        FROM facturas
        WHERE id_factura = ?
        """;

        try (
                Connection conexion = claseConexion.getConexion(); PreparedStatement sentenciaSql = conexion.prepareStatement(consultaSql)) {

            sentenciaSql.setInt(1, idFactura);

            ResultSet resultadoConsulta = sentenciaSql.executeQuery();

            if (resultadoConsulta.next()) {

                facturaEncontrada = new Factura();

                facturaEncontrada.setIdFactura(resultadoConsulta.getInt("id_factura"));

                facturaEncontrada.setIdPedido(resultadoConsulta.getInt("id_pedido"));

                facturaEncontrada.setFechaHoraFactura(resultadoConsulta.getTimestamp("fechaHora_factura"));

                facturaEncontrada.setSubtotal(resultadoConsulta.getDouble("subtotal"));

                facturaEncontrada.setIva(resultadoConsulta.getDouble("iva"));

                facturaEncontrada.setTotal(resultadoConsulta.getDouble("total"));

                facturaEncontrada.setEstadoPago(resultadoConsulta.getString("estado_pago"));
            }

        } catch (Exception e) {

            System.out.println("Error obteniendo factura: " + e.getMessage());
        }

        return facturaEncontrada;
    }

    /*

        METODO 3. OBTENER FACTURA POR PEDIDO


            Busca la factura asociada a un pedido.
     */
    public Factura obtenerFacturaPorPedido(
            int idPedido) {

        Factura facturaEncontrada = null;

        String consultaSql = """
        SELECT *
        FROM facturas
        WHERE id_pedido = ?
        LIMIT 1
        """;

        try (
                Connection conexion = claseConexion.getConexion(); PreparedStatement sentenciaSql = conexion.prepareStatement(consultaSql)) {

            sentenciaSql.setInt(1, idPedido);

            ResultSet resultadoConsulta = sentenciaSql.executeQuery();

            if (resultadoConsulta.next()) {

                facturaEncontrada = new Factura();

                facturaEncontrada.setIdFactura(resultadoConsulta.getInt("id_factura"));

                facturaEncontrada.setIdPedido(resultadoConsulta.getInt("id_pedido"));

                facturaEncontrada.setFechaHoraFactura(resultadoConsulta.getTimestamp("fechaHora_factura"));

                facturaEncontrada.setSubtotal(resultadoConsulta.getDouble("subtotal"));

                facturaEncontrada.setIva(resultadoConsulta.getDouble("iva"));

                facturaEncontrada.setTotal(resultadoConsulta.getDouble("total"));

                facturaEncontrada.setEstadoPago(resultadoConsulta.getString("estado_pago"));
            }

        } catch (Exception e) {

            System.out.println("Error obteniendo factura por pedido: " + e.getMessage());
        }

        return facturaEncontrada;
    }

}
