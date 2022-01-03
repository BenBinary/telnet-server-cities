
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author FHBBook
 */
public class TestFile {
    
    
    public static void main(String[] args) {
        
         try {
             
            File cityfile = new File("test.txt");
            
            if (cityfile.exists()) {
                System.out.println("it exists");
            }
            
            System.out.println("If it exists: " + cityfile.exists());
            System.out.println("If it can read: " + cityfile.canRead());
            System.out.println("File " + cityfile.getAbsolutePath());
            // C:\Users\FHBBook\Documents\NetBeansProjects\SrvSocket1
            
            Scanner myreader = new Scanner(cityfile);

            myreader.close(); 
            
        } catch (FileNotFoundException fnfe) {
            
            System.out.println(fnfe.getLocalizedMessage());
            
            
        } 
    }
    
}
