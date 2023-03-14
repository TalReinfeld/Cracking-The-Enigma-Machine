package components.body.details;

import components.body.machine.DynamicMachineComponentFactory;
import components.body.machine.ReflectorController;
import components.body.machine.RotorController;
import components.body.main.BodyController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import machineDtos.EngineDTO;
import scheme.generated.CTEReflector;
import scheme.generated.CTERotor;

import java.util.List;

public class EngineDetailsController implements RotorParent, ReflectorParent{

    BodyController parentController;
    @FXML private HBox rotorsPane;
    @FXML private HBox reflectorsPane;
    @FXML private FlowPane abcPane;

    public void setParentController(BodyController controller){
        parentController = controller;
    }

    public void initialComponent(EngineDTO engineDetails){

        setReflectorPane(engineDetails.getEngineComponentsInfo().getOptionalReflectors());
        setRotorsPane(engineDetails.getEngineComponentsInfo().getOptionalRotors());
        setAbcPane(engineDetails.getEngineComponentsInfo().getABC());
    }

    private void setRotorsPane(List<CTERotor> optionalRotors){

        rotorsPane.getChildren().clear();
        for(int i=0 ; i < optionalRotors.size(); i++){

            RotorController newController = DynamicMachineComponentFactory.createRotorOnPane(rotorsPane,this);
            newController.setRotorIdChoiceBox(i+1);
        }
    }

    private void setReflectorPane(List<CTEReflector> optionalReflectors){

        reflectorsPane.getChildren().clear();
        for (CTEReflector reflector: optionalReflectors){

            ReflectorController newReflector = DynamicMachineComponentFactory.createReflectorOnPane(reflectorsPane, this);
            newReflector.setCurrentReflector(reflector);
        }
    }

    private void setAbcPane(String abc){

        for (Character c: abc.toCharArray()) {

            Label charLabel = new Label(c.toString());
            abcPane.getChildren().add(charLabel);
        }

    }

    @Override
    public CTERotor getRotor(int id) {

        return parentController.getEngineDetails().getEngineComponentsInfo().getOptionalRotors().stream().
                filter(rotor -> rotor.getId() == id).findFirst().get();
    }

    public void clearComponent() {
        rotorsPane.getChildren().clear();
        reflectorsPane.getChildren().clear();
        abcPane.getChildren().clear();
    }
}
