package model;
import model.interfaces.Listable;
import model.interfaces.Searchable;
public class Listing implements Listable, Searchable {

    private Textbook textbook;    // Composition

    private Seller seller;        // Composition

    @Override
    public boolean matchesQuery(String query) {
        return false;
    }

    @Override
    public String getSearchableContent() {
        return "";
    }


    // NOT inheritance - a listing is NOT a textbook

    // A listing CONTAINS a textbook

}
