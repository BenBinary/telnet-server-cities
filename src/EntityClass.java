/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author FHBBook
 */
public class EntityClass {
    
    String ent_de_nom;
    String ent_de_gen;
    String ent_de_acc;
    String ent_de_dat;
    String ent_de_gender;
    int city_ref;
    String type;

    public EntityClass(String ent_de_nom, String ent_de_gen, String ent_de_acc, String ent_de_dat, String ent_de_gender, int city_ref, String type) {
        this.ent_de_nom = ent_de_nom;
        this.ent_de_gen = ent_de_gen;
        this.ent_de_acc = ent_de_acc;
        this.ent_de_dat = ent_de_dat;
        this.ent_de_gender = ent_de_gender;
        this.city_ref = city_ref;
        this.type = type;
    }

    @Override
    public String toString() {
       
        return this.ent_de_nom + " " + this.ent_de_gen;
        
    }
    
    
    
}
