package ee.jyri.scimport.error;

public class NoSuchUserException extends Exception {
    public NoSuchUserException(String message) {
        super(message);
    }

}
