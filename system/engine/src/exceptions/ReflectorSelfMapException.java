package exceptions;

public class ReflectorSelfMapException extends  Error{

    public ReflectorSelfMapException(String id ,int selfReflect){
        super("components.Reflector with id - " + id + " have a self reflect for \""+ selfReflect + "\"");
    }
}
