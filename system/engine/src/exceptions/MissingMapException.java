package exceptions;

public class MissingMapException extends Exception{

    public MissingMapException(Object obj, Object id){
        super (obj.getClass().getSimpleName() + " with id number - " + id + " is missing mappings");
    }
}
