/*
    MÉTODO 1 OBTENER VENTAS DEL DÍA

    MÉTODO 2  OBTENER VENTAS POR FECHAS

    MÉTODO 3 OBTENER VENTAS POR MES

    METODO 4 OBTENER PRODUCTOS MÁS VENDIDOS  --> ya no existe

    METODO 5 OBTENER VENTAS POR CATEGORÍA

    METODO 6  OBTENER TOTAL FACTURADO 

    METODO 7 — TOP 5 PRODUCTOS MÁS VENDIDOS

    METODO 8 — BUSCAR PRODUCTO POR NOMBRE

    METODO 9 — BUSCAR PRODUCTOS POR CATEGORÍA

 */
package com.dao;

import com.conexion.claseConexion;
import com.modelo.ProductoMasVendido;
import com.modelo.ResumenVentas;
import com.modelo.VentasCategoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class VentasDao {

    /*
    MÉTODO 1 OBTENER VENTAS DEL DÍA

    RESPONSABILIDAD; Consultar todas las facturas pagadas del día actual y devolver un objeto ResumenVentas con:

    - Número de facturas.
    - Subtotal vendido.
    - IVA recaudado.
    - Total facturado.
     */
    public ResumenVentas obtenerVentasDelDia() {

        /*
        se prepara la conexion a la base de datos
         */
        Connection conexionBD = claseConexion.getConexion();

        PreparedStatement consultaPreparada = null;

        /*
        se ejecuta la consulta
         */
        ResultSet resultadoConsulta = null;

        /*
        se instacia la clase, para guarada el resultado de la consulta al final en el return
         */
        ResumenVentas resumen = new ResumenVentas();

        /*
        COUNT(*) AS --> contar todas, as, darle en nomvbre o asignar el resultado a numerofacturas
        cuanta todas las factura y almacena es numeroFacturas --> columna invisble
        from de la tabla factura
        where es el filtro, por fecha, curdate fecha de hoy
        and, es la condicion del where, y tiene que cumlir amabas condiciones, estadopago sea pagada
        COALESCE significa que en vez de regresar null no regrese 0, si en ese dia que se reaiza la consulta no se abrio o no se hizo niguna venta
         */
        String consultaSQL = """
        SELECT
            COUNT(*) AS numeroFacturas,
            COALESCE(SUM(subtotal),0) AS subtotal,
            COALESCE (SUM(iva),0) AS iva,
            COALESCE (SUM(total),0) AS total
        FROM facturas
        WHERE DATE(fechaHora_factura)=CURDATE()
        AND estado_pago='pagada'
         """;
        try {

            // Prepara la consulta SQL
            consultaPreparada = conexionBD.prepareStatement(consultaSQL);

            // Ejecuta la consulta
            resultadoConsulta = consultaPreparada.executeQuery();

            /*
            executeQuery() devuelve un ResultSet con el resultado del SELECT.
             */
            if (resultadoConsulta.next()) {

                /*
            Si existe una fila, obtenemos cada columna calculada  y la guardamos en el objeto ResumenVentas.
                 */
                resumen.setNumeroFacturas(resultadoConsulta.getInt("numeroFacturas"));

                resumen.setSubtotal(resultadoConsulta.getDouble("subtotal"));

                resumen.setIva(resultadoConsulta.getDouble("iva"));

                resumen.setTotal(resultadoConsulta.getDouble("total"));
            }

        } catch (Exception error) {

            System.out.println("Error obteniendo ventas del día: "
                    + error.getMessage());

        } finally {

            try {
                if (resultadoConsulta != null) {
                    resultadoConsulta.close();
                }
            } catch (Exception e) {
            }

            try {
                if (consultaPreparada != null) {
                    consultaPreparada.close();
                }
            } catch (Exception e) {
            }

            try {
                if (conexionBD != null) {
                    conexionBD.close();
                }
            } catch (Exception e) {
            }

        }

        return resumen;
    }

    /*

MÉTODO 2  OBTENER VENTAS POR FECHAS

RESPONSABILIDAD

Consultar todas las facturas pagadas comprendidas entre dos fechas seleccionadas por el administrador y devolver un resumen de ventas.

El resumen incluye:

- Número de facturas.
- Subtotal vendido.
- IVA recaudado.
- Total facturado.

PARÁMETROS

fechaInicio
→ Fecha inicial del reporte.

fechaFin
→ Fecha final del reporte.

VALOR RETORNADO

ResumenVentas

→ Devuelve un objeto con toda la información consolidada del rango de fechas solicitado.
     */
    public ResumenVentas obtenerVentasPorFechas(String fechaInicio,
            String fechaFin) {

        /*
  
    PASO 1  PREPARAR LA CONEXIÓN CON MYSQL
   

    Se abre la conexión con la base de datos para poder ejecutar la consulta.
         */
        Connection conexionBD = claseConexion.getConexion();

        /*
    Objeto encargado de preparar la sentencia SQL.

    Inicialmente queda en null porque todavía no existe ninguna consulta.
         */
        PreparedStatement consultaPreparada = null;

        /*
    Objeto donde MySQL devolverá el resultado del SELECT.

    También inicia en null porque aún no se ha ejecutado la consulta.
         */
        ResultSet resultadoConsulta = null;

        /*
    Se crea el objeto que almacenará el resumen de ventas.

    Al finalizar el método este objeto será devuelto al controlador.
         */
        ResumenVentas resumen = new ResumenVentas();

        /*
  
    PASO 2  CREAR LA CONSULTA SQL
  

    COUNT(*)

    Cuenta cuántas facturas pagadas existen.

    SUM()

    Suma los valores de todas las facturas encontradas.

    COALESCE()

    Si no existen ventas devuelve cero en lugar de NULL.

    BETWEEN ? AND ?

    Filtra únicamente las facturas comprendidas entre las dos fechas seleccionadas por el administrador.

    estado_pago='pagada'

    Garantiza que solamente se tengan en cuenta las facturas realmente cobradas.
         */
        String consultaSQL = """
        SELECT
            COUNT(*) AS numeroFacturas,
            COALESCE(SUM(subtotal),0) AS subtotal,
            COALESCE(SUM(iva),0) AS iva,
            COALESCE(SUM(total),0) AS total
        FROM facturas
        WHERE DATE(fechaHora_factura)
        BETWEEN ? AND ?
        AND estado_pago='pagada'
        """;

        try {

            /*
        
        PASO 3 PREPARAR LA CONSULTA
       

        MySQL recibe la sentencia SQL pero todavía no la ejecuta.
             */
            consultaPreparada = conexionBD.prepareStatement(consultaSQL);

            /*
        
        PASO 4 INYECTAR LOS PARÁMETROS
       

        El primer signo (?) será reemplazado por la fecha inicial.

        Ejemplo

        BETWEEN '2026-07-01' AND ?
             */
            consultaPreparada.setString(1, fechaInicio);

            /*
        El segundo signo (?) será reemplazado por la fecha final.

        Resultado final:

        BETWEEN '2026-07-01' AND '2026-07-15'
             */
            consultaPreparada.setString(2, fechaFin);

            /*
       
        PASO 5 EJECUTAR EL SELECT
        

        executeQuery()

        Envía la consulta a MySQL.

        MySQL devuelve una tabla temporal llamada ResultSet.

        Esa tabla contiene una única fila con el resumen.
             */
            resultadoConsulta = consultaPreparada.executeQuery();

            /*
       
        PASO 6 LEER EL RESULTADO
      

        next()

        Mueve el cursor hacia la primera fila.

        Como la consulta utiliza COUNT() y SUM(),  siempre existirá una única fila.
             */
            if (resultadoConsulta.next()) {

                /*
            Cada columna obtenida desde MySQL se copia al objeto ResumenVentas.
                 */
                resumen.setNumeroFacturas(resultadoConsulta.getInt("numeroFacturas"));

                resumen.setSubtotal(resultadoConsulta.getDouble("subtotal"));

                resumen.setIva(resultadoConsulta.getDouble("iva"));

                resumen.setTotal(resultadoConsulta.getDouble("total"));
            }

        } catch (Exception error) {

            /*
        Si ocurre cualquier error durante la consulta, se muestra el mensaje en consola.
             */
            System.out.println(
                    "Error obteniendo ventas por fechas: "
                    + error.getMessage());

        } finally {

            /*
        
        PASO 7 CERRAR LOS RECURSOS
        

        Siempre deben cerrarse en orden inverso a su creación para evitar fugas de memoria y conexiones abiertas.
             */
            try {

                if (resultadoConsulta != null) {

                    resultadoConsulta.close();
                }

            } catch (Exception e) {
            }

            try {

                if (consultaPreparada != null) {

                    consultaPreparada.close();
                }

            } catch (Exception e) {
            }

            try {

                if (conexionBD != null) {

                    conexionBD.close();
                }

            } catch (Exception e) {
            }

        }

        /*
   
    PASO 8  RETORNAR EL RESULTADO
    

    Se devuelve el objeto ResumenVentas completamente lleno para que el controlador lo envíe posteriormente al JSP.
         */
        return resumen;
    }

    /*

        MÉTODO 3 OBTENER VENTAS POR MES


RESPONSABILIDAD

Consultar todas las facturas pagadas de un mes y un año específicos,
calculando el resumen general de ventas.

El resumen incluye:

- Número de facturas.
- Subtotal vendido.
- IVA recaudado.
- Total facturado.

PARÁMETROS

mes
→ Número del mes que se desea consultar.
  Ejemplo:
  Enero = 1
  Febrero = 2
  ...
  Julio = 7

anio
→ Año al que pertenece el mes consultado.

VALOR RETORNADO

ResumenVentas

→ Devuelve un objeto con toda la información consolidada
del mes seleccionado.
     */
    public ResumenVentas obtenerVentasPorMes(int mes, int anio) {

        /*
    
    PASO 1 PREPARAR LA CONEXIÓN CON MYSQL
   

    Se abre la conexión con la base de datos.
         */
        Connection conexionBD = claseConexion.getConexion();

        /*
    Objeto encargado de preparar la consulta SQL.

    Inicialmente permanece en null hasta crear la sentencia.
         */
        PreparedStatement consultaPreparada = null;

        /*
    Objeto que almacenará el resultado del SELECT.
         */
        ResultSet resultadoConsulta = null;

        /*
    Se crea el objeto donde se almacenará el resumen
    que posteriormente será retornado al controlador.
         */
        ResumenVentas resumen = new ResumenVentas();

        /*
   
    PASO 2 CREAR LA CONSULTA SQL
   

    COUNT(*)

    Cuenta el número de facturas pagadas.

    SUM()

    Suma todos los valores encontrados.

    COALESCE()

    Si no existen ventas devuelve 0 en lugar de NULL.

    MONTH()

    Extrae el número del mes de la fecha.

    YEAR()

    Extrae el año de la fecha.

    estado_pago='pagada'

    Garantiza que solamente se tengan en cuenta las facturas ya canceladas.
         */
        String consultaSQL = """
        SELECT
            COUNT(*) AS numeroFacturas,
            COALESCE(SUM(subtotal),0) AS subtotal,
            COALESCE(SUM(iva),0) AS iva,
            COALESCE(SUM(total),0) AS total
        FROM facturas
        WHERE MONTH(fechaHora_factura)=?
        AND YEAR(fechaHora_factura)=?
        AND estado_pago='pagada'
        """;

        try {

            /*
       
        PASO 3 PREPARAR LA CONSULTA
       

        Se prepara la sentencia SQL para ser enviada a MySQL.
             */
            consultaPreparada = conexionBD.prepareStatement(consultaSQL);

            /*
       
        PASO 4  INYECTAR LOS PARÁMETROS
        

        El primer signo (?) será reemplazado por el mes.

        Ejemplo:

        MONTH(fechaHora_factura)=7
             */
            consultaPreparada.setInt(1, mes);

            /*
        El segundo signo (?) será reemplazado por el año.

        Ejemplo:

        YEAR(fechaHora_factura)=2026
             */
            consultaPreparada.setInt(2, anio);

            /*
       
        PASO 5   EJECUTAR LA CONSULTA
        

        executeQuery()

        Envía el SELECT a MySQL.

        El resultado se almacena dentro del ResultSet.
             */
            resultadoConsulta = consultaPreparada.executeQuery();

            /*
       
        PASO 6  LEER EL RESULTADO
       

        next()

        Mueve el cursor hacia la primera fila.

        Como la consulta utiliza funciones de agregación
        (COUNT y SUM), solamente existirá una fila.
             */
            if (resultadoConsulta.next()) {

                /*
            Se copia cada valor obtenido desde MySQL
            hacia el objeto ResumenVentas.
                 */
                resumen.setNumeroFacturas(resultadoConsulta.getInt("numeroFacturas"));

                resumen.setSubtotal(resultadoConsulta.getDouble("subtotal"));

                resumen.setIva(resultadoConsulta.getDouble("iva"));

                resumen.setTotal(resultadoConsulta.getDouble("total"));
            }

        } catch (Exception error) {

            /*
        Si ocurre cualquier error durante la consulta, se informa mediante la consola.
             */
            System.out.println(
                    "Error obteniendo ventas por mes: "
                    + error.getMessage());

        } finally {

            /*
      
        PASO 7 CERRAR LOS RECURSOS
       

        Se cierran todos los recursos utilizados para evitar fugas de memoria.
             */
            try {
                if (resultadoConsulta != null) {
                    resultadoConsulta.close();
                }
            } catch (Exception e) {
            }

            try {
                if (consultaPreparada != null) {
                    consultaPreparada.close();
                }
            } catch (Exception e) {
            }

            try {
                if (conexionBD != null) {
                    conexionBD.close();
                }
            } catch (Exception e) {
            }
        }

        /*
   
    PASO 8  RETORNAR EL RESULTADO
   

    Se devuelve el objeto ResumenVentas completamente lleno con la información del mes consultado.
         */
        return resumen;
    }

    /*

    /*
        
        METODO 5 OBTENER VENTAS POR CATEGORÍA

        RESPONSABILIDAD

        - Consultar todas las categorías que han generado ventas.
        - Calcular la cantidad total de productos vendidos por categoría.
        - Calcular el dinero total vendido por categoría.
        - Ordenar las categorías desde la de mayor venta hasta la de menor venta.

        TABLAS UTILIZADAS

        - productos
        - detallepedido
        - pedidos
        - facturas

        VALOR RETORNADO

        List<VentasCategoria>

        Devuelve una lista donde cada objeto representa
        una categoría del restaurante.
        
     */
    public List<VentasCategoria> obtenerVentasPorCategoria() {

        /*
    PASO 1  Abrir conexión con la base de datos.
         */
        Connection conexionBD = claseConexion.getConexion();

        PreparedStatement consultaPreparada = null;

        ResultSet resultadoConsulta = null;

        /*
    PASO 2 Crear la lista que almacenará las categorías.
         */
        List<VentasCategoria> listaCategorias = new ArrayList<>();

        /*
    PASO 3  Consulta SQL.
         */
        String consultaVentasCategoria = """
            SELECT
                productos.categoria_producot,
                SUM(detallePedido.cantidad_producto) AS cantidadVendida,
                SUM(detallePedido.cantidad_producto * detallePedido.precio_unitarioventa) AS totalVendido
            FROM productos
            INNER JOIN detallepedido detallePedido ON productos.id_producto = detallePedido.id_producto
            INNER JOIN pedidos pedido ON pedido.id_pedido = detallePedido.id_pedido
            INNER JOIN facturas ON facturas.id_pedido = pedido.id_pedido
            WHERE facturas.estado_pago = 'pagada'
            GROUP BY productos.categoria_producot
            ORDER BY totalVendido DESC
            """;

        try {

            /*
        PASO 4  Preparar la consulta.
             */
            consultaPreparada = conexionBD.prepareStatement(consultaVentasCategoria);

            /*
        PASO 5  Ejecutar la consulta.
             */
            resultadoConsulta = consultaPreparada.executeQuery();

            /*
        PASO 6  Recorrer todas las categorías encontradas.
             */
            while (resultadoConsulta.next()) {

                VentasCategoria categoria = new VentasCategoria();

                categoria.setCategoriaProducto(resultadoConsulta.getString("categoria_producot"));

                categoria.setCantidadVendida(resultadoConsulta.getInt("cantidadVendida"));

                categoria.setTotalVendido(resultadoConsulta.getDouble("totalVendido"));

                /*
            Agregar la categoría a la lista.
                 */
                listaCategorias.add(categoria);
            }

        } catch (Exception error) {

            System.out.println(
                    "Error obteniendo ventas por categoría: "
                    + error.getMessage());

        } finally {

            try {
                if (resultadoConsulta != null) {
                    resultadoConsulta.close();
                }
            } catch (Exception e) {
            }

            try {
                if (consultaPreparada != null) {
                    consultaPreparada.close();
                }
            } catch (Exception e) {
            }

            try {
                if (conexionBD != null) {
                    conexionBD.close();
                }
            } catch (Exception e) {
            }

        }

        return listaCategorias;
    }

    /*
=
        METODO 6  OBTENER TOTAL FACTURADO 
    
        RESPONSABILIDAD

        - Calcular el dinero total facturado por el restaurante.
        - Solo tiene en cuenta las facturas pagadas.
    - se reinicia automáticamente cada 1 de enero

        TABLA UTILIZADA

        - facturas

        VALOR RETORNADO

        ResumenVentas

        Devuelve un objeto que contiene únicamente    el total facturado del sistema.


     */
    public ResumenVentas obtenerTotalFacturado() {

        Connection conexionBD = claseConexion.getConexion();
        PreparedStatement consultaPreparada = null;
        ResultSet resultadoConsulta = null;
        ResumenVentas resumen = new ResumenVentas();

        // YEAR(CURDATE()) trae el año actual automáticamente
        // cuando cambie el año, la consulta empieza desde cero sola
        String consultaTotalAnioActual = """
        SELECT
            COALESCE(SUM(total), 0) AS totalFacturado,
            COUNT(*) AS numeroFacturas
        FROM facturas
        WHERE estado_pago = 'pagada'
        AND YEAR(fechaHora_factura) = YEAR(CURDATE())
        """;

        try {
            consultaPreparada = conexionBD.prepareStatement(consultaTotalAnioActual);
            resultadoConsulta = consultaPreparada.executeQuery();

            if (resultadoConsulta.next()) {
                resumen.setTotal(resultadoConsulta.getDouble("totalFacturado"));
                resumen.setNumeroFacturas(resultadoConsulta.getInt("numeroFacturas"));
            }

        } catch (Exception error) {
            System.out.println("Error obteniendo total del año: " + error.getMessage());
        } finally {
            try {
                if (resultadoConsulta != null) {
                    resultadoConsulta.close();
                }
            } catch (Exception e) {
            }
            try {
                if (consultaPreparada != null) {
                    consultaPreparada.close();
                }
            } catch (Exception e) {
            }
            try {
                if (conexionBD != null) {
                    conexionBD.close();
                }
            } catch (Exception e) {
            }
        }

        return resumen;
    }

    /*
    METODO 7 — TOP 5 PRODUCTOS MÁS VENDIDOS
    - por defecto muestra solo los 5 primeros
     */
    public List<ProductoMasVendido> obtenerTop5ProductosMasVendidos() {

        Connection conexionBD = claseConexion.getConexion();
        PreparedStatement consultaPreparada = null;
        ResultSet resultadoConsulta = null;
        List<ProductoMasVendido> listaTop5 = new ArrayList<>();

        String consultaTop5 = """
        SELECT
            productos.id_producto,
            productos.nombre_producto,
            SUM(detallePedido.cantidad_producto) AS cantidadVendida,
            SUM(detallePedido.cantidad_producto * detallePedido.precio_unitarioventa) AS totalVendido
        FROM productos
        INNER JOIN detallepedido detallePedido ON productos.id_producto = detallePedido.id_producto
        INNER JOIN pedidos pedido ON pedido.id_pedido = detallePedido.id_pedido
        INNER JOIN facturas ON facturas.id_pedido = pedido.id_pedido
        WHERE facturas.estado_pago = 'pagada'
        GROUP BY productos.id_producto, productos.nombre_producto
        ORDER BY cantidadVendida DESC
        LIMIT 5
        """;

        try {
            consultaPreparada = conexionBD.prepareStatement(consultaTop5);
            resultadoConsulta = consultaPreparada.executeQuery();

            while (resultadoConsulta.next()) {
                ProductoMasVendido productoActual = new ProductoMasVendido();
                productoActual.setIdProducto(resultadoConsulta.getInt("id_producto"));
                productoActual.setNombreProducto(resultadoConsulta.getString("nombre_producto"));
                productoActual.setCantidadVendida(resultadoConsulta.getInt("cantidadVendida"));
                productoActual.setTotalVendido(resultadoConsulta.getDouble("totalVendido"));
                listaTop5.add(productoActual);
            }
        } catch (Exception error) {
            System.out.println("Error obteniendo top 5: " + error.getMessage());
        } finally {
            try {
                if (resultadoConsulta != null) {
                    resultadoConsulta.close();
                }
            } catch (Exception e) {
            }
            try {
                if (consultaPreparada != null) {
                    consultaPreparada.close();
                }
            } catch (Exception e) {
            }
            try {
                if (conexionBD != null) {
                    conexionBD.close();
                }
            } catch (Exception e) {
            }
        }
        return listaTop5;
    }

    /*
    METODO 8 — BUSCAR PRODUCTO POR NOMBRE
    - filtra productos que contengan el texto buscado
    - usa LIKE para búsqueda parcial
     */
    public List<ProductoMasVendido> buscarProductosPorNombre(String nombreBuscado) {

        Connection conexionBD = claseConexion.getConexion();
        PreparedStatement consultaPreparada = null;
        ResultSet resultadoConsulta = null;
        List<ProductoMasVendido> listaFiltrada = new ArrayList<>();

        String consultaBuscarNombre = """
        SELECT
            productos.id_producto,
            productos.nombre_producto,
            SUM(detallePedido.cantidad_producto) AS cantidadVendida,
            SUM(detallePedido.cantidad_producto * detallePedido.precio_unitarioventa) AS totalVendido
        FROM productos
        INNER JOIN detallepedido detallePedido ON productos.id_producto = detallePedido.id_producto
        INNER JOIN pedidos pedido ON pedido.id_pedido = detallePedido.id_pedido
        INNER JOIN facturas ON facturas.id_pedido = pedido.id_pedido
        WHERE facturas.estado_pago = 'pagada'
        AND productos.nombre_producto LIKE ?
        GROUP BY productos.id_producto, productos.nombre_producto
        ORDER BY cantidadVendida DESC
        """;

        try {
            consultaPreparada = conexionBD.prepareStatement(consultaBuscarNombre);
            // % antes y después permite buscar el texto en cualquier posición
            // ejemplo: "hamb" encuentra "Hamburguesa Clásica"
            consultaPreparada.setString(1, "%" + nombreBuscado + "%");
            resultadoConsulta = consultaPreparada.executeQuery();

            while (resultadoConsulta.next()) {
                ProductoMasVendido productoActual = new ProductoMasVendido();
                productoActual.setIdProducto(resultadoConsulta.getInt("id_producto"));
                productoActual.setNombreProducto(resultadoConsulta.getString("nombre_producto"));
                productoActual.setCantidadVendida(resultadoConsulta.getInt("cantidadVendida"));
                productoActual.setTotalVendido(resultadoConsulta.getDouble("totalVendido"));
                listaFiltrada.add(productoActual);
            }
        } catch (Exception error) {
            System.out.println("Error buscando producto: " + error.getMessage());
        } finally {
            try {
                if (resultadoConsulta != null) {
                    resultadoConsulta.close();
                }
            } catch (Exception e) {
            }
            try {
                if (consultaPreparada != null) {
                    consultaPreparada.close();
                }
            } catch (Exception e) {
            }
            try {
                if (conexionBD != null) {
                    conexionBD.close();
                }
            } catch (Exception e) {
            }
        }
        return listaFiltrada;
    }

    /*
    METODO 9 — BUSCAR PRODUCTOS POR CATEGORÍA
    - filtra todos los productos de una categoría específica
     */
    public List<ProductoMasVendido> buscarProductosPorCategoria(String categoriaBuscada) {

        Connection conexionBD = claseConexion.getConexion();
        PreparedStatement consultaPreparada = null;
        ResultSet resultadoConsulta = null;
        List<ProductoMasVendido> listaCategoria = new ArrayList<>();

        String consultaBuscarCategoria = """
        SELECT
            productos.id_producto,
            productos.nombre_producto,
            SUM(detallePedido.cantidad_producto) AS cantidadVendida,
            SUM(detallePedido.cantidad_producto * detallePedido.precio_unitarioventa) AS totalVendido
        FROM productos
        INNER JOIN detallepedido detallePedido ON productos.id_producto = detallePedido.id_producto
        INNER JOIN pedidos pedido ON pedido.id_pedido = detallePedido.id_pedido
        INNER JOIN facturas ON facturas.id_pedido = pedido.id_pedido
        WHERE facturas.estado_pago = 'pagada'
        AND productos.categoria_producot = ?
        GROUP BY productos.id_producto, productos.nombre_producto
        ORDER BY cantidadVendida DESC
        """;

        try {
            consultaPreparada = conexionBD.prepareStatement(consultaBuscarCategoria);
            consultaPreparada.setString(1, categoriaBuscada);
            resultadoConsulta = consultaPreparada.executeQuery();

            while (resultadoConsulta.next()) {
                ProductoMasVendido productoActual = new ProductoMasVendido();
                productoActual.setIdProducto(resultadoConsulta.getInt("id_producto"));
                productoActual.setNombreProducto(resultadoConsulta.getString("nombre_producto"));
                productoActual.setCantidadVendida(resultadoConsulta.getInt("cantidadVendida"));
                productoActual.setTotalVendido(resultadoConsulta.getDouble("totalVendido"));
                listaCategoria.add(productoActual);
            }
        } catch (Exception error) {
            System.out.println("Error buscando por categoría: " + error.getMessage());
        } finally {
            try {
                if (resultadoConsulta != null) {
                    resultadoConsulta.close();
                }
            } catch (Exception e) {
            }
            try {
                if (consultaPreparada != null) {
                    consultaPreparada.close();
                }
            } catch (Exception e) {
            }
            try {
                if (conexionBD != null) {
                    conexionBD.close();
                }
            } catch (Exception e) {
            }
        }
        return listaCategoria;
    }

}
