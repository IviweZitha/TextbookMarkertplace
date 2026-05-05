package model.interfaces;

public interface Searchable {

    boolean matchesQuery(String query);  // Any class implementing this MUST have this method

    String getSearchableContent();

}