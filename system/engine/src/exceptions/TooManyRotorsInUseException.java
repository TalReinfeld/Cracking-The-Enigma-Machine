package exceptions;

public class TooManyRotorsInUseException extends Exception{

     public TooManyRotorsInUseException(int numOfRotorsInTheSystem){
          super("Too many rotors in use, you have " + numOfRotorsInTheSystem + " and you can use at most 99");
     }
}
