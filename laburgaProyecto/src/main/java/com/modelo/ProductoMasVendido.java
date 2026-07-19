package com.modelo;

public class ProductoMasVendido {

    private int idProducto;
    private String nombreProducto;
    private int cantidadVendida;
    private double totalVendido;

    public ProductoMasVendido() {
    }

    public ProductoMasVendido(int idProducto,
            String nombreProducto,
            int cantidadVendida,
            double totalVendido) {

        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.cantidadVendida = cantidadVendida;
        this.totalVendido = totalVendido;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public int getCantidadVendida() {
        return cantidadVendida;
    }

    public void setCantidadVendida(int cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }

    public double getTotalVendido() {
        return totalVendido;
    }

    public void setTotalVendido(double totalVendido) {
        this.totalVendido = totalVendido;
    }

}