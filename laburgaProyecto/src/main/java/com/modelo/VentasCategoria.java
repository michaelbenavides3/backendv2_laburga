
package com.modelo;

public class VentasCategoria {
    
    private String categoriaProducto;
    private int cantidadVendida;
    private double totalVendido;
    
    
    public VentasCategoria(){
        
    }
    
    
    public VentasCategoria (String categoriaProducto, int cantidadVendida, double totalVendido){
        
        this.categoriaProducto = categoriaProducto;
        this.cantidadVendida = cantidadVendida;
        this.totalVendido = totalVendido;
    }
    
    public String getCategoriaProducto(){
        return categoriaProducto;
    }
    public void setCategoriaProducto(String categoriaProducto){
        this.categoriaProducto = categoriaProducto;
    }
    
    public int getCantidadVendida(){
        return cantidadVendida;
    }
    public void setCantidadVendida(int cantidadVendida){
        this.cantidadVendida = cantidadVendida;
    }
    
    public double getTotalVendido(){
        return totalVendido;
    }
    public void setTotalVendido(double totalVendido){
        this.totalVendido = totalVendido;
    }
    
}
