package exceptions;

public class MultipleMappingException extends Exception{

    public MultipleMappingException(Character c){
        super("You have multiple mapping for " + c);
    }

    public MultipleMappingException(Object sign, Object objName, Object id){
        super(objName.getClass().getSimpleName() + " with id number - " + id + " have multiple mapping for \"" + sign + "\"");
    }
}
