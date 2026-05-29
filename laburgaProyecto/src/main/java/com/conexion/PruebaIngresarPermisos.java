
package com.conexion;
import com.dao.PermisosDAO;
import com.modelo.Permisos;


public class PruebaIngresarPermisos {

    
    public static void main(String[] args) {
       
        System.out.println("iniciando registros");
        
        //instancio mi administrador de datos para poder enviarlos a mysql
        PermisosDAO administradorPermisos = new PermisosDAO();
        
        //creo una variable reutilizable
        
        Permisos permisoTemporal;
        
        //seccion control de pedidos para mesero
        
        permisoTemporal = new Permisos();
        permisoTemporal.setSlugPermisos("pedido.crear");
        permisoTemporal.setNombrePermiso("crear pedidos");
        permisoTemporal.setDescripcionPermiso("permite registrar una nueva orden de consumo para una mesa.");
        administradorPermisos.registrarNuevopermiso(permisoTemporal);
        
        permisoTemporal = new Permisos();
        permisoTemporal.setSlugPermisos("pedido.editar");
        permisoTemporal.setNombrePermiso("editar pedidos");
        permisoTemporal.setDescripcionPermiso("permite modifica productos agregados, de un pedido activo.");
        administradorPermisos.registrarNuevopermiso(permisoTemporal);
        
        permisoTemporal = new Permisos();
        permisoTemporal.setSlugPermisos("pedido.visualizar");
        permisoTemporal.setNombrePermiso("visualizar pedido");
        permisoTemporal.setDescripcionPermiso("permitir ver la lista y el estado de todos los pedidos al dia");
        administradorPermisos.registrarNuevopermiso(permisoTemporal);
        
        permisoTemporal = new Permisos();
        permisoTemporal.setSlugPermisos("pedido.cancelar");
        permisoTemporal.setNombrePermiso("cancelar pedido");
        permisoTemporal.setDescripcionPermiso("permitir cancelar un pedido por completo o por error de solicitud");
        administradorPermisos.registrarNuevopermiso(permisoTemporal);
        
        permisoTemporal = new Permisos();
        permisoTemporal.setSlugPermisos("pedido.enviar");
        permisoTemporal.setNombrePermiso("enviar pedido");
        permisoTemporal.setDescripcionPermiso("permitir enviar el pedido finalizado al cajero para realizar la factura de cobro");
        administradorPermisos.registrarNuevopermiso(permisoTemporal);
        
        
        
        //seccion control de mesas
        
        permisoTemporal = new Permisos();
        permisoTemporal.setSlugPermisos("mesa.ver");
        permisoTemporal.setNombrePermiso("ver mesas");
        permisoTemporal.setDescripcionPermiso("permite visualizar el mapa de las mesas y la disponibilidad");
        administradorPermisos.registrarNuevopermiso(permisoTemporal);
                
        
        permisoTemporal = new Permisos();
        permisoTemporal.setSlugPermisos("mesa.gestionar");
        permisoTemporal.setNombrePermiso("gestionar mesas");
        permisoTemporal.setDescripcionPermiso("permite cambiar el estado de las mesas (ocpada, disponible, mantenimiento, reservada)");
        administradorPermisos.registrarNuevopermiso(permisoTemporal);
        
        permisoTemporal = new Permisos();
        permisoTemporal.setSlugPermisos("mesa.liberar");
        permisoTemporal.setNombrePermiso("liberar mesas");
        permisoTemporal.setDescripcionPermiso("permite liberar un mesa despues de realizar el pago");
        administradorPermisos.registrarNuevopermiso(permisoTemporal);
        
        
        //permisos de gestion de clientes
        
        permisoTemporal = new Permisos();
        permisoTemporal.setSlugPermisos("cliente.crear");
        permisoTemporal.setNombrePermiso("crear cliente");
        permisoTemporal.setDescripcionPermiso("permite registrar un cliente nuevo en la base de datos");
        administradorPermisos.registrarNuevopermiso(permisoTemporal);
        
        permisoTemporal = new Permisos();
        permisoTemporal.setSlugPermisos("cliente.ver");
        permisoTemporal.setNombrePermiso("ver clientes");
        permisoTemporal.setDescripcionPermiso("permite consultar la informacion de los clientes");
        administradorPermisos.registrarNuevopermiso(permisoTemporal);
        
        //seccion control de carta. 
        
        permisoTemporal = new Permisos();
        permisoTemporal.setSlugPermisos("producto.crear");
        permisoTemporal.setNombrePermiso("crear productos");
        permisoTemporal.setDescripcionPermiso("permite crear nuevo platos o menu de temporada");
        administradorPermisos.registrarNuevopermiso(permisoTemporal);
        
          permisoTemporal = new Permisos();
        permisoTemporal.setSlugPermisos("producto.editar");
        permisoTemporal.setNombrePermiso("editar productos");
        permisoTemporal.setDescripcionPermiso("permite ajustar precios de la carta");
        administradorPermisos.registrarNuevopermiso(permisoTemporal);
        
        
        permisoTemporal = new Permisos();
        permisoTemporal.setSlugPermisos("producto.desactivar");
        permisoTemporal.setNombrePermiso("desactivar producto");
        permisoTemporal.setDescripcionPermiso("permite desactivar temporalmente un producto o un menu de la carta");
        administradorPermisos.registrarNuevopermiso(permisoTemporal);
        
        //seccion control de caja y facturacion
        
        permisoTemporal = new Permisos();
        permisoTemporal.setSlugPermisos("factura.crear");
        permisoTemporal.setNombrePermiso("crear factura");
        permisoTemporal.setDescripcionPermiso("permite generar el ticket con el total de cobro");
        administradorPermisos.registrarNuevopermiso(permisoTemporal);
        
        permisoTemporal = new Permisos();
        permisoTemporal.setSlugPermisos("factura.ver");
        permisoTemporal.setNombrePermiso("ver factura");
        permisoTemporal.setDescripcionPermiso("permite revisar la factura de las venta de los prodctos");
        administradorPermisos.registrarNuevopermiso(permisoTemporal);
        
        permisoTemporal = new Permisos();
        permisoTemporal.setSlugPermisos("pago.registrar");
        permisoTemporal.setNombrePermiso("registrar pago");
        permisoTemporal.setDescripcionPermiso("permite registrar pagos asociado a una factura");
        administradorPermisos.registrarNuevopermiso(permisoTemporal);
        
        permisoTemporal = new Permisos();
        permisoTemporal.setSlugPermisos("pagos.ver");
        permisoTemporal.setNombrePermiso("ver pagos");
        permisoTemporal.setDescripcionPermiso("permite consultar los pagos recibidos por dia");
        administradorPermisos.registrarNuevopermiso(permisoTemporal);
        
        // panel para administradir
        
        permisoTemporal = new Permisos();
        permisoTemporal.setSlugPermisos("usuario.crear");
        permisoTemporal.setNombrePermiso("crear usuario");
        permisoTemporal.setDescripcionPermiso("permite crear usuario nuevos usuarios");
        administradorPermisos.registrarNuevopermiso(permisoTemporal);
        
        permisoTemporal = new Permisos();
        permisoTemporal.setSlugPermisos("usuario.editar");
        permisoTemporal.setNombrePermiso("editar usuario");
        permisoTemporal.setDescripcionPermiso("permite editar informacion de los usuarios existente");
        administradorPermisos.registrarNuevopermiso(permisoTemporal);
        
        permisoTemporal = new Permisos();
        permisoTemporal.setSlugPermisos("usuario.desactivar");
        permisoTemporal.setNombrePermiso("desactivar usuario");
        permisoTemporal.setDescripcionPermiso("permite desactivar un usuario");
        administradorPermisos.registrarNuevopermiso(permisoTemporal);
        
        permisoTemporal = new Permisos();
        permisoTemporal.setSlugPermisos("rol.crear");
        permisoTemporal.setNombrePermiso("crear rol");
        permisoTemporal.setDescripcionPermiso("permite agregar nuevos puesto de trabajo al restaurante o empresa");
        administradorPermisos.registrarNuevopermiso(permisoTemporal);
        
        permisoTemporal = new Permisos();
        permisoTemporal.setSlugPermisos("rol.editar");
        permisoTemporal.setNombrePermiso("editar rol");
        permisoTemporal.setDescripcionPermiso("permite editar o cambiar los nombres o descripcion de los cargos");
        administradorPermisos.registrarNuevopermiso(permisoTemporal);
        
        permisoTemporal = new Permisos();
        permisoTemporal.setSlugPermisos("permiso.crear");
        permisoTemporal.setNombrePermiso("crear permisos");
        permisoTemporal.setDescripcionPermiso("permite inyectar nuevos permisos al programa o sotfware");
        administradorPermisos.registrarNuevopermiso(permisoTemporal);
        
        permisoTemporal = new Permisos();
        permisoTemporal.setSlugPermisos("permisos.editar");
        permisoTemporal.setNombrePermiso("editar permisos");
        permisoTemporal.setDescripcionPermiso("permite cambiar los slug o nombre de los permisos");
        administradorPermisos.registrarNuevopermiso(permisoTemporal);
        
        System.out.println("final de permisos. enviado a mysql");
        
    }
    
}
