package logic;

import components.ConversionTable;
import components.PlugBoard;
import components.Reflector;
import components.Rotor;
import exceptions.CharacterNotInAbcException;
import exceptions.MachineNotDefinedException;
import exceptions.NoFileLoadedException;
import javafx.util.Pair;
import machine.Machine;
import machineDtos.*;
import scheme.generated.CTEEnigma;
import scheme.generated.CTEMachine;
import scheme.generated.CTEReflector;
import scheme.generated.CTERotor;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EnigmaEngine implements EnigmaSystemEngine, Serializable {

    private Machine enigmaMachine = null;
    private MachineInfoDTO currentInitialMachineInfo = null;
    private MachineInfoDTO currentMachineInfo = null;
    private String ABC;
    private final List<Rotor> optionalRotors = new ArrayList<>();
    private final List<Reflector> optionalReflectors = new ArrayList<>();
    private int rotorsCount;
    private Map<MachineInfoDTO, Map<Pair<String,String>, Long>> historyAndStat = new LinkedHashMap<>();
    private LastEncryptedMessage lastEncryptedDetails;
    @Override
    public void loadXmlFile(String path) throws Exception {

        CTEEnigma enigmaMachineCTE = EngineLogic.createEnigmaFromFile(path.trim());
        EngineLogic.checkMachineIsValid(enigmaMachineCTE.getCTEMachine());
        engineInit(enigmaMachineCTE.getCTEMachine());
        DecipherLogic.checkDecipherIsValid(enigmaMachineCTE.getCTEDecipher());
        DecipherLogic.initDecipher(enigmaMachineCTE.getCTEDecipher());
    }

    private void engineInit(CTEMachine enigmaMachineCTE){

        ABC = enigmaMachineCTE.getABC().trim().toUpperCase();
        rotorsCount = enigmaMachineCTE.getRotorsCount();

        optionalRotors.clear();
        for (CTERotor rotor : enigmaMachineCTE.getCTERotors().getCTERotor()) {

            ConversionTable conversionTable = new ConversionTable();

            rotor.getCTEPositioning().forEach((position) ->
                    conversionTable.add(position.getRight().toUpperCase().charAt(0), position.getLeft().toUpperCase().charAt(0)));

            optionalRotors.add(new Rotor(rotor.getId(), rotor.getNotch() - 1, conversionTable));
        }

        optionalReflectors.clear();
        for (CTEReflector reflector : enigmaMachineCTE.getCTEReflectors().getCTEReflector()){

            Map<Integer,Integer> reflectorMap = new HashMap<>();
            reflector.getCTEReflect().forEach((reflect)-> {
                reflectorMap.put(reflect.getInput() - 1, reflect.getOutput() - 1);
                reflectorMap.put(reflect.getOutput() - 1,reflect.getInput() - 1);
            });
            optionalReflectors.add(new Reflector(reflector.getId(), reflectorMap));
        }

        enigmaMachine = null;
        currentInitialMachineInfo = null;
        historyAndStat = new LinkedHashMap<>();
    }

    @Override
    public EngineDTO displayingMachineSpecification() {

        if(!isEngineInitialized()){
            throw new NoFileLoadedException();
        }

        if(currentInitialMachineInfo!= null) {
            currentMachineInfo = initMachineInfo();
        }

        return new EngineDTO(optionalRotors.size(), optionalReflectors.size(), rotorsCount, encryptedMsgSum() ,
                currentInitialMachineInfo,currentMachineInfo, new EngineComponentsCTEDTO(optionalRotors,optionalReflectors,ABC));
    }

    @Override
    public void manualMachineInit(EnigmaMachineDTO args) {

        if (!isEngineInitialized()) {
            throw new NoFileLoadedException();
        }

        List<Rotor> rotors = new ArrayList<>();
        List<Integer> rotorsPositions = new ArrayList<>();
        Reflector reflector;
        PlugBoard plugBoard = new PlugBoard();

        for (Integer id : args.getRotorsID()) {
            rotors.add(optionalRotors.stream().filter(rotor -> rotor.getId() == id).findFirst().get());
        }

        IntStream.range(0,args.getRotorsInitPosition().size()).forEach(
                i-> rotorsPositions.add(rotors.get(i).getPositionOfChar(args.getRotorsInitPosition().get(i))));

        reflector = optionalReflectors.stream().filter(
                reflector1 -> Objects.equals(reflector1.getId(), args.getReflectorID())).findFirst().get();

        args.getPlugs().keySet().forEach(key -> plugBoard.add(key, args.getPlugs().get(key)));

        enigmaMachine = new Machine(rotors, rotorsPositions, reflector, ABC, plugBoard);
        currentInitialMachineInfo = initMachineInfo();
        historyAndStat.put(currentInitialMachineInfo, new LinkedHashMap<>());
    }

    @Override
    public void automaticMachineInit() {

        if(!isEngineInitialized()){
            throw new NoFileLoadedException();
        }

        Random rand = new Random();
        List<Rotor> rotors = new ArrayList<>();
        List<Integer> rotorsPositions = new ArrayList<>();
        Reflector reflector = optionalReflectors.get(rand.nextInt(optionalReflectors.size()));;
        PlugBoard plugBoard = automaticCreatePlugBoard(rand);

        boolean[] chosenRotors = new boolean[optionalRotors.size()];
        int rotorChosenIndex, i = 0;

        while(i != rotorsCount){
            rotorChosenIndex = rand.nextInt(optionalRotors.size());
            if(!chosenRotors[rotorChosenIndex]) {
                rotors.add((optionalRotors.get(rotorChosenIndex)));
                rotorsPositions.add(rand.nextInt(ABC.length()));
                chosenRotors[rotorChosenIndex] = true;
                i++;
            }
        }

        enigmaMachine = new Machine(rotors,rotorsPositions,reflector,ABC,plugBoard);
        currentInitialMachineInfo = initMachineInfo();
        historyAndStat.put(currentInitialMachineInfo, new LinkedHashMap<>());
    }

    @Override
    public String encryptString(String message) {

        StringBuilder encryptedString = new StringBuilder();

        if(enigmaMachine == null){
            throw new MachineNotDefinedException();
        }

        message = message.toUpperCase();
        checkIfCharactersInABC(message);

        long encryptedTime = System.nanoTime();

        for (Character c:message.toCharArray()) {
            encryptedString.append(enigmaMachine.encryption(c).toString());
        }

        if(lastEncryptedDetails == null) {
            lastEncryptedDetails = new LastEncryptedMessage(message,encryptedString.toString(),encryptedTime);
        }else {
            lastEncryptedDetails.setMessage(lastEncryptedDetails.getMessage().concat(message));
            lastEncryptedDetails.setEncrypted(lastEncryptedDetails.getEncrypted().concat(encryptedString.toString()));
        }
        return encryptedString.toString();
    }

    public void updateHistory(){

        long encryptedTime = System.nanoTime() - lastEncryptedDetails.getTime();

        historyAndStat.get(currentInitialMachineInfo).put(new Pair<>(
                lastEncryptedDetails.getMessage(), lastEncryptedDetails.getEncrypted()),encryptedTime);

        lastEncryptedDetails = null;
    }

    @Override
    public void resetTheMachine() {

        if (enigmaMachine == null) {
            throw new MachineNotDefinedException();
        }

        List<Character> rotorsInitPositions = currentInitialMachineInfo.getRotorsInitPosition();

        IntStream.range(0, rotorsInitPositions.size()).forEach(
                rotorIndex -> enigmaMachine.setInitPositionForRotor(rotorIndex, rotorsInitPositions.get(rotorIndex)));
    }

    @Override
    public HistoryAndStatisticDTO getHistoryAndStatistics() {

        if (!isEngineInitialized()) {
            throw new NoFileLoadedException();
        }

        return new HistoryAndStatisticDTO(historyAndStat);
    }

    public void writeEngineToFile(String fileName) throws IOException {

        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))){
            out.writeObject(this);
            out.flush();
        }
    }

    public static EnigmaEngine readEngineFromFile(String fileName) throws Exception{

        try(ObjectInputStream in = (new ObjectInputStream(new FileInputStream(fileName)))) {

            return (EnigmaEngine) in.readObject();
        }catch (StreamCorruptedException e){
            throw new Exception("the file you insert is not compatible to engine");
        }
    }

    public boolean isEngineInitialized(){

        return ABC != null;
    }

    public void checkIfCharactersInABC(String input){       // maybe change it to Collection instead of string

        Set<Character> notInAbcChars = input.chars().mapToObj(c->(char)c).filter(c->!ABC.contains(c.toString())).collect(Collectors.toSet());
        if(notInAbcChars.size() != 0){
            throw new CharacterNotInAbcException(notInAbcChars);
        }
    }

    private MachineInfoDTO initMachineInfo(){

        List<Integer> rotorsId = enigmaMachine.getRotorsId();
        List<Character> rotorsPositions = enigmaMachine.getRotorsPositions();
        List<Integer> notchDistanceInRotors = enigmaMachine.getNotchDistanceFromPositions();
        String reflectorId = enigmaMachine.getReflectorId();
        Map<Character,Character> plugs = enigmaMachine.getPlugs();
        return new MachineInfoDTO(rotorsId,notchDistanceInRotors, rotorsPositions ,reflectorId, plugs);
    }

    private PlugBoard automaticCreatePlugBoard(Random rand){

        PlugBoard plugBoard = new PlugBoard();

        int numOfPlugs = rand.nextInt(ABC.length()/2 + 1);
        List<Character> listOfAvailableChars = ABC.chars().mapToObj(e->(char)e).collect(Collectors.toList());
        Character first,second;

        for(int i=0; i < numOfPlugs; ++i) {

            first = listOfAvailableChars.get(rand.nextInt(listOfAvailableChars.size()));
            listOfAvailableChars.remove(first);
            second = listOfAvailableChars.get(rand.nextInt(listOfAvailableChars.size()));
            listOfAvailableChars.remove(second);
            plugBoard.add(first,second);
        }

        return plugBoard;
    }

    private int encryptedMsgSum(){

        return historyAndStat.values().stream().mapToInt(Map::size).sum();
    }

    public EngineComponentsDTO getEngineComponentsDto(){
        return new EngineComponentsDTO(optionalRotors,optionalReflectors, ABC);
    }


}