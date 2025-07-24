package com.mycompany.bbadmintipificacion.controller;

import com.mycompany.bbadmintipificacion.entity.Usuario;
import com.mycompany.bbadmintipificacion.entity.Campana;
import com.mycompany.bbadmintipificacion.service.CampanaService;
import com.mycompany.bbadmintipificacion.service.ProductoCampanaService;
import com.mycompany.bbadmintipificacion.dto.ProductoCampanaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador para la gestión de campañas
 */
@Controller
@RequestMapping("/campanas")
public class CampanaController {
    
    private static final Logger logger = LoggerFactory.getLogger(CampanaController.class);
    
    @Autowired
    private CampanaService campanaService;
    
    @Autowired
    private ProductoCampanaService productoCampanaService;
    
     
    
    /**
     * Visualizar una campaña (solo lectura)
     */
    @GetMapping("/visualizar/{id}")
    public String visualizarCampana(@PathVariable Integer id, 
                                   HttpSession session, 
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        
        // Verificar autenticación
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            logger.warn("Intento de acceso sin autenticación a visualizar campaña");
            return "redirect:/login";
        }
        
        try {
            // Obtener la campaña usando buscarPorId
            Optional<Campana> campanaOpt = campanaService.buscarPorId(id);
            if (!campanaOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Campaña no encontrada");
                return "redirect:/dashboard";
            }
            
            Campana campana = campanaOpt.get();
            
            // Obtener productos asociados a la campaña
            List<ProductoCampanaDTO> productos = productoCampanaService.obtenerProductosPorCampana(id);
            
            // Log para depuración
            logger.info("Productos encontrados para edición: {}", productos.size());
            if (productos.isEmpty()) {
                logger.warn("No se encontraron productos para la campaña {}", id);
            }
            
            // Agregar datos al modelo
            model.addAttribute("usuario", usuario);
            model.addAttribute("campana", campana);
            model.addAttribute("productos", productos);
            
            logger.info("Visualizando campaña: {} (ID: {})", campana.getNombre(), id);
            
            return "visualizar-campana";
            
        } catch (Exception e) {
            logger.error("Error al visualizar campaña {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error al cargar la campaña");
            return "redirect:/dashboard";
        }
    }
    
    /**
     * Mostrar formulario de edición de campaña
     */
    @GetMapping("/editar/{id}")
    public String editarCampana(@PathVariable Integer id, 
                               HttpSession session, 
                               Model model,
                               RedirectAttributes redirectAttributes) {
        
        // Verificar autenticación
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            logger.warn("Intento de acceso sin autenticación a editar campaña");
            return "redirect:/login";
        }
        
        try {
            // Obtener la campaña usando buscarPorId
            Optional<Campana> campanaOpt = campanaService.buscarPorId(id);
            if (!campanaOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Campaña no encontrada");
                return "redirect:/dashboard";
            }
            
            Campana campana = campanaOpt.get();
            
            // Obtener productos asociados a la campaña
            List<ProductoCampanaDTO> productos = productoCampanaService.obtenerProductosPorCampana(id);
            
            // Agregar datos al modelo
            model.addAttribute("usuario", usuario);
            model.addAttribute("campana", campana);
            model.addAttribute("productos", productos);
            
            logger.info("Editando campaña: {} (ID: {})", campana.getNombre(), id);
            
            return "editar-campana";
            
        } catch (Exception e) {
            logger.error("Error al cargar formulario de edición para campaña {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error al cargar la campaña");
            return "redirect:/dashboard";
        }
    }
    
    /**
     * Actualizar nombre de campaña (AJAX)
     */
    @PostMapping("/actualizar-nombre")
    @ResponseBody
    public String actualizarNombreCampana(@RequestParam Integer id,
                                         @RequestParam String nombre,
                                         HttpSession session) {
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "{\"error\": \"No autenticado\"}";
        }
        
        try {
            boolean actualizado = campanaService.actualizarNombreCampana(id, nombre);
            if (actualizado) {
                logger.info("Nombre de campaña actualizado: {} -> {}", id, nombre);
                return "{\"success\": true, \"mensaje\": \"Nombre actualizado exitosamente\"}";
            } else {
                return "{\"error\": \"No se pudo actualizar el nombre\"}";
            }
        } catch (Exception e) {
            logger.error("Error al actualizar nombre de campaña: {}", e.getMessage(), e);
            return "{\"error\": \"" + e.getMessage().replace("\"", "'") + "\"}";
        }
    }
    
    /**
     * Desactivar productos seleccionados (AJAX)
     */
    @PostMapping("/desactivar-productos")
    @ResponseBody
    public String desactivarProductos(@RequestParam Integer campanaId,
                                     @RequestParam List<Integer> productIds,
                                     HttpSession session) {
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "{\"error\": \"No autenticado\"}";
        }
        
        try {
            int desactivados = productoCampanaService.desactivarProductos(campanaId, productIds);
            logger.info("Productos desactivados: {} de campaña {}", desactivados, campanaId);
            return "{\"success\": true, \"mensaje\": \"" + desactivados + " productos desactivados\"}";
        } catch (Exception e) {
            logger.error("Error al desactivar productos: {}", e.getMessage(), e);
            return "{\"error\": \"" + e.getMessage().replace("\"", "'") + "\"}";
        }
    }
    
    /**
     * Activar campaña (AJAX)
     */
    @PostMapping("/activar/{id}")
    @ResponseBody
    public String activarCampana(@PathVariable Integer id, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "{\"error\": \"No autenticado\"}";
        }
        
        try {
            boolean activada = campanaService.activarCampana(id);
            if (activada) {
                logger.info("Campaña activada: {}", id);
                return "{\"success\": true, \"mensaje\": \"Campaña activada exitosamente\"}";
            } else {
                return "{\"error\": \"No se pudo activar la campaña\"}";
            }
        } catch (Exception e) {
            logger.error("Error al activar campaña: {}", e.getMessage(), e);
            return "{\"error\": \"" + e.getMessage().replace("\"", "'") + "\"}";
        }
    }
    
    /**
     * Desactivar campaña (AJAX)
     */
    @PostMapping("/desactivar/{id}")
    @ResponseBody
    public String desactivarCampana(@PathVariable Integer id, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "{\"error\": \"No autenticado\"}";
        }
        
        try {
            boolean desactivada = campanaService.desactivarCampana(id);
            if (desactivada) {
                logger.info("Campaña desactivada: {}", id);
                return "{\"success\": true, \"mensaje\": \"Campaña desactivada exitosamente\"}";
            } else {
                return "{\"error\": \"No se pudo desactivar la campaña\"}";
            }
        } catch (Exception e) {
            logger.error("Error al desactivar campaña: {}", e.getMessage(), e);
            return "{\"error\": \"" + e.getMessage().replace("\"", "'") + "\"}";
        }
    }
    
    /**
     * Actualizar producto, servicio y subservicio (AJAX)
     */
    @PostMapping("/actualizar-producto-servicio")
    @ResponseBody
    public String actualizarProductoServicio(@RequestBody Map<String, Object> datos,
                                           HttpSession session) {
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "{\"error\": \"No autenticado\"}";
        }
        
        try {
            Integer productoId = Integer.valueOf(datos.get("productoId").toString());
            Integer servicioId = Integer.valueOf(datos.get("servicioId").toString());
            Integer subservicioId = Integer.valueOf(datos.get("subservicioId").toString());
            String nombreProducto = datos.get("nombreProducto").toString();
            String nombreServicio = datos.get("nombreServicio").toString();
            String nombreSubservicio = datos.get("nombreSubservicio").toString();
            
            boolean actualizado = productoCampanaService.actualizarProductoServicioSubservicio(
                productoId, servicioId, subservicioId,
                nombreProducto, nombreServicio, nombreSubservicio
            );
            
            if (actualizado) {
                logger.info("Producto/Servicio/Subservicio actualizado exitosamente");
                return "{\"success\": true, \"mensaje\": \"Actualización exitosa\"}";
            } else {
                return "{\"error\": \"No se pudo actualizar\"}";
            }
        } catch (Exception e) {
            logger.error("Error al actualizar: {}", e.getMessage(), e);
            return "{\"error\": \"" + e.getMessage().replace("\"", "'") + "\"}";
        }
    }
    
    
      /**
     * Crear nuevo  producto (AJAX)
     */
    @PostMapping("/crear-producto")
    @ResponseBody
    public String crearProducto(@RequestBody Map<String, Object> datos,
                               HttpSession session) {
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "{\"error\": \"No autenticado\"}";
        }
        
        try {
            
            String nombre = datos.get("nombre").toString();
            String descripcion = datos.getOrDefault("descripcion", "").toString();
            String campanaId = datos.get("campanaId").toString();
            Integer productoId = productoCampanaService.crearProducto(nombre, descripcion,campanaId);
            
            if (productoId != null) {
                logger.info("Producto creado exitosamente con ID: {}", productoId);
                return "{\"success\": true, \"mensaje\": \"Producto creado exitosamente\", \"id\": " + productoId + "}";
            } else {
                return "{\"error\": \"No se pudo crear el producto\"}";
            }
        } catch (Exception e) {
            logger.error("Error al crear producto: {}", e.getMessage(), e);
            return "{\"error\": \"" + e.getMessage().replace("\"", "'") + "\"}";
        }
    }
    
    /**
     * Crear nuevo servicio y asociarlo a producto (AJAX)
     */
    @PostMapping("/crear-servicio")
    @ResponseBody
    public String crearServicio(@RequestBody Map<String, Object> datos,
                               HttpSession session) {
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "{\"error\": \"No autenticado\"}";
        }
        
        try {
            Integer productoId = Integer.valueOf(datos.get("productoId").toString());
            String nombre = datos.get("nombre").toString();
            String descripcion = datos.getOrDefault("descripcion", "").toString();
            
            Integer servicioId = productoCampanaService.crearServicioYAsociar(productoId, nombre, descripcion);
            
            if (servicioId != null) {
                logger.info("Servicio creado exitosamente con ID: {}", servicioId);
                return "{\"success\": true, \"mensaje\": \"Servicio creado exitosamente\", \"id\": " + servicioId + "}";
            } else {
                return "{\"error\": \"No se pudo crear el servicio\"}";
            }
        } catch (Exception e) {
            logger.error("Error al crear servicio: {}", e.getMessage(), e);
            return "{\"error\": \"" + e.getMessage().replace("\"", "'") + "\"}";
        }
    }
    
    /**
     * Crear nuevo subservicio y asociarlo a servicio (AJAX)
     */
    @PostMapping("/crear-subservicio")
    @ResponseBody
    public String crearSubservicio(@RequestBody Map<String, Object> datos,
                                  HttpSession session) {
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "{\"error\": \"No autenticado\"}";
        }
        
        try {
            Integer servicioId = Integer.valueOf(datos.get("servicioId").toString());
            String nombre = datos.get("nombre").toString();
            String descripcion = datos.getOrDefault("descripcion", "").toString();
            
            Integer subservicioId = productoCampanaService.crearSubservicioYAsociar(servicioId, nombre, descripcion);
            
            if (subservicioId != null) {
                logger.info("Subservicio creado exitosamente con ID: {}", subservicioId);
                return "{\"success\": true, \"mensaje\": \"Subservicio creado exitosamente\", \"id\": " + subservicioId + "}";
            } else {
                return "{\"error\": \"No se pudo crear el subservicio\"}";
            }
        } catch (Exception e) {
            logger.error("Error al crear subservicio: {}", e.getMessage(), e);
            return "{\"error\": \"" + e.getMessage().replace("\"", "'") + "\"}";
        }
    }
    
    /**
     * Desactivar un producto específico (AJAX)
     */
    @PostMapping("/desactivar-producto/{id}")
    @ResponseBody
    public String desactivarProductoEspecifico(@PathVariable Integer id,
                                              HttpSession session) {
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "{\"error\": \"No autenticado\"}";
        }
        
        try {
            boolean desactivado = productoCampanaService.desactivarProducto(id);
            
            if (desactivado) {
                logger.info("Producto {} desactivado exitosamente", id);
                return "{\"success\": true, \"mensaje\": \"Producto desactivado exitosamente\"}";
            } else {
                return "{\"error\": \"No se pudo desactivar el producto\"}";
            }
        } catch (Exception e) {
            logger.error("Error al desactivar producto: {}", e.getMessage(), e);
            return "{\"error\": \"" + e.getMessage().replace("\"", "'") + "\"}";
        }
    }
}