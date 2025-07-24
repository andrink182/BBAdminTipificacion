package com.mycompany.bbadmintipificacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableJpaRepositories(basePackages = "com.mycompany.bbadmintipificacion.repository")
public class CatalogAdminApplication {
    public static void main(String[] args) {
        System.out.println("🚀 Iniciando Catalog Admin Application...");
        SpringApplication.run(CatalogAdminApplication.class, args);
        System.out.println("✅ Aplicación iniciada correctamente");
    }
}