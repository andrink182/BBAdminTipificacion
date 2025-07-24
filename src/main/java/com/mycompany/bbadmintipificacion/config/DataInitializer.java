package com.mycompany.bbadmintipificacion.config;

import com.mycompany.bbadmintipificacion.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public void run(String... args) throws Exception {
        logger.info("🚀 Iniciando inicialización de datos...");
        
        try {
            usuarioService.inicializarUsuariosPorDefecto();
            logger.info("✅ Inicialización de datos completada exitosamente");
        } catch (Exception e) {
            logger.error("❌ Error durante la inicialización de datos: {}", e.getMessage(), e);
        }
    }
}