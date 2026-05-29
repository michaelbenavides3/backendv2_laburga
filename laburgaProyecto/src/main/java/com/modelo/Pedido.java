package com.modelo;

import java.sql.Timestamp;

public class Pedido {
    private int idPedido;
    private int idMesa;
    private int idMesero;
    private Timestamp fechaPedido;
    private String estadoPedido;
    private String detalle;
    private double total;

    public Pedido() {
    }

    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

    public int getIdMesa() { return idMesa; }
    public void setIdMesa(int idMesa) { this.idMesa = idMesa; }

    public int getIdMesero() { return idMesero; }
    public void setIdMesero(int idMesero) { this.idMesero = idMesero; }

    public Timestamp getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(Timestamp fechaPedido) { this.fechaPedido = fechaPedido; }

    public String getEstadoPedido() { return estadoPedido; }
    public void setEstadoPedido(String estadoPedido) { this.estadoPedido = estadoPedido; }

    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}