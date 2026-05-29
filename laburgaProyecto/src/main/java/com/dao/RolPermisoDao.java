package com.dao;

//importo la clase de la conexion
import com.conexion.claseConexion;
//importo el modulo de rol permisos para manejar los datos 
import com.modelo.RolPermisos;
//importo las herramientas para crear variables para mantener la conexcion sql
import java.sql.Connection;
import java.sql.PreparedStatement;
//importo el recolector de errores 
import java.sql.SQLDataException;
import java.sql.SQLException;

//se define mi clase
public class RolPermisoDao {

    //empiezando utiizando el metodo post. que es el encargado de asignar un permiso a un rol
    //ejmplo- administrador -> crear usuario
    //mesero -> crear pedido
    //recibe un objeto lleno con id del rol, id del permiso. devuelve true = si todo salio bien, false = si ocurrio un problema
    public boolean asignarPermisoARol(RolPermisos nuevaUnionObjeto) {
        //creo una variable para la conexion fisica de la bd y la inicializo vacia 
        Connection conexionFisicaBaseDatos = null;
        //variable para preparar la instruccion de sql
        PreparedStatement sentenciaSqlPreparada = null;
        //esta variable empieza falso y solo pasa a verdadero, si la operacion fue exitos
        boolean operacionAsignacionExitosa = false;

        //consulto el sql para guardar la relacion ente rol y permisos. 
        String consultaInsertarSql = "INSERT INTO rolPermisos (id_rol, id_permisos, activo_rolpermiso) VALUE (?, ?, ?)";

        //abro mi bloque de try catch, para proteger la base de datos del software de que se congele el programa
        try {
            //abrimos la conexion utilizando el metodo get con la clase conexion 
            conexionFisicaBaseDatos = claseConexion.getConexion();
            //verificamos que la conexion este correcata 
            if (conexionFisicaBaseDatos != null) {
                //preparamos la consutla sql
                sentenciaSqlPreparada = conexionFisicaBaseDatos.prepareStatement(consultaInsertarSql);
                //se remplaza el perimer ? con el mimero de id de rol ejemplo 2 que es el mesero
                sentenciaSqlPreparada.setInt(1, nuevaUnionObjeto.getIdRol());
                //se rempaza el segundo ?. con el nuemro de id del permisoej 5 crear pedido
                sentenciaSqlPreparada.setInt(2, nuevaUnionObjeto.getIdPermisos());
                //se remplaza el 3 ? 
                sentenciaSqlPreparada.setBoolean(3, nuevaUnionObjeto.isActivoRolpermiso());
                //envio la orden final a mysql, esta instrcuion de devuleve cuantas filas nuevas se crearon
                int cantidadFilasCreadas = sentenciaSqlPreparada.executeUpdate();
                //si la cantidad es mayor que 0, esto significa que la fila se guardo de manera correcta
                if (cantidadFilasCreadas > 0) {
                    operacionAsignacionExitosa = true;
                    //imprimo un mensaje de exito
                    System.out.println("exitoso, se vinculo el permiso con el rol correspondiente");
                }
            }
        } catch (SQLException errorBaseDatos) {
            System.out.println("error al intentar asociar el rol con el permiso en MYSQL: " + errorBaseDatos.getMessage());
            //cierro el blique de captura de errores de la base de datos
        } finally {
            //acitvo el protocolo de cerrado obligatorio
            try {
                //si la variable se queda abierta ocupando espacio
                if (sentenciaSqlPreparada != null) {
                    sentenciaSqlPreparada.close();//cierro la sentencia preparada
                }
                if (conexionFisicaBaseDatos != null) {
                    conexionFisicaBaseDatos.close();
                    System.out.println("conexion de asignacion de permisos, cerrada de forma limpia");
                }
            } catch (SQLException errorAlCerrar) {
                System.out.println("error al cerrar los recursos: " + errorAlCerrar.getMessage());
            }
        }
        return operacionAsignacionExitosa;
    }

    //metodo update o modificar nos sirve para habilitar o inhabilitar un permiso
    public boolean camibiarEstadoPermisoDeRol(int idRolParaModificar, int idPermisoParaModificar, boolean nuevoEstadoHabilitado) {
        //creo una variable para la conexion fisica de la bd y la inicializo vacia 
        Connection conexionFisicaBaseDatos = null;
        //variable para preparar la instruccion de sql
        PreparedStatement sentenciaSqlPreparada = null;
        //se incia en false, solo cambiara a verdadero cuando la operacion se exitosa
        boolean operacionActualizacionExitosa = false;
        //modifica la columna de estado basandome en la pareja de IDS, para asegurarme que solo afecte esta fila
        String consultaActualizacionSql = "UPDATE rolPermisos SET activo_rolpermiso = ? where id_rol = ? AND id_permisos = ?";

        try {
            //abrimos la conexion utilizando el metodo get con la clase conexion 
            conexionFisicaBaseDatos = claseConexion.getConexion();
            //verificamos que la conexion este correcata 
            if (conexionFisicaBaseDatos != null) {
                //preparamos la consutla sql
                sentenciaSqlPreparada = conexionFisicaBaseDatos.prepareStatement(consultaActualizacionSql);
                //remplazo el primer ? con el nuevo estado boolean true para actualizado false para apagado
                sentenciaSqlPreparada.setBoolean(1, nuevoEstadoHabilitado);
                //remplazo el segundo ? para el puesto de trabajo o el rol que voy a moficiar
                sentenciaSqlPreparada.setInt(2, idRolParaModificar);
                //remplazo el 3? con el id del permiso que quiero prender o a pagar
                sentenciaSqlPreparada.setInt(3, idPermisoParaModificar);
                int cantidadFilasModificadas = sentenciaSqlPreparada.executeUpdate();

                if (cantidadFilasModificadas > 0) {
                    operacionActualizacionExitosa = true;
                    System.out.println("se cambio el estado del permiso para este rol");

                }
            }
        } catch (SQLException errorBaseDatos) {
            System.out.println("error al intentar cambiar el estado del permiso en MYSQL: " + errorBaseDatos.getMessage());
            //cierro el blique de captura de errores de la base de datos
        } finally {
            //acitvo el protocolo de cerrado obligatorio
            try {
                //si la variable se queda abierta ocupando espacio
                if (sentenciaSqlPreparada != null) {
                    sentenciaSqlPreparada.close();//cierro la sentencia preparada
                }
                if (conexionFisicaBaseDatos != null) {
                    conexionFisicaBaseDatos.close();
                    System.out.println("conexion de asignacion de permisos, cerrada de forma limpia");
                }
            } catch (SQLException errorAlCerrar) {
                System.out.println("error al cerrar los recursos: " + errorAlCerrar.getMessage());
            }
        }
        return operacionActualizacionExitosa;
    }
}
