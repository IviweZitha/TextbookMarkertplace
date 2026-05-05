package model;
import model.abstracts.ListItem;
import model.interfaces.Searchable;
public class Textbook extends ListItem implements Searchable, Cloneable {



    // MUST implement abstract methods from ListItem

    @Override

    public String getCategory() {

        return "Textbook";

    }



    // MUST implement interface methods from Searchable

    @Override

    public boolean matchesQuery(String query) {

        // Implementation here
      return false;
    }

    @Override
    public String getSearchableContent() {
        return "";
    }

}
