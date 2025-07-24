package com.mycompany.bbadmintipificacion.repository;

import com.mycompany.bbadmintipificacion.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository para la entidad Producto
 * Compatible con el esquema: ProductoID (INT), Nombre (NVARCHAR), Descripcion (NVARCHAR), Activo (VARCHAR)
 * La relación con Campana se maneja a través de CampanaProducto
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    
    /**
     * Contar total de productos
     */
    @Query("SELECT COUNT(p) FROM Producto p")
    long countTotalProductos();
    
    /**
     * Contar productos activos (Activo = '1')
     */
    @Query("SELECT COUNT(p) FROM Producto p WHERE p.activo = '1'")
    long countProductosActivos();
    
    /**
     * Obtener productos por campaña ID
     * Modificado para usar JOIN con CampanaProducto
     */
    @Query("SELECT DISTINCT p FROM Producto p " +
           "INNER JOIN CampanaProducto cp ON p.productoId = cp.productoId " +
           "WHERE cp.campanaId = :campanaId " +
           "ORDER BY p.productoId DESC")
    List<Producto> findByCampanaId(@Param("campanaId") Integer campanaId);
    
    /**
     * Obtener productos activos por campaña ID
     * Modificado para verificar que la relación en CampanaProducto esté activa
     */
    @Query("SELECT DISTINCT p FROM Producto p " +
           "INNER JOIN CampanaProducto cp ON p.productoId = cp.productoId " +
           "WHERE cp.campanaId = :campanaId AND cp.activo = '1' " +
           "ORDER BY p.productoId DESC")
    List<Producto> findByCampanaIdAndActivo(@Param("campanaId") Integer campanaId);
    
    /**
     * Contar productos por campaña ID
     * NOTA: Esta consulta está comentada en el original, usando el countProductosByCampanaId del CampanaProductoRepository
     */
    @Query("SELECT COUNT(DISTINCT p) FROM Producto p " +
           "INNER JOIN CampanaProducto cp ON p.productoId = cp.productoId " +
           "WHERE cp.campanaId = :campanaId")
    long countByCampanaId(@Param("campanaId") Integer campanaId);
    
    /**
     * Contar productos activos por campaña ID
     * NOTA: Usar countProductosActivosByCampanaId del CampanaProductoRepository para mejor rendimiento
     */
    @Query("SELECT COUNT(DISTINCT p) FROM Producto p " +
           "INNER JOIN CampanaProducto cp ON p.productoId = cp.productoId " +
           "WHERE cp.campanaId = :campanaId AND cp.activo = '1'")
    long countByCampanaIdAndActivo(@Param("campanaId") Integer campanaId);
    
    /**
     * Obtener todos los productos activos
     */
    @Query("SELECT p FROM Producto p WHERE p.activo = '1' ORDER BY p.productoId DESC")
    List<Producto> findByActivoTrue();
    
    /**
     * Buscar productos por nombre
     */
    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) ORDER BY p.productoId DESC")
    List<Producto> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);
    
    /**
     * Verificar si existe un producto con ese nombre
     */
    @Query("SELECT COUNT(p) > 0 FROM Producto p WHERE LOWER(p.nombre) = LOWER(:nombre)")
    boolean existsByNombreIgnoreCase(@Param("nombre") String nombre);
}