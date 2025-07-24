package com.mycompany.bbadmintipificacion.repository;

import com.mycompany.bbadmintipificacion.entity.CampanaProducto;
import com.mycompany.bbadmintipificacion.entity.CampanaProductoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository para la entidad CampanaProducto
 * Compatible con el esquema: CampanaId (INT), ProductoID (INT), fecha_asignacion, Activo (VARCHAR(1))
 */
@Repository
public interface CampanaProductoRepository extends JpaRepository<CampanaProducto, CampanaProductoId> {
    
    /**
     * Contar productos por campaña ID
     */
    @Query("SELECT COUNT(cp) FROM CampanaProducto cp WHERE cp.campanaId = :campanaId")
    long countProductosByCampanaId(@Param("campanaId") Integer campanaId);
    
    /**
     * Contar productos activos por campaña ID (Activo = '1')
     */
    @Query("SELECT COUNT(cp) FROM CampanaProducto cp WHERE cp.campanaId = :campanaId AND cp.activo = '1'")
    long countProductosActivosByCampanaId(@Param("campanaId") Integer campanaId); // Cambiado a String '1'
}