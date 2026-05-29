
package com.dao;

//importo las clases de mi conexion y modelos y roles
import com.conexion.claseConexion;
import com.modelo.Roles;

//importo las herramientas de java oara ejecutrar las sentencias de mysql
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RolesDao {

    //crear un nuevo metodo para regitsr un rol nuevo, en la base de datos
    //recibe com paramentro un obejto de tipo roles, con la informacion que envio el nuevo usuario
    //devuelve true si se guardo correctamente o false, si esta si ocurrio algun problemas
    //Roles es el tipo de dato, nuevoRol es el parametro, es el nombre que lleva la variable para utilizarla dentro del metodo
    public boolean registrarNuevoRol(Roles nuevoRolObjeto){
        
        Connection conexionFisicaBaseDatos = null;
        //preparedStatement es: evita atques, organiza mejor, evita errores de comillas
        PreparedStatement sentenciaSqlPreparada = null;
        //esta variable empieza falsa, y solo cambiara a verdadeor si el registro tiene exito 
        boolean operacionRegistroExitoso = false;
        
        //escribo la orden de insercion tal como se encuentra en mysql 
        String consultaInsertaSql = "INSERT INTO roles (nombre_rol, descripcion_rol ) VALUES  (?, ?)";
        //abro un try catch para realizar las operacion y que no dañen el software
        try{
            //abro el canal a la conexion utilizando el metodo getconexion de mi claseconexion
            conexionFisicaBaseDatos = claseConexion.getConexion();
            //verificamos si el realmente esta funcioando 
            if(conexionFisicaBaseDatos !=null){
                sentenciaSqlPreparada = conexionFisicaBaseDatos.prepareStatement(consultaInsertaSql);
                //remplazo el primer ?
                sentenciaSqlPreparada.setString(1, nuevoRolObjeto.getNombreRol());
                //remplazo el segundo ?
                sentenciaSqlPreparada.setString(2, nuevoRolObjeto.getDescripcionRol());
                //envio la orden final al servidor de mi proyecto esta funcionado me devuelve un numero
                //que represetan cuantas filas se crearn en la tabla de bd
                int cantidadFilasAfectadas = sentenciaSqlPreparada.executeUpdate();//executeUpdate se usa cuando queremos ingresar, modificar, eliminar
                
                //si la cantidad es mayor que 0 signifca que la fila se guardo correctamente
                if(cantidadFilasAfectadas > 0){
                    operacionRegistroExitoso = true;
                    System.out.println("El nuevo rol se guardo correctamente en la base de datos");
                }
            }
        }catch(SQLException errorBaseDatos){
            System.out.println("error al intentar registar el nuevo rol en mssql: " + errorBaseDatos.getMessage());
        }finally{
            try{
                if(sentenciaSqlPreparada !=null){
                    sentenciaSqlPreparada.close();
                }
                if(conexionFisicaBaseDatos !=null){
                    conexionFisicaBaseDatos.close();
                    System.out.println("conexion de registro de roles cerrada");
                }
            }catch(SQLException errorAlCerrar){
                System.out.println("error al cerrar datos de roles: " + errorAlCerrar.getMessage());
            }
        }
        return operacionRegistroExitoso;
        
        
    }
    
    //metodo get (traer las listas)
    public List<Roles> obtenerListaTodosLosRoles(){
        //creo una lista vacia para ir almacaendado todo los roles encontrados
        List<Roles> listaDeRolesEncontrados = new ArrayList<>();
        //variable utilziada para la conexion de la base de datos y la dejo vacia
        Connection conexionFisicaBaseDatos = null;
        //variable para la consutla y la dejo vacia
        PreparedStatement sentenciaSqlPreparada = null;
        //inicializo vacia la variable que recibira las filas y la columnas de la tabla roles
        ResultSet filasResultadosSql = null;
        String consultaSeleccionarSql = null;
        //abro un try catch para realizar las pruebas sin ncesidad de que sebloquee el software
        try{
            //arbo la liena de conexion directa con la base de datos
            conexionFisicaBaseDatos = claseConexion.getConexion();
              //por medio de la sentencia if, reviso que la conexion con el servidor se haya realizado correctamente+
            if(conexionFisicaBaseDatos != null){
                //preparo la consutka de lectura dentro de la conexion que tengo activa
                sentenciaSqlPreparada = conexionFisicaBaseDatos.prepareStatement(consultaSeleccionarSql);
                //ejecuto la consulta usando "executeQuery", porque solo voy a leer datos no a modificarlos
                //todas las filas que encuentre el servidor, se depsitara dentro de "filasresultadosql"
                filasResultadosSql = sentenciaSqlPreparada.executeQuery();
                //uso un ciclo while, que recorre fila por fila, mientras la bandeja, detecte que hay una fila de resultados
                //("next()"), el ciclo seguira corriendo
                while(filasResultadosSql.next()){
                    //creo un objeto de tipo roles completamente vacio para llenarlo con las filas
                    Roles rolTemporalEncontrado = new Roles();
                    //saco el numero identificador del rol
                    rolTemporalEncontrado.setIdRol(filasResultadosSql.getInt("id_rol"));
                    rolTemporalEncontrado.setNombreRol(filasResultadosSql.getString("nombre_rol"));
                    rolTemporalEncontrado.setDescripcionRol(filasResultadosSql.getString("descripcion_rol"));
                    //ingreso el rol que ya tiene todos los datos cargados dentro de mi lista general
                    listaDeRolesEncontrados.add(rolTemporalEncontrado);
                }
        }
    }catch(SQLException errorBaseDatos){
            //imprimo el reporte de la falla
            System.out.println("error al intentar obtener la lista de permisos en mysql" + errorBaseDatos.getMessage());
        }finally{
            //se aciva el protocolo de apagado de seguridad pra limpiar los contenedores de mi servidor
            try{
                //si la bandeja temporal de fila se queo abierta con infrmacion adentro
                if(filasResultadosSql !=null){
                    //se destrye la bandeja de lectura para liberar ese espacio en el sistema
                    filasResultadosSql.close();
                }
                //si la sentencia preparada se quedo ocupando espacio
                if(sentenciaSqlPreparada !=null){
                    sentenciaSqlPreparada.close();
                }
                //si la liena de comunicaion con mysql quedo encendida
                if(conexionFisicaBaseDatos !=null){
                    conexionFisicaBaseDatos.close();
                    
                    //imprimo el siguiente mensaje
                    System.out.println("conexion de lectura de permisos cerrada");
                }
            }catch(SQLException errorAlCerrar){
                System.out.println("error al cerrar la lectura de permisos" + errorAlCerrar.getMessage());
            }
        }
        //regreso la lista completa con todos los permisos que encontre en la base de datos(puede ir vacia si no hay nada)
        return listaDeRolesEncontrados;
        
    }


}
