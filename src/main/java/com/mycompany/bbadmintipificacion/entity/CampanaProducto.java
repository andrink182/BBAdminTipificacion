package com.mycompany.bbadmintipificacion.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad CampanaProducto que mapea con la tabla 'bb_campanas_producto'
 * Esquema: CampanaId (INT), ProductoID (INT), fecha_asignacion, Activo (VARCHAR(1))
 */
@Entity
@Table(name = "bb_campanas_producto")
@IdClass(CampanaProductoId.class)
public class CampanaProducto {
    
    @Id
    @Column(name = "CampanaId")
    private Integer campanaId;
    
    @Id
    @Column(name = "ProductoID")
    private Integer productoId;
    
    @Column(name = "fecha_asignacion")
    private LocalDateTime fechaAsignacion;
    
    @Column(name = "Activo", length = 1)
    private String activo; // Cambiado de Integer a String para coincidir con VARCHAR(1)
    
    // Constructor vacío requerido por JPA
    public CampanaProducto() {
        this.activo = "1"; // Por defecto activo (String)
        this.fechaAsignacion = LocalDateTime.now();
    }
    
    // Constructor con parámetros
    public CampanaProducto(Integer campanaId, Integer productoId) {
        this();
        this.campanaId = campanaId;
        this.productoId = productoId;
    }
    
    // Getters y Setters
    public Integer getCampanaId() {
        return campanaId;
    }
    
    public void setCampanaId(Integer campanaId) {
        this.campanaId = campanaId;
    }
    
    public Integer getProductoId() {
        return productoId;
    }
    
    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }
    
    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }
    
    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }
    
    public String getActivo() {
        return activo;
    }
    
    public void setActivo(String activo) {
        this.activo = activo;
    }
    
    // Métodos auxiliares para trabajar con String
    public boolean isActivo() {
        return "1".equals(this.activo);
    }
    
    public void setActivoBoolean(boolean activo) {
        this.activo = activo ? "1" : "0";
    }
    
    @Override
    public String toString() {
        return "CampanaProducto{" +
                "campanaId=" + campanaId +
                ", productoId=" + productoId +
                ", fechaAsignacion=" + fechaAsignacion +
                ", activo='" + activo + '\'' +
                '}';
    }
}