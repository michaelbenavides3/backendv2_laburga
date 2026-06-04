/*
   ----METODOS QUE SE ENCUENTRAN EN EL DAO USUARIADAO-----
  
   - 1.nombre metodo; REGISTRAR NUEVO USUARIO --> registra empleado
   - 2. Metodo OBTENER LISTA DE TODOS LOS USUARIOS  --> consulta empleados registrados
   - 3. METODO VERIFICAR CREDENCIALES DE INGRESO    --> validar inicio de sesion
   - 4. METODO OBTENER ULTIMO INSERTADO   --> recuperar el id  generado por mysql
   - 5. METODO OBTENER LISTA DE USUARIOS CON ROL --> consultar usuario junto con su rol
   - 6. METODO ACTUALIZAR ESTADO USUARIO --> activr o desactivar un usuario
*/



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
    
    /*
    1 metodo; en este metodo vamoa crear un nuevo usuario o trabajar es devido caso. 
    - lo utiliza el controlador usuariocontrolador.java\
    - vista quien origina la peticion es a-nuevoTrabajo.jsp
    - su funcion principal es registrar un nuevo empleado o trabjado en la tabla
    -su proceso, recibe un usuario nuevo con los datos capturados del formulario, abre la conexion con mysql,
    inserta; nombre, nombreUsuario, contrase;a, idrol.
    1.nombre metodo; REGISTRAR NUEVO USUARIO
    */

    //metodo para registrar un nuevo empleado o trabajador
    //es un metodo publico que nos devuelve true o false, 
    //registrarnuevousuario es el nombre del metodo
    //donde recive un usuario
    //y se almace en una variable llamada nuevousuarioobjeto
    public int registrarNuevoUsuario(Usuario nuevoUsuarioObjeto) {

        // Conexión física con la base de datos
        Connection conexionFisicaBaseDatos = null;

        // Sentencia SQL preparada para insertar el usuario
        PreparedStatement sentenciaSqlPreparada = null;

        // ResultSet para capturar el ID generado por MySQL
        ResultSet resultadoIdGenerado = null;

        // Aquí guardaremos el ID del usuario recién creado
        int idUsuarioGenerado = -1;

        // Consulta SQL de inserción (NO incluye id_usuario porque es AUTO_INCREMENT)
        String consultaInsertarUsuarioSql
                = "INSERT INTO usuario (nombre_completo, nombre_usuario, contraseña_usuario, id_rol) VALUES (?, ?, ?, ?)";

        try {
            // Abrimos conexión a la base de datos
            conexionFisicaBaseDatos = claseConexion.getConexion();

            if (conexionFisicaBaseDatos != null) {

                // Preparamos la consulta permitiendo obtener el ID generado
                sentenciaSqlPreparada = conexionFisicaBaseDatos.prepareStatement(
                        consultaInsertarUsuarioSql,
                        PreparedStatement.RETURN_GENERATED_KEYS
                );

                // Asignamos los valores del objeto Usuario
                sentenciaSqlPreparada.setString(1, nuevoUsuarioObjeto.getNombreCompleto());
                sentenciaSqlPreparada.setString(2, nuevoUsuarioObjeto.getNombreUsuario());
                sentenciaSqlPreparada.setString(3, nuevoUsuarioObjeto.getContraseñaUsuario());
                sentenciaSqlPreparada.setInt(4, nuevoUsuarioObjeto.getIdRol());

                // Ejecutamos el INSERT en la base de datos
                int filasAfectadas = sentenciaSqlPreparada.executeUpdate();

                // Verificamos si realmente se insertó el usuario
                if (filasAfectadas > 0) {

                    // Obtenemos el ID generado automáticamente por MySQL
                    resultadoIdGenerado = sentenciaSqlPreparada.getGeneratedKeys();

                    if (resultadoIdGenerado.next()) {
                        idUsuarioGenerado = resultadoIdGenerado.getInt(1);
                    }

                    System.out.println("El nuevo empleado se registró correctamente");
                    System.out.println("ID del usuario generado: " + idUsuarioGenerado);
                }
            }

        } catch (SQLException errorBaseDatos) {
            System.out.println("Error al registrar usuario en la base de datos: " + errorBaseDatos.getMessage());

        } finally {
            try {
                if (resultadoIdGenerado != null) {
                    resultadoIdGenerado.close();
                }

                if (sentenciaSqlPreparada != null) {
                    sentenciaSqlPreparada.close();
                }

                if (conexionFisicaBaseDatos != null) {
                    conexionFisicaBaseDatos.close();
                    System.out.println("Conexión cerrada correctamente");
                }

            } catch (SQLException errorCerrar) {
                System.out.println("Error al cerrar conexiones: " + errorCerrar.getMessage());
            }
        }

        // Retornamos el ID real del usuario creado
        return idUsuarioGenerado;
    }
    
    /*
    2. Metodo OBTENER LISTA DE TODOS LOS USUARIOS
    
    -controlado que lo utiliza es usuariocontrolador
    -vista que consume la informacion a-usuarios.jsp
    -retorna una lista de usuario
    -su proceso es consultar la tabla usuarios, recorre cada fila encobntrada convierte cada registro sql en un objeto
    agrega cada objeto a una lista, retorna la lista completa
    */

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
    
    /*
    3. METODO VERIFICAR CREDENCIALES DE INGRESO
        -Este metodo lo utiliza logincontrolador para el acceso del personal
        -vista de origin esta mezcalda con index.html y t-login que es formulario donde se logea los usuarios   
        -su funcion princpal es es validar el acceso de los empleados al sistema
        -su proceso, recibe un usuaurio, busca la coincidenciaen la tabla usuarios, verifica que el usuario este activo,
        si esta acticvo crea un objeto usuario y carga los datos
        
    */
    
    
    
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
    
    
    /*
    4. METODO OBTENER ULTIMO INSERTADO
        
        -Llamado desde 
    */
    
    public int obtenerUltimoIdInsertado(Connection con) throws SQLException {
        String sql = "SELECT LAST_INSERT_ID()";
        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    
    /*
    5. METODO OBTENER LISTA DE USUARIOS CON ROL
    
    - LA Vista dodne se utiliza en a-listar-usurios.jsp
    -funcion principal obtener todos los usuarios registrados junto con el nombre descriptivo de su rol
    -su proceso consutla la tabla de usuarios relaizar un innner join con la tabla roles, obtiene la informacion combinada
    y retorna una lista completa
    
    */
    //un metodo para obtener el usaurio con su numero de rol
    // =====================================================================
// OPERACIÓN GET:
// Obtener todos los usuarios junto con el nombre de su rol.
// =====================================================================
    public List<Usuario> obtenerListaUsuariosConRol() {

        // Lista vacía donde iré almacenando cada usuario encontrado.
        List<Usuario> listaDeUsuariosEncontrados = new ArrayList<>();

        // Variable para la conexión física con MySQL.
        Connection conexionFisicaBaseDatos = null;

        // Variable para preparar la consulta SQL.
        PreparedStatement sentenciaSqlPreparada = null;

        // Variable que almacenará temporalmente las filas devueltas por MySQL.
        ResultSet filasResultadosSql = null;

        // Consulta SQL:
        // Se realiza un INNER JOIN entre la tabla usuario y la tabla roles.
        //
        // 
        // Porque en la tabla usuario solo existe el id_rol.
        //
        // 
        // usuario.id_rol = 2
        //
        // Pero el administrador necesita ver:
        // "cajero"
        //
        // Entonces hacemos el JOIN para traer el nombre del rol.
        String consultaSeleccionarSql
                = "SELECT "     //con el select se elige que columnas desea consular 
                + "usuario.id_usuario, "      // se consultan estos campos
                + "usuario.nombre_completo, "  // se consultan estos campos
                + "usuario.nombre_usuario, "    // se consultan estos campos
                + "usuario.estado_usuario, "    // se consultan estos campos
                + "roles.nombre_rol "       // se consultan estos campos
                + "FROM usuario "  // con el from de se le indica que la informacion se encuentra en usuario
                + "INNER JOIN roles "   //con el inner join de la tabla roles, se treare todo los registros que tengan coincidencia en ambas tablas
                + "ON usuario.id_rol = roles.id_rol " // se le dice a la busqueda que haga en la tabla roles, aquel id que coincida con el idrol de la tabla guardad en usuario
                + "ORDER BY usuario.id_usuario";

        try {

            // Abrimos la conexión con la base de datos.
            conexionFisicaBaseDatos = claseConexion.getConexion();

            // Verificamos que la conexión exista.
            if (conexionFisicaBaseDatos != null) {

                // Preparamos la consulta SQL.
                sentenciaSqlPreparada
                        = conexionFisicaBaseDatos.prepareStatement(consultaSeleccionarSql);

                // Ejecutamos la consulta.
                filasResultadosSql
                        = sentenciaSqlPreparada.executeQuery();

                // Recorremos una por una todas las filas encontradas.
                while (filasResultadosSql.next()) {

                    // Creamos un objeto vacío para cargar los datos.
                    Usuario usuarioTemporalEncontrado = new Usuario();

                    // DATOS DE LA TABLA USUARIO
                    usuarioTemporalEncontrado.setIdUsuario(
                            filasResultadosSql.getInt("id_usuario"));

                    usuarioTemporalEncontrado.setNombreCompleto(
                            filasResultadosSql.getString("nombre_completo"));

                    usuarioTemporalEncontrado.setNombreUsuario(
                            filasResultadosSql.getString("nombre_usuario"));

                    usuarioTemporalEncontrado.setEstadoUsuario(
                            filasResultadosSql.getString("estado_usuario"));

                    // DATO DE LA TABLA ROLES
                    usuarioTemporalEncontrado.setNombreRol(
                            filasResultadosSql.getString("nombre_rol"));

                    // Agregamos el usuario completamente cargado a la lista.
                    listaDeUsuariosEncontrados.add(usuarioTemporalEncontrado);
                }
            }

        } catch (SQLException errorBaseDatos) {

            System.out.println(
                    "error al intentar obtener la lista de usuarios con rol: "
                    + errorBaseDatos.getMessage());

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

                    System.out.println(
                            "conexion de lectura de usuarios con rol cerrada correctamente");
                }

            } catch (SQLException errorAlCerrar) {

                System.out.println(
                        "error al cerrar los recursos de lectura de usuarios: "
                        + errorAlCerrar.getMessage());
            }
        }

        // Devuelvo la lista completa.
        return listaDeUsuariosEncontrados;
    }

       
    /*
    6. METODO ACTUALIZAR ESTADO USUARIO
    
        -llamado desde usauriocontrolador
        -su funcion es modificar el estado de un usuario de activo o desactivo
        
        
    */
    // metodo encargado de cambiar el estado de un usuario
// recibe el id del usuario y el nuevo estado que se desea guardar
// retorna true si la actualizacion fue exitosa
    public boolean actualizarEstadoUsuario(int identificadorUsuario,String nuevoEstadoUsuario) {

        Connection conexionFisicaBaseDatos = null;
        PreparedStatement sentenciaSqlPreparada = null;

        boolean operacionActualizacionExitosa = false;
        /*
        UPDATE usuario:modificar la tabla usuario.
        SET estado_usuario = ?: el primer ? es el nuevo estado que se quiere poner. Normalmente  valores como 'activo', 'desactivado'.
        WHERE id_usuario = ?: el segundo ? indica a qué usuario exacto le cambias el estado. Sin el WHERE se cambiaría a todos.
        */
        String consultaActualizarSql
                = "UPDATE usuario "
                + "SET estado_usuario = ? "
                + "WHERE id_usuario = ?";

        try {

            conexionFisicaBaseDatos = claseConexion.getConexion();

            if (conexionFisicaBaseDatos != null) {

                sentenciaSqlPreparada= conexionFisicaBaseDatos.prepareStatement(consultaActualizarSql);

                // reemplaza el primer ?
                sentenciaSqlPreparada.setString(1,nuevoEstadoUsuario);

                // reemplaza el segundo ?
                sentenciaSqlPreparada.setInt(2,identificadorUsuario);

                int cantidadFilasActualizadas= sentenciaSqlPreparada.executeUpdate();

                if (cantidadFilasActualizadas > 0) {operacionActualizacionExitosa = true;

                    System.out.println("estado del usuario actualizado correctamente");
                }
            }

        } catch (SQLException errorBaseDatos) {

            System.out.println("error al actualizar estado del usuario: "+ errorBaseDatos.getMessage());

        } finally {

            try {

                if (sentenciaSqlPreparada != null) {
                    sentenciaSqlPreparada.close();
                }

                if (conexionFisicaBaseDatos != null) {
                    conexionFisicaBaseDatos.close();

                    System.out.println("conexion de actualizacion de usuario cerrada");
                }

            } catch (SQLException errorAlCerrar) {

                System.out.println("error al cerrar actualizacion de usuario: "+ errorAlCerrar.getMessage());
            }
        }

        return operacionActualizacionExitosa;
    }

}
