package com.dao;

import com.conexion.claseConexion;
import com.modelo.Pago;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*

## PAGO DAO

METODO 1. REGISTRAR PAGO --> Guarda un nuevo pago asociado a una factura.

METODO 2. OBTENER PAGO POR ID

## METODO 3. OBTENER PAGO POR FACTURA  --> Permite saber si una factura ya tiene pago registrado.

 */
public class PagoDao {


    /*

        METODO 1. REGISTRAR PAGO


        Guarda un nuevo pago asociado a una factura.
     */
    public boolean registrarPago( Pago nuevoPagoObjeto) {

        String consultaSql = """
        INSERT INTO pagos
        (
            id_factura,
            id_mediodePago
        )
        VALUES (?, ?)
        """;

        try (
                Connection conexion = claseConexion.getConexion(); PreparedStatement sentenciaSql = conexion.prepareStatement(consultaSql)) {

            sentenciaSql.setInt( 1, nuevoPagoObjeto.getIdFactura());

            sentenciaSql.setInt( 2, nuevoPagoObjeto.getIdMetodoPago());

            return sentenciaSql.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println( "Error registrando pago: "+ e.getMessage());
        }

        return false;
    }

    /*

            METODO 2. OBTENER PAGO POR ID

     */
    public Pago obtenerPagoPorId( int idPago) {

        Pago pagoEncontrado = null;

        String consultaSql = """
        SELECT *
        FROM pagos
        WHERE id_pago = ?
        """;

        try (
                Connection conexion= claseConexion.getConexion(); PreparedStatement sentenciaSql = conexion.prepareStatement( consultaSql)) {

            sentenciaSql.setInt(1, idPago);

            ResultSet resultadoConsulta = sentenciaSql.executeQuery();

            if (resultadoConsulta.next()) {

                pagoEncontrado = new Pago();

                pagoEncontrado.setIdPago( resultadoConsulta.getInt( "id_pago"));

                pagoEncontrado.setIdFactura( resultadoConsulta.getInt( "id_factura"));

                pagoEncontrado.setIdMetodoPago( resultadoConsulta.getInt( "id_mediodePago"));

                pagoEncontrado.setFechaHoraPago( resultadoConsulta.getTimestamp( "fechaHora_pago"));
            }

        } catch (Exception e) { System.out.println("Error obteniendo pago: "+ e.getMessage());
        }

        return pagoEncontrado;
    }

    /*

            METODO 3. OBTENER PAGO POR FACTURA


            Permite saber si una factura ya tiene pago registrado.
     */
    public Pago obtenerPagoPorFactura(int idFactura) {

        Pago pagoEncontrado = null;

        String consultaSql = """
        SELECT *
        FROM pagos
        WHERE id_factura = ?
        LIMIT 1
        """;

        try (
                Connection conexion= claseConexion.getConexion(); PreparedStatement sentenciaSql = conexion.prepareStatement( consultaSql)) {

            sentenciaSql.setInt( 1, idFactura);

            ResultSet resultadoConsulta = sentenciaSql.executeQuery();

            if (resultadoConsulta.next()) {

                pagoEncontrado  = new Pago();

                pagoEncontrado.setIdPago( resultadoConsulta.getInt( "id_pago"));

                pagoEncontrado.setIdFactura(  resultadoConsulta.getInt( "id_factura"));

                pagoEncontrado.setIdMetodoPago(  resultadoConsulta.getInt( "id_mediodePago"));

                pagoEncontrado.setFechaHoraPago( resultadoConsulta.getTimestamp( "fechaHora_pago"));
            }

        } catch (Exception e) {

            System.out.println( "Error obteniendo pago por factura: " + e.getMessage());
        }

        return pagoEncontrado;
    }

}
