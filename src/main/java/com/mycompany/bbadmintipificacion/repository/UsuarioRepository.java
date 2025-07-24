package com.mycompany.bbadmintipificacion.repository;

import com.mycompany.bbadmintipificacion.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para la entidad Usuario
 * Compatible con el esquema: id, username, password, nombre, fecha_creacion, ultimo_acceso, activo
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    /**
     * Buscar usuario por username (case insensitive)
     */
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.username) = LOWER(:username)")
    Optional<Usuario> findByUsernameIgnoreCase(@Param("username") String username);
    
    /**
     * Buscar usuario por username (case sensitive)
     */
    Optional<Usuario> findByUsername(String username);
    
    /**
     * Verificar si existe un usuario con ese username
     */
    @Query("SELECT COUNT(u) > 0 FROM Usuario u WHERE LOWER(u.username) = LOWER(:username)")
    boolean existsByUsernameIgnoreCase(@Param("username") String username);
    
    /**
     * Buscar usuarios activos
     */
    @Query("SELECT u FROM Usuario u WHERE u.activo = true ORDER BY u.fechaCreacion DESC")
    List<Usuario> findByActivoTrue();
    
    /**
     * Buscar usuarios inactivos
     */
    @Query("SELECT u FROM Usuario u WHERE u.activo = false ORDER BY u.fechaCreacion DESC")
    List<Usuario> findByActivoFalse();
    
    /**
     * Buscar por nombre (like)
     */
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Usuario> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);
    
    /**
     * Contar usuarios activos
     */
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.activo = true")
    long countActiveUsers();
    
    /**
     * Obtener los últimos usuarios creados
     */
    @Query("SELECT u FROM Usuario u ORDER BY u.fechaCreacion DESC")
    List<Usuario> findAllOrderByFechaCreacionDesc();
}