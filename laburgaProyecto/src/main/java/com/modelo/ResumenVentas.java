package com.modelo;

public class ResumenVentas {

    private int numeroPedidos;
    private int numeroFacturas;
    private double subtotal;
    private double iva;
    private double total;

    public ResumenVentas() {

    }

    public ResumenVentas(int numeroPedidos, int numeroFacturas, double subtotal, double iva, double total) {

        this.numeroPedidos = numeroPedidos;
        this.numeroFacturas = numeroFacturas;
        this.subtotal = subtotal;
        this.iva = iva;
        this.total = total;

    }

    public int getNumeroPedidos() {
        return numeroPedidos;
    }

    public void setNumeroPedido(int numeroPedidos) {
        this.numeroPedidos = numeroPedidos;
    }
    
     public int getNumeroFacturas() {
        return numeroFacturas;
    }
    public void setNumeroFacturas(int numeroFacturas) {
        this.numeroFacturas = numeroFacturas;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
 
}
