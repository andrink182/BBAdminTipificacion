package com.mycompany.bbadmintipificacion.entity;

import jakarta.persistence.*;

/**
 * Entidad Producto que mapea con la tabla 'bb_productos_lov'
 * Esquema: ProductoID, Nombre, Descripcion
 */
@Entity
@Table(name = "bb_productos_lov")
public class Producto {
    
    @Id
    @Column(name = "ProductoID")
    private Integer productoId;
    
    @Column(name = "Nombre", length = 255)
    private String nombre;
    
    @Column(name = "Descripcion", length = 500)
    private String descripcion;
    
     @Column(name = "Activo", length = 1)
    private String activo = "1"; // Por defecto activo
    // Constructor vacío requerido por JPA
    public Producto() {}
    
    // Constructor con parámetros
    public Producto(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    
    // Getters y Setters
    public Integer getProductoId() {
        return productoId;
    }
    
    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
     public String getActivo() {
        return activo;
    }
    
    public void setActivo(String activo) {
        this.activo = activo;
    }
    
    @Override
    public String toString() {
        return "Producto{" +
                "productoId=" + productoId +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}