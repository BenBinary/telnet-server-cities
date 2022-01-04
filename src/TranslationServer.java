
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

    private static String get_city(String city) {

        for (Cityclass singleCity : ACL) {
            if (singleCity.city_nom.matches(city)) {
                return singleCity.toString();
            }
        }

        return "";
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

        /*
        for (String k : Keywords.) {
            
        } */
        keywords.add(Keywords.Airports.toString());

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

            } else if (str.matches("Airports" + ".*")) {
                
                
                // 1. find the city from ALC by the numeric value of the ref-attribute
                str = str.replace(Keywords.Airports + " ", "");
                String city = str.replace(Keywords.Airports + " ", "");
                out.println("Translation: " + get_city(city));
                
                // 2. All attributs of ALE with the attribute (Airport) and the requested city
                out.println("Entities: " + get_entity("Airports"));
                // TODO:
                
                // 3. Return all mathed attributes of ALE 

            

                if (str.trim().equals("BYE")) {
                    done = true;
                }

            } else {
                //out.println("Translation: " + get_translation(str));
                out.println("Translation: " + get_city(str));

                if (str.trim().equals("BYE")) {
                    done = true;
                }
            }
        }
        incoming.close();
    }
}
