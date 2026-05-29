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
     * Inserta en 'clientes', recupera el ID generado, e inserta el teléfono y correo.
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
     * VALIDA SI UN CLIENTE YA EXISTE POR CÉDULA
     * Busca en MySQL si el documento de identidad ya se encuentra registrado.
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
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
        }
    }
    
    
    
}