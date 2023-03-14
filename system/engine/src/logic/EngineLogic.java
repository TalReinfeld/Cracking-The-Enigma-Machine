package logic;

import exceptions.*;
import scheme.generated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EngineLogic  {

    public static CTEEnigma createEnigmaFromFile(String path) throws FileNotFoundException, JAXBException {

        CTEEnigma enigmaMachine = null;

        try {
            InputStream inputStream = new FileInputStream(new File(path.trim()));
            enigmaMachine = deserializeFrom(inputStream);
        } catch (FileNotFoundException var3) {
            throw new FileNotFoundException("There isn't file according to the path that you insert");
        } catch (JAXBException var3) {
            throw new JAXBException("Your xml file is not following the rules of the scheme");
        }

        return enigmaMachine;
    }

    private static CTEEnigma deserializeFrom(InputStream in) throws JAXBException {

        JAXBContext jc = JAXBContext.newInstance("scheme.generated");
        Unmarshaller u = jc.createUnmarshaller();

        return (CTEEnigma) u.unmarshal(in);
    }

    public static void checkMachineIsValid(CTEMachine machine) throws InvalidAbcException, MultipleMappingException, NotchOutOfRangeException, IdMissingInRangeException, ConvertorsInMachineOutOfRangeException, TooManyRotorsInUseException, MissingMapException {

        if (machine.getABC().trim().length() % 2 != 0) {
            throw new InvalidAbcException(machine.getABC().trim().length());
        }

        rotorsIsValid(machine.getCTERotors(), machine.getRotorsCount(), machine.getABC().toUpperCase().trim());
        reflectorsIsValid(machine.getCTEReflectors(), machine.getABC().toUpperCase().trim());
    }

    private static void rotorsIsValid(CTERotors rotorsList, int rotorsCount, String ABC) throws IdMissingInRangeException, NotchOutOfRangeException, MultipleMappingException, ConvertorsInMachineOutOfRangeException, TooManyRotorsInUseException, MissingMapException {

        int i = 1, notchLocation;

        if (rotorsCount > 99) {
            throw new TooManyRotorsInUseException(rotorsCount);
        }
        else if(rotorsCount < 2 || rotorsCount > rotorsList.getCTERotor().size()) {
            throw new ConvertorsInMachineOutOfRangeException("Rotors", rotorsCount, 2, rotorsList.getCTERotor().size());
        }

        rotorsList.getCTERotor().sort((rotor1, rotor2) -> rotor1.getId() - rotor2.getId());
        for (CTERotor rotor : rotorsList.getCTERotor()) {
            notchLocation = rotor.getNotch();
            if (rotor.getId() != i) {
                throw  new IdMissingInRangeException(rotor, i);
            }
            else if (notchLocation > ABC.length() || notchLocation < 1) {
                throw new NotchOutOfRangeException(rotor.getId(), notchLocation, ABC.length());
            }
            checkPositioningSingleValueMapping(rotor, ABC);

            i++;
        }
    }

    private static void reflectorsIsValid(CTEReflectors reflectorsList, String ABC) throws MultipleMappingException, IdMissingInRangeException, ConvertorsInMachineOutOfRangeException, MissingMapException {

        boolean[] checkId = new boolean[reflectorsList.getCTEReflector().size()];

        if(reflectorsList.getCTEReflector().size() < 1 || reflectorsList.getCTEReflector().size() > 5) {
            throw new ConvertorsInMachineOutOfRangeException("Reflectors", reflectorsList.getCTEReflector().size(), 1 , 5);
        }

        for (CTEReflector reflector : reflectorsList.getCTEReflector()) {
            int index = idDecoder(reflector.getId()) - 1;
            if(index<reflectorsList.getCTEReflector().size() && index >= 0) {
                checkId[index] = true;
            }

            checkReflectSingleValueMapping(reflector, ABC);
        }

        for (int i=0 ; i< checkId.length ; ++i) {
            if(!checkId[i]){
                throw new IdMissingInRangeException(reflectorsList.getCTEReflector().get(0), idEncoder(i+1));
            }
        }
    }

    private static void checkPositioningSingleValueMapping(CTERotor currentRotor, String ABC) throws MultipleMappingException, MissingMapException {

        Set<String> leftSet = new HashSet<>();
        Set<String> rightSet = new HashSet<>();

        for (CTEPositioning positioning : currentRotor.getCTEPositioning()) {
            if (!ABC.contains(positioning.getLeft().toUpperCase())) {
                throw new CharacterNotInAbcException(positioning.getLeft().toUpperCase().charAt(0));
            }
            else if(!leftSet.add(positioning.getLeft().toUpperCase())) {
                throw new MultipleMappingException(positioning.getLeft().toUpperCase().charAt(0), currentRotor, currentRotor.getId());
            }
            else if(!ABC.contains(positioning.getRight().toUpperCase())){
                throw new CharacterNotInAbcException(positioning.getRight().toUpperCase().charAt(0));
            }
            else if (!rightSet.add(positioning.getRight().toUpperCase())) {
                throw new MultipleMappingException(positioning.getRight().toUpperCase().charAt(0), currentRotor, currentRotor.getId());
            }
        }

        if(leftSet.size() != rightSet.size() || rightSet.size() != ABC.length()){
            throw new MissingMapException(currentRotor,currentRotor.getId());
        }
    }

    private static void checkReflectSingleValueMapping(CTEReflector currentReflector, String ABC) throws MultipleMappingException, MissingMapException {

        Set<Integer> mapSet = new HashSet<>();

        for (CTEReflect reflect : currentReflector.getCTEReflect()) {
            if ((reflect.getInput() > ABC.length() || reflect.getInput() < 1)) {
                throw new reflectOutOfRangeException(reflect.getInput(),currentReflector.getId());
            }
            else if (!mapSet.add(reflect.getInput())) {
                throw new MultipleMappingException(reflect.getInput(),currentReflector,currentReflector.getId());
            }
            else if (reflect.getInput() == reflect.getOutput()) {
                throw new ReflectorSelfMapException(currentReflector.getId(), reflect.getInput());
            }
            else if (reflect.getOutput() > ABC.length() || reflect.getOutput() < 1) {
                throw new reflectOutOfRangeException(reflect.getOutput(),currentReflector.getId());
            }
            else if (!mapSet.add(reflect.getOutput())) {
                throw new MultipleMappingException(reflect.getOutput(),currentReflector,currentReflector.getId());
            }
        }

        if(mapSet.size() != ABC.length()){
            throw new MissingMapException(currentReflector,currentReflector.getId());
        }
    }

    public static int idDecoder(String id){

        try {
            return RomanNumeral.valueOf(id).evaluateNumber();
        }catch (IllegalArgumentException e){
            return 0;
        }
    }

    public static RomanNumeral idEncoder(int id){

        return Arrays.stream(RomanNumeral.values()).filter((romanNumeral) -> romanNumeral.evaluateNumber() == id).findFirst().get();
    }
}