package com.dao;

import com.conexion.claseConexion;
import com.modelo.Factura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*   

## FACTURA DAO

        METODO 1. REGISTRAR FACTURA --> Guarda una nueva factura.

        METODO 2. OBTENER FACTURA POR ID --> Busca una factura usando su id_factura.

        METODO 3. OBTENER FACTURA POR PEDIDO --> Busca la factura asociada a un pedido.

        METODO 4. OBTENER ID FACTURA POR PEDIDO --> Obtener el id de la factura recién creada para registrar posteriormente el pago.

        METODO 5. ACTUALIZAR ESTADO FACTURA --> cambiar la factura de pendiente a pagada.

        METODO 6.  REGISTRAR FACTURA RETORNANDO ID --> Registrar una nueva factura en la base de datos y devolver el id_factura generado automáticamente

 */
public class FacturaDao {


    /*

METODO 1. REGISTRAR FACTURA


Guarda una nueva factura en la base de datos.
     */
    public boolean registrarFactura(Factura nuevaFacturaObjeto) {

        String consultaSql = """
        INSERT INTO facturas
        (
            id_pedido,
            subtotal,
            iva,
            total,
            estado_pago
        )
        VALUES (?, ?, ?, ?, ?)
        """;

        try (
                Connection conexion = claseConexion.getConexion(); PreparedStatement sentenciaSql = conexion.prepareStatement(consultaSql)) {

            sentenciaSql.setInt(1, nuevaFacturaObjeto.getIdPedido());

            sentenciaSql.setDouble(2, nuevaFacturaObjeto.getSubtotal());

            sentenciaSql.setDouble(3, nuevaFacturaObjeto.getIva());

            sentenciaSql.setDouble(4, nuevaFacturaObjeto.getTotal());

            sentenciaSql.setString(5, nuevaFacturaObjeto.getEstadoPago());

            return sentenciaSql.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println("Error registrando factura: " + e.getMessage());
        }

        return false;
    }

    /*

METODO 2. OBTENER FACTURA POR ID

     */
    public Factura obtenerFacturaPorId(
            int idFactura) {

        Factura facturaEncontrada = null;

        String consultaSql = """
        SELECT *
        FROM facturas
        WHERE id_factura = ?
        """;

        try (
                Connection conexion = claseConexion.getConexion(); PreparedStatement sentenciaSql = conexion.prepareStatement(consultaSql)) {

            sentenciaSql.setInt(1, idFactura);

            ResultSet resultadoConsulta = sentenciaSql.executeQuery();

            if (resultadoConsulta.next()) {

                facturaEncontrada = new Factura();

                facturaEncontrada.setIdFactura(resultadoConsulta.getInt("id_factura"));

                facturaEncontrada.setIdPedido(resultadoConsulta.getInt("id_pedido"));

                facturaEncontrada.setFechaHoraFactura(resultadoConsulta.getTimestamp("fechaHora_factura"));

                facturaEncontrada.setSubtotal(resultadoConsulta.getDouble("subtotal"));

                facturaEncontrada.setIva(resultadoConsulta.getDouble("iva"));

                facturaEncontrada.setTotal(resultadoConsulta.getDouble("total"));

                facturaEncontrada.setEstadoPago(resultadoConsulta.getString("estado_pago"));
            }

        } catch (Exception e) {

            System.out.println("Error obteniendo factura: " + e.getMessage());
        }

        return facturaEncontrada;
    }

    /*

        METODO 3. OBTENER FACTURA POR PEDIDO


            Busca la factura asociada a un pedido.
     */
    public Factura obtenerFacturaPorPedido(
            int idPedido) {

        Factura facturaEncontrada = null;

        String consultaSql = """
        SELECT *
        FROM facturas
        WHERE id_pedido = ?
        LIMIT 1
        """;

        try (
                Connection conexion = claseConexion.getConexion(); PreparedStatement sentenciaSql = conexion.prepareStatement(consultaSql)) {

            sentenciaSql.setInt(1, idPedido);

            ResultSet resultadoConsulta = sentenciaSql.executeQuery();

            if (resultadoConsulta.next()) {

                facturaEncontrada = new Factura();

                facturaEncontrada.setIdFactura(resultadoConsulta.getInt("id_factura"));

                facturaEncontrada.setIdPedido(resultadoConsulta.getInt("id_pedido"));

                facturaEncontrada.setFechaHoraFactura(resultadoConsulta.getTimestamp("fechaHora_factura"));

                facturaEncontrada.setSubtotal(resultadoConsulta.getDouble("subtotal"));

                facturaEncontrada.setIva(resultadoConsulta.getDouble("iva"));

                facturaEncontrada.setTotal(resultadoConsulta.getDouble("total"));

                facturaEncontrada.setEstadoPago(resultadoConsulta.getString("estado_pago"));
            }

        } catch (Exception e) {

            System.out.println("Error obteniendo factura por pedido: " + e.getMessage());
        }

        return facturaEncontrada;
    }

