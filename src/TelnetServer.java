
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class TelnetServer {

    private static ArrayList<Cityclass> ACL = new ArrayList<>();
    private static ArrayList<EntityClass> ALE = new ArrayList<>();
    
    // Methods for Thread Handling
    private static int max=5;
    private static int used=0;
    private static Thread handlers[]=new Thread[max];
    
    private static void pack() {
        int not_terminated=0;
        for (int i=0; i<used; i++) {
            if (handlers[i].getState()!=Thread.State.TERMINATED) {
                handlers[not_terminated]=handlers[i];
                not_terminated++;
            }
        }
        used=not_terminated;
    }
    

    /**
     * 
     * Fills the array with cities by reading it from the cities.txt file.
     * Executed in the beginning of executing the file.
     * 
     */
    private static void fill_cities() {

        try {

            File cityfile = new File("res/cities.txt");

            if (cityfile.exists()) {

                Scanner myreader = new Scanner(cityfile);

                while (myreader.hasNext()) {

                    String raw = myreader.nextLine();
                    String[] arr = raw.split("#");
                    Cityclass city = new Cityclass(Integer.valueOf(arr[0]), arr[1], arr[2], arr[3], arr[4], arr[5], arr[6]);
                    ACL.add(city);

                }

                myreader.close();

            }

        } catch (FileNotFoundException fnfe) {

            System.out.println(fnfe.getLocalizedMessage());
        }

    }
    
    /**
     * Read the entities out of a text-file.
     * Executed at every beginnning of running a file.
     * 
     */
    private static void fill_entities() {

        try {

            File cityfile = new File("res/namedEntities.txt");

            if (cityfile.exists()) {

                Scanner myreader = new Scanner(cityfile);

                while (myreader.hasNext()) {

                    String raw = myreader.nextLine();
                    String[] arr = raw.split("#");
                    EntityClass entity = new EntityClass(arr[0], arr[1], arr[2], arr[3], arr[4], Integer.valueOf(arr[5]), arr[6]);

                    ALE.add(entity);

                }

                myreader.close();

            }

        } catch (FileNotFoundException fnfe) {

            System.out.println(fnfe.getLocalizedMessage());
        }

    }

    /**
     * Get the city which is requested.
     * 
     * @param city
     * @return 
     */
    static String get_city(String city) {

        for (Cityclass singleCity : ACL) {
            if (singleCity.getCity_nom().matches(city)) {
                return singleCity.toString();
            }
        }

        return "";
    }
    
    /**
     * Filters all entities for the mentioned Parameters.
     * 
     * @param entity
     * @param cityID
     * @return 
     */
    static ArrayList<EntityClass> filterEntities(String entity, int cityID) {
        
        ArrayList<EntityClass> filteredEntities = new ArrayList<>();
        
        for (EntityClass singleEntity : ALE) {
            
            // Checks if it's certain entity
            if (singleEntity.type.matches(entity)) {
                
                // Checks if it's the specified City
                if (singleEntity.city_ref == cityID) {
                    filteredEntities.add(singleEntity);
                }
                
            }
        }
        
        return filteredEntities;
        
    }
    
    /**
     * Get the city order Number, which is assigned to the city
     * 
     * @param city
     * @return 
     */
     static int get_cityID(String city) {

        for (Cityclass singleCity : ACL) {
            if (singleCity.getCity_nom().matches(city)) {
                return singleCity.getOrderNumber();
            }
        }

        return 0;
    }
    


    public static int ECHOPORT = 6789;

    public static void main(String argv[]) {
        
        int SH_counter=0; // socket handler counter

        fill_cities();
        fill_entities();

        ServerSocket s = null;
        
        try {
            s = new ServerSocket(ECHOPORT);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
        while (true) {
            
            if (used==max) {
                pack();
                continue;
            }
            
            
            Socket incoming = null;
            
            // Established the connection
            try {
                incoming = s.accept();
            } catch (IOException e) {
                System.out.println("IOException while accept " + e);
                continue;
            }
            
            // Timeout for Incoming Thread
            try {
                incoming.setSoTimeout(30000); // 30 seconds
            } catch(SocketException e) {
                System.out.println("SocketException when setSoTimeout " + e);
                e.printStackTrace();
            }
            
            // Handles here the socket
            SocketHandler sh = new SocketHandler(incoming);
            Thread t = new Thread(sh, "Socket-Handler-" + SH_counter++);

            // Start the Thread
            t.start();

            // Putting it into the 
            handlers[used++]=t;
            
            System.out.println("Threads:");
            
            for (int i = 0; i < used; i++) {
                System.out.println(handlers[i].getName() 
                        + " " + handlers[i].getState());
            }
            
           
        }
    }

  
}
