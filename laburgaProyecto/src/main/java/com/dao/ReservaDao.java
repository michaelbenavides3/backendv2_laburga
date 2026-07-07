/*




Este DAO es el encargado de administrar todas las operaciones relacionadas
    con las reservas de mesas del restaurante.

    METODOS DISPONIBLES:

    1. METODO REGISTRAR NUEVA RESERVA      -> registra una nueva reserva en la base de datos.

    2. METODO OBTENER LISTA RESERVAS       -> obtiene todas las reservas registradas.

    3. METODO VALIDAR MESA RESERVADA       -> verifica si una mesa ya se encuentra reservada para una fecha y hora. si esta reservada bloquear las reservas nuevs

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

        PreparedStatement sentenciaDetalleReserva = null;

        ResultSet resultadoIdGenerado = null;

        boolean operacionRegistroExitosa = false;

        /*

            INSERT DE LA RESERVA.

         */
        String consultaInsertarReserva = """
            INSERT INTO reservas
            (
                id_clientes,
                fehca_reserva,
                hora_reserva,
                persona_reserva,
                observaciones_reserva,
                estado_reserva
            )
                VALUES (?, ?, ?, ?, ?, ?)
            """;

        /*

            INSERT EN DETALLERESERVAMESA

            Guarda qué mesa pertenece a la reserva.

         */
        String consultaInsertarDetalle = """
            INSERT INTO detalleReservaMesa
            (
                id_reserva,
                id_mesa
            )
            VALUES (?, ?)
            """;

        try {

            conexionFisicaBaseDatos = claseConexion.getConexion();

            if (conexionFisicaBaseDatos != null) {

                conexionFisicaBaseDatos.setAutoCommit(false);

                /*
        
                    PASO 1. REGISTRAR RESERVA
        
                 */
                sentenciaReserva = conexionFisicaBaseDatos.prepareStatement(consultaInsertarReserva, PreparedStatement.RETURN_GENERATED_KEYS);

                sentenciaReserva.setInt(1, nuevaReservaObjeto.getIdCliente());

                sentenciaReserva.setDate(2, nuevaReservaObjeto.getFechaReserva());

                sentenciaReserva.setTime(3, nuevaReservaObjeto.getHoraReserva());

                sentenciaReserva.setInt(4, nuevaReservaObjeto.getPersonasReserva());

                sentenciaReserva.setString(5, nuevaReservaObjeto.getObservacionesReserva());

                sentenciaReserva.setString(6, nuevaReservaObjeto.getEstadoReserva());

                sentenciaReserva.executeUpdate();

                /*
        
                     PASO 2.OBTENER ID DE LA RESERVA GENERADA
        
                 */
                resultadoIdGenerado = sentenciaReserva.getGeneratedKeys();

                int idReservaGenerada = 0;

                if (resultadoIdGenerado.next()) {

                    idReservaGenerada = resultadoIdGenerado.getInt(1);
                }

                /*
        
                    PASO 3. GUARDAR RELACION RESERVA - MESA
        
                 */
                sentenciaDetalleReserva = conexionFisicaBaseDatos.prepareStatement(consultaInsertarDetalle);

                sentenciaDetalleReserva.setInt(1, idReservaGenerada);

                sentenciaDetalleReserva.setInt(2, nuevaReservaObjeto.getIdMesa());

                sentenciaDetalleReserva.executeUpdate();

                /*
        
                     CONFIRMAR TRANSACCION
        
                 */
                conexionFisicaBaseDatos.commit();

                operacionRegistroExitosa = true;

                System.out.println("Reserva registrada correctamente."
                );
            }

        } catch (SQLException errorBaseDatos) {

            try {

                if (conexionFisicaBaseDatos != null) {

                    conexionFisicaBaseDatos.rollback();
                }

            } catch (SQLException e) {

                e.printStackTrace();
            }

            System.out.println("Error al registrar la reserva: " + errorBaseDatos.getMessage()
            );

        } finally {

            try {

                if (resultadoIdGenerado != null) {
                    resultadoIdGenerado.close();
                }

                if (sentenciaDetalleReserva != null) {
                    sentenciaDetalleReserva.close();
                }

                if (sentenciaReserva != null) {
                    sentenciaReserva.close();
                }

                if (conexionFisicaBaseDatos != null) {

                    conexionFisicaBaseDatos.close();

                    System.out.println("Conexion de reservas cerrada correctamente."
                    );
                }

            } catch (SQLException errorAlCerrar) {

                System.out.println("Error al cerrar recursos: " + errorAlCerrar.getMessage()
                );
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
                + "detalleReservaMesa.id_mesa, "
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
                + "INNER JOIN detalleReservaMesa "
                + "ON reservas.id_reservas = detalleReservaMesa.id_reserva "
                + "INNER JOIN clientes " //vinculamos con la tabla clientes
                + "ON reservas.id_clientes = clientes.id_clientes " // Condición de unión: coincide el ID del cliente
                + "INNER JOIN mesas " // Vinculación con tabla de mesas
                + "ON detalleReservaMesa.id_mesa = mesas.id_mesas " // Condición de unión: coincide el ID de la mesa
                + "ORDER BY reservas.fehca_reserva, reservas.hora_reserva";     // Orden cronológico de las reservas

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
                System.out.println("Consultando reservas...");

                while (filasResultadoConsultaSql.next()) {

                    // crear objeto temporal vacío
                    Reserva reservaTemporalEncontrada = new Reserva();

                    // DATOS DE LA TABLA RESERVAS
                    reservaTemporalEncontrada.setIdReserva(filasResultadoConsultaSql.getInt("id_reservas"));

                    reservaTemporalEncontrada.setIdMesa(filasResultadoConsultaSql.getInt("id_mesa"));

                    reservaTemporalEncontrada.setIdCliente(filasResultadoConsultaSql.getInt("id_clientes"));

                    reservaTemporalEncontrada.setFechaReserva(filasResultadoConsultaSql.getDate("fehca_reserva"));

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

                    System.out.println("Reserva encontrada: " + filasResultadoConsultaSql.getInt("id_reservas"));
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

    OBJETIVO

    Verificar si la mesa ya posee una reserva ACTIVA durante las siguientes dos horas.

    Si existe una reserva activa no permitirá registrar otra.

    Si la reserva fue finalizada antes (estado = finalizada) la mesa volverá a estar disponible.

     */
    public boolean validarMesaReservada(  int identificadorMesa, Date fechaReserva, Time horaReserva) {

        
        
        Connection conexionFisicaBaseDatos = null;

        PreparedStatement sentenciaSqlPreparada = null;

        ResultSet filasResultadoConsultaSql = null;

        boolean mesaYaReservada = false;

        /*
        CONSULTA SQL

        Busca reservas que:

        1. Sean de la misma mesa.
        2. Sean el mismo día.
        3. Estén activas.
        4. Su hora esté comprendida entre la hora solicitada y dos horas después.

        Ejemplo

        Nueva reserva: 7:00 pm

        Buscará reservas entre 7:00 pm  y  9:00 pm

         */
        String consultaValidacionSql = """
                     
        SELECT reservas.id_reservas
         /* Trae el ID de la reserva si encuentra un choque */

        FROM reservas
         /* Busca en la tabla principal de reservas */                                                                     

        INNER JOIN detalleReservaMesa
        /* Conecta con la tabla que sabe que mesa tiene cada reserva */
                                                                      
        ON reservas.id_reservas = detalleReservaMesa.id_reserva
         /* Usa el ID de la reserva como puente de union */                               
        WHERE detalleReservaMesa.id_mesa = ?
         /* FILTRO 1: Evalua la mesa que pide el cliente */                              
        AND reservas.fehca_reserva = ?
         /* FILTRO 2: Evalua la fecha (ojo con la ortografia 'fehca') */                               
        AND reservas.estado_reserva = 'reservada'
         /* FILTRO 3: Solo toma en cuenta reservas activas y vigentes */                               
        AND ?
         /* FILTRO 4: Aqui Java inyecta la HORA NUEVA que pide el cliente */                                                                      
        BETWEEN reservas.hora_reserva
         /* Verifica si la hora nueva es mayor o igual a la hora de una reserva existente */                                                                      
        AND ADDTIME(reservas.hora_reserva,'02:00:00')
        /* Y si es menor o igual a esa hora existente mas 2 horas de tolerancia */  
                                                                    
        """;
         
        

        try {

            conexionFisicaBaseDatos = claseConexion.getConexion();

            if (conexionFisicaBaseDatos != null) {

                sentenciaSqlPreparada = conexionFisicaBaseDatos.prepareStatement( consultaValidacionSql);

                /*
                PARAMETRO 1 Id de la mesa.
                 */
                sentenciaSqlPreparada.setInt( 1, identificadorMesa);

                /*
                PARAMETRO 2 Fecha solicitada.
                 */
                sentenciaSqlPreparada.setDate( 2, fechaReserva);

                /*
                PARAMETRO 3 Hora inicial del rango.

                Ejemplo

                7:00 pm
                 */
                sentenciaSqlPreparada.setTime( 3, horaReserva);

                /*
                PARAMETRO 4 Hora utilizada para calcular las dos horas posteriores.

                Ejemplo

                ADDTIME(7:00,2)

                Resultado

                9:00 pm
                 */
               /* sentenciaSqlPreparada.setTime( 4, horaReserva);*/

                filasResultadoConsultaSql = sentenciaSqlPreparada.executeQuery();

                /*
                Si existe al menos un registro

                significa que la mesa ya está  ocupada dentro del rango permitido.
                 */
                if (filasResultadoConsultaSql.next()) {

                    mesaYaReservada = true;

                    System.out.println( "La mesa ya posee una reserva activa " + "durante las próximas dos horas.");
                }

            }

        } catch (SQLException errorBaseDatos) {

            System.out.println( "Error validando disponibilidad: " + errorBaseDatos.getMessage());

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

                    System.out.println( "Conexión de validación cerrada correctamente.");
                }

            } catch (SQLException errorCerrar) {

                System.out.println( "Error cerrando recursos: " + errorCerrar.getMessage());
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
