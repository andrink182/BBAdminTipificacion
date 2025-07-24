package com.mycompany.bbadmintipificacion.controller;

import com.mycompany.bbadmintipificacion.entity.Usuario;
import com.mycompany.bbadmintipificacion.entity.Campana;
import com.mycompany.bbadmintipificacion.service.UsuarioService;
import com.mycompany.bbadmintipificacion.service.CampanaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 * Controlador de autenticación para el sistema Administrador de Tipificaciones
 */
@Controller
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private CampanaService campanaService;
    
    /**
     * Página principal - redirige al login si no está autenticado
     */
    @GetMapping("/")
    public String index(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            logger.info("Usuario ya autenticado, redirigiendo a dashboard: {}", usuario.getUsername());
            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }
    
    /**
     * Mostrar página de login
     */
    @GetMapping("/login")
    public String mostrarLogin(HttpSession session, Model model) {
        // Si ya está logueado, redirigir al dashboard
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            return "redirect:/dashboard";
        }
        
        logger.debug("Mostrando página de login");
        return "login";
    }
    
    /**
     * Procesar login
     */
    @PostMapping("/login")
    public String procesarLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        
        logger.info("Intento de login para usuario: {}", username);
        
        try {
            // Validar campos vacíos
            if (username == null || username.trim().isEmpty() || 
                password == null || password.trim().isEmpty()) {
                
                logger.warn("Intento de login con campos vacíos");
                redirectAttributes.addFlashAttribute("error", "Por favor complete todos los campos");
                return "redirect:/login";
            }
            
            // Autenticar usuario
            Usuario usuario = usuarioService.authenticate(username.trim(), password);
            
            if (usuario != null) {
                // Login exitoso
                session.setAttribute("usuario", usuario);
                logger.info("Login exitoso para usuario: {} (ID: {})", usuario.getUsername(), usuario.getId());
                
                return "redirect:/dashboard";
            } else {
                // Login fallido
                logger.warn("Login fallido para usuario: {}", username);
                redirectAttributes.addFlashAttribute("error", "Usuario o contraseña incorrectos");
                return "redirect:/login";
            }
            
        } catch (Exception e) {
            logger.error("Error durante el proceso de login para usuario {}: {}", username, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error del sistema. Intente nuevamente");
            return "redirect:/login";
        }
    }
    
    /**
     * Dashboard principal (antes welcome)
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // Verificar autenticación
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            logger.warn("Intento de acceso a dashboard sin autenticación");
            return "redirect:/login";
        }
        
        try {
            // Obtener estadísticas y campañas
            CampanaService.EstadisticasCampana estadisticas = campanaService.obtenerEstadisticas();
            List<Campana> campanas = campanaService.obtenerTodasLasCampanas();
            
            // Agregar información al modelo
            model.addAttribute("usuario", usuario);
            model.addAttribute("estadisticas", estadisticas);
            model.addAttribute("campanas", campanas);
            model.addAttribute("campanaService", campanaService); // Para obtener productos por campaña
            
            logger.debug("Mostrando dashboard para usuario: {}", usuario.getUsername());
            return "dashboard"; // El archivo se puede renombrar a dashboard.html si se prefiere
            
        } catch (Exception e) {
            logger.error("Error al cargar dashboard para usuario {}: {}", usuario.getUsername(), e.getMessage(), e);
            model.addAttribute("error", "Error al cargar el dashboard");
            return "dashboard";
        }
    }
    
    /**
     * Cerrar sesión
     */
    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        
        if (usuario != null) {
            logger.info("Usuario cerrando sesión: {}", usuario.getUsername());
        }
        
        // Invalidar sesión
        session.invalidate();
        
        redirectAttributes.addFlashAttribute("message", "Sesión cerrada exitosamente");
        logger.info("Sesión cerrada exitosamente");
        
        return "redirect:/login";
    }
    
    /**
     * Crear nueva campaña
     */
    @PostMapping("/api/crear-campana")
    @ResponseBody
    public String crearCampana(@RequestParam String nombre, HttpSession session) {
        logger.info("=== API CREAR CAMPAÑA INICIADA ===");
        logger.info("Parámetro nombre recibido: '{}'", nombre);
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            logger.warn("Usuario no autenticado intentando crear campaña");
            return "{\"error\": \"No autenticado\"}";
        }
        
        logger.info("Usuario autenticado: {}", usuario.getUsername());
        
        try {
            logger.info("Llamando a campanaService.crearCampana...");
            Campana nuevaCampana = campanaService.crearCampana(nombre);
            
            if (nuevaCampana != null) {
                logger.info("Campaña creada exitosamente en controller: {}", nuevaCampana);
                return "{\"success\": true, \"mensaje\": \"Campaña creada exitosamente\", \"id\": " + nuevaCampana.getCampanaId() + "}";
            } else {
                logger.error("CampanaService retornó null");
                return "{\"error\": \"No se pudo crear la campaña\"}";
            }
        } catch (Exception e) {
            logger.error("=== ERROR EN CONTROLLER AL CREAR CAMPAÑA ===");
            logger.error("Tipo de error: {}", e.getClass().getSimpleName());
            logger.error("Mensaje: {}", e.getMessage());
            logger.error("Stack trace:", e);
            return "{\"error\": \"" + e.getMessage().replace("\"", "'") + "\"}";
        }
    }
    
    /**
     * API endpoint para obtener información del usuario actual (para AJAX)
     */
    @GetMapping("/api/usuario-actual")
    @ResponseBody
    public String usuarioActual(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "{\"error\": \"No autenticado\"}";
        }
        
        return "{"
                + "\"id\": " + usuario.getId() + ","
                + "\"username\": \"" + usuario.getUsername() + "\","
                + "\"nombre\": \"" + usuario.getNombre() + "\","
                + "\"activo\": " + usuario.getActivo() + ","
                + "\"rol\": \"" + usuario.getRol() + "\""
                + "}";
    }
    
    /**
     * Verificar estado de la sesión (para AJAX)
     */
    @GetMapping("/api/verificar-sesion")
    @ResponseBody
    public String verificarSesion(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        return usuario != null ? "activa" : "inactiva";
    }
    
    /**
     * Página de acceso denegado (para futuras implementaciones)
     */
    @GetMapping("/acceso-denegado")
    public String accesoDenegado(Model model) {
        logger.warn("Acceso denegado");
        model.addAttribute("error", "No tiene permisos para acceder a esta página");
        return "error";
    }
    
    /**
     * Manejo de errores 404
     */
    @RequestMapping("/error/404")
    public String error404(Model model) {
        model.addAttribute("error", "Página no encontrada");
        return "error";
    }
    
    /**
     * Manejo de errores 500
     */
    @RequestMapping("/error/500")
    public String error500(Model model) {
        model.addAttribute("error", "Error interno del servidor");
        return "error";
    }
    
}