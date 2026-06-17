package com.modelo;

import java.sql.Timestamp;

public class Factura {

    private int idFactura;

    private int idPedido;

    private Timestamp fechaHoraFactura;

    private double subtotal;

    private double iva;

    private double total;

    private String estadoPago;

    public Factura() {

    }

    public Factura(int idFactura, int idPedido, Timestamp fechaHoraFactura, double subtotal, double iva, double total, String estadoPago) {

        this.idFactura = idFactura;
        this.idPedido = idPedido;
        this.fechaHoraFactura = fechaHoraFactura;
        this.subtotal = subtotal;
        this.iva = iva;
        this.total = total;
        this.estadoPago = estadoPago;
    }

    /* ---------------------------------- GET Y SET ID FACTURA ---------------------------------- */
    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    /* ---------------------------------- GET Y SET ID PEDIDO ---------------------------------- */
    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    /* ---------------------------------- GET Y SET FECHA FACTURA ---------------------------------- */
    public Timestamp getFechaHoraFactura() {
        return fechaHoraFactura;
    }

    public void setFechaHoraFactura(Timestamp fechaHoraFactura) {
        this.fechaHoraFactura = fechaHoraFactura;
    }

    /* ---------------------------------- GET Y SET SUBTOTAL ---------------------------------- */
    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    /* ---------------------------------- GET Y SET IVA ---------------------------------- */
    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    /* ---------------------------------- GET Y SET TOTAL ---------------------------------- */
    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    /* ---------------------------------- GET Y SET ESTADO PAGO ---------------------------------- */
    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

}
