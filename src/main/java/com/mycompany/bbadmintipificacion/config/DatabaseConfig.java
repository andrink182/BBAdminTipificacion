/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bbadmintipificacion.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import com.mycompany.bbadmintipificacion.utils.GBNDEncrypt;

/**
 *
 * @author crivera
 */
@Configuration
public class DatabaseConfig {
    
     @Value("${GBNBBKy}")
    private String hashKey;
    @Value("${db.encpwd}")
    private String encPWD;
   @Value("${db.cn}")
    private String connectionString;
    @Value("${db.username}")
    private String userName;
    
    //@Value("${datasource.encryption.key}")
   // private String encryptionKey;
    
    @Bean
    @Primary
    public DataSource dataSource() throws Exception {
        // Desencriptar password manualmente
        //String decryptedPassword = decryptPassword(encryptedPassword, encryptionKey);
        GBNDEncrypt encryptor = new GBNDEncrypt(hashKey);
        String pwdDEC = encryptor.decrypt(encPWD);
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(connectionString);
        dataSource.setUsername(userName);
        dataSource.setPassword(pwdDEC);
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        
        return dataSource;
    }
    
    
}