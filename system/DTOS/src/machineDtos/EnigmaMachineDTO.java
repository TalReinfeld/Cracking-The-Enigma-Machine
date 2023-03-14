package machineDtos;

import java.util.List;
import java.util.Map;

public interface EnigmaMachineDTO {
    List<Integer> getRotorsID();
    List<Character> getRotorsInitPosition();
    String getReflectorID();
    Map<Character, Character> getPlugs();
}
