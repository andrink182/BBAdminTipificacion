package com.mycompany.bbadmintipificacion.dto;

/**
 * DTO para representar la información completa de un producto
 * con sus servicios y subservicios asociados a una campaña
 */
public class ProductoCampanaDTO {
    
    private Integer productoId;
    private String nombreProducto;
    private String nombreServicio;
    private String nombreSubservicio;
    private String activo;
    private Integer servicioId;
    private Integer subservicioId;
    
    // Constructor vacío
    public ProductoCampanaDTO() {
    }
    
    // Constructor completo
    public ProductoCampanaDTO(Integer productoId, String nombreProducto, 
                             String nombreServicio, String nombreSubservicio, 
                             String activo) {
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.nombreServicio = nombreServicio;
        this.nombreSubservicio = nombreSubservicio;
        this.activo = activo;
    }
    
    // Constructor con IDs
    public ProductoCampanaDTO(Integer productoId, String nombreProducto, 
                             String nombreServicio, Integer servicioId,
                             String nombreSubservicio, Integer subservicioId, 
                             String activo) {
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.nombreServicio = nombreServicio;
        this.servicioId = servicioId;
        this.nombreSubservicio = nombreSubservicio;
        this.subservicioId = subservicioId;
        this.activo = activo;
    }
    
    // Getters y Setters
    public Integer getProductoId() {
        return productoId;
    }
    
    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }
    
    public String getNombreProducto() {
        return nombreProducto;
    }
    
    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }
    
    public String getNombreServicio() {
        return nombreServicio;
    }
    
    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }
    
    public String getNombreSubservicio() {
        return nombreSubservicio;
    }
    
    public void setNombreSubservicio(String nombreSubservicio) {
        this.nombreSubservicio = nombreSubservicio;
    }
    
    public String getActivo() {
        return activo;
    }
    
    public void setActivo(String activo) {
        this.activo = activo;
    }
    
    public Integer getServicioId() {
        return servicioId;
    }
    
    public void setServicioId(Integer servicioId) {
        this.servicioId = servicioId;
    }
    
    public Integer getSubservicioId() {
        return subservicioId;
    }
    
    public void setSubservicioId(Integer subservicioId) {
        this.subservicioId = subservicioId;
    }
    
    // Métodos helper
    public boolean isActivo() {
        return "1".equals(this.activo);
    }
    
    @Override
    public String toString() {
        return "ProductoCampanaDTO{" +
                "productoId=" + productoId +
                ", nombreProducto='" + nombreProducto + '\'' +
                ", nombreServicio='" + nombreServicio + '\'' +
                ", nombreSubservicio='" + nombreSubservicio + '\'' +
                ", activo='" + activo + '\'' +
                '}';
    }
}