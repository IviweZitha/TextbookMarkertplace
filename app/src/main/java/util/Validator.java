package util;

public final class Validator {

    // Private constructor prevents instantiation

    private Validator() {

        throw new AssertionError("Utility class cannot be instantiated");

    }



    // Static methods - called without creating object

    public static boolean isValidEmail(String email) {

        // Validation logic
       return false;
    }

}

