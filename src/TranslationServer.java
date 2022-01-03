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
    private static HashMap<String, String> Translations = new HashMap<String, String>();
    static {
        Translations.put("car", "blub");
        Translations.put("drive", "οδηγώ");
        Translations.put("fruits", "φρούτα");
        Translations.put("orange", "πορτοκάλι");
        Translations.put("apple", "μήλο");
        Translations.put("apricot", "βερίκοκο");
        Translations.put("pear", "αχλάδι");
        Translations.put("vegetables", "λαχανικά");
        Translations.put("tomato", "ντομάτα");
        Translations.put("eggplant", "μελιτζάνα");
        Translations.put("meat", "κρέας");
        Translations.put("pork", "χοιρινό");
        Translations.put("veal", "μοσχάρι");
        Translations.put("beef", "μοσχάρι");
        Translations.put("eat", "τρώγω");
    }
    
    private static String get_translation(String original) {
        return Translations.get(original);
    }
    
    /**
     * Fills the array with cities by reading it from the cities.txt file
     */
    private static void fill_cities() {
        
        try {
             
            File cityfile = new File("res/cities.txt");
            
            if (cityfile.exists()) {
                System.out.println("it exists");
            }
            System.out.println("File " + cityfile.getAbsolutePath());
            // C:\Users\FHBBook\Documents\NetBeansProjects\SrvSocket1
            
            
            Scanner myreader = new Scanner(cityfile);
            
            while (myreader.hasNext()) {
                
                String raw = myreader.nextLine();
                String[] arr = raw.split("#");
                
                System.out.println(arr[1]);
                Cityclass city = new Cityclass(Integer.valueOf(arr[0]), arr[1], arr[2], arr[3], arr[4], arr[5], arr[6]);    
                ACL.add(city);
                
            } 
            
            myreader.close(); 
            
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

    public static int ECHOPORT = 6789;
    
    public static void main(String argv[]) {

        fill_cities();
        
        ServerSocket s = null;
        try {
            s = new ServerSocket(ECHOPORT);
        } catch(IOException e) {
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
            } catch(IOException e) {
                System.out.println("IOException when handleSocket " + e);
            }
            try {
                incoming.close();
            } catch(IOException e) {
                // no message required
            }
        }
    }
    
    public static void handleSocket(Socket incoming) throws IOException {
        
        BufferedReader reader =
            new BufferedReader(new InputStreamReader(
            incoming.getInputStream()));
        
        // Changed cp to 850 for western european standards
        PrintStream out = new PrintStream(incoming.getOutputStream(),true,"cp850");    

        out.println("Hello. Enter BYE to exit");
        boolean done = false;
        
        // Add all keywords to the HashSet
        Set<String> keywords = new HashSet<String>();
    
        /*
        for (String k : Keywords.) {
            
        } */
        
        
        keywords.add(Keywords.Airports.toString());
        
        while ( ! done) {
            String str = reader.readLine();
            if (str == null) {
                done = true;
                System.out.println("Null received");
            }
            else if (str.trim().equals("City.*")) {
             
                out.println("Translation: " + get_city(str));
                
                str = str.replace("City ", "");
                
                if (str.trim().equals("BYE"))
                    done = true;
              
            }
            else if (str.trim().equals(Keywords.Airports.toString() + ".*")) {
             
                str = str.replace(Keywords.Airports + " ", "");
                
                System.out.println("found");
                
                out.println("Translation: " + get_city(str));
                
                if (str.trim().equals("BYE"))
                    done = true;
              
            }
            else {
                //out.println("Translation: " + get_translation(str));
                out.println("Translation: " + get_city(str));
                
                if (str.trim().equals("BYE"))
                    done = true;
            }
        }
        incoming.close();
    }
}