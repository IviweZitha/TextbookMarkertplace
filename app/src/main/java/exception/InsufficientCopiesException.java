package exception;



/**

 * Custom exception thrown when there aren't enough copies available.

 */

public class InsufficientCopiesException extends RuntimeException {



    private final int requestedCopies;

    private final int availableCopies;



    public InsufficientCopiesException(String message, int requested, int available) {

        super(message);

        this.requestedCopies = requested;

        this.availableCopies = available;

    }



    public InsufficientCopiesException(int requested, int available) {

        super(String.format("Requested %d copies but only %d available", requested, available));

        this.requestedCopies = requested;

        this.availableCopies = available;

    }



    public int getRequestedCopies() {

        return requestedCopies;

    }



    public int getAvailableCopies() {

        return availableCopies;

    }



    public int getShortfall() {

        return requestedCopies - availableCopies;

    }

}
