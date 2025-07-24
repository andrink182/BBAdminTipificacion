package com.mycompany.bbadmintipificacion.repository;

import com.mycompany.bbadmintipificacion.entity.Campana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para la entidad Campaña
 * Compatible con el esquema: CampanaId (INT), Nombre (VARCHAR), Activo (VARCHAR(1))
 */
@Repository
public interface CampanaRepository extends JpaRepository<Campana, Integer> {
    
    /**
     * Obtener todas las campañas activas (Activo = '1')
     */
    @Query("SELECT c FROM Campana c WHERE c.activo = '1' ORDER BY c.campanaId DESC")
    List<Campana> findByActivoTrue();
    
    /**
     * Contar campañas activas (Activo = '1')
     */
    @Query("SELECT COUNT(c) FROM Campana c WHERE c.activo = '1'")
    long countCampanasActivas();
    
    /**
     * Contar todas las campañas
     */
    @Query("SELECT COUNT(c) FROM Campana c")
    long countTotalCampanas();
    
    /**
     * Buscar campañas por nombre (like)
     */
    @Query("SELECT c FROM Campana c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) ORDER BY c.campanaId DESC")
    List<Campana> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);
    
    /**
     * Obtener todas las campañas ordenadas por ID descendente
     */
    @Query("SELECT c FROM Campana c ORDER BY c.campanaId DESC")
    List<Campana> findAllOrderByCampanaIdDesc();
    
    /**
     * Verificar si existe una campaña con ese nombre
     */
    @Query("SELECT COUNT(c) > 0 FROM Campana c WHERE LOWER(c.nombre) = LOWER(:nombre)")
    boolean existsByNombreIgnoreCase(@Param("nombre") String nombre);
    
    /**
     * Buscar campaña por nombre exacto
     */
    @Query("SELECT c FROM Campana c WHERE LOWER(c.nombre) = LOWER(:nombre)")
    Campana findByNombreIgnoreCase(@Param("nombre") String nombre);
}