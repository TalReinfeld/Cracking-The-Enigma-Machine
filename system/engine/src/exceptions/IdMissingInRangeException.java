package exceptions;

public class IdMissingInRangeException extends Exception{

    public IdMissingInRangeException(Object obj, Object id){
        super(obj.getClass().getSimpleName() + " with id number - " + id + " does not exist in the system");
    }
}

