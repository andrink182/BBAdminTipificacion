package com.mycompany.bbadmintipificacion.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase para la clave compuesta de CampanaProducto
 * Representa la clave primaria compuesta de bb_campanas_producto
 */
public class CampanaProductoId implements Serializable {
    
    private Long campanaId;
    private Long productoId;
    
    // Constructor vacío requerido por JPA
    public CampanaProductoId() {}
    
    // Constructor con parámetros
    public CampanaProductoId(Long campanaId, Long productoId) {
        this.campanaId = campanaId;
        this.productoId = productoId;
    }
    
    // Getters y Setters
    public Long getCampanaId() {
        return campanaId;
    }
    
    public void setCampanaId(Long campanaId) {
        this.campanaId = campanaId;
    }
    
    public Long getProductoId() {
        return productoId;
    }
    
    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }
    
    // equals y hashCode son OBLIGATORIOS para claves compuestas
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CampanaProductoId that = (CampanaProductoId) o;
        return Objects.equals(campanaId, that.campanaId) &&
               Objects.equals(productoId, that.productoId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(campanaId, productoId);
    }
    
    @Override
    public String toString() {
        return "CampanaProductoId{" +
                "campanaId=" + campanaId +
                ", productoId=" + productoId +
                '}';
    }
}