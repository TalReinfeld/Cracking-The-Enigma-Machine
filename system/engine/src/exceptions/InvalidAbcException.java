package exceptions;

public class InvalidAbcException extends Exception {

    public InvalidAbcException(int abcSize){
        super((abcSize == 0) ? "Invalid ABC - ABC input is empty" : "Invalid ABC - ABC input is odd");
    }
}
