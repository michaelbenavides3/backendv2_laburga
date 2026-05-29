package com.dao;

import com.conexion.claseConexion;
import com.modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//comienza la clase encargada de administrar todos los accesos a la tabla de usuario
public class UsuarioDao {

    //metodo para registrar un nuevo empleado o trabajador
    //es un metodo publico que nos devuelve true o false, 
    //registrarnuevousuario es el nombre del metodo
    //donde recive un usuario
    //y se almace en una variable llamada nuevousuarioobjeto
    public boolean registrarNuevoUsuario(Usuario nuevoUsuarioObjeto) {

        //variable para la conexion fisisca y vacia para no generar conflictos
        Connection conexionFisicaBaseDatos = null;
        //variable para preparar la orden de insercion y tambien se deja null desde el princicipo
        PreparedStatement sentenciaSqlPreparada = null;
        //esta variable empieza en false y solo pasara a verdadero si la inserccion funciona
        boolean operacionRegistroExitosa = false;

        //llamo la instruccion sql exacta tal cuadl como es en mysql 
        //nota no inserto "id_usuario" porque es autoincremetn, ni el estado ni la fecha porque tiene valores
        //por defecto lo estoy utilizando con default
        String consultaInsertarSql = "INSERT INTO usuario (nombre_completo, nombre_usuario, contraseña_usuario, id_rol) VALUES (?, ?, ?, ?)";

        //ABRO MI BLOQUE DE CON EL TRY, evitando que es software se rompa
        try {
            //abro la comunicacion llamando al metodo get.conexion de mi claseconexion
            conexionFisicaBaseDatos = claseConexion.getConexion();
            //veririfios si la conexion realmente fucniona
            if (conexionFisicaBaseDatos != null) {

                sentenciaSqlPreparada = conexionFisicaBaseDatos.prepareStatement(consultaInsertarSql);
                //se reemplaza el primer?
                sentenciaSqlPreparada.setString(1, nuevoUsuarioObjeto.getNombreCompleto());
                sentenciaSqlPreparada.setString(2, nuevoUsuarioObjeto.getNombreUsuario());
                sentenciaSqlPreparada.setString(3, nuevoUsuarioObjeto.getContraseñaUsuario());
                sentenciaSqlPreparada.setInt(4, nuevoUsuarioObjeto.getIdRol());

                //manda ls instrucciones a mysql para guardar modificar o eliminar datos
                int cantidadFilasAfectadas = sentenciaSqlPreparada.executeUpdate();

                //comprobamos si es mayor a 0 significa que el usaurio fue crado
                if (cantidadFilasAfectadas > 0) {

                    operacionRegistroExitosa = true;

                    System.out.println("el nuevo empleado se registro correctamente");
                }
            }
        } catch (SQLException errorbaseDatos) {
            System.out.println("error al intentar registrar el nuevo usuario en la base de datos: " + errorbaseDatos.getMessage());

        } finally {
            try {
                if (sentenciaSqlPreparada != null) {
                    sentenciaSqlPreparada.close();

                }
                if (conexionFisicaBaseDatos != null) {
                    conexionFisicaBaseDatos.close();
                    System.out.println("conexion de registro de usuario cerrada de manera ordenada");
                }
            } catch (SQLException errorAlCerrar) {
                System.out.println("error al ceera los canales de datos de usuarios: " + errorAlCerrar.getMessage());
            }
        }
        return operacionRegistroExitosa;
    }

    //metodo get para traer la lista de los empleados registrados
    public List<Usuario> obtenerListaTodosLosUsuarios() {

        //creo una lista vacia para ir metiendo a los trabajadores uno por uno
        List<Usuario> listaDeUsuariosEncontrados = new ArrayList<>();

        Connection conexionFisicaBaseDatos = null;

        PreparedStatement sentenciaSqlPreparada = null;

        ResultSet filasResultadosSql = null;

        //escribo mi consulta sql para pedirle a mysql para que me deje ver las columnas principales de la tabla usuario
        String consultaSeleccionarSql = "SELECT id_usuario, nombre_completo, nombre_usuario, contraseña_usuario, id_rol, estado_usuario, fecha_creacion_usuario FROM usuario";

        try {
            conexionFisicaBaseDatos = claseConexion.getConexion();

            if (conexionFisicaBaseDatos != null) {
                //preparo la consulta sql dentro de la conexion que ya esta abierta
                sentenciaSqlPreparada = conexionFisicaBaseDatos.prepareStatement(consultaSeleccionarSql);
                //ejecuta la consitla usanso excutequery porque solaente voy a leer informacion
                //todo lo que se encuentra se guarda temproalmente en filasresultado
                filasResultadosSql = sentenciaSqlPreparada.executeQuery();

                // Recorro fila por fila mientras existan resultados disponibles.
                while (filasResultadosSql.next()) {

                    // Creo un objeto Usuario vacío para llenarlo con los datos de esta fila.
                    Usuario usuarioTemporalEncontrado = new Usuario();

                    // Obtengo el id del usuario desde MySQL y lo guardo dentro del objeto Java.
                    usuarioTemporalEncontrado.setIdUsuario(filasResultadosSql.getInt("id_usuario"));

                    // Obtengo el nombre completo y lo guardo dentro del objeto.
                    usuarioTemporalEncontrado.setNombreCompleto(filasResultadosSql.getString("nombre_completo"));

                    // Obtengo el nombre de usuario y lo guardo dentro del objeto.
                    usuarioTemporalEncontrado.setNombreUsuario(filasResultadosSql.getString("nombre_usuario"));

                    // Obtengo la contraseña del usuario y la guardo dentro del objeto.
                    usuarioTemporalEncontrado.setContraseñaUsuario(filasResultadosSql.getString("contraseña_usuario"));

                    // Obtengo el id del rol asociado al usuario.
                    usuarioTemporalEncontrado.setIdRol(filasResultadosSql.getInt("id_rol"));

                    // Obtengo el estado actual del usuario.
                    usuarioTemporalEncontrado.setEstadoUsuario(filasResultadosSql.getString("estado_usuario"));

                    // Obtengo la fecha de creación registrada en MySQL.
                    usuarioTemporalEncontrado.setFechaCreacionUsuario(filasResultadosSql.getTimestamp("fecha_creacion_usuario"));

                    // Agrego el usuario ya completo dentro de la lista general.
                    listaDeUsuariosEncontrados.add(usuarioTemporalEncontrado);

                }
            }
            //si ocuree un probelma o una columna esta mal escrita el programa enteara automaticamtne aca
        } catch (SQLException errorBaseDato) {
            System.out.println("error al intenttar obtener la lista de usuarios: " + errorBaseDato.getMessage());
        } finally {
            try {
                if (filasResultadosSql != null) {
                    filasResultadosSql.close();
                }
                if (sentenciaSqlPreparada != null) {
                    sentenciaSqlPreparada.close();
                }
                if (conexionFisicaBaseDatos != null) {
                    conexionFisicaBaseDatos.close();
                    System.out.println("conexion de lectura de usuarios cerrada de forma segura");
                }
            } catch (SQLException errorAlCerrar) {
                System.out.println("error al cerrar los canales de lectura de usaurio" + errorAlCerrar.getMessage());
            }
        }
        return listaDeUsuariosEncontrados;
    }

    // =========================================================================
    // OPERACIÓN: POST DE VALIDACIÓN (Buscar un Usuario para el Inicio de Sesión)
    // =========================================================================
    // Creo este método para recibir la identificación y la clave desde mi formulario web.
    // Irá a buscar en MySQL si existe un empleado con esos datos exactos.
    // Devolverá un objeto de tipo "Usuario" lleno si lo encuentra, o "null" si las credenciales están mal.
    public Usuario verificarCredencialesIngreso(String identificacionDigitada, String claveDigitada) {

        // Creo una variable para la conexión física y otra para preparar el código SQL de consulta.
        Connection conexionFisicaBaseDatos = null;
        PreparedStatement sentenciaSqlPreparada = null;

        // El "ResultSet" es la bandeja que sostendrá la fila del empleado si MySQL encuentra una coincidencia.
        ResultSet filaResultadoSql = null;

        // Creo un objeto de usuario vacío que empezará en null. Solo lo llenaré si los datos coinciden.
        Usuario usuarioValidadoEncontrado = null;

        // Escribo mi consulta SQL. Le pido que busque en la tabla al usuario que tenga esa identificación,
        // esa clave exacta, y que además esté en estado 'activo' para trabajar en mi restaurante.
        String consultaValidarSql = "SELECT id_usuario, nombre_completo, nombre_usuario, id_rol, estado_usuario FROM usuario WHERE nombre_usuario = ? AND contraseña_usuario = ? AND estado_usuario = 'activo'";

        try {
            // Abro la línea de comunicación directa con mi base de datos de MySQL.
            conexionFisicaBaseDatos = claseConexion.getConexion();

            if (conexionFisicaBaseDatos != null) {
                // Preparo mi consulta de selección segura.
                sentenciaSqlPreparada = conexionFisicaBaseDatos.prepareStatement(consultaValidarSql);

                // Sustituyo los signos "?" por los textos reales que digitó el empleado en el navegador web.
                sentenciaSqlPreparada.setString(1, identificacionDigitada);
                sentenciaSqlPreparada.setString(2, claveDigitada);

                // Ejecuto la consulta de lectura.
                filaResultadoSql = sentenciaSqlPreparada.executeQuery();

                // Si la bandeja encuentra una fila válida (".next()"), significa que el usuario sí existe y puso bien su clave.
                if (filaResultadoSql.next()) {
                    // Instancio mi objeto de usuario para sacarlo de su estado null.
                    usuarioValidadoEncontrado = new Usuario();

                    // Saco la información de las columnas de MySQL y las guardo en mi objeto de Java.
                    usuarioValidadoEncontrado.setIdUsuario(filaResultadoSql.getInt("id_usuario"));
                    usuarioValidadoEncontrado.setNombreCompleto(filaResultadoSql.getString("nombre_completo"));
                    usuarioValidadoEncontrado.setNombreUsuario(filaResultadoSql.getString("nombre_usuario"));
                    usuarioValidadoEncontrado.setIdRol(filaResultadoSql.getInt("id_rol"));
                    usuarioValidadoEncontrado.setEstadoUsuario(filaResultadoSql.getString("estado_usuario"));
                }
            }

        } catch (SQLException errorBaseDatos) {
            System.out.println("Error al intentar validar las credenciales en MySQL: " + errorBaseDatos.getMessage());
        } finally {
            // Aplico mi protocolo de apagado obligatorio para liberar la memoria de mi servidor.
            try {
                if (filaResultadoSql != null) {
                    filaResultadoSql.close();
                }
                if (sentenciaSqlPreparada != null) {
                    sentenciaSqlPreparada.close();
                }
                if (conexionFisicaBaseDatos != null) {
                    conexionFisicaBaseDatos.close();
                }
            } catch (SQLException errorAlCerrar) {
                System.out.println("Error al cerrar los canales de validacion: " + errorAlCerrar.getMessage());
            }
        }

        // Devuelvo el usuario relleno (éxito) o null (si puso mal la clave o no existe).
        return usuarioValidadoEncontrado;
    }

}

