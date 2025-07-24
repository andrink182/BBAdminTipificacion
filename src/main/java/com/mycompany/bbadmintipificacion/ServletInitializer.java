package com.mycompany.bbadmintipificacion; // Usando el mismo paquete

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        // AQUÍ ES DONDE CONECTAMOS AMBAS CLASES:
        // Le decimos a Tomcat que use tu clase principal "CatalogAdminApplication" como la configuración.
        return application.sources(CatalogAdminApplication.class);
    }
}