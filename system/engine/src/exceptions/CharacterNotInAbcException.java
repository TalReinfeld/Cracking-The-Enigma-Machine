package exceptions;

import java.util.Set;

public class CharacterNotInAbcException extends Error{

    public CharacterNotInAbcException(char character){
        super("The character \"" + character + "\" is not part of the ABC");
    }

    public CharacterNotInAbcException(Set<Character> characters){
        super("The characters " + characters + " is not part of the ABC");
    }
}
