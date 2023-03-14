package components;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Reflector implements Convertor<Integer>, Serializable, Cloneable {

    private final String id;
    private final Map<Integer, Integer> reflectorConversions;

    public Reflector(String id, Map<Integer,Integer> reflectorMap){

        this.id = id;
        reflectorConversions = reflectorMap;
    }

    public synchronized Map<Integer, Integer> getReflectorConversions() {
        return new HashMap<>(reflectorConversions);
    }

    public String getId() {

        return id;
    }

    @Override
    public synchronized Integer convert(Integer position) {

        return reflectorConversions.get(position);
    }

    @Override
    public Reflector clone() {

        Reflector clone = new Reflector(id, new HashMap<>(reflectorConversions));

        return clone;
    }
}
