package exception;



/**

 * Custom exception thrown when attempting to create a duplicate listing.

 * Demonstrates CUSTOM EXCEPTION HANDLING.

 */

public class DuplicateListingException extends Exception {



    private String duplicateField;

    private Object duplicateValue;



    public DuplicateListingException(String message) {

        super(message);

    }



    public DuplicateListingException(String message, String field, Object value) {

        super(message);

        this.duplicateField = field;

        this.duplicateValue = value;

    }



    public DuplicateListingException(String message, Throwable cause) {

        super(message, cause);

    }



    public String getDuplicateField() {

        return duplicateField;

    }



    public Object getDuplicateValue() {

        return duplicateValue;

    }



    @Override

    public String toString() {

        if (duplicateField != null) {

            return String.format("DuplicateListingException: %s [Field: %s, Value: %s]",

                    getMessage(), duplicateField, duplicateValue);

        }

        return "DuplicateListingException: " + getMessage();

    }

}