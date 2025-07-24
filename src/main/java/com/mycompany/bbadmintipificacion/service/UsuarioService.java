package com.mycompany.bbadmintipificacion.service;

import com.mycompany.bbadmintipificacion.entity.Usuario;
import com.mycompany.bbadmintipificacion.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para manejo de usuarios
 * Compatible con esquema: id, username, password, nombre, fecha_creacion, ultimo_acceso, activo
 */
@Service
@Transactional
public class UsuarioService {
    
    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    /**
     * Autenticar usuario usando BCrypt
     */
    public Usuario authenticate(String username, String password) {
        if (username == null || password == null) {
            logger.warn("Intento de autenticación con username o password nulo");
            return null;
        }
        
        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByUsernameIgnoreCase(username.trim());
            
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                
                // Verificar que el usuario esté activo
                if (!usuario.getActivo()) {
                    logger.warn("Intento de login con usuario inactivo: {}", username);
                    return null;
                }
                
                // Verificar password usando BCrypt
                if (passwordEncoder.matches(password, usuario.getPassword())) {
                    // Actualizar último acceso
                    usuario.setUltimoAcceso(LocalDateTime.now());
                    usuarioRepository.save(usuario);
                    
                    logger.info("Autenticación exitosa para usuario: {}", username);
                    return usuario;
                } else {
                    logger.warn("Password incorrecto para usuario: {}", username);
                }
            } else {
                logger.warn("Usuario no encontrado: {}", username);
            }
        } catch (Exception e) {
            logger.error("Error durante la autenticación del usuario {}: {}", username, e.getMessage(), e);
        }
        
        return null;
    }
    
    /**
     * Crear un nuevo usuario
     */
    public Usuario crearUsuario(String username, String password, String nombre) {
        try {
            // Verificar si el usuario ya existe
            if (usuarioRepository.existsByUsernameIgnoreCase(username)) {
                logger.warn("Intento de crear usuario que ya existe: {}", username);
                return null;
            }
            
            // Crear nuevo usuario
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setUsername(username.trim());
            nuevoUsuario.setPassword(passwordEncoder.encode(password));
            nuevoUsuario.setNombre(nombre.trim());
            nuevoUsuario.setFechaCreacion(LocalDateTime.now());
            nuevoUsuario.setActivo(true);
            
            Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
            logger.info("Usuario creado exitosamente: {} con nombre: {}", username, nombre);
            return usuarioGuardado;
            
        } catch (Exception e) {
            logger.error("Error al crear usuario {}: {}", username, e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Buscar usuario por ID
     */
    public Optional<Usuario> buscarPorId(Long id) {
        try {
            return usuarioRepository.findById(id);
        } catch (Exception e) {
            logger.error("Error al buscar usuario por ID {}: {}", id, e.getMessage(), e);
            return Optional.empty();
        }
    }
    
    /**
     * Buscar usuario por username
     */
    public Optional<Usuario> buscarPorUsername(String username) {
        try {
            return usuarioRepository.findByUsernameIgnoreCase(username);
        } catch (Exception e) {
            logger.error("Error al buscar usuario por username {}: {}", username, e.getMessage(), e);
            return Optional.empty();
        }
    }
    
    /**
     * Obtener todos los usuarios activos
     */
    public List<Usuario> obtenerUsuariosActivos() {
        try {
            return usuarioRepository.findByActivoTrue();
        } catch (Exception e) {
            logger.error("Error al obtener usuarios activos: {}", e.getMessage(), e);
            return List.of();
        }
    }
    
    /**
     * Desactivar usuario
     */
    public boolean desactivarUsuario(Long id) {
        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                usuario.setActivo(false);
                usuarioRepository.save(usuario);
                logger.info("Usuario desactivado: {}", usuario.getUsername());
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("Error al desactivar usuario {}: {}", id, e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Cambiar password de usuario
     */
    public boolean cambiarPassword(Long id, String nuevaPassword) {
        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                usuario.setPassword(passwordEncoder.encode(nuevaPassword));
                usuarioRepository.save(usuario);
                logger.info("Password cambiado para usuario: {}", usuario.getUsername());
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("Error al cambiar password del usuario {}: {}", id, e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Inicializar usuarios por defecto
     */
    public void inicializarUsuariosPorDefecto() {
        try {
            // Verificar si ya existen usuarios
            long cantidadUsuarios = usuarioRepository.count();
            if (cantidadUsuarios > 0) {
                logger.info("Ya existen {} usuarios en la base de datos. Saltando inicialización.", cantidadUsuarios);
                return;
            }
            
            // Crear usuarios por defecto
            crearUsuario("admin", "admin123", "Administrador del Sistema");
            crearUsuario("leslie", "leslie123", "Leslie Rivera");
            crearUsuario("test", "test123", "Usuario de Prueba");
            
            logger.info("✅ Usuarios por defecto inicializados correctamente");
            
        } catch (Exception e) {
            logger.error("Error al inicializar usuarios por defecto: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Obtener estadísticas de usuarios
     */
    public String obtenerEstadisticas() {
        try {
            long totalUsuarios = usuarioRepository.count();
            long usuariosActivos = usuarioRepository.countActiveUsers();
            
            return String.format("Total: %d | Activos: %d | Inactivos: %d", 
                    totalUsuarios, usuariosActivos, (totalUsuarios - usuariosActivos));
        } catch (Exception e) {
            logger.error("Error al obtener estadísticas: {}", e.getMessage(), e);
            return "Error al obtener estadísticas";
        }
    }
}