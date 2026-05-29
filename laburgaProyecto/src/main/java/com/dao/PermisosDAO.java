package com.dao;

//importamos la clase de conexion, hacia la mysql
import com.conexion.claseConexion;
//importamos los permisos para manejar las prpiedases
import com.modelo.Permisos;

//importamos las herramientas de java para interacturar con la bse de datos
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//importamos las herramientas para crear listas dinamicas
import java.util.ArrayList;
import java.util.List;

public class PermisosDAO {

    //vamos a registra un nuevo permiso.
    //creo este metodo para recibir un objeto completo de tipo "permisos" y guardarlo de fomra fisica
    //en las filas de mi tabla si devuelve "true" la operacion fue extiosa "false" ocurrio un probelma en la base de datos
    public boolean registrarNuevopermiso(Permisos nuevoPermisoObjeto) {

        //creo una variable para la liena de conexion y otra para la sentencia de texto
        //amabas inician vacias "null", para asegurar de que arranqen limpias
        Connection conexionFisicaBaseDatos = null;
        PreparedStatement sentenciasSqlPreparada = null;

        //creo un variable boolena que empieza en falso, solo pasara a verdadero cuando se confirme la sentencia en mysql
        boolean operacionRegistroExitoso = false;

        //escribo la intrucion para insentar registro.
        String consutlaInsertarSql = "INSERT INTO permisos (slug, nombre_permiso, descripcion_permiso) values (?, ?, ?)";

        try {
            //intento llamar al metodo getconexion de mi clase conexion
            conexionFisicaBaseDatos = claseConexion.getConexion();
            //verifico que la conexion se encuentra aboerta y no vacia
            if (conexionFisicaBaseDatos != null) {

                sentenciasSqlPreparada = conexionFisicaBaseDatos.prepareStatement(consutlaInsertarSql);

                //remplazo los signos (?), en orden reales de im objeto en java
                //explicacion aca del get <-----
                sentenciasSqlPreparada.setString(1, nuevoPermisoObjeto.getSlugPermisos()); //se reemplaza el primer ? con el slug,(ejemplo: 'pedido.crear')
                sentenciasSqlPreparada.setString(2, nuevoPermisoObjeto.getNombrePermiso()); //se reemplaza el seguno ?, con el nombre del permiso (crear un pedido)
                sentenciasSqlPreparada.setString(3, nuevoPermisoObjeto.getDescripcionPermiso());// se reemplaza el tercer ?. con la descripcion detallad

                //envio la orden final al servidor de mi proyecto esta funcion de me devuelve un numero
                //que representa cuantas filas se crearon en la tabla de la base de datos
                int cantidadFilasAfectadas = sentenciasSqlPreparada.executeUpdate();

                //con una condicional de if, si la tencia es mayor a 0 siginifica que la operacion fue extios
                if (cantidadFilasAfectadas > 0) {
                    //cambio mi variable a verdadero a true porque la operacion se completo
                    operacionRegistroExitoso = true;

                    //imprimo un aviso mostrando el mensaje de confirmacion
                    System.out.println("Se registro el nuevo permiso de forma correcta en MYSQL");
                }
            }
        } catch (SQLException errorBaseDatos) {
            //si el slug que se intenta registra ya existe o si se excribe mal el sistema saltara al catch
            System.out.println("Error al intentra registrar el permisos en MYSQL");
        } finally {
            try {
                //se ejecuta obligatoriamente pase lo que pase, para cerrar las llaves y asi se evita que el servidor se coloque lento
                if (sentenciasSqlPreparada != null) {
                    sentenciasSqlPreparada.close();
                }
                if (conexionFisicaBaseDatos != null) {
                    conexionFisicaBaseDatos.close();
                    System.out.println("conexion de registros cerrada de manera segura");
                }
            } catch (SQLException errorAlCerrar) {
                System.out.println("error al cerrar los canales de datos permitidos");

            }
        }
        //le devuelve el resultado final si es (verdadero o falso) al fomrulairo quese mando a llamar
        return operacionRegistroExitoso;
    }
    
    //GET: traer la lista de todos los permisos
    //creamos este metodo para ir mysql, scar todas las filas que existan en l tabla "permisos"
    //y guardarlas ordenadamente en una lista 
    public List<Permisos>obtenerListaTodosLosPermisos(){
        
        //creo una lista vacia para ir almacenando los permisos uno por uno, a medida que la base de datos lo vaya leyendo
        List<Permisos> listaDePermisosEncontrados = new ArrayList<>();
        //creo una variable para la conexion y la dejo vacia antes de inciar la lectura
        Connection conexionFisicaBaseDatos = null;
        //creo una variable para ala orden de sql de consulta y la inicializo vacia
        PreparedStatement sentenciaSqlPreparada = null;
        //inicializo vacia mo bandeja de java, recibira las filas y las columnas de vuelyas por mysql
        ResultSet filasResultadosSql = null;
        //escribo mi consulta sql, para pedirle a mysql que deje ver todas la columnas de la tabla permisos
        String consultaSeleccionarSql = "SELECT id_permisos, slug, nombre_permiso, descripcion_permiso FROM permisos";
        //declaro un try catch para realizar la operacion sin bloquear el software
        try{
            //abro la liena de comuniacion directa con la base de datos de mi proyecto
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
                    //creo un objeto de tipo permisos, para completar con la fila de este campo
                    Permisos permisosTemporalEncontrado = new Permisos();
                    //saco el numero identificador de la fila actual de mysql y lo inyecto en mi objeto
                    permisosTemporalEncontrado.setIdPermisos(filasResultadosSql.getInt("id_permisos"));
                    //saco la palabra clave de la fina y lo guardo en la propieda slug de mi objeto
                    permisosTemporalEncontrado.setSlugPermisos(filasResultadosSql.getString("slug"));
                    //
                    permisosTemporalEncontrado.setNombrePermiso(filasResultadosSql.getString("nombre_permiso"));
                    //
                    permisosTemporalEncontrado.setDescripcionPermiso(filasResultadosSql.getString("descripcion_permiso"));
                    
                    //con el permiso que ya tiene todos sus datos cragados, dentro de mi lista general de la carpeta
                    listaDePermisosEncontrados.add(permisosTemporalEncontrado);
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
        return listaDePermisosEncontrados;
        
    }
}
