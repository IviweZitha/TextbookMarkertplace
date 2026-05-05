package exception;



import java.util.ArrayList;

import java.util.List;



/**

 * Custom exception thrown when listing data is invalid.

 * Demonstrates EXCEPTION WITH ERROR DETAILS.

 */

public class InvalidListingException extends Exception {



    private final List<String> validationErrors;

    private final String fieldName;



    public InvalidListingException(String message) {

        super(message);

        this.validationErrors = new ArrayList<>();

        this.fieldName = null;

    }



    public InvalidListingException(String message, String fieldName) {

        super(message);

        this.validationErrors = new ArrayList<>();

        this.validationErrors.add(message);

        this.fieldName = fieldName;

    }



    public InvalidListingException(List<String> errors) {

        super("Invalid listing: " + errors.size() + " error(s) found");

        this.validationErrors = new ArrayList<>(errors);

        this.fieldName = null;

    }



    public void addError(String error) {

        validationErrors.add(error);

    }



    public List<String> getValidationErrors() {

        return new ArrayList<>(validationErrors);

    }



    public String getFieldName() {

        return fieldName;

    }



    public boolean hasMultipleErrors() {

        return validationErrors.size() > 1;

    }



    @Override

    public String toString() {

        if (validationErrors.isEmpty()) {

            return "InvalidListingException: " + getMessage();

        }



        StringBuilder sb = new StringBuilder("InvalidListingException:\n");

        for (int i = 0; i < validationErrors.size(); i++) {

            sb.append(" ").append(i + 1).append(". ").append(validationErrors.get(i)).append("\n");

        }

        return sb.toString();

    }

}



