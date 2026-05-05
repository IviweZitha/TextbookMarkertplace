package manager;
import java.util.ArrayList;

public class ListingManager {

    private static ListingManager instance;
    private final ArrayList<Object> listings;


    // Private constructor - prevents external instantiation

    private ListingManager() {

        this.listings = new ArrayList<>();

    }



    // Public access point

    public static synchronized ListingManager getInstance() {

        if (instance == null) {

            instance = new ListingManager();

        }

        return instance;

    }

}

