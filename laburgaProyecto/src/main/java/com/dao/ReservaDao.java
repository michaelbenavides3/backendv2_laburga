/*




Este DAO es el encargado de administrar todas las operaciones relacionadas
    con las reservas de mesas del restaurante.

    METODOS DISPONIBLES:

    1. METODO REGISTRAR NUEVA RESERVA      -> registra una nueva reserva en la base de datos.

    2. METODO OBTENER LISTA RESERVAS       -> obtiene todas las reservas registradas.

    3. METODO VALIDAR MESA RESERVADA       -> verifica si una mesa ya se encuentra reservada para una fecha y hora.

    4. METODO PARA OBTENER LA CAPACIDAD POR MESA  --> obtendremos la capacidad por mesas, si la reserva es mayor las persona que la capacidad no puede aplicar

    5. METODO FINALIZAR RESERVA       -> cambia el estado de una reserva de 'reservada' a 'finalizada'.
 */
package com.dao;

import com.conexion.claseConexion;
import com.modelo.Mesa;
import com.modelo.Reserva;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

import java.util.ArrayList;
import java.util.List;

public class ReservaDao {

    /*
     1. METODO REGISTRAR NUEVA RESERVA
    
    
        -OBJETIVO PRINCIAPL, registrar una nueva reserva dentro de la base de datos
        -controlador que lo utiliza reservaControlador
    
     */
    public boolean registrarNuevaReserva(Reserva nuevaReservaObjeto) {
        Connection conexionFisicaBaseDatos = null;
        PreparedStatement sentenciaReserva = null;
        PreparedStatement sentenciaMesa = null;
        boolean operacionRegistroExitosa = false;

        // CORRECCIÓN: Cambié 'fehca_reserva' por 'fecha_reserva'
        String consultaInsertarSql = "INSERT INTO reservas "
                + "(id_mesas, id_clientes, fehca_reserva, hora_reserva, "
                + "persona_reserva, observaciones_reserva, estado_reserva) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        // SQL para actualizar el estado de la mesa (ajusta 'estado' al nombre real de tu columna)
        String consultaActualizarMesa = "UPDATE mesas SET estado_mesa = 'reservada' WHERE id_mesas = ?";

        try {
            conexionFisicaBaseDatos = claseConexion.getConexion();
            if (conexionFisicaBaseDatos != null) {

                // Iniciamos transacción
                conexionFisicaBaseDatos.setAutoCommit(false);

                // 1. Registrar la reserva
                sentenciaReserva = conexionFisicaBaseDatos.prepareStatement(consultaInsertarSql);
                sentenciaReserva.setInt(1, nuevaReservaObjeto.getIdMesa());
                sentenciaReserva.setInt(2, nuevaReservaObjeto.getIdCliente());
                sentenciaReserva.setDate(3, nuevaReservaObjeto.getFechaReserva());
                sentenciaReserva.setTime(4, nuevaReservaObjeto.getHoraReserva());
                sentenciaReserva.setInt(5, nuevaReservaObjeto.getPersonasReserva());
                sentenciaReserva.setString(6, nuevaReservaObjeto.getObservacionesReserva());
                sentenciaReserva.setString(7, nuevaReservaObjeto.getEstadoReserva());
                sentenciaReserva.executeUpdate();

                // 2. Actualizar la mesa
                sentenciaMesa = conexionFisicaBaseDatos.prepareStatement(consultaActualizarMesa);
                sentenciaMesa.setInt(1, nuevaReservaObjeto.getIdMesa());
                sentenciaMesa.executeUpdate();

                // Confirmar cambios en ambas tablas
                conexionFisicaBaseDatos.commit();
                operacionRegistroExitosa = true;
                System.out.println("La reserva fue registrada y la mesa actualizada correctamente.");
            }
        } catch (SQLException errorBaseDatos) {
            try {
                if (conexionFisicaBaseDatos != null) {
                    conexionFisicaBaseDatos.rollback();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("Error al registrar la reserva en mysql: " + errorBaseDatos.getMessage());
        } finally {
            try {

                if (sentenciaReserva != null) {
                    sentenciaReserva.close();

                }

                if (conexionFisicaBaseDatos != null) {

                    conexionFisicaBaseDatos.close();

                    System.out.println("conexion de registro de reservas cerrada correctamente");

                }

            } catch (SQLException errorAlCerrar) {

                System.out.println("error al cerrar recursos de reservas: " + errorAlCerrar.getMessage());

            }

        }
        return operacionRegistroExitosa;
    }

    /*
    
    
        -2. METODO OBTENER LISTA RESERVAS
        - objetivo principal obtener todas las reservas registrada en la base de datos
        - retorna una lista con todas las reserbas encontradas
        - 
     */
    public List<Reserva> obtenerListaReservas() {

        // lista donde se almacenarán todas las reservas encontradas
        List<Reserva> listaReservasEncontradas = new ArrayList<>();

        // variable para la conexión física con MySQL
        Connection conexionFisicaBaseDatos = null;

        // variable para preparar la consulta SQL
        PreparedStatement sentenciaSqlPreparada = null;

        // variable que almacenará temporalmente las filas devueltas por MySQL
        ResultSet filasResultadoConsultaSql = null;

        String consultaSeleccionarReservasSql
                /*
                    * El objetivo de esta consulta es realizar un reporte consolidado de las reservas.
                    * Utilizamos múltiples INNER JOIN para transformar IDs numéricos en datos 
                    * comprensibles para el usuario final (nombres de clientes y números de mesa).
                 */
                = "SELECT "
                + "reservas.id_reservas, "
                + "reservas.id_mesas, "
                + "reservas.id_clientes, "
                + "reservas.fehca_reserva, "
                + "reservas.hora_reserva, "
                + "reservas.persona_reserva, "
                + "reservas.observaciones_reserva, "
                + "reservas.estado_reserva, "
                + "reservas.fecharegistro_reserva, "
                + "clientes.nombrecompleto_cliente, "
                + "mesas.numero_mesa "
                + "FROM reservas " //tabla base donde se hace la cunsulta
                + "INNER JOIN clientes " //vinculamos con la tabla clientes
                + "ON reservas.id_clientes = clientes.id_clientes " // Condición de unión: coincide el ID del cliente
                + "INNER JOIN mesas " // Vinculación con tabla de mesas
                + "ON reservas.id_mesas = mesas.id_mesas " // Condición de unión: coincide el ID de la mesa
                + "ORDER BY reservas.fecha_reserva, reservas.hora_reserva";     // Orden cronológico de las reservas

        try {

            // abrir la conexión con MySQL
            conexionFisicaBaseDatos = claseConexion.getConexion();

            // verificar que la conexión exista
            if (conexionFisicaBaseDatos != null) {

                // preparar la consulta
                sentenciaSqlPreparada = conexionFisicaBaseDatos.prepareStatement(consultaSeleccionarReservasSql);

                // ejecutar la consulta
                filasResultadoConsultaSql = sentenciaSqlPreparada.executeQuery();

                /*
                Recorremos todas las filas encontradas.

                Cada fila representa una reserva distinta.
                 */
                while (filasResultadoConsultaSql.next()) {

                    // crear objeto temporal vacío
                    Reserva reservaTemporalEncontrada = new Reserva();

                    // DATOS DE LA TABLA RESERVAS
                    reservaTemporalEncontrada.setIdReserva(filasResultadoConsultaSql.getInt("id_reservas"));

                    reservaTemporalEncontrada.setIdMesa(filasResultadoConsultaSql.getInt("id_mesas"));

                    reservaTemporalEncontrada.setIdCliente(filasResultadoConsultaSql.getInt("id_clientes"));

                    reservaTemporalEncontrada.setFechaReserva(filasResultadoConsultaSql.getDate("fecha_reserva"));

                    reservaTemporalEncontrada.setHoraReserva(filasResultadoConsultaSql.getTime("hora_reserva"));

                    reservaTemporalEncontrada.setPersonasReserva(filasResultadoConsultaSql.getInt("persona_reserva"));

                    reservaTemporalEncontrada.setObservacionesReserva(filasResultadoConsultaSql.getString("observaciones_reserva"));

                    reservaTemporalEncontrada.setEstadoReserva(filasResultadoConsultaSql.getString("estado_reserva"));

                    reservaTemporalEncontrada.setFechaRegistroReserva(filasResultadoConsultaSql.getTimestamp("fecharegistro_reserva"));

                    // DATOS OBTENIDOS POR JOIN
                    /*
                    Estos campos NO existen dentro de la tabla reservas.

                    Los obtenemos gracias a los INNER JOIN.
                     */
                    reservaTemporalEncontrada.setNombreCliente(filasResultadoConsultaSql.getString("nombrecompleto_cliente"));

                    reservaTemporalEncontrada.setNumeroMesa(filasResultadoConsultaSql.getInt("numero_mesa"));

                    // agregar la reserva cargada completamente a la lista
                    listaReservasEncontradas.add(reservaTemporalEncontrada);
                }
            }

        } catch (SQLException errorBaseDatos) {

            System.out.println(
                    "error al intentar obtener la lista de reservas: " + errorBaseDatos.getMessage());

        } finally {

            try {

                if (filasResultadoConsultaSql != null) {
                    filasResultadoConsultaSql.close();
                }

                if (sentenciaSqlPreparada != null) {
                    sentenciaSqlPreparada.close();
                }

                if (conexionFisicaBaseDatos != null) {

                    conexionFisicaBaseDatos.close();

                    System.out.println("conexion de lectura de reservas cerrada correctamente");
                }

            } catch (SQLException errorAlCerrarRecursos) {

                System.out.println("error al cerrar recursos de lectura de reservas: " + errorAlCerrarRecursos.getMessage());
            }
        }

        // devolver la lista completa de reservas
        return listaReservasEncontradas;
    }

    /*
    
    3. METODO VALIDAR MESA RESERVADA
         
        // verificar si una mesa se encuetra reservada para ese dia y hora 
    
            identificadorMesa
                -> número identificador de la mesa que se desea reservar.

            fechaReserva
                -> fecha para la cual se desea realizar la reserva.

            horaReserva
                -> hora para la cual se desea realizar la reserva.
     */
    public boolean validarMesaReservada(
            int identificadorMesa,
            Date fechaReserva,
            Time horaReserva) {

        // variable para abrir la conexión física con MySQL
        Connection conexionFisicaBaseDatos = null;

        // variable para preparar la consulta SQL
        PreparedStatement sentenciaSqlPreparada = null;

        // variable donde MySQL depositará el resultado
        ResultSet filasResultadoConsultaSql = null;

        // variable que almacenará la respuesta final
        boolean mesaYaReservada = false;

        /*
            * Esta consulta realiza una validación de disponibilidad técnica.
            * Buscamos si ya existe una reserva activa para una mesa específica
            * en una fecha y hora determinadas.
         */
        String consultaValidacionSql
                = "SELECT id_reservas "
                + "FROM reservas "
                + "WHERE id_mesas = ? " //filtramos el id de la mesa seleccioanda
                + "AND fehca_reserva = ? " //  filtramos por fecha de servas
                + "AND hora_reserva = ? " // por hora de reserva
                + "AND estado_reserva = 'reservada'";  // y si el estado sale reservada

        try {

            // abrir conexión
            conexionFisicaBaseDatos = claseConexion.getConexion();

            if (conexionFisicaBaseDatos != null) {

                // preparar consulta
                sentenciaSqlPreparada = conexionFisicaBaseDatos.prepareStatement(consultaValidacionSql);

                // reemplazar primer ?
                sentenciaSqlPreparada.setInt(1, identificadorMesa);

                // reemplazar segundo ?
                sentenciaSqlPreparada.setDate(2, fechaReserva);

                // reemplazar tercer ?
                sentenciaSqlPreparada.setTime(3, horaReserva);

                // ejecutar consulta
                filasResultadoConsultaSql = sentenciaSqlPreparada.executeQuery();

                /*
                Si next() devuelve true
                significa que encontró al menos una reserva.
                 */
                if (filasResultadoConsultaSql.next()) {

                    mesaYaReservada = true;

                    System.out.println("la mesa ya se encuentra reservada para esa fecha y hora");
                }

            }

        } catch (SQLException errorBaseDatos) {

            System.out.println("error al validar disponibilidad de la mesa: " + errorBaseDatos.getMessage());

        } finally {

            try {

                if (filasResultadoConsultaSql != null) {
                    filasResultadoConsultaSql.close();
                }

                if (sentenciaSqlPreparada != null) {
                    sentenciaSqlPreparada.close();
                }

                if (conexionFisicaBaseDatos != null) {

                    conexionFisicaBaseDatos.close();

                    System.out.println("conexion de validacion de reservas cerrada correctamente");
                }

            } catch (SQLException errorAlCerrar) {

                System.out.println("error al cerrar recursos de validacion: " + errorAlCerrar.getMessage());
            }
        }

        return mesaYaReservada;
    }

    /*
     4. METODO PARA OBTENER LA CAPACIDAD POR MESA
    
        - Objetivo: Obtener el número máximo de personas que soporta una mesa específica.
        - Utilidad: Permite validar en el controlador si la cantidad de personas 
          de la reserva es permitida según la capacidad física de la mesa.
     */
    public int obtenerCapacidadMesa(int identificadorMesa) {

        // variable para abrir la conexión física con MySQL
        Connection conexionFisicaBaseDatos = null;

        // variable para preparar la consulta SQL
        PreparedStatement sentenciaSqlPreparada = null;

        // variable para el resultado de la consulta
        ResultSet filasResultadoConsultaSql = null;

        // variable que almacenará la capacidad encontrada, por defecto 0
        int capacidadMesa = 0;

        // consulta SQL para obtener la capacidad de la mesa
        String consultaCapacidadSql = "SELECT capcidad_mesa FROM mesas WHERE id_mesas = ?";

        try {
            // abrir conexión
            conexionFisicaBaseDatos = claseConexion.getConexion();

            if (conexionFisicaBaseDatos != null) {

                // preparar consulta
                sentenciaSqlPreparada = conexionFisicaBaseDatos.prepareStatement(consultaCapacidadSql);

                // reemplazar el parámetro de la mesa
                sentenciaSqlPreparada.setInt(1, identificadorMesa);

                // ejecutar consulta
                filasResultadoConsultaSql = sentenciaSqlPreparada.executeQuery();

                // si se encuentra el registro, extraemos la capacidad
                if (filasResultadoConsultaSql.next()) {
                    capacidadMesa = filasResultadoConsultaSql.getInt("capcidad_mesa");
                }
            }

        } catch (SQLException errorBaseDatos) {
            System.out.println("error al obtener la capacidad de la mesa: " + errorBaseDatos.getMessage());

        } finally {
            try {
                if (filasResultadoConsultaSql != null) {
                    filasResultadoConsultaSql.close();
                }

                if (sentenciaSqlPreparada != null) {
                    sentenciaSqlPreparada.close();
                }

                if (conexionFisicaBaseDatos != null) {
                    conexionFisicaBaseDatos.close();
                    System.out.println("conexion de consulta de capacidad cerrada correctamente");
                }

            } catch (SQLException errorAlCerrar) {
                System.out.println("error al cerrar recursos de capacidad: " + errorAlCerrar.getMessage());
            }
        }

        // devolvemos la capacidad obtenida (o 0 si no se encontró la mesa)
        return capacidadMesa;
    }

    /*
     5. METODO FINALIZAR RESERVA
    
        - Objetivo: Cambiar el estado de la reserva a 'finalizada' 
          una vez que el cliente haya llegado y ocupado la mesa.
        - Controlador que lo utiliza: reservaControlador
     */
    public boolean finalizarReserva(int identificadorReserva) {

        // variable para abrir la conexión física con MySQL
        Connection conexionFisicaBaseDatos = null;

        // variable para preparar la instrucción SQL
        PreparedStatement sentenciaSqlPreparada = null;

        // variable que indica si la actualización fue exitosa
        boolean operacionFinalizacionExitosa = false;

        // consulta SQL para actualizar el estado
        String consultaFinalizarSql = "UPDATE reservas SET estado_reserva = 'finalizada' WHERE id_reservas = ?";

        try {
            // abrir la conexión con la base de datos
            conexionFisicaBaseDatos = claseConexion.getConexion();

            if (conexionFisicaBaseDatos != null) {

                // preparar la consulta SQL
                sentenciaSqlPreparada = conexionFisicaBaseDatos.prepareStatement(consultaFinalizarSql);

                // asociamos el ID de la reserva al signo ?
                sentenciaSqlPreparada.setInt(1, identificadorReserva);

                // ejecutamos la actualización
                int cantidadFilasAfectadas = sentenciaSqlPreparada.executeUpdate();

                // verificamos si se actualizó correctamente
                if (cantidadFilasAfectadas > 0) {
                    operacionFinalizacionExitosa = true;
                    System.out.println("la reserva ha sido finalizada correctamente");
                }
            }

        } catch (SQLException errorBaseDatos) {
            System.out.println("error al finalizar la reserva: " + errorBaseDatos.getMessage());

        } finally {
            try {
                if (sentenciaSqlPreparada != null) {
                    sentenciaSqlPreparada.close();
                }

                if (conexionFisicaBaseDatos != null) {
                    conexionFisicaBaseDatos.close();
                    System.out.println("conexion de finalizacion de reserva cerrada correctamente");
                }

            } catch (SQLException errorAlCerrar) {
                System.out.println("error al cerrar recursos de finalizacion: " + errorAlCerrar.getMessage());
            }
        }

        return operacionFinalizacionExitosa;
    }

}
