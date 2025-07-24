package com.mycompany.bbadmintipificacion.entity;

import jakarta.persistence.*;

/**
 * Entidad Campaña que mapea con la tabla 'bb_campanas'
 * Esquema: CampanaId (INT IDENTITY), Nombre, Activo
 */
@Entity
@Table(name = "bb_campanas")
public class Campana {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CampanaId")
    private Integer campanaId; // Cambiado de Long a Integer para coincidir con INT en BD
    
    @Column(name = "Nombre", nullable = false, length = 255)
    private String nombre;
    
    @Column(name = "Activo", nullable = false)
    private String activo;
    
    // Constructor vacío requerido por JPA
    public Campana() {
        this.activo = "1"; // Por defecto activa
    }
    
    // Constructor con parámetros
    public Campana(String nombre) {
        this();
        this.nombre = nombre;
    }
    
    // Getters y Setters
    public Integer getCampanaId() {
        return campanaId;
    }
    
    public void setCampanaId(Integer campanaId) {
        this.campanaId = campanaId;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getActivo() {
        return activo;
    }
    
    public void setActivo(String activo) {
        this.activo = activo;
    }
    
    // Métodos auxiliares
    public boolean isActiva() {
        return this.activo != null && this.activo == "1";
    }
    
    public String getEstadoTexto() {
        return isActiva() ? "Activo" : "Inactivo";
    }
    
    @Override
    public String toString() {
        return "Campana{" +
                "campanaId=" + campanaId +
                ", nombre='" + nombre + '\'' +
                ", activo=" + activo +
                '}';
    }
}