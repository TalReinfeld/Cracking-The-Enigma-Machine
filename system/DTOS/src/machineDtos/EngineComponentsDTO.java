package machineDtos;

import components.Reflector;
import components.Rotor;

import java.util.ArrayList;
import java.util.List;

public class EngineComponentsDTO {

    private final List<Rotor> optionalRotors = new ArrayList<>();
    private final List<Reflector> optionalReflectors = new ArrayList<>();
    private final String ABC;

    public EngineComponentsDTO(List<Rotor> rotors, List<Reflector> reflectors, String abc){

        rotors.forEach(rotor -> optionalRotors.add(rotor.clone()));
        reflectors.forEach(reflector -> optionalReflectors.add(reflector.clone()));
        ABC = abc;
    }

    public List<Rotor> getOptionalRotors(){

        return optionalRotors;
    }

    public List<Reflector> getOptionalReflectors() {

        return optionalReflectors;
    }

    public String getABC() {
        return ABC;
    }
}
