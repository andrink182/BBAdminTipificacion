package com.mycompany.bbadmintipificacion.service;

import com.mycompany.bbadmintipificacion.dto.ProductoCampanaDTO;
import com.mycompany.bbadmintipificacion.repository.CampanaProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

/**
 * Servicio para manejar productos asociados a campañas con sus servicios
 */
@Service
@Transactional
public class ProductoCampanaService {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductoCampanaService.class);
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private CampanaProductoRepository campanaProductoRepository;
    
    /**
     * Obtener productos con servicios y subservicios por campaña
     */
    public List<ProductoCampanaDTO> obtenerProductosPorCampana(Integer campanaId) {
        logger.info("Obteniendo productos para campaña ID: {}", campanaId);
        
        String sql = """
            SELECT p.ProductoID, p.Nombre as NombreProducto, 
                   sl.ServicioID, sl.Nombre as NombreServicio, 
                   ssl.SubservicioID, ssl.Nombre as NombreSubservicio, 
                   p.Activo
            FROM bb_productos_lov p
            LEFT JOIN bb_campanas_producto cp ON p.ProductoID = cp.ProductoID
            LEFT JOIN bb_productos_servicios ps ON p.ProductoID = ps.ProductoID
            LEFT JOIN bb_servicios_lov sl ON sl.ServicioID = ps.ServicioID
            LEFT JOIN bb_servicios_subservicios ss ON ps.ServicioID = ss.ServicioID
            LEFT JOIN bb_sub_servicios_lov ssl ON ss.SubservicioID = ssl.SubservicioID
            WHERE cp.CampanaId = ?
            ORDER BY p.Nombre
        """;
        
        try {
            List<ProductoCampanaDTO> productos = jdbcTemplate.query(sql, 
                new Object[]{campanaId},
                (rs, rowNum) -> new ProductoCampanaDTO(
                    rs.getInt("ProductoID"),
                    rs.getString("NombreProducto"),
                    rs.getString("NombreServicio"),
                    rs.getInt("ServicioID"),
                    rs.getString("NombreSubservicio"),
                    rs.getInt("SubservicioID"),
                    rs.getString("Activo")
                )
            );
            
            logger.info("Se encontraron {} productos para la campaña {}", productos.size(), campanaId);
            return productos;
            
        } catch (Exception e) {
            logger.error("Error al obtener productos de campaña {}: {}", campanaId, e.getMessage(), e);
            throw new RuntimeException("Error al obtener productos de la campaña", e);
        }
    }
    
    /**
     * Obtener productos activos con servicios y subservicios por campaña
     */
    public List<ProductoCampanaDTO> obtenerProductosActivosPorCampana(Integer campanaId) {
        logger.info("Obteniendo productos activos para campaña ID: {}", campanaId);
        
        String sql = """
            SELECT p.ProductoID, p.Nombre as NombreProducto, 
                   sl.ServicioID, sl.Nombre as NombreServicio, 
                   ssl.SubservicioID, ssl.Nombre as NombreSubservicio, 
                   p.Activo
            FROM bb_productos_lov p
            INNER JOIN bb_campanas_producto cp ON p.ProductoID = cp.ProductoID
            INNER JOIN bb_productos_servicios ps ON p.ProductoID = ps.ProductoID
            INNER JOIN bb_servicios_lov sl ON sl.ServicioID = ps.ServicioID
            INNER JOIN bb_servicios_subservicios ss ON ps.ServicioID = ss.ServicioID
            INNER JOIN bb_sub_servicios_lov ssl ON ss.SubservicioID = ssl.SubservicioID
            WHERE cp.CampanaId = ? AND cp.Activo = '1'
            ORDER BY p.Nombre
        """;
        
        try {
            return jdbcTemplate.query(sql, 
                new Object[]{campanaId},
                (rs, rowNum) -> new ProductoCampanaDTO(
                    rs.getInt("ProductoID"),
                    rs.getString("NombreProducto"),
                    rs.getString("NombreServicio"),
                    rs.getInt("ServicioID"),
                    rs.getString("NombreSubservicio"),
                    rs.getInt("SubservicioID"),
                    rs.getString("Activo")
                )
            );
        } catch (Exception e) {
            logger.error("Error al obtener productos activos de campaña {}: {}", campanaId, e.getMessage(), e);
            throw new RuntimeException("Error al obtener productos activos de la campaña", e);
        }
    }
    
    /**
     * Desactivar múltiples productos de una campaña
     */
    @Transactional
    public int desactivarProductos(Integer campanaId, List<Integer> productIds) {
        logger.info("Desactivando {} productos de la campaña {}", productIds.size(), campanaId);
        
        String sql = """
            UPDATE bb_campanas_producto 
            SET Activo = '0' 
            WHERE CampanaId = ? AND ProductoID = ?
        """;
        
        int totalDesactivados = 0;
        
        try {
            for (Integer productoId : productIds) {
                int affected = jdbcTemplate.update(sql, campanaId, productoId);
                totalDesactivados += affected;
                logger.debug("Producto {} desactivado en campaña {}", productoId, campanaId);
            }
            
            logger.info("Total de productos desactivados: {}", totalDesactivados);
            return totalDesactivados;
            
        } catch (Exception e) {
            logger.error("Error al desactivar productos: {}", e.getMessage(), e);
            throw new RuntimeException("Error al desactivar productos", e);
        }
    }
    
    /**
     * Activar un producto en una campaña
     */
    @Transactional
    public boolean activarProductoEnCampana(Integer campanaId, Integer productoId) {
        logger.info("Activando producto {} en campaña {}", productoId, campanaId);
        
        String sql = """
            UPDATE bb_campanas_producto 
            SET Activo = '1' 
            WHERE CampanaId = ? AND ProductoID = ?
        """;
        
        try {
            int affected = jdbcTemplate.update(sql, campanaId, productoId);
            return affected > 0;
        } catch (Exception e) {
            logger.error("Error al activar producto: {}", e.getMessage(), e);
            throw new RuntimeException("Error al activar producto", e);
        }
    }
    
    /**
     * Verificar si un producto existe en una campaña
     */
    public boolean existeProductoEnCampana(Integer campanaId, Integer productoId) {
        String sql = """
            SELECT COUNT(*) 
            FROM bb_campanas_producto 
            WHERE CampanaId = ? AND ProductoID = ?
        """;
        
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, campanaId, productoId);
            return count != null && count > 0;
        } catch (Exception e) {
            logger.error("Error al verificar producto en campaña: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Actualizar nombres de producto, servicio y subservicio
     */
    @Transactional
    public boolean actualizarProductoServicioSubservicio(Integer productoId, Integer servicioId, 
                                                        Integer subservicioId, String nombreProducto, 
                                                        String nombreServicio, String nombreSubservicio) {
        logger.info("Actualizando producto {}, servicio {}, subservicio {}", productoId, servicioId, subservicioId);
        
        try {
            // Actualizar producto
            String sqlProducto = "UPDATE bb_productos_lov SET Nombre = ? WHERE ProductoID = ?";
            jdbcTemplate.update(sqlProducto, nombreProducto, productoId);
            
            // Actualizar servicio
            String sqlServicio = "UPDATE bb_servicios_lov SET Nombre = ? WHERE ServicioID = ?";
            jdbcTemplate.update(sqlServicio, nombreServicio, servicioId);
            
            // Actualizar subservicio
            String sqlSubservicio = "UPDATE bb_sub_servicios_lov SET Nombre = ? WHERE SubservicioID = ?";
            jdbcTemplate.update(sqlSubservicio, nombreSubservicio, subservicioId);
            
            logger.info("Actualización exitosa de producto, servicio y subservicio");
            return true;
            
        } catch (Exception e) {
            logger.error("Error al actualizar: {}", e.getMessage(), e);
            throw new RuntimeException("Error al actualizar los datos", e);
        }
    }
    
    /**
     * Crear producto 
     */
    @Transactional
    public Integer crearProducto(String nombre, String descripcion, String campanaId){
          logger.info("Creando nuevo producto '{}'", nombre);
    try{
          String sqlInsertProducto = """
                INSERT INTO bb_productos_lov (Nombre, Descripcion, Activo)
                VALUES (?, ?, '1')
            """;
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sqlInsertProducto, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, nombre);
                ps.setString(2, descripcion != null ? descripcion : "");
                return ps;
            }, keyHolder);
            Integer productoId = keyHolder.getKey().intValue();
            logger.info("Producto creado con ID: {}", productoId);
            String sqlAsociarCP = """
                INSERT INTO bb_campanas_producto (CampanaId, ProductoID, Activo)
                VALUES (?, ?,'1')
            """;
            jdbcTemplate.update(sqlAsociarCP,campanaId,productoId);
        return productoId;
    }catch (Exception e) {
            logger.error("Error al crear producto: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear el producto", e);
        }
        
    
    }
    /**
     * Crear nuevo servicio y asociarlo a un producto
     */
    @Transactional
    public Integer crearServicioYAsociar(Integer productoId, String nombre, String descripcion) {
        logger.info("Creando nuevo servicio '{}' para producto {}", nombre, productoId);
        
        try {
            // Insertar nuevo servicio
            String sqlInsertServicio = """
                INSERT INTO bb_servicios_lov (Nombre, Descripcion, Activo)
                VALUES (?, ?, '1')
            """;
            
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sqlInsertServicio, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, nombre);
                ps.setString(2, descripcion != null ? descripcion : "");
                return ps;
            }, keyHolder);
            
            Integer servicioId = keyHolder.getKey().intValue();
            logger.info("Servicio creado con ID: {}", servicioId);
            
            // Asociar servicio al producto
            String sqlAsociar = """
                INSERT INTO bb_productos_servicios (ProductoID, ServicioID)
                VALUES (?, ?)
            """;
            jdbcTemplate.update(sqlAsociar, productoId, servicioId);
            
            logger.info("Servicio {} asociado exitosamente al producto {}", servicioId, productoId);
            return servicioId;
            
        } catch (Exception e) {
            logger.error("Error al crear servicio: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear el servicio", e);
        }
    }
    
    /**
     * Crear nuevo subservicio y asociarlo a un servicio
     */
    @Transactional
    public Integer crearSubservicioYAsociar(Integer servicioId, String nombre, String descripcion) {
        logger.info("Creando nuevo subservicio '{}' para servicio {}", nombre, servicioId);
        
        try {
            // Insertar nuevo subservicio
            String sqlInsertSubservicio = """
                INSERT INTO bb_sub_servicios_lov (Nombre, Descripcion, Activo)
                VALUES (?, ?, '1')
            """;
            
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sqlInsertSubservicio, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, nombre);
                ps.setString(2, descripcion != null ? descripcion : "");
                return ps;
            }, keyHolder);
            
            Integer subservicioId = keyHolder.getKey().intValue();
            logger.info("Subservicio creado con ID: {}", subservicioId);
            
            // Asociar subservicio al servicio
            String sqlAsociar = """
                INSERT INTO bb_servicios_subservicios (ServicioID, SubservicioID)
                VALUES (?, ?)
            """;
            jdbcTemplate.update(sqlAsociar, servicioId, subservicioId);
            
            logger.info("Subservicio {} asociado exitosamente al servicio {}", subservicioId, servicioId);
            return subservicioId;
            
        } catch (Exception e) {
            logger.error("Error al crear subservicio: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear el subservicio", e);
        }
    }
    
    /**
     * Desactivar un producto específico
     */
    @Transactional
    public boolean desactivarProducto(Integer productoId) {
        logger.info("Desactivando producto ID: {}", productoId);
        
        String sql = "UPDATE bb_productos_lov SET Activo = '0' WHERE ProductoID = ?";
        
        try {
            int affected = jdbcTemplate.update(sql, productoId);
            if (affected > 0) {
                logger.info("Producto {} desactivado exitosamente", productoId);
                return true;
            } else {
                logger.warn("No se encontró el producto {} para desactivar", productoId);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error al desactivar producto: {}", e.getMessage(), e);
            throw new RuntimeException("Error al desactivar el producto", e);
        }
    }
}