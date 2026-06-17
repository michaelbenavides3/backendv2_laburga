package com.dao;

import com.conexion.claseConexion;
import com.modelo.MedioPago;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

   
/*

## MEDIO PAGO DAO

METODO 1. REGISTRAR MEDIO DE PAGO --> Permite guardar un nuevo método de pago en la base de datos, registrando su nombre, descripción y estado inicial (activo o inactivo).

METODO 2. LISTAR MEDIOS DE PAGO --> Obtiene todos los métodos de pago registrados en la base de datos y los almacena en una lista para mostrarlos en el sistema.

METODO 3. OBTENER MEDIO DE PAGO POR ID -->  Busca y devuelve la información completa de un método de pago específico utilizando su identificador único

METODO 4. ACTIVAR O DESACTIVAR MEDIO DE PAGO -->  Permite activar o desactivar un método de pago existente modificando el valor del campo activo, sin eliminar el registro de la base de datos.

---

*/

public class MedioPagoDao {


/*

        METODO 1. REGISTRAR MEDIO DE PAGO
    
        Permite guardar un nuevo método de pago en la base de datos, registrando su nombre, descripción y estado inicial (activo o inactivo).

*/

public boolean registrarMedioPago(MedioPago nuevoMetodoPagoObjeto) {

        String consultaSql = """
        INSERT INTO mediospagos
        (
            metodo_pago,
            descripcion,
            activo
        )
        VALUES (?, ?, ?)
        """;

        try (
                Connection conexion = claseConexion.getConexion(); PreparedStatement sentenciaSql = conexion.prepareStatement(consultaSql)) {

            sentenciaSql.setString( 1, nuevoMetodoPagoObjeto.getMetodoPago());

            sentenciaSql.setString( 2, nuevoMetodoPagoObjeto.getDescripcion());

            sentenciaSql.setBoolean( 3, nuevoMetodoPagoObjeto.isActivo());

            return sentenciaSql.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println( "Error registrando metodo pago: " + e.getMessage());
        }

        return false;
    }

    /*

                METODO 2. LISTAR MEDIOS DE PAGO



                   Obtiene todos los métodos de pago registrados en la base de datos y los almacena en una lista para mostrarlos en el sistema.

     */
    public List<MedioPago> listarMediosPago() {
        List<MedioPago> listaMediosPago = new ArrayList<>();

        String consultaSql = """
        SELECT *
        FROM mediospagos
        """;

        try (
                Connection conexion = claseConexion.getConexion(); PreparedStatement sentenciaSql = conexion.prepareStatement( consultaSql)) {

            ResultSet resultadoConsulta = sentenciaSql.executeQuery();

            while (resultadoConsulta.next()) {

                MedioPago metodoPago = new MedioPago();

                metodoPago.setIdMetodoPago( resultadoConsulta.getInt( "id_mediodePago"));

                metodoPago.setMetodoPago( resultadoConsulta.getString( "metodo_pago"));

                metodoPago.setDescripcion( resultadoConsulta.getString( "descripcion"));

                metodoPago.setActivo( resultadoConsulta.getBoolean("activo"));

                listaMediosPago.add( metodoPago);
            }

        } catch (Exception e) {

            System.out.println( "Error listando medios de pago: " + e.getMessage());
        }

        return listaMediosPago;
    }

    /*

        METODO 3. OBTENER MEDIO DE PAGO POR ID
    
    
        Busca y devuelve la información completa de un método de pago específico utilizando su identificador único

     */
    public MedioPago obtenerMedioPagoPorId( int idMetodoPago) {

        MedioPago metodoPago = null;

        String consultaSql = """
        SELECT *
        FROM mediospagos
        WHERE id_mediodePago = ?
        """;

        try (
                Connection conexion = claseConexion.getConexion(); PreparedStatement sentenciaSql = conexion.prepareStatement( consultaSql)) {

            sentenciaSql.setInt( 1,idMetodoPago);

            ResultSet resultadoConsulta = sentenciaSql.executeQuery();

            if (resultadoConsulta.next()) {

                metodoPago = new MedioPago();

                metodoPago.setIdMetodoPago( resultadoConsulta.getInt( "id_mediodePago"));

                metodoPago.setMetodoPago( resultadoConsulta.getString( "metodo_pago"));

                metodoPago.setDescripcion( resultadoConsulta.getString( "descripcion"));

                metodoPago.setActivo( resultadoConsulta.getBoolean( "activo"));
            }

        } catch (Exception e) {

            System.out.println( "Error obteniendo metodo pago: " + e.getMessage());
        }

        return metodoPago;
    }

    /*

            METODO 4. CAMBIAR ESTADO
    
    
            Permite activar o desactivar un método de pago existente modificando el valor del campo activo, sin eliminar el registro de la base de datos.

     */
    public boolean cambiarEstadoMedioPago(int idMetodoPago, boolean nuevoEstado) {

        String consultaSql = """
        UPDATE mediospagos
        SET activo = ?
        WHERE id_mediodePago = ?
        """;

        try (
                Connection conexion = claseConexion.getConexion(); PreparedStatement sentenciaSql = conexion.prepareStatement( consultaSql)) {

            sentenciaSql.setBoolean( 1, nuevoEstado);

            sentenciaSql.setInt( 2, idMetodoPago);

            return sentenciaSql.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println( "Error cambiando estado metodo pago: " + e.getMessage());
        }

        return false;
    }

}
