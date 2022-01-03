/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author FHBBook
 */
public class findFile {
    
    public static void main(String[] args) {
        
         File cityfile = new File("C:\\Users\\FHBBook\\Documents\\NetBeansProjects\\SrvSocket1\\test.txt");
         // FileInputStream fos = new FileInputStream(cityfile);
        try {
            
            //BufferedReader br = new BufferedReader(new FileReader(cityfile));
            //Path p = new Path("C:\\Users\\FHBBook\\Documents\\NetBeansProjects\\SrvSocket1\\test.txt");
            FileReader fr = new FileReader("test.txt.txt");
            Scanner s = new Scanner(fr);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(findFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(findFile.class.getName()).log(Level.SEVERE, null, ex);
        }
           
            System.out.println("If it exists: " + cityfile.exists());
            System.out.println("If it can read: " + cityfile.canRead());
            System.out.println("File " + cityfile.getAbsolutePath());
    }
    
}
