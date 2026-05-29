package com.modelo;

import java.util.Date;

public class Cliente {
    private int idCliente;
    private String nombreCompleto;
    private String documentoIdentidad;
    private Date fechaNacimiento; // Por ahora puede ir null si el formulario no lo pide

    // Atributos auxiliares para capturar el primer teléfono y correo desde el formulario
    private String telefono;
    private String correo;

    public Cliente() {
    }

    // Constructor práctico para el formulario web
    public Cliente(String nombreCompleto, String documentoIdentidad, String telefono, String correo) {
        this.nombreCompleto = nombreCompleto;
        this.documentoIdentidad = documentoIdentidad;
        this.telefono = telefono;
        this.correo = correo;
    }

    // Getters y Setters
    public int getIdCliente() { 
        return idCliente; 
    }
    public void setIdCliente(int idCliente) { 
        this.idCliente = idCliente; 
    }

    public String getNombreCompleto() { 
        return nombreCompleto; 
    }
    public void setNombreCompleto(String nombreCompleto) { 
        this.nombreCompleto = nombreCompleto; 
    }

    public String getDocumentoIdentidad() { 
        return documentoIdentidad; 
    }
    public void setDocumentoIdentidad(String documentoIdentidad) { 
        this.documentoIdentidad = documentoIdentidad; 
    }

    public Date getFechaNacimiento() { 
        return fechaNacimiento; 
    }
    public void setFechaNacimiento(Date fechaNacimiento) { 
        this.fechaNacimiento = fechaNacimiento; 
    }

    public String getTelefono() { 
        return telefono; 
    }
    public void setTelefono(String telefono) { 
        this.telefono = telefono; 
    }

    public String getCorreo() { 
        return correo; 
    }
    public void setCorreo(String correo) { 
        this.correo = correo; 
    }
}