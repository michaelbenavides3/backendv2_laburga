
/*

responsabilidad: administrar el registro y validacion de clientes


    - 1. METODO REGISTRAR CLIENTE COMPLETO --> registra cliente telefono  y correo 
 
    - 2. METODO EXISTE CLIENTE  --> verfica si un cc ya existe

    - 3. METODO OBTENER ID CLIENTE POR TELEFONO --> obtener clinete por el numero del telefono 

    - 4. METODO REGISTRAR CLIENTE Y RETORNAAR ID --> este metodo se utiliza para cuando un clinente no esta registrado en la base de datos, se crea automatico y me deja registrar en reservas

    - 5. METODO 5 BUSCAR CLIENTE POR TELÉFONO --> cuando un cliente existe pueda traer los datos del cliente solo por el telefono y se autocomplete el formulario


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

    /*
        
        METODO 4 REGISTAR CLIENTE Y RETORNAR ID
    
     */
    public int registrarClienteYRetornarId(String nombre,String documentoIdentidad, String telefono) {

        /*
        CONEXIÓN A BASE DE DATOS

        - Abre la conexión con MySQL
        - Permite ejecutar sentencias SQL
         */
        Connection conexionBaseDatos = claseConexion.getConexion();

        /*
        PREPARED STATEMENT CLIENTE

        - Ejecuta el INSERT en la tabla clientes
        - Se usa PreparedStatement para evitar inyección SQL
         */
        PreparedStatement sentenciaCliente = null;

        /*
        PREPARED STATEMENT TELÉFONO

        - Inserta el teléfono en la tabla relacional clientetelefono
        - Relaciona cliente ↔ teléfono
         */
        PreparedStatement sentenciaTelefono = null;

        /*
        RESULTSET CLAVES GENERADAS

        - Guarda el ID que MySQL genera automáticamente
        - Ejemplo: si el cliente es ID 5, aquí se obtiene ese valor
         */
        ResultSet clavesGeneradas = null;

        /*
        SQL 1: INSERTAR CLIENTE

        Solo guardamos el nombre porque:
        - El teléfono va en otra tabla (normalización)
         */
        String sqlCliente = """
                INSERT INTO clientes (nombrecompleto_cliente, documentoidentidad_cliente)
                VALUES (?, ?)
            """;

        /*
        SQL 2: INSERTAR TELÉFONO

        - Relaciona el cliente con su número
        - Usa el id_cliente generado en el paso anterior
         */
        String sqlTelefono = """
                INSERT INTO clientetelefono (id_cliente, cliente_telefono)
                VALUES (?, ?)
            """;

        try {

            /*
            PASO 1: INSERTAR CLIENTE
             */
            sentenciaCliente = conexionBaseDatos.prepareStatement(sqlCliente, Statement.RETURN_GENERATED_KEYS);

            /*
            Reemplaza el "?" con el nombre del cliente
             */
            sentenciaCliente.setString(1, nombre);
            sentenciaCliente.setString(2, documentoIdentidad);

            /*
            Ejecuta el INSERT en la tabla clientes
             */
            sentenciaCliente.executeUpdate();

            /*
            PASO 2: OBTENER EL ID GENERADO

            - MySQL genera automáticamente el id_cliente (AUTO_INCREMENT)
            - Ejemplo: 1, 2, 3, 4...
             */
            clavesGeneradas = sentenciaCliente.getGeneratedKeys();

            /*
            Verifica si se generó un ID correctamente
             */
            if (clavesGeneradas.next()) {

                /*
                Guardamos el ID generado por la base de datos
                 */
                int idCliente = clavesGeneradas.getInt(1);

                /*
                PASO 3: INSERTAR TELÉFONO RELACIONADO
                 */
                sentenciaTelefono = conexionBaseDatos.prepareStatement(sqlTelefono);

                /*
                Reemplaza el primer "?" → id del cliente
                 */
                sentenciaTelefono.setInt(1, idCliente);

                /*
                Reemplaza el segundo "?" → número de teléfono
                 */
                sentenciaTelefono.setString(2, telefono);

                /*
                Ejecuta el INSERT en clientetelefono
                 */
                sentenciaTelefono.executeUpdate();

                /*
                RETURN EXITOSO

                - Devuelve el ID del cliente creado
                - Esto permite usarlo en la reserva inmediatamente
                 */
                return idCliente;
            }

        } catch (Exception e) {

            /*
            ERROR GENERAL

            Si algo falla:
            - conexión
            - SQL
            - inserción
             */
            System.out.println("Error creando cliente: " + e.getMessage());
        }

        /*
        RETURN -1 = ERROR o FALLA no se pudo crear ni obtener el cliente”


        Porque el método debe devolver un int sí o sí, pero cuando falla NO hay ID válido.

        Entonces:
        - ID real: 1, 2, 3, 4...
        - Error: -1
         */
        return -1;
    }

    /*
    
    METODO 5: BUSCAR CLIENTE POR TELÉFONO
   

    OBJETIVO:
        Buscar un cliente en la base de datos utilizando su número de teléfono como criterio de búsqueda.

        Si el cliente existe, devuelve un objeto Cliente con toda su información para autocompletar el formulario de reserva automáticamente.

        Si el cliente NO existe, devuelve null para que el mesero pueda ingresar el nombre manualmente.

    FLUJO:
    
        Mesero ingresa teléfono en el formulario
                ↓
        BuscarClientePorTelefonoControlador recibe el teléfono
                ↓
        Llama a este método con el teléfono recibido
                ↓
        Se ejecuta SELECT con JOIN en MySQL
                ↓
        Si existe → devuelve objeto Cliente con nombre y teléfono Si no existe → devuelve null

    POR QUÉ SE HACE UN JOIN:
    
        El nombre del cliente está en la tabla "clientes"
        El teléfono del cliente está en la tabla "clientetelefono"
        Son dos tablas diferentes relacionadas por id_cliente
        Sin JOIN tendríamos que hacer dos consultas separadas
        Con JOIN traemos todo en una sola consulta

    EJEMPLO DE CONSULTA SQL QUE SE EJECUTA:
        SELECT clientes.id_cliente,
               clientes.nombrecompleto_cliente,
               clientetelefono.cliente_telefono
        FROM clientes
        INNER JOIN clientetelefono
        ON clientes.id_cliente = clientetelefono.id_cliente
        WHERE clientetelefono.cliente_telefono = '3001234567'

    PARÁMETRO: telefonoBuscado → número de teléfono ingresado por el mesero
                          ejemplo: "3001234567"

    RETORNO: Cliente  → si se encontró el cliente con ese teléfono  null     → si no existe ningún cliente con ese teléfono
     */
    
    
    public Cliente buscarClientePorTelefono(String telefonoBuscado) {

        /*
    VARIABLE DE RESULTADO

    Iniciamos en null porque todavía no sabemos si el cliente existe en la base de datos.

    Si lo encontramos, esta variable dejará de ser null y tendrá todos los datos del cliente.

    Si no lo encontramos, se devuelve null al final para que el formulario lo maneje correctamente.
         */
        Cliente clienteEncontrado = null;

        /*
    CONSULTA SQL CON JOIN

    Necesitamos unir dos tablas porque la información del cliente está dividida por normalización:

    TABLA clientes:
        - id_cliente          → identificador único
        - nombrecompleto_cliente → nombre que queremos mostrar

    TABLA clientetelefono:
        - id_cliente          → llave foránea que conecta con clientes
        - cliente_telefono    → número de teléfono del cliente

    INNER JOIN:
        Une ambas tablas cuando id_cliente coincide en ambas. Solo devuelve registros que existan en LAS DOS tablas.

    WHERE clientetelefono.cliente_telefono = ?:
        Filtra únicamente el cliente que tenga ese número.
        El "?" será reemplazado de forma segura con setString() para evitar ataques de inyección SQL.
         */
        String consultaBuscarClientePorTelefono
                = "SELECT clientes.id_clientes, "
                + "clientes.nombrecompleto_cliente, "
                + "clientes.documentoidentidad_cliente,"
                + "clientetelefono.cliente_telefono "
                + "FROM clientes "
                + "INNER JOIN clientetelefono "
                + "ON clientes.id_clientes = clientetelefono.id_cliente "
                + "WHERE clientetelefono.cliente_telefono = ?";

        /*
    TRY-WITH-RESOURCES

    Abre la conexión y el PreparedStatement automáticamente.
    Los cierra solos al terminar, sin necesidad de finally.
    Esto evita fugas de memoria y conexiones abiertas.
         */
        try (
                Connection conexionBaseDatos = claseConexion.getConexion(); PreparedStatement sentenciaPreparada = conexionBaseDatos
                .prepareStatement(consultaBuscarClientePorTelefono)) {

            /*
        REEMPLAZAR EL PARÁMETRO

        setString(1, telefonoBuscado):
            - El "1" indica que reemplazamos el primer "?"
            - telefonoBuscado es el número que ingresó el mesero
            - Ejemplo: "3001234567"

        Esto convierte la consulta en: WHERE clientetelefono.cliente_telefono = '3001234567'
             */
            sentenciaPreparada.setString(1, telefonoBuscado);

            /*
        EJECUTAR LA CONSULTA

        executeQuery() envía el SELECT a MySQL y devuelve un ResultSet con los resultados.

        ResultSet es como una tabla temporal en memoria que contiene las filas que MySQL encontró.
             */
            ResultSet resultadoConsulta = sentenciaPreparada.executeQuery();

            /*
        VERIFICAR SI SE ENCONTRÓ UN RESULTADO

        resultadoConsulta.next():
            - Mueve el cursor a la primera fila del resultado
            - Devuelve true si hay datos
            - Devuelve false si no encontró ningún cliente

        Solo esperamos UNA fila porque el teléfono debe ser único por cliente en la base de datos.
             */
            if (resultadoConsulta.next()) {

                /*
            CLIENTE ENCONTRADO

            Creamos un objeto Cliente vacío para llenarlo con los datos de la base de datos.
                 */
                clienteEncontrado = new Cliente();

                /*
            ASIGNAR ID DEL CLIENTE

            getInt("id_cliente"): Lee el valor de la columna "id_cliente"  de la fila actual del ResultSet.

            Ejemplo: si el cliente tiene id 7, aquí guardamos 7.
                 */
                clienteEncontrado.setIdCliente(
                        resultadoConsulta.getInt("id_clientes"));

                /*
            ASIGNAR NOMBRE DEL CLIENTE

            getString("nombrecompleto_cliente"): Lee el nombre completo guardado en la tabla clientes.

            Ejemplo: "María García"  Este valor se mostrará automáticamente en el formulario.
                 */
                clienteEncontrado.setNombreCompleto(
                        resultadoConsulta.getString("nombrecompleto_cliente"));
                
                
                /*
                
                CAPTURAR DOCUMENTO DE IDENTIDAD
                
                */
                   /*
                mediante una varibale de tipo texto almacenamos la cc o documento de identidad
                */
                String documento = resultadoConsulta.getString("documentoidentidad_cliente");
                clienteEncontrado.setDocumentoIdentidad(documento != null ? documento : "");
                
                

                /*
                
                
            ASIGNAR TELÉFONO DEL CLIENTE

            getString("cliente_telefono"):  Lee el teléfono guardado en la tabla clientetelefono.

            Ejemplo: "3001234567"
                 */
                clienteEncontrado.setTelefono(
                        resultadoConsulta.getString("cliente_telefono"));

                // confirmamos en consola que el cliente fue encontrado
                System.out.println("DEBUG: Cliente encontrado → "
                        + clienteEncontrado.getNombreCompleto()
                        + " | doc;  " + clienteEncontrado.getDocumentoIdentidad()
                        + " | Teléfono: " + clienteEncontrado.getTelefono());

            } else {

                /*
            CLIENTE NO ENCONTRADO

            Si no hay resultados significa que ningún cliente tiene ese número de teléfono registrado.

            En este caso clienteEncontrado sigue siendo null  y el formulario lo manejará mostrando un mensaje para que el mesero ingrese el nombre manualmente.
                 */
                System.out.println("DEBUG: No existe cliente con teléfono → "
                        + telefonoBuscado);
            }

        } catch (Exception errorConsulta) {

            /*
        MANEJO DE ERRORES

        Si ocurre cualquier error durante la consulta:
            - Problema de conexión con MySQL
            - Error en la sintaxis SQL
            - Columna no encontrada en el ResultSet

        Se imprime el mensaje en consola para diagnóstico. clienteEncontrado sigue siendo null y se devuelve así.
             */
            System.out.println("Error buscando cliente por teléfono: "
                    + errorConsulta.getMessage());
        }

        /*
    RETORNO FINAL

    Si el cliente fue encontrado → devuelve el objeto Cliente lleno
    Si no fue encontrado         → devuelve null

    El controlador y el JSP manejan ambos casos:
        Cliente != null → autocompletamos el nombre en el formulario
        Cliente == null → mostramos mensaje y el mesero escribe el nombre
         */
        return clienteEncontrado;
    }

}
