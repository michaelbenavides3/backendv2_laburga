package com.modelo;

/*

## MODELO MEDIO PAGO

Representa un método de pago disponible
en el sistema.

Ejemplos:

1. Efectivo
2. Nequi
3. Daviplata
4. Tarjeta

---

 */
public class MedioPago {


    /*

VARIABLES DE LA TABLA MEDIOSPAGOS

     */
    private int idMetodoPago;

    private String metodoPago;

    private String descripcion;

    private boolean activo;

    /*

CONSTRUCTOR VACIO

     */
    public MedioPago() {

    }

    /*

CONSTRUCTOR COMPLETO

     */
    public MedioPago(int idMetodoPago, String metodoPago, String descripcion, boolean activo) {

        this.idMetodoPago = idMetodoPago;
        this.metodoPago = metodoPago;
        this.descripcion = descripcion;
        this.activo = activo;
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

GET Y SET METODO PAGO

     */
    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    /*

GET Y SET DESCRIPCION

     */
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /*

GET Y SET ACTIVO

     */
    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

}
