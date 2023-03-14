package machineDtos;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MachineInfoDTO implements EnigmaMachineDTO, Serializable {

    private final List<Integer> rotorsID;
    private final List<Integer> notchDistanceFromPositions;
    private final List<Character> rotorsInitPosition;
    private final String reflectorID;
    private final Map<Character,Character> plugs;

    public MachineInfoDTO(List<Integer> rotorsID,List<Integer> notchDistance,List<Character> rotorsInitPosition, String reflectorID, Map<Character, Character> plugs) {
        this.rotorsID = rotorsID;
        this.rotorsInitPosition = rotorsInitPosition;
        this.reflectorID = reflectorID;
        this.plugs = plugs;
        notchDistanceFromPositions = notchDistance;
    }

    public List<Integer> getRotorsID() {

        return rotorsID;
    }

    public List<Character> getRotorsInitPosition() {

        return rotorsInitPosition;
    }

    public String getReflectorID() {

        return reflectorID;
    }

    public Map<Character, Character> getPlugs() {

        return plugs;
    }

    public List<Integer> getNotchDistanceFromPositions() {

        return notchDistanceFromPositions;
    }
}
