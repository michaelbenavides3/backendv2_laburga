package com.modelo;

import java.sql.Timestamp;

   

/*

## MODELO PAGO

Representa un pago realizado sobre una factura.

Contiene:

1. idPago
2. idFactura
3. idMetodoPago
4. fechaHoraPago

---

*/

public class Pago {

/*

VARIABLES DE LA TABLA PAGOS

*/

private int idPago;

    private int idFactura;

    private int idMetodoPago;

    private Timestamp fechaHoraPago;

    /*

CONSTRUCTOR VACIO

     */
    public Pago() {

    }

    /*

CONSTRUCTOR COMPLETO

     */
    public Pago(int idPago,int idFactura, int idMetodoPago,Timestamp fechaHoraPago) {

        this.idPago = idPago;
        this.idFactura = idFactura;
        this.idMetodoPago = idMetodoPago;
        this.fechaHoraPago = fechaHoraPago;
    }

    /*

GET Y SET ID PAGO
--
     */
    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    /*

GET Y SET ID FACTURA

     */
    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    /*

GET Y SET ID METODO PAGO
    
     */
    public int getIdMetodoPago() {
        return idMetodoPago;
    }

    public void setIdMetodoPago(int idMetodoPago) {
        this.idMetodoPago = idMetodoPago;
    }

    /*

GET Y SET FECHA PAGO

     */
    public Timestamp getFechaHoraPago() {
        return fechaHoraPago;
    }

    public void setFechaHoraPago(Timestamp fechaHoraPago) {
        this.fechaHoraPago = fechaHoraPago;
    }

}
