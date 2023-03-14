package components;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PlugBoard implements Convertor<Character>, Serializable, Cloneable {

    private final Map<Character, Character> plugChars = new HashMap<>();

    public Map<Character, Character> getPlugChars() {

        Map<Character,Character> returnMap = new HashMap<>();

        for (Character C : plugChars.keySet()) {
            if(!(returnMap.containsKey(C) || returnMap.containsValue(C))){
                returnMap.put(C,plugChars.get(C));
            }
        }

        return returnMap;
    }

    public void add(Character key, Character value){

        plugChars.put(key, value);
        plugChars.put(value, key);
    }

    @Override
    public Character convert(Character character) {

        return plugChars.get(character);
    }

    @Override
    public PlugBoard clone() {
        try {
            PlugBoard clone = (PlugBoard) super.clone();
            clone.plugChars.putAll(plugChars);

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
