package logic;

import exceptions.ConvertorsInMachineOutOfRangeException;
import manager.DecryptionManager;
import scheme.generated.CTEDecipher;

import java.util.*;
import java.util.stream.Collectors;

public class DecipherLogic {

    public static final int MINIMUM_AGENTS = 2;
    public static final int MAXIMUM_AGENTS = 50;
    public static final int MAXIMUM_TASKS = 1000;

    public static void checkDecipherIsValid(CTEDecipher decipher) {

        int amountOfAgents = decipher.getAgents();

        if (!(amountOfAgents > MINIMUM_AGENTS && amountOfAgents < MAXIMUM_AGENTS)) {
            throw new ConvertorsInMachineOutOfRangeException("Agents",amountOfAgents,MINIMUM_AGENTS,MAXIMUM_AGENTS);
        }
    }

    public static void initDecipher(CTEDecipher decipher) {

        Set<String> dictionary = stringToWords(decipher.getCTEDictionary().getWords());
        String excludeChars = decipher.getCTEDictionary().getExcludeChars();
        Set<String> dictionaryAfter = new HashSet<>();

        for (String word:dictionary) {

                dictionaryAfter.add(excludeSpecialCharactersFromWord(word,excludeChars));
        }

        DecryptionManager.setMaxNumberOfAgents(decipher.getAgents());
        DecryptionManager.setWordsDictionary(dictionaryAfter);
        DecryptionManager.setExcludeChars(excludeChars);
    }

    public static Set<String> stringToWords(String str){

        return Arrays.stream(str.split(" ")).collect(Collectors.toSet());
    }

    public static String excludeSpecialCharactersFromWord(String word, String specialChars){

        StringBuilder tempWord = new StringBuilder(word);
        int index;

        for (Character c: specialChars.toCharArray()) {

            while ((index = tempWord.indexOf(c.toString())) != -1){
                tempWord.deleteCharAt(index);
            }
        }

        return tempWord.toString();
    }
}
