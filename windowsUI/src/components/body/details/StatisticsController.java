package components.body.details;

import consoleComponents.OutputMessages;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import logic.events.StatisticsUpdateEventListener;
import machineDtos.HistoryAndStatisticDTO;

public class StatisticsController implements StatisticsUpdateEventListener {

    @FXML private Label statisticsLabel;

    @Override
    public void invoke(HistoryAndStatisticDTO arg) {

        statisticsLabel.setText(OutputMessages.historyMsg(arg));
    }

    public void clearController() {
        statisticsLabel.setText("");
    }
}