    /*
        
        METODO 4. OBTENER ID FACTURA POR PEDIDO
       
        OBJETIVO: Obtener el id de la factura recién creada para registrar posteriormente el pago.
     */
    public int obtenerIdFacturaPorPedido(int idPedido) {

        /*
        
            select que seleccionar el idfactura de la tabala facturas, con el where filtramos la busqueda de idpedido, que le pertenezcan unicamente
            al numero de pedido que le pasa el signo ?
            ordenar idfactura de forma descecendente, de las mas nueva a la mas antigua
            limit1 sequeda con el resultado mas alto, como se encuentra desordednada
        
        esta consulta nos sirve pra cneontrar la factura mas reciente de un pedido
         
         */
        String sql = """
        SELECT id_factura
        FROM facturas
        WHERE id_pedido = ?
        ORDER BY id_factura DESC
        LIMIT 1
        """;

        try (
                Connection con = claseConexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPedido);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return rs.getInt("id_factura");
            }

        } catch (Exception e) {

            System.out.println("Error obteniendo id factura: " + e.getMessage());
        }

        return 0;
    }

    /*
   
    METODO 5. ACTUALIZAR ESTADO FACTURA
    
    OBJETIVO: Cambiar la factura de pendiente a pagada.
    
     */
    public boolean actualizarEstadoFactura(int idFactura, String nuevoEstado) {

        /*
            update es para modificar actualizar  la informacion dentro de facturas 
            set estadopago, defiene el cambio a realizar "pendiente, pagada"
            where idfactura, aplica la condicion con el ? recibe el id de la factura exacta que se desea modificar
        
        
         */
        String sql = """
        UPDATE facturas
        SET estado_pago = ?
        WHERE id_factura = ?
        """;

        try (
                Connection con = claseConexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);

            ps.setInt(2, idFactura);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println("Error actualizando factura: " + e.getMessage());
        }

        return false;
    }

    public int registrarFacturaRetornandoId( Factura nuevaFacturaObjeto) {

        /*
    
                INSERT INTO-> indica que vamos a guardar una nueva factura

                id_pedido -> pedido asociado a la factura

                subtotal -> valor antes del IVA

                iva-> impuesto calculado

                total -> subtotal + iva

                estado_pago -> normalmente inicia como pendiente
    
         */
        String consultaSql = """
        INSERT INTO facturas
        (
            id_pedido,
            subtotal,
            iva,
            total,
            estado_pago
        )
        VALUES (?, ?, ?, ?, ?)
        """;

        try (
                Connection conexion = claseConexion.getConexion(); PreparedStatement sentenciaSql= conexion.prepareStatement(consultaSql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            /*
        
                      ASIGNAMOS LOS VALORES QUE VIENEN DEL OBJETO FACTURA
        
             */
            sentenciaSql.setInt( 1, nuevaFacturaObjeto.getIdPedido());

            sentenciaSql.setDouble( 2, nuevaFacturaObjeto.getSubtotal());

            sentenciaSql.setDouble( 3, nuevaFacturaObjeto.getIva());

            sentenciaSql.setDouble( 4, nuevaFacturaObjeto.getTotal());

            sentenciaSql.setString( 5, nuevaFacturaObjeto.getEstadoPago());

            /*
        
                EJECUTAMOS EL INSERT
        
             */
            int cantidadFilasInsertadas = sentenciaSql.executeUpdate();

            /*
        
                 SI SE INSERTÓ CORRECTAMENTE
        
             */
            if (cantidadFilasInsertadas > 0) {

                /*
            
                    OBTENEMOS EL ID GENERADO POR AUTO_INCREMENT
            
                 */
                ResultSet resultadoIdGenerado = sentenciaSql.getGeneratedKeys();

                if (resultadoIdGenerado.next()) {

                    /*
                
                RETORNAMOS EL ID_FACTURA
                
                     */
                    return resultadoIdGenerado.getInt(1);
                }
            }

        } catch (Exception error) {

            System.out.println( "Error registrando factura: "+ error.getMessage());
        }

        /*
    
             SI FALLA RETORNAMOS 0
    
         */
        return 0;
    }

}
