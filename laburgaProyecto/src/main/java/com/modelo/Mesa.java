package com.modelo;

public class Mesa {
    
    //constantes
    public static final String ESTADO_DISPONIBLE = "disponible";
    public static final String ESTADO_OCUPADA = "ocupada";
    public static final String ESTADO_RESERVADA = "reservada";
    public static final String ESTADO_PENDIENTE_COBRO = "pendiente_cobro";
    
    
    
    //atributos
    private int idMesas;
    private int numeroMesa;
    private int capcidadMesa; // <--- capacidad mesas
    private String estadoMesa;

    public Mesa() {
    }

    public Mesa(int idMesas, int numeroMesa, int capcidadMesa, String estadoMesa) {
        this.idMesas = idMesas;
        this.numeroMesa = numeroMesa;
        this.capcidadMesa = capcidadMesa; // <--- capacidad mesas
        this.estadoMesa = estadoMesa;
    }

    public int getIdMesas() { return idMesas; }
    public void setIdMesas(int idMesas) { this.idMesas = idMesas; }

    public int getNumeroMesa() { return numeroMesa; }
    public void setNumeroMesa(int numeroMesa) { this.numeroMesa = numeroMesa; }

    // Cambiados los métodos Get y Set
    public int getCapcidadMesa() { return capcidadMesa; } 
    public void setCapcidadMesa(int capcidMesa) { this.capcidadMesa = capcidMesa; }

    public String getEstadoMesa() { return estadoMesa; }
    public void setEstadoMesa(String estadoMesa) { this.estadoMesa = estadoMesa; }
}