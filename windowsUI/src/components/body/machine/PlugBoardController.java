package components.body.machine;

import components.body.details.CodeCalibrationController;
import components.body.details.PlugBoardParent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlugBoardController {

    private static final String connectSign = "< - >";
    @FXML private ListView<String> plugsListView;
    private PlugBoardParent parentController;

    public void bind(PlugBoardController controller){

        plugsListView.itemsProperty().bind(controller.plugsListView.itemsProperty());
    }

    public void setParent(PlugBoardParent parent){

        parentController = parent;
    }

    public void addPlug(Character left, Character right){

        plugsListView.getItems().add(left + connectSign + right);
    }

    @FXML
    void onMouseClickedPlug(MouseEvent event) {

        if(event.getClickCount() == 2 && plugsListView.getSelectionModel().getSelectedItems().size() != 0
        && parentController instanceof CodeCalibrationController){

            String chars = plugsListView.getSelectionModel().getSelectedItem().replaceAll(connectSign,"");
            plugsListView.getItems().remove(plugsListView.getSelectionModel().getSelectedIndex());
            ((CodeCalibrationController)parentController).onMouseClickedPlug(chars);
        }
    }


    public Map<Character,Character> createPlugsMap(){

        Map<Character,Character> plugs = new HashMap<>();
        List<String> pairs = new ArrayList<>();

        plugsListView.getItems().forEach(str ->pairs.add(str.replaceAll(connectSign,"")));
        pairs.forEach(pair-> plugs.put(pair.charAt(0),pair.charAt(1)));

        return plugs;
    }

    public void createPlugBoardFromMap(Map<Character, Character> plugs) {

        plugs.keySet().forEach(key -> addPlug(key, plugs.get(key)));
    }

    public void clear(){
        plugsListView.getItems().clear();
    }
}
