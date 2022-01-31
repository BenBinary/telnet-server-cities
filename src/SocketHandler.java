
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author FHBBook
 */
public class SocketHandler implements Runnable {

    private Socket incoming_socket;
    
    public SocketHandler (Socket in) {
        incoming_socket=in;
    }
    
    @Override
    public void run() {
        BufferedReader reader=null;
        PrintStream out=null;
        boolean done = false;
        Set<String> keywords = null;
        
        
        try {
            reader = new BufferedReader(
                new InputStreamReader(
                incoming_socket.getInputStream()));
            out = new PrintStream(
                incoming_socket.getOutputStream(),true,"Cp850");
            out.println("Hello. Enter BYE to exit");
            
             // Add all keywords to the HashSet
             keywords = new HashSet<String>();

             keywords.add(Keywords.Airports.toString());
             keywords.add(Keywords.Sights.toString());
             keywords.add(Keywords.Businesses.toString());
             keywords.add(Keywords.Hospitals.toString());
             keywords.add(Keywords.Universities.toString());
             keywords.add(Keywords.Museums.toString());
            
        } catch (IOException e) {
            System.out.println("IOException during " + Thread.currentThread().getName() + " SocketHandler initialization " + e);
            done = true;
        }
        
        while ( ! done) {
            String str=null;
            
            try {
                str = reader.readLine();
                
                if (str == null) {

                done = true;
                System.out.println("Null received");

                } else if (str.matches("City.*")) {

                    out.print("City found");

                    str = str.replace("City ", "");
                    out.println("Translation: " + TelnetServer.get_city(str));

                    if (str.trim().equals("BYE")) {
                        done = true;
                    }

                } else if (str.matches(keywords + ".*")) {

                    String[] splitStr = str.split(" ");

                    String entityType = splitStr[0];

                    // 1. find the city from ALC by the numeric value of the ref-attribute
                    str = str.replace(entityType + " ", "");
                    String city = str.replace(entityType + " ", "");
                    int cityID = TelnetServer.get_cityID(city);
                    // out.println("Translation: " + get_city(city));

                    // 2. All attributs of ALE with the attribute (Airport) and the requested city
                    ArrayList<EntityClass> filteredCities = null;
                    filteredCities = TelnetServer.filterEntities(entityType, cityID);

                    // 3. Return all mathed attributes of ALE
                    int j = 0;
                    for (EntityClass entity : filteredCities) {
                        j++;

                        out.println("Filtered Entites Number " + j + ": " +  entity.toString());
                    }
                
            } }
            catch (InterruptedIOException e) {
                System.out.println("Time expired during " + Thread.currentThread().getName() + " SocketHandler reading " + e);
            } catch(IOException e) {
                System.out.println("IOException during " + Thread.currentThread().getName() + " SocketHandler reading " + e);
            }
            if (str == null) {
                done = true;
                System.out.println(Thread.currentThread().getName() + " Null received");
            }
            else {
                // out.println("Μετάφραση: " + TranslationServer.get_translation(str.toLowerCase()));
                if (str.trim().toUpperCase().equals("BYE"))
                    done = true;
            }
        }
        try {
            incoming_socket.close();
        } catch (IOException e) {
            // no message required
        }
    }
}
    

