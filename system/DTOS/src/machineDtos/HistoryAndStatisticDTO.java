package machineDtos;

import javafx.util.Pair;

import java.util.LinkedHashMap;
import java.util.Map;

public class HistoryAndStatisticDTO {

    private Map<MachineInfoDTO, Map<Pair<String,String>, Long>> historyAndStatistics = new LinkedHashMap<>();

    public HistoryAndStatisticDTO(Map<MachineInfoDTO, Map<Pair<String,String>, Long>> details){

        historyAndStatistics = details;
    }

    public Map<MachineInfoDTO, Map<Pair<String, String>, Long>> getHistoryAndStat() {

        return historyAndStatistics;
    }
}
