package com.mycompany.bbadmintipificacion.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad Usuario que mapea con la tabla 'usuarios' existente
 * Esquema: id, username, password, nombre, fecha_creacion, ultimo_acceso, activo
 */
@Entity
@Table(name = "usuarios")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;
    
    @Column(name = "password", nullable = false, length = 255)
    private String password;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;
    
    @Column(name = "activo", nullable = false)
    private Boolean activo;
    
    // Constructor vacío requerido por JPA
    public Usuario() {
        this.fechaCreacion = LocalDateTime.now();
        this.activo = true;
    }
    
    // Constructor con parámetros
    public Usuario(String username, String password, String nombre) {
        this();
        this.username = username;
        this.password = password;
        this.nombre = nombre;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDateTime getUltimoAcceso() {
        return ultimoAcceso;
    }
    
    public void setUltimoAcceso(LocalDateTime ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }
    
    public Boolean getActivo() {
        return activo;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    
    // Métodos auxiliares para compatibilidad
    public String getNombreCompleto() {
        return this.nombre;
    }
    
    public String getEmail() {
        return this.username + "@company.com"; // Email simulado basado en username
    }
    
    public String getRol() {
        // Lógica simple de rol basada en username
        if ("admin".equalsIgnoreCase(this.username)) {
            return "ADMIN";
        }
        return "USER";
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nombre='" + nombre + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", ultimoAcceso=" + ultimoAcceso +
                ", activo=" + activo +
                '}';
    }
}