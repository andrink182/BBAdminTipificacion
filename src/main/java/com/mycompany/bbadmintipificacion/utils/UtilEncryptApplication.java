/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bbadmintipificacion.utils;


import java.util.Scanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class UtilEncryptApplication {
    
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(UtilEncryptApplication.class, args);
        Scanner scanner = new Scanner(System.in);
        GBNDEncrypt encryptor = context.getBean(GBNDEncrypt.class);
        
        while (true) {
            System.out.print("Desea encriptar o desencriptar? (E/D, o escriba ':exits' para salir): ");
            String opcion = scanner.nextLine().trim().toUpperCase();
            
            if (":EXITS".equals(opcion)) {
                System.out.println("Saliendo del programa...");
                break;
            }
            
            if (!"E".equals(opcion) && !"D".equals(opcion)) {
                System.out.println("Opcion no valida. Intente de nuevo.");
                continue;
            }
            
            System.out.print("Ingrese el texto: ");
            String texto = scanner.nextLine();
            
            try {
                if ("E".equals(opcion)) {
                    //String encryptedText = encryptor.encrypt(texto);
        //            System.out.println("Texto encriptado: " + encryptedText);

                    //String decryptedText = encryptor.decrypt(encryptedText);
      //              System.out.println("Texto desencriptado: " + decryptedText);
                } else {
                   // String decryptedText = encryptor.decrypt(texto);
    //                System.out.println("Texto desencriptado: " + decryptedText);
//
                 //   String encryptedText = encryptor.encrypt(decryptedText);
  //                  System.out.println("Texto encriptado: " + encryptedText);
                }
            } catch (Exception e) {
                System.err.println("Error en el proceso: " + e.getMessage());
            }
        }
        
        scanner.close();
    }
}
