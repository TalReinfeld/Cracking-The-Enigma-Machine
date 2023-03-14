package components.body.details;

import components.body.machine.DynamicMachineComponentFactory;
import components.body.machine.PlugBoardController;
import components.body.machine.ReflectorController;
import components.body.machine.RotorController;
import components.body.main.BodyController;
import consoleComponents.OutputMessages;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import logic.events.CodeSetEventListener;
import machineDtos.EngineDTO;
import scheme.generated.CTEReflector;
import scheme.generated.CTERotor;

import java.util.ArrayList;
import java.util.List;

public class MachineConfigurationController implements CodeSetEventListener, ReflectorParent, RotorParent,PlugBoardParent {

    BodyController parentController;
    @FXML private Label stringConfiguration;
    @FXML private StackPane reflectorPane;
    @FXML private HBox rotorsPane;
    @FXML private SplitPane plugBoardComponent;
    @FXML private PlugBoardController plugBoardComponentController;
    private final SimpleBooleanProperty isCodeConfigurationSet = new SimpleBooleanProperty(false);
    private final List<MachineConfigurationController> listenersWhenChange = new ArrayList<>();

    public SimpleBooleanProperty getIsCodeConfigurationSetProperty(){

        return isCodeConfigurationSet;
    }

    public void setParentController(BodyController controller){

        parentController = controller;
    }

    public void bind(MachineConfigurationController controller){

        stringConfiguration.textProperty().bind(controller.stringConfiguration.textProperty());
        plugBoardComponentController.bind(controller.plugBoardComponentController);
        controller.listenersWhenChange.add(this);
    }

    @Override
    public void invoke(EngineDTO updatedValue) {

        stringConfiguration.setText(OutputMessages.currentMachineSpecification(updatedValue.getMachineCurrentInfo()));

        setReflectorPane(updatedValue);
        setRotorsPane(updatedValue);
        setPlugBoard(updatedValue);
        isCodeConfigurationSet.set(true);

        for (MachineConfigurationController controller:listenersWhenChange) {

            controller.setReflectorPane(updatedValue);
            controller.setRotorsPane(updatedValue);
        }

        /*updatedValue.getMachineCurrentInfo().getNotchDistanceFromPositions()*/
    }

    private void setPlugBoard(EngineDTO updatedValue){

        plugBoardComponentController.setParent(this);
        plugBoardComponentController.clear();
        plugBoardComponentController.createPlugBoardFromMap(updatedValue.getMachineCurrentInfo().getPlugs());
    }

    private void setReflectorPane(EngineDTO updatedValue){

        reflectorPane.getChildren().clear();
        CTEReflector chosenReflector = updatedValue.getEngineComponentsInfo().getOptionalReflectors().stream().filter(
                reflector -> reflector.getId().equals(updatedValue.getMachineCurrentInfo().getReflectorID())).findFirst().get();
        ReflectorController controller = DynamicMachineComponentFactory.createReflectorOnPane(reflectorPane, this);
        controller.setCurrentReflector(chosenReflector);
    }

    private void setRotorsPane(EngineDTO updatedValue){

        rotorsPane.getChildren().clear();
        for (int i = 0; i<updatedValue.getNumOfUsedRotors(); i++) {

            int currentId = updatedValue.getMachineInitialInfo().getRotorsID().get(i);
            CTERotor chosenRotor = updatedValue.getEngineComponentsInfo().getOptionalRotors().stream().filter(
                    rotor -> rotor.getId() == currentId).findFirst().get();

            RotorController rotorController = DynamicMachineComponentFactory.createRotorOnPane(rotorsPane, this);
            rotorController.setRotorIdChoiceBox(currentId);
            rotorController.setCurrentRotor(chosenRotor,updatedValue.getMachineCurrentInfo().getRotorsInitPosition().get(i));
        }
    }

    @Override
    public CTERotor getRotor(int id) {
        return parentController.getEngineDetails().getEngineComponentsInfo().getOptionalRotors().stream().
                filter(rotor -> rotor.getId() == id).findFirst().get();
    }

    public void clearComponent() {
        if(!stringConfiguration.textProperty().isBound()) {
            stringConfiguration.setText("");
        }
        reflectorPane.getChildren().clear();
        rotorsPane.getChildren().clear();
        plugBoardComponentController.clear();
    }
}
