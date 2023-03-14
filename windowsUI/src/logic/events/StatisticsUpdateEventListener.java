package logic.events;

import logic.events.handler.Invokable;
import machineDtos.HistoryAndStatisticDTO;

public interface StatisticsUpdateEventListener extends Invokable<HistoryAndStatisticDTO> {

    public void invoke(HistoryAndStatisticDTO arg);
}
