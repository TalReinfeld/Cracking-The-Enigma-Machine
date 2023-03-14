package consoleComponents;

import exceptions.*;
import logic.EnigmaEngine;
import logic.EnigmaMachineUI;
import logic.EnigmaSystemEngine;
import machineDtos.EngineDTO;
import machineDtos.HistoryAndStatisticDTO;
import machineDtos.MachineInfoDTO;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MachineUI implements EnigmaMachineUI {

    private EnigmaSystemEngine enigmaSystem = new EnigmaEngine();
    private final Scanner scanner = new Scanner(System.in);

    public void run() throws NoSuchMethodException {

        OutputMessages.initMenuMsg();
        String input;
        AtomicInteger choice = new AtomicInteger();

        do {
            System.out.println(OutputMessages.getMenuMsg());
            input = scanner.nextLine();
            try {
                choice.set(Integer.parseInt(input));
                Arrays.stream(MachineUI.class.getDeclaredMethods()).filter(
                                method -> method.isAnnotationPresent(SortedMethod.class) &&
                                        method.getAnnotation(SortedMethod.class).value() == choice.get()).findFirst().get().
                        invoke(this, null);

            } catch (InvocationTargetException e) {
                System.out.println(e.getTargetException().getMessage());
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage() + " - invalid input");
            } catch (IllegalAccessException | NoSuchElementException e) {
                System.out.println(OutputMessages.outOfRangeInputMsg());
            } catch (Exception | Error e) {
                System.out.println(e.getMessage());
            }

        } while (choice.get() != MachineUI.class.getMethod("exit", null).getAnnotation(SortedMethod.class).value());

        System.out.println("BYE :)");
    }

    @Override
    @SortedMethod(value = 1)
    public void loadNewXmlFile(){

        System.out.println(OutputMessages.getPathMsg());
        String xmlPath = scanner.nextLine();

        while (!Files.exists(Paths.get(xmlPath)) || !xmlPath.toString().substring(xmlPath.lastIndexOf(".") + 1).equals("xml")) {

            System.out.println(OutputMessages.invalidPathMsg());
            xmlPath = scanner.nextLine();
            if (xmlPath.toUpperCase().equals("Q")) {
                return;
            }
        }
        try{
            enigmaSystem.loadXmlFile(xmlPath);
            System.out.println(OutputMessages.getSuccessfulLoadMsg());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    @SortedMethod(value = 2)
    public void displayingMachineSpecification() {

        EngineDTO machineSpecification = enigmaSystem.displayingMachineSpecification();
        System.out.println(OutputMessages.machineSpecification(machineSpecification));
    }

    @Override
    @SortedMethod(value = 3)
    public void manualInitialCodeConfiguration() {

        checkFileLoaded();
        EngineDTO machineSpecification = enigmaSystem.displayingMachineSpecification();

        List<Integer> rotorsIDs = UILogic.getRotorsIDsInput(scanner, machineSpecification.getNumOfUsedRotors(), machineSpecification.getNumOfOptionalRotors());
        List<Character> rotorsInitialPositions = UILogic.getRotorsInitialPositionsInput(enigmaSystem, scanner, machineSpecification.getNumOfUsedRotors());
        String reflectorId = UILogic.getReflectorIdInput(scanner, machineSpecification.getNumOfOptionalReflectors());
        Map<Character, Character> plugs = null;
        try {
            plugs = UILogic.getPlugsInput(enigmaSystem, scanner);
        } catch (MultipleMappingException e) {
            throw new Error(e.getMessage());
        }

        MachineInfoDTO args = new MachineInfoDTO(rotorsIDs, null, rotorsInitialPositions, reflectorId, plugs);
        enigmaSystem.manualMachineInit(args);
    }

    @Override
    @SortedMethod(value = 4)
    public void automaticInitialCodeConfiguration() {

        enigmaSystem.automaticMachineInit();
    }

    @Override
    @SortedMethod(value = 5)
    public void encryptInput(){

        checkFileLoaded();
        System.out.println(OutputMessages.getStringMsg());

        String msgToEncrypt = scanner.nextLine();
        String encryptedMsg = enigmaSystem.encryptString(msgToEncrypt);
        if(enigmaSystem instanceof EnigmaEngine){
          ((EnigmaEngine) enigmaSystem).updateHistory();
        }

        System.out.println(OutputMessages.encryptedStringMsg(encryptedMsg));
    }

    @Override
    @SortedMethod(value = 6)
    public void resetCurrentCode(){

        enigmaSystem.resetTheMachine();
        System.out.println(OutputMessages.resetCodeMsg());
    }

    @Override
    @SortedMethod(value = 7)
    public void getHistoryAndStatistics() {

        HistoryAndStatisticDTO details = enigmaSystem.getHistoryAndStatistics();
        System.out.println(OutputMessages.historyMsg(details));
    }

    @Override
    @SortedMethod(value = 8)
    public void exit() {}

    @SortedMethod(value = 9)
    public void writeCurrentMachineStateToFile() throws IOException {

        System.out.println(OutputMessages.getSaveFileMsg());
        String fileName = scanner.nextLine();

        if(enigmaSystem instanceof EnigmaEngine){
            ((EnigmaEngine) enigmaSystem).writeEngineToFile(fileName);
            System.out.println(OutputMessages.getSuccessfulSaveMsg());
        }
    }

    @SortedMethod(value = 10)
    public void readMachineStateFromFile() throws Exception{

        System.out.println(OutputMessages.getSaveFileMsg());
        String fileName = scanner.nextLine();

        if(enigmaSystem instanceof EnigmaEngine) {
            enigmaSystem = EnigmaEngine.readEngineFromFile(fileName);
            System.out.println(OutputMessages.getSuccessfulLoadMsg());
        }
    }

    private void checkFileLoaded(){

        if(enigmaSystem instanceof EnigmaEngine && !((EnigmaEngine) enigmaSystem).isEngineInitialized()){
            throw new NoFileLoadedException();
        }
    }
}
