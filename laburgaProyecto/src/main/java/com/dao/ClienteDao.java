
/*

responsabilidad: administrar el registro y validacion de clientes


    - 1. METODO REGISTRAR CLIENTE COMPLETO --> registra cliente telefono  y correo 
 
    - 2. METODO EXISTE CLIENTE  --> verfica si un cc ya existe

    - 3. METODO OBTENER ID CLIENTE POR TELEFONO --> obtener clinete por el numero del telefono 


 */
package com.dao;

import com.conexion.claseConexion;
import com.modelo.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ClienteDao {

    /**
     *
     * 1. METODO REGISTRAR CLIENTE COMPLETO
     *
     *
     *
     * Inserta en 'clientes', recupera el ID generado, e inserta el teléfono y
     * correo.
     */
    public boolean registrarClienteCompleto(Cliente cliente) {
        // Establecemos la conexión oficial con el servidor MySQL de Labur-GA
        Connection accesoBD = claseConexion.getConexion();

        // Declaramos los objetos de operación para evitar fugas de memoria (Memory Leaks)
        PreparedStatement psCliente = null;
        PreparedStatement psTelefono = null;
        PreparedStatement psCorreo = null;

        // Objeto para capturar temporalmente los IDs autogenerados por el motor de BD
        ResultSet rsClave = null;

        // Sentencias SQL limpias y parametrizadas para mitigar ataques de Inyección SQL
        String sqlCliente = "INSERT INTO clientes (nombrecompleto_cliente, documentoidentidad_cliente) VALUES (?, ?)";
        String sqlTelefono = "INSERT INTO clienteTelefono (id_cliente, cliente_telefono) VALUES (?, ?)";
        String sqlCorreo = "INSERT INTO clienteCorreos (id_cliente, cliente_correo) VALUES (?, ?)";

        try {
            // == PASO 1: REGISTRO DE DATOS BÁSICOS DEL CLIENTE ==

            // Preparamos la consulta para la tabla principal. 
            // Usamos 'Statement.RETURN_GENERATED_KEYS' porque le exigimos a MySQL que guarde el ID autogenerado para usarlo abajo.
            psCliente = accesoBD.prepareStatement(sqlCliente, Statement.RETURN_GENERATED_KEYS);

            // Reemplazamos el primer '?' con el nombre completo del cliente que viene desde el formulario
            psCliente.setString(1, cliente.getNombreCompleto());

            // Reemplazamos el segundo '?' con el documento de identidad (cédula) del cliente
            psCliente.setString(2, cliente.getDocumentoIdentidad());

            // Enviamos la orden a MySQL para insertar el cliente. Devuelve cuántas filas se crearon.
            int filasCliente = psCliente.executeUpdate();

            // Si la cantidad de filas creadas es mayor a 0, significa que el cliente se guardó con éxito
            if (filasCliente > 0) {

                // Recogemos la llave (ID) que MySQL le asignó automáticamente a ese cliente en la base de datos
                rsClave = psCliente.getGeneratedKeys();

                // Nos posicionamos en el primer registro encontrado en esa respuesta
                if (rsClave.next()) {

                    // Extraemos el número de ID recuperado (la columna 1) y lo guardamos en esta variable
                    int idClienteGenerado = rsClave.getInt(1);

                    // == PASO 2: ASOCIAR EL TELÉFONO EN LA TABLA HIJA ==
                    // Preparamos la instrucción para insertar en la tabla relacional 'clienteTelefono'
                    psTelefono = accesoBD.prepareStatement(sqlTelefono);

                    // Reemplazamos el primer '?' con el ID que acabamos de recuperar de la tabla principal
                    psTelefono.setInt(1, idClienteGenerado);

                    // Reemplazamos el segundo '?' con el número de teléfono que el usuario digitó en la pantalla
                    psTelefono.setString(2, cliente.getTelefono());

                    // Mandamos la orden a MySQL para que guarde el teléfono amarrado a ese ID
                    psTelefono.executeUpdate();

                    // == PASO 3: ASOCIAR EL CORREO EN LA TABLA HIJA ==
                    // Preparamos la instrucción para insertar en la tabla relacional 'clienteCorreos'
                    psCorreo = accesoBD.prepareStatement(sqlCorreo);

                    // Reemplazamos el primer '?' con el mismo ID recuperado del cliente
                    psCorreo.setInt(1, idClienteGenerado);

                    // Reemplazamos el segundo '?' con el correo electrónico del formulario
                    psCorreo.setString(2, cliente.getCorreo());

                    // Mandamos la orden a MySQL para que guarde el correo amarrado a ese ID
                    psCorreo.executeUpdate();

                    // Si la ejecución llegó limpia hasta este punto sin romperse, confirmamos el éxito total devolviendo true
                    return true;
                }
            }
        } catch (Exception error) {
            System.out.println("Error en ClienteDao transaccional: " + error.getMessage());
        }
        return false;
    }

    /**
     *
     *
     *
     * 2. METODO EXISTE CLIENTE
     *
     *
     *
     * VALIDA SI UN CLIENTE YA EXISTE POR CÉDULA Busca en MySQL si el documento
     * de identidad ya se encuentra registrado. etse metodo se utiliza en en
     * clientecontrolador, para saber si la cc ya existe no deja registar
     */
    public boolean existeCliente(String cedula) {
        Connection accesoBD = claseConexion.getConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        //le oredna qe busque en la tabla clientes cualquier registro de la clumna documentoidentidad_cliente
        //el where es como es filtro que va traer solamente ese numero de cc, sin where treria todos los cc
        String sql = "SELECT * FROM clientes WHERE documentoidentidad_cliente = ?";

        try {
            ps = accesoBD.prepareStatement(sql);
            ps.setString(1, cedula);
            rs = ps.executeQuery();

            // Si el ResultSet tiene un registro, significa que la cédula YA EXISTE
            return rs.next();

        } catch (Exception e) {
            System.out.println("Error al validar existencia del cliente: " + e.getMessage());
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /*
    
    
       METODO 3 OBTENER ID CLIENTE POR TELEFONO 
    
        buscar al cliente por el id de telefono que resgitsra para luego utilizarlos al momento de realizar la rerserva de una mesa
     */
    public int obtenerIdClientePorTelefono(String telefono) {

        // Se abre la conexión física con la base de datos MySQL
        Connection conexionBaseDatos = claseConexion.getConexion();

        // Objeto que ejecuta la consulta SQL preparada (evita inyección SQL)
        PreparedStatement consultaPreparada = null;

        // Objeto que almacena el resultado devuelto por MySQL
        ResultSet resultadoConsulta = null;

        /*
        SENTENCIA SQL:

        SELECT id_cliente
        FROM clientetelefono
        WHERE cliente_telefono = ?

        EXPLICACIÓN:

        - SELECT id_cliente
            → Solo queremos traer el ID del cliente, no toda la fila

        - FROM clientetelefono
            → Buscamos en la tabla donde se guardan los teléfonos

        - WHERE cliente_telefono = ?
            → Filtramos por el número de teléfono recibido desde el formulario
            → El "?" será reemplazado de forma segura con setString()
         */
        String consultaSQL = """
            SELECT id_cliente
            FROM clientetelefono
            WHERE cliente_telefono = ?
        """;

        try {

            // Se prepara la consulta SQL para ejecutarla en MySQL
            consultaPreparada = conexionBaseDatos.prepareStatement(consultaSQL);

            // Se reemplaza el "?" con el número de teléfono ingresado por el usuario
            consultaPreparada.setString(1, telefono);

            // Se ejecuta la consulta en la base de datos
            resultadoConsulta = consultaPreparada.executeQuery();

            /*
            Verificamos si la consulta devolvió resultados:

            - Si existe una fila → el cliente existe
            - Si no existe → no hay cliente con ese teléfono
             */
            if (resultadoConsulta.next()) {

                // Se obtiene el id_cliente encontrado en la base de datos
                int idClienteEncontrado = resultadoConsulta.getInt("id_cliente");

                // Se retorna el ID del cliente
                return idClienteEncontrado;
            }

        } catch (Exception error) {

            // Mensaje en consola si ocurre un error en la consulta
            System.out.println("Error buscando cliente por teléfono: " + error.getMessage());

        } finally {

            /*
            CIERRE DE RECURSOS

            Se cierran en orden inverso a su creación para evitar fugas de memoria
             */
            try {
                if (resultadoConsulta != null) {
                    resultadoConsulta.close();
                }
            } catch (Exception errorCerrar) {
            }

            try {
                if (consultaPreparada != null) {
                    consultaPreparada.close();
                }
            } catch (Exception errorCerrar) {
            }

            try {
                if (conexionBaseDatos != null) {
                    conexionBaseDatos.close();
                }
            } catch (Exception errorCerrar) {
            }
        }

        // Si no se encontró el cliente, se retorna -1 como indicador de "no existe"
        return -1;
    }

    public int registrarClienteYRetornarId(String nombre, String telefono) {

        Connection conexionBaseDatos = claseConexion.getConexion();

        PreparedStatement sentenciaCliente = null;
        PreparedStatement sentenciaTelefono = null;

        ResultSet clavesGeneradas = null;

        String sqlCliente = """
                INSERT INTO clientes (nombrecompleto_cliente)
                VALUES (?)
            """;

        String sqlTelefono = """
                 INSERT INTO clientetelefono (id_cliente, cliente_telefono)
                VALUES (?, ?)
            """;

        try {

            // 1. Insertar cliente (solo nombre)
            sentenciaCliente = conexionBaseDatos.prepareStatement(sqlCliente, Statement.RETURN_GENERATED_KEYS);

            sentenciaCliente.setString(1, nombre);

            sentenciaCliente.executeUpdate();

            clavesGeneradas = sentenciaCliente.getGeneratedKeys();

            if (clavesGeneradas.next()) {

                int idCliente = clavesGeneradas.getInt(1);

                // 2. Insertar teléfono relacionado
                sentenciaTelefono = conexionBaseDatos.prepareStatement(sqlTelefono);

                sentenciaTelefono.setInt(1, idCliente);
                sentenciaTelefono.setString(2, telefono);

                sentenciaTelefono.executeUpdate();

                return idCliente;
            }

        } catch (Exception e) {
            System.out.println("Error creando cliente: " + e.getMessage());
        }

        return -1;
    }

}
