package com.modelo;

public class TelefonoUsuario {
    private int idTelefono; // Es buena práctica tener el ID de la propia tabla
    private int idUsuario;
    private String numero;

    public TelefonoUsuario() {
    }

    public TelefonoUsuario(int idTelefono, int idUsuario, String numero) {
        this.idTelefono = idTelefono;
        this.idUsuario = idUsuario;
        this.numero = numero;
    }

    public int getIdTelefono() { return idTelefono; }
    public void setIdTelefono(int idTelefono) { this.idTelefono = idTelefono; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
}