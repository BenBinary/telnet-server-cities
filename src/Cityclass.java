/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author FHBBook
 */
public class Cityclass {
    
    int orderNumber;
    String city_nom;
    String city_gen;
    String city_acc;
    String city_dat;
    String city_english;
    String city_state;

    public Cityclass(int orderNumber, String city_nom, String city_gen, String city_acc, String city_dat, String city_english, String city_state) {
        this.orderNumber = orderNumber;
        this.city_nom = city_nom;
        this.city_gen = city_gen;
        this.city_acc = city_acc;
        this.city_dat = city_dat;
        this.city_english = city_english;
        this.city_state = city_state;
    }

    @Override
    public String toString() {
        return city_nom + " " + city_gen + " " + city_acc + " " + city_dat + " " + city_english + " " + city_state;
    }

    // Removing ^ by returnin
    public String getCity_nom() {
        
        return city_nom.replace("^", "");
    }
    
    

    public int getOrderNumber() {
        return orderNumber;
    }
    
    
    
    
    
    
    
    
}
