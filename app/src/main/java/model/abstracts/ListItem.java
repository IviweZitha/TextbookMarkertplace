package model.abstracts;

public abstract class ListItem {
    // ABSTRACT - means subclasses MUST implement this
    private String title;
    public abstract String getCategory();



// TEMPLATE METHOD - defines structure, subclasses can extend

    public final String getFormattedDisplayInfo() {

        // Fixed structure

        StringBuilder sb = new StringBuilder();

        sb.append("[").append(getCategory()).append("]\n"); // Calls abstract method

        sb.append("Title: ").append(title).append("\n");

        return sb.toString();

    }
}
