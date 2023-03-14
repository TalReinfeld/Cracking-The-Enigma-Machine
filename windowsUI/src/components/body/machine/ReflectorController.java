package components.body.machine;

import components.body.details.ReflectorParent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.util.Pair;
import scheme.generated.CTEReflect;
import scheme.generated.CTEReflector;

public class ReflectorController {

    @FXML
    private Label reflectorId;
    private CTEReflector currentReflector;

    @FXML
    private ListView<Pair<Integer,Integer>> reflectorList;
    private ReflectorParent parentController;

    public void setParentController(ReflectorParent parent){

        parentController = parent;

    }

    public void setCurrentReflector(CTEReflector reflector) {
        currentReflector = reflector;

        reflectorId.setText(currentReflector.getId());

        //Map<Integer,Integer> reflectMap = currentReflector.getReflectorConversions();
//
//        for (Integer key : reflectMap.keySet()) {
//
//            reflectorList.getItems().add(new Pair<>(key,reflectMap.get(key)));
//        }

        for (CTEReflect reflect:reflector.getCTEReflect()){
            reflectorList.getItems().add(new Pair<>(reflect.getInput(),reflect.getOutput()));
        }
    }
}