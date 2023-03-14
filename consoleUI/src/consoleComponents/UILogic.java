package consoleComponents;

import exceptions.IdOutOfRangeException;
import exceptions.MultipleMappingException;
import logic.EngineLogic;
import logic.EnigmaEngine;
import logic.EnigmaSystemEngine;

import java.util.*;
import java.util.stream.Collectors;

public class UILogic {

    public static List<Integer> getRotorsIDsInput(Scanner scanner, int numberOfIds, int maxId) {

        System.out.println(OutputMessages.getRotorsIdMsg(numberOfIds));
        String input = scanner.nextLine();
        List<Integer> IDs;

        try {
            IDs = UILogic.getIntListFromString(input);        // throw exception
        } catch (NumberFormatException e){
            throw new NumberFormatException(e.getMessage() + " is not a valid ID");
        }

        if(IDs.size() != numberOfIds){                              // different number of ids
            throw new Error(OutputMessages.invalidNumberOfRotorsMsg());
        }
        else if(IDs.size() != new HashSet<>(IDs).size()){                // if insert same id twice or more
            throw new Error(OutputMessages.duplicateIdOfRotorsMsg());
        }

        Set<Integer> idsOutOfRange =  IDs.stream().filter(id-> id<1 || id > maxId).collect(Collectors.toSet());
        if(idsOutOfRange.size()!= 0){
            throw new IdOutOfRangeException(idsOutOfRange);
        }

        return IDs;
    }

    public static List<Integer> getIntListFromString(String s){

        List<Integer> intList = new ArrayList<>();

        if(s.lastIndexOf(",") == s.length() -1){
            throw new NumberFormatException("\"\"");
        }

        List<String> numberStrings = Arrays.asList(s.split(","));
        numberStrings.forEach(numStr->intList.add(0,Integer.parseInt(numStr)));

        return intList;
    }

    public static String getReflectorIdInput(Scanner scanner, int numOfReflectorsInSystem){

        System.out.println(OutputMessages.getReflectorIdMenuMsg(numOfReflectorsInSystem));
        String input = scanner.nextLine();
        int reflectorIdChoice = UILogic.parseStringToIntInRange(input, 1 , numOfReflectorsInSystem);    // throw exception

        return EngineLogic.idEncoder(reflectorIdChoice).toString();
    }

    public static int parseStringToIntInRange(String input, int min, int max){

        int number = Integer.parseInt(input);   // throw numberFormat

        if(number < min || number > max){
            throw new Error(OutputMessages.outOfRangeInputMsg());
        }

        return number;
    }

    public static Map<Character, Character> getPlugsInput(EnigmaSystemEngine enigmaMachine , Scanner scanner) throws MultipleMappingException {

        System.out.println(OutputMessages.getPlugsMsg());
        String input = scanner.nextLine().toUpperCase();

        if(input.length() % 2 != 0){
            throw new Error(OutputMessages.invalidPlugsInputMsg());
        }
        else if (enigmaMachine instanceof EnigmaEngine) {
            ((EnigmaEngine) enigmaMachine).checkIfCharactersInABC(input);
        }

        Map<Character, Character> plugPairs = new HashMap<>();

        for(int i=0 ; i<input.length(); i+=2){
            if(input.charAt(i) == input.charAt(i+1) || plugPairs.containsKey(input.charAt(i)) || plugPairs.containsValue(input.charAt(i))){
                throw new MultipleMappingException(input.charAt(i));
            }
            else if(plugPairs.containsKey(input.charAt(i+1)) || plugPairs.containsValue(input.charAt(i+1))){
                throw new MultipleMappingException(input.charAt(i+1));
            }
            else {
                plugPairs.put(input.charAt(i),input.charAt(i+1));
            }
        }

        return plugPairs;
    }

    public static List<Character> getRotorsInitialPositionsInput(EnigmaSystemEngine enigmaEngine, Scanner scanner, int numberOfRotors){

        System.out.println(OutputMessages.getRotorsPositionMsg(numberOfRotors));
        String input = scanner.nextLine().toUpperCase();

        if(input.length() != numberOfRotors){
            throw new Error(OutputMessages.invalidNumberOfInitialPositionsMsg());
        }
        else if (enigmaEngine instanceof EnigmaEngine) {
            ((EnigmaEngine) enigmaEngine).checkIfCharactersInABC(input);
        }

        List<Character> initialPositions = input.chars().mapToObj(c->(char)c).collect(Collectors.toList());
        Collections.reverse(initialPositions);

        return initialPositions;
    }
}
