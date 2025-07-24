package com.mycompany.bbadmintipificacion.service;

import com.mycompany.bbadmintipificacion.entity.Campana;
import com.mycompany.bbadmintipificacion.repository.CampanaRepository;
import com.mycompany.bbadmintipificacion.repository.CampanaProductoRepository;
import com.mycompany.bbadmintipificacion.repository.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para manejo de campañas
 * Compatible con esquema: bb_campanas, bb_productos_lov, bb_campanas_producto
 */
@Service
@Transactional
public class CampanaService {
    
    private static final Logger logger = LoggerFactory.getLogger(CampanaService.class);
    
    @Autowired
    private CampanaRepository campanaRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private CampanaProductoRepository campanaProductoRepository;
    
    /**
     * Crear una nueva campaña - Versión simplificada para debugging
     */
    public Campana crearCampana(String nombre) {
        logger.info("=== INICIANDO CREACIÓN DE CAMPAÑA ===");
        logger.info("Nombre recibido: '{}'", nombre);
        
        try {
            // Validar que el nombre no esté vacío
            if (nombre == null || nombre.trim().isEmpty()) {
                logger.warn("Nombre de campaña vacío o nulo");
                throw new IllegalArgumentException("El nombre de la campaña no puede estar vacío");
            }
            
            String nombreLimpio = nombre.trim();
            logger.info("Nombre después de trim: '{}'", nombreLimpio);
            
            // Verificar si la campaña ya existe
            logger.info("Verificando si la campaña ya existe...");
            boolean existe = campanaRepository.existsByNombreIgnoreCase(nombreLimpio);
            logger.info("¿Existe campaña con ese nombre?: {}", existe);
            
            if (existe) {
                logger.warn("La campaña '{}' ya existe", nombreLimpio);
                throw new IllegalArgumentException("Ya existe una campaña con ese nombre");
            }
            
            // Crear nueva campaña
            logger.info("Creando nueva instancia de Campana...");
            Campana nuevaCampana = new Campana();
            nuevaCampana.setNombre(nombreLimpio);
            nuevaCampana.setActivo("1"); // String "1" para activa
            
            logger.info("Campaña antes de guardar: {}", nuevaCampana);
            
            // Guardar en base de datos
            logger.info("Intentando guardar en base de datos...");
            Campana campanaGuardada = campanaRepository.save(nuevaCampana);
            
            logger.info("Campaña guardada exitosamente: {}", campanaGuardada);
            logger.info("=== CREACIÓN DE CAMPAÑA EXITOSA ===");
            
            return campanaGuardada;
            
        } catch (Exception e) {
            logger.error("=== ERROR EN CREACIÓN DE CAMPAÑA ===");
            logger.error("Tipo de error: {}", e.getClass().getSimpleName());
            logger.error("Mensaje de error: {}", e.getMessage());
            logger.error("Stack trace completo:", e);
            
            // Re-lanzar la excepción para que Spring maneje el rollback
            throw new RuntimeException("Error al crear campaña: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtener todas las campañas ordenadas por ID descendente
     */
    public List<Campana> obtenerTodasLasCampanas() {
        try {
            List<Campana> campanas = campanaRepository.findAllOrderByCampanaIdDesc();
            logger.debug("Obtenidas {} campañas", campanas.size());
            return campanas;
        } catch (Exception e) {
            logger.error("Error al obtener todas las campañas: {}", e.getMessage(), e);
            return List.of(); // Lista vacía en caso de error
        }
    }
    
    /**
     * Obtener campañas activas solamente
     */
    public List<Campana> obtenerCampanasActivas() {
        try {
            List<Campana> campanasActivas = campanaRepository.findByActivoTrue();
            logger.debug("Obtenidas {} campañas activas", campanasActivas.size());
            return campanasActivas;
        } catch (Exception e) {
            logger.error("Error al obtener campañas activas: {}", e.getMessage(), e);
            return List.of();
        }
    }
    
    /**
     * Buscar campaña por ID
     */
    public Optional<Campana> buscarPorId(Integer id) { // Cambiado de Long a Integer
        try {
            if (id == null || id <= 0) {
                logger.warn("ID de campaña inválido: {}", id);
                return Optional.empty();
            }
            
            Optional<Campana> campana = campanaRepository.findById(id);
            if (campana.isPresent()) {
                logger.debug("Campaña encontrada: {} (ID: {})", campana.get().getNombre(), id);
            } else {
                logger.debug("Campaña no encontrada con ID: {}", id);
            }
            return campana;
            
        } catch (Exception e) {
            logger.error("Error al buscar campaña por ID {}: {}", id, e.getMessage(), e);
            return Optional.empty();
        }
    }
    
    /**
     * Buscar campañas por nombre (búsqueda parcial)
     */
    public List<Campana> buscarPorNombre(String nombre) {
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                return obtenerTodasLasCampanas();
            }
            
            List<Campana> campanas = campanaRepository.findByNombreContainingIgnoreCase(nombre.trim());
            logger.debug("Encontradas {} campañas con nombre similar a: {}", campanas.size(), nombre);
            return campanas;
            
        } catch (Exception e) {
            logger.error("Error al buscar campañas por nombre {}: {}", nombre, e.getMessage(), e);
            return List.of();
        }
    }
    
    /**
     * Obtener número de productos asignados a una campaña específica
     */
    public long obtenerNumeroProductosPorCampana(Integer campanaId) { // Cambiado de Long a Integer
        try {
            if (campanaId == null || campanaId <= 0) {
                logger.warn("ID de campaña inválido para contar productos: {}", campanaId);
                return 0;
            }
            
            long numeroProductos = campanaProductoRepository.countProductosByCampanaId(campanaId); // Ya no necesita conversión
            logger.debug("Campaña {} tiene {} productos asignados", campanaId, numeroProductos);
            return numeroProductos;
            
        } catch (Exception e) {
            logger.error("Error al obtener productos de campaña {}: {}", campanaId, e.getMessage(), e);
            return 0;
        }
    }
    
    /**
     * Obtener número de productos activos asignados a una campaña
     */
    public long obtenerNumeroProductosActivosPorCampana(Integer campanaId) { // Cambiado de Long a Integer
        try {
            if (campanaId == null || campanaId <= 0) {
                return 0;
            }
            
            long productosActivos = campanaProductoRepository.countProductosActivosByCampanaId(campanaId); // Ya no necesita conversión
            logger.debug("Campaña {} tiene {} productos activos", campanaId, productosActivos);
            return productosActivos;
            
        } catch (Exception e) {
            logger.error("Error al obtener productos activos de campaña {}: {}", campanaId, e.getMessage(), e);
            return 0;
        }
    }
    
    /**
     * Activar una campaña (cambiar estado a activo)
     */
    public boolean activarCampana(Integer id) { // Cambiado de Long a Integer
        try {
            Optional<Campana> campanaOpt = buscarPorId(id);
            if (campanaOpt.isPresent()) {
                Campana campana = campanaOpt.get();
                campana.setActivo("1"); // String "1" para activa
                campanaRepository.save(campana);
                logger.info("Campaña activada: {} (ID: {})", campana.getNombre(), id);
                return true;
            } else {
                logger.warn("No se encontró campaña con ID {} para activar", id);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error al activar campaña {}: {}", id, e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Desactivar una campaña (cambiar estado a inactivo)
     */
    public boolean desactivarCampana(Integer id) { // Cambiado de Long a Integer
        try {
            Optional<Campana> campanaOpt = buscarPorId(id);
            if (campanaOpt.isPresent()) {
                Campana campana = campanaOpt.get();
                campana.setActivo("0"); // String "0" para inactiva
                campanaRepository.save(campana);
                logger.info("Campaña desactivada: {} (ID: {})", campana.getNombre(), id);
                return true;
            } else {
                logger.warn("No se encontró campaña con ID {} para desactivar", id);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error al desactivar campaña {}: {}", id, e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Actualizar nombre de una campaña
     */
    public boolean actualizarNombreCampana(Integer id, String nuevoNombre) { // Cambiado de Long a Integer
        try {
            if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
                logger.warn("Intento de actualizar campaña {} con nombre vacío", id);
                return false;
            }
            
            Optional<Campana> campanaOpt = buscarPorId(id);
            if (campanaOpt.isPresent()) {
                Campana campana = campanaOpt.get();
                String nombreAnterior = campana.getNombre();
                campana.setNombre(nuevoNombre.trim());
                campanaRepository.save(campana);
                logger.info("Campaña actualizada: '{}' → '{}' (ID: {})", nombreAnterior, nuevoNombre, id);
                return true;
            } else {
                logger.warn("No se encontró campaña con ID {} para actualizar", id);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error al actualizar campaña {}: {}", id, e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Eliminar campaña completamente (cuidado: esto es permanente)
     */
    public boolean eliminarCampana(Integer id) { // Cambiado de Long a Integer
        try {
            Optional<Campana> campanaOpt = buscarPorId(id);
            if (campanaOpt.isPresent()) {
                Campana campana = campanaOpt.get();
                String nombreCampana = campana.getNombre();
                
                // Verificar si tiene productos asignados
                long productosAsignados = obtenerNumeroProductosPorCampana(id);
                if (productosAsignados > 0) {
                    logger.warn("No se puede eliminar campaña {} porque tiene {} productos asignados", 
                              nombreCampana, productosAsignados);
                    return false;
                }
                
                campanaRepository.delete(campana);
                logger.info("Campaña eliminada: {} (ID: {})", nombreCampana, id);
                return true;
            } else {
                logger.warn("No se encontró campaña con ID {} para eliminar", id);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error al eliminar campaña {}: {}", id, e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Obtener estadísticas generales del sistema
     */
    public EstadisticasCampana obtenerEstadisticas() {
        try {
            long totalCampanas = campanaRepository.countTotalCampanas();
            long campanasActivas = campanaRepository.countCampanasActivas();
            long totalProductos = productoRepository.countTotalProductos();
            
            EstadisticasCampana stats = new EstadisticasCampana(totalCampanas, campanasActivas, totalProductos);
            logger.debug("Estadísticas obtenidas: {} campañas totales, {} activas, {} productos", 
                        totalCampanas, campanasActivas, totalProductos);
            return stats;
            
        } catch (Exception e) {
            logger.error("Error al obtener estadísticas: {}", e.getMessage(), e);
            return new EstadisticasCampana(0, 0, 0);
        }
    }
    
    /**
     * Verificar si existe una campaña con un nombre específico
     */
    public boolean existeCampanaConNombre(String nombre) {
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                return false;
            }
            return campanaRepository.existsByNombreIgnoreCase(nombre.trim());
        } catch (Exception e) {
            logger.error("Error al verificar existencia de campaña con nombre {}: {}", nombre, e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Clase interna para manejar estadísticas de campañas
     */
    public static class EstadisticasCampana {
        private final long totalCampanas;
        private final long campanasActivas;
        private final long totalProductos;
        
        public EstadisticasCampana(long totalCampanas, long campanasActivas, long totalProductos) {
            this.totalCampanas = totalCampanas;
            this.campanasActivas = campanasActivas;
            this.totalProductos = totalProductos;
        }
        
        // Getters
        public long getTotalCampanas() { 
            return totalCampanas; 
        }
        
        public long getCampanasActivas() { 
            return campanasActivas; 
        }
        
        public long getTotalProductos() { 
            return totalProductos; 
        }
        
        public long getCampanasInactivas() { 
            return totalCampanas - campanasActivas; 
        }
        
        @Override
        public String toString() {
            return String.format("EstadisticasCampana{totalCampanas=%d, activas=%d, inactivas=%d, totalProductos=%d}", 
                               totalCampanas, campanasActivas, getCampanasInactivas(), totalProductos);
        }
    }
}