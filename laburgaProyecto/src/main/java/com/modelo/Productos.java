
package com.modelo;


public class Productos {
    
    //declaro los campos que se encutran dentro de mi tabla productos
    private int idProducto;
    private String nombreProducto;
    private String descripcionProducto;
    private double precioBaseProducto;
    private String categoriaProducto;
    private boolean disponibleProducto;
    
    //creamos el cosntructos vcio para despues ingresarle los datos o inyectarle los datos
    public Productos(){
        
    }
    
    public Productos(int idProducto, String nombreProducto, String descripcionProducto, double precioBaseProducto, String categoriaProducto, boolean disponibleProducto){
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.descripcionProducto = descripcionProducto;
        this.precioBaseProducto = precioBaseProducto;
        this.categoriaProducto = categoriaProducto;
        this.disponibleProducto = disponibleProducto;
    }
    
    //creamos los metodo get y setter para cada variable 
    
    // //metodo para obtener o leer el id del rol
    //utilizamos int, porque necesitamos que nos devuelva un valor
    public  int getIdProducto(){
        return idProducto;
    }
     //permite que java ele se asigne al objeto el id que ya se genero en la base de datos
    //utilizamos void, porque no necesita que nos devuelva nada, solo haga cambios y lo sguarde dentro del objeto
    public void setIdProducto(int idproducto){
        this.idProducto = idproducto;
    }
    // //metodo para obtener o leer el id del rol
    //utilizamos int, porque necesitamos que nos devuelva un valor
    public String getNombreProducto() {
        return nombreProducto;
    }
     //permite que java ele se asigne al objeto el id que ya se genero en la base de datos
    //utilizamos void, porque no necesita que nos devuelva nada, solo haga cambios y lo sguarde dentro del objeto
    public void setNombreProducto(String nombreProducto){
        this.nombreProducto = nombreProducto;
    }
    // //metodo para obtener o leer el id del rol
    //utilizamos int, porque necesitamos que nos devuelva un valor
    public String getDescripcionProducto(){
        return descripcionProducto;
    }
     //permite que java ele se asigne al objeto el id que ya se genero en la base de datos
    //utilizamos void, porque no necesita que nos devuelva nada, solo haga cambios y lo sguarde dentro del objeto
    public void setDescripcionProducto(String descripcionProducto){
        this.descripcionProducto = descripcionProducto;
    }
    // //metodo para obtener o leer el id del rol
    //utilizamos int, porque necesitamos que nos devuelva un valor
    public double getPrecioBaseProducto(){
        return precioBaseProducto;
    }
     //permite que java ele se asigne al objeto el id que ya se genero en la base de datos
    //utilizamos void, porque no necesita que nos devuelva nada, solo haga cambios y lo sguarde dentro del objeto
    public void setPrecioBaseProducto(double precioBaseProducto){
        this.precioBaseProducto = precioBaseProducto;
    }
    // //metodo para obtener o leer el id del rol
    //utilizamos int, porque necesitamos que nos devuelva un valor
    public String getCategoriaProducto(){
        return categoriaProducto;
    }
     //permite que java ele se asigne al objeto el id que ya se genero en la base de datos
    //utilizamos void, porque no necesita que nos devuelva nada, solo haga cambios y lo sguarde dentro del objeto
    public void setCategoriaProducto(String categoriaProducto){
        this.categoriaProducto = categoriaProducto;
    }
    // //metodo para obtener o leer el id del rol
    //utilizamos int, porque necesitamos que nos devuelva un valor
    //por regla general cuando es get, se reemplaza por is, cuando es boolean
    public boolean isDisponibleProducto(){
        return disponibleProducto;
    }
     //permite que java ele se asigne al objeto el id que ya se genero en la base de datos
    //utilizamos void, porque no necesita que nos devuelva nada, solo haga cambios y lo sguarde dentro del objeto
    public void setDisponibleProducto(boolean disponibleProducto){
        this.disponibleProducto = disponibleProducto;
    }
}
