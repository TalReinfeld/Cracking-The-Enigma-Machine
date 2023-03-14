package logic;

import machineDtos.EngineDTO;
import machineDtos.EnigmaMachineDTO;
import machineDtos.HistoryAndStatisticDTO;

public interface EnigmaSystemEngine {

    void loadXmlFile(String path) throws Exception;

    EngineDTO displayingMachineSpecification();

    void manualMachineInit(EnigmaMachineDTO args);

    void automaticMachineInit();

    String encryptString(String input);

    void resetTheMachine();

    HistoryAndStatisticDTO getHistoryAndStatistics();
}