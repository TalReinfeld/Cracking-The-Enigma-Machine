package exceptions;

public class NoFileLoadedException extends Error {

    public NoFileLoadedException(){
        super("You need to load file");
    }
}
