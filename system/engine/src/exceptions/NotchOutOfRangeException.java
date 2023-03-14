package exceptions;

public class NotchOutOfRangeException extends Exception{

    public NotchOutOfRangeException(int id, int notch, int size){
        super("components.Rotor with id - " + id + " have notch in position \"" + notch + "\" the range is between 1 - " + size);
    }
}
