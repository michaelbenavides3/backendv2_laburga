package com.modelo;

public class ProductoImagen {

    private int idProductoImagenes;

    private int idProducto;

    private String rutaArchivoImagen;

    private boolean imagenActualizada;

    public ProductoImagen() {

    }

    public ProductoImagen(int idProductoImagenes,int idProducto,String rutaArchivoImagen,boolean imagenActualizada) {

        this.idProductoImagenes = idProductoImagenes;
        this.idProducto = idProducto;
        this.rutaArchivoImagen = rutaArchivoImagen;
        this.imagenActualizada = imagenActualizada;
    }

    /*
   
    GET Y SET ID IMAGEN
   
     */
    public int getIdProductoImagenes() {
        return idProductoImagenes;
    }

    public void setIdProductoImagenes( int idProductoImagenes) {

        this.idProductoImagenes = idProductoImagenes;
    }

    /*
   
    GET Y SET ID PRODUCTO
   
     */
    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {

        this.idProducto = idProducto;
    }

    /*
   
    GET Y SET RUTA IMAGEN
    
     */
    public String getRutaArchivoImagen() {
        return rutaArchivoImagen;
    }

    public void setRutaArchivoImagen( String rutaArchivoImagen) {

        this.rutaArchivoImagen = rutaArchivoImagen;
    }

    /*
    
    GET Y SET IMAGEN ACTUALIZADA
    
     */
    public boolean isImagenActualizada() {
        return imagenActualizada;
    }

    public void setImagenActualizada( boolean imagenActualizada) {

        this.imagenActualizada = imagenActualizada;
    }
}