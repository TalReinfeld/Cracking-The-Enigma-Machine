package machine;

import components.PlugBoard;
import components.Reflector;
import components.Rotor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Machine implements EnigmaMachine, Serializable {

    private final List<Rotor> rotors;
    private final List<Integer> rotorsPositions;
    private final List<Character> ABC;
    private final Reflector reflector;
    private final PlugBoard plugBoard;

    public Machine(List<Rotor> rotorsList, List<Integer> rotorsPositions, Reflector reflector, String abc, PlugBoard plugBoard) {

        this.reflector = reflector;
        rotors = rotorsList;
        this.rotorsPositions = rotorsPositions;
        ABC = abc.chars().mapToObj((c)->(char)c).collect(Collectors.toList());
        this.plugBoard = plugBoard;
    }

    public List<Integer> getRotorsId(){

        return rotors.stream().map(Rotor::getId).collect(Collectors.toList());
    }

    public String getReflectorId() {

        return reflector.getId();
    }

    public List<Character> getRotorsPositions(){

        return IntStream.range(0, rotors.size()).mapToObj(
                i -> rotors.get(i).getCharInPosition(rotorsPositions.get(i))).collect(Collectors.toList());
    }

    public Map<Character,Character> getPlugs(){

        return plugBoard.getPlugChars();
    }

    @Override
    public Character encryption(Character value) {

        Character encryptedChar = plugBoard.convert(value);
        int position = encryptedChar == null ? ABC.indexOf(value) : ABC.indexOf(encryptedChar);

        rotateRotors();
        for (int i = 0; i < rotors.size(); i++) {   // maybe change it to lambda exp?
            position = rotors.get(i).convert((position + rotorsPositions.get(i))% ABC.size());
            position = (ABC.size() + position - rotorsPositions.get(i)) % ABC.size();
        }

        position = reflector.convert(position);
        for (int i = rotors.size() - 1; i >= 0; i--) {
            position = rotors.get(i).convert((position + rotorsPositions.get(i)) % ABC.size());
            position = (ABC.size() + position - rotorsPositions.get(i)) % ABC.size();
        }

        encryptedChar = ABC.get(position);

        return  plugBoard.convert(encryptedChar) == null ? encryptedChar : plugBoard.convert(encryptedChar);
    }

    @Override
    public void rotateRotors(){

        int index=0;

        do{
            setNewPositionForRotor(index);
        }
        while(rotors.get(index).getNotchPosition() == rotorsPositions.get(index) && rotors.size() > ++index);
    }

    private void setNewPositionForRotor(int index){

        int  newValue = (rotorsPositions.get(index) + 1) % ABC.size();

        rotorsPositions.set(index, newValue);
    }

    public void setInitPositionForRotor(int index, char initChar){

        rotorsPositions.set(index, rotors.get(index).getPositionOfChar(initChar));
    }

    public List<Integer> getNotchDistanceFromPositions(){

        return IntStream.range(0,rotors.size()).mapToObj(this::getNotchDistanceFromPosition).collect(Collectors.toList());
    }

    public int getNotchDistanceFromPosition(int index){

        return (ABC.size() + rotors.get(index).getNotchPosition() - rotorsPositions.get(index)) % ABC.size();
    }
}
