package exception;



/**

 * Custom exception thrown when banking information is invalid.

 */

public class InvalidBankingInfoException extends Exception {



    private final String invalidField;

    private final String providedValue;



    public InvalidBankingInfoException(String message) {

        super(message);

        this.invalidField = null;

        this.providedValue = null;

    }



    public InvalidBankingInfoException(String message, String field, String value) {

        super(message);

        this.invalidField = field;

        this.providedValue = value;

    }



    public String getInvalidField() {

        return invalidField;

    }



    public String getProvidedValue() {

        return providedValue;

    }



    public String getSafeMessage() {

        // Don't expose sensitive data in messages

        if (invalidField != null && "accountNumber".equals(invalidField)) {

            return "Invalid account number provided";

        }

        return getMessage();

    }

}