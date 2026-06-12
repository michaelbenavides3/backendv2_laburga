package com.modelo;

public class DetallePedido {
    private int idDetalle;
    private int idPedido;
    private int idProducto;
    private int cantidad;
    private double precioVenta;
    private String observaciones;
    private String nombreProducto;
    private double subtotalLinea;

    // Constructor vacío
    public DetallePedido() {}

    // Constructor con parámetros
    public DetallePedido(int idDetalle, int idPedido, int idProducto, int cantidad, double precioVenta, String observaciones, String nombreProducto, double subtotalLinea) {
        this.idDetalle = idDetalle;
        this.idPedido = idPedido;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioVenta = precioVenta;
        this.observaciones = observaciones;
        this.nombreProducto = nombreProducto;
        this.subtotalLinea = subtotalLinea;
    }

    // Getters y Setters
    public int getIdDetalle() { return idDetalle; }
    public void setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }

    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(double precioVenta) { this.precioVenta = precioVenta; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    
    public String getNombreProducto(){
        return nombreProducto;
    }
    public void setNombreProducto(String nombreProducto){
        this.nombreProducto = nombreProducto;
    }
    
    public double getSubtotalLinea(){
        return subtotalLinea;
    }
    public void setSubtotalLinea(double subtotalLinea){
        this.subtotalLinea = subtotalLinea;
    }
}