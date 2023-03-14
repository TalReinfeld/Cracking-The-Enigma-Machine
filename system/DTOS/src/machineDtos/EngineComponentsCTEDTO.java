package machineDtos;

import components.Reflector;
import components.Rotor;
import javafx.util.Pair;
import scheme.generated.CTEPositioning;
import scheme.generated.CTEReflect;
import scheme.generated.CTEReflector;
import scheme.generated.CTERotor;

import java.util.ArrayList;
import java.util.List;

public class EngineComponentsCTEDTO {

    private final List<CTERotor> optionalRotors = new ArrayList<>();
    private final List<CTEReflector> optionalReflectors = new ArrayList<>();
    private final String ABC;

    public EngineComponentsCTEDTO(List<Rotor> rotors, List<Reflector> reflectors, String abc){

        for (Rotor rotor: rotors) {
            CTERotor duplicate = new CTERotor();
            duplicate.setId(rotor.getId());
            duplicate.setNotch(rotor.getNotchPosition());

            for(int i=0; i< rotor.getConversionTableSize();i++){
                CTEPositioning positioning = new CTEPositioning();
                Pair pair = rotor.getPosition(i);
                positioning.setLeft(pair.getKey().toString());
                positioning.setRight(pair.getValue().toString());
                duplicate.getCTEPositioning().add(positioning);
            }

            optionalRotors.add(duplicate);
        }

        for(Reflector reflector: reflectors){
            CTEReflector duplicate = new CTEReflector();
            duplicate.setId(reflector.getId());

            for (Integer key :reflector.getReflectorConversions().keySet()){
                CTEReflect reflect = new CTEReflect();
                reflect.setInput(key);
                reflect.setOutput(reflector.getReflectorConversions().get(key));
                duplicate.getCTEReflect().add(reflect);
            }

            optionalReflectors.add(duplicate);
        }

        ABC = abc;
    }

    public List<CTERotor> getOptionalRotors(){

        return optionalRotors;
    }

    public List<CTEReflector> getOptionalReflectors() {

        return optionalReflectors;
    }

    public String getABC() {
        return ABC;
    }
}
