
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
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class TranslationServer {

    private static ArrayList<Cityclass> ACL = new ArrayList<>();
    private static ArrayList<EntityClass> ALE = new ArrayList<>();

    /**
     * Fills the array with cities by reading it from the cities.txt file
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
     * Read the entities out of a text-file
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
     * Get the city which is needed.
     * 
     * @param city
     * @return 
     */
    private static String get_city(String city) {

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
    private static ArrayList<EntityClass> filterEntities(String entity, int cityID) {
        
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
     private static int get_cityID(String city) {

        for (Cityclass singleCity : ACL) {
            if (singleCity.getCity_nom().matches(city)) {
                return singleCity.getOrderNumber();
            }
        }

        return 0;
    }
    
    /**
     * Is looking up entity
     * 
     * @param entity
     * @return 
     */
    private static String get_entity(String entity) {

        for (EntityClass singleEntity : ALE) {
            if (singleEntity.type.matches(entity)) {
                
                
                return singleEntity.toString();
            }
        }

        return "";
    }

    public static int ECHOPORT = 6789;

    public static void main(String argv[]) {

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
            Socket incoming = null;
            try {
                incoming = s.accept();
            } catch (IOException e) {
                System.out.println("IOException while accept " + e);
                continue;
            }
            try {
                handleSocket(incoming);
            } catch (IOException e) {
                System.out.println("IOException when handleSocket " + e);
            }
            try {
                incoming.close();
            } catch (IOException e) {
                // no message required
            }
        }
    }

    public static void handleSocket(Socket incoming) throws IOException {

        BufferedReader reader
                = new BufferedReader(new InputStreamReader(
                        incoming.getInputStream()));

        // Changed cp to 850 for western european standards
        PrintStream out = new PrintStream(incoming.getOutputStream(), true, "cp850");

        out.println("Hello. Enter BYE to exit");
        boolean done = false;

        // Add all keywords to the HashSet
        Set<String> keywords = new HashSet<String>();
 
        keywords.add(Keywords.Airports.toString());
        keywords.add(Keywords.Sights.toString());
        keywords.add(Keywords.Businesses.toString());
        keywords.add(Keywords.Hospitals.toString());
        keywords.add(Keywords.Universities.toString());
        keywords.add(Keywords.Museums.toString());

        while (!done) {
            String str = reader.readLine();
            if (str == null) {

                done = true;
                System.out.println("Null received");

            } else if (str.matches("City.*")) {

                out.print("City found");

                str = str.replace("City ", "");
                out.println("Translation: " + get_city(str));

                if (str.trim().equals("BYE")) {
                    done = true;
                }

            } else if (str.matches(keywords + ".*")) {
                
                String[] splitStr = str.split(" ");
                
                String entityType = splitStr[0];
                
                // 1. find the city from ALC by the numeric value of the ref-attribute
                str = str.replace(entityType + " ", "");
                String city = str.replace(entityType + " ", "");
                int cityID = get_cityID(city);
                // out.println("Translation: " + get_city(city));
                
                // 2. All attributs of ALE with the attribute (Airport) and the requested city
                ArrayList<EntityClass> filteredCities = null;
                filteredCities = filterEntities(entityType, cityID);
    
                // 3. Return all mathed attributes of ALE
                int j = 0;
                for (EntityClass entity : filteredCities) {
                    j++;
                    
                    out.println("Filtered Entites Number " + j + ": " +  entity.toString());
                }
            

                if (str.trim().equals("BYE")) {
                    done = true;
                }

            } else {
             
                out.println("Translation: " + get_city(str));

                if (str.trim().equals("BYE")) {
                    done = true;
                }
            }
        }
        incoming.close();
    }
}
