/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bbadmintipificacion.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

/**
 *
 * @author crivera
 */
@Component  // Agregar esta anotación
@Configuration
   public class ApiConfig {
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(Environment env) {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        
        // Obtener la ruta del archivo externo configurado en application.properties
        String externalPropertiesPath = env.getProperty("spring.config.additional-location");
            
        // Configurar las fuentes de propiedades
        configurer.setLocations(
            new ClassPathResource("application.properties"),
            new FileSystemResource(externalPropertiesPath)
          //  new FileSystemResource("C:\\BBPORTAL\\application.properties")

        );
        
        // No fallar si algún archivo no existe
        configurer.setIgnoreResourceNotFound(true);
        
        return configurer;
    }
  
    
}
