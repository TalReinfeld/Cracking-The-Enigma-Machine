package components.body.details;

import components.body.machine.DynamicMachineComponentFactory;
import components.body.machine.PlugBoardController;
import components.body.machine.RotorController;
import components.body.main.BodyController;
import consoleComponents.OutputMessages;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import logic.EngineLogic;
import logic.events.CodeSetEventListener;
import machineDtos.EngineDTO;
import machineDtos.MachineInfoDTO;
import scheme.generated.CTERotor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CodeCalibrationController implements RotorParent, PlugBoardParent, CodeSetEventListener {

    BodyController parentController;
    @FXML private Button randomButton;
    @FXML private Button manualSetButton;
    @FXML private HBox rotorsPane;
    @FXML private VBox reflectorPane;
    @FXML private ChoiceBox<Character> leftPlugChoiceBox;
    @FXML private Button connectPlugsButton;
    @FXML private ChoiceBox<Character> rightPlugChoiceBox;
    @FXML private SplitPane plugBoardComponent;
    @FXML private PlugBoardController plugBoardComponentController;
    @FXML private Label initialConfigurationLabel;

    private final ToggleGroup availableReflectorsGroup = new ToggleGroup();
    private final List<RotorController> rotorsChosen = new ArrayList<>();
    private final SimpleObjectProperty<MachineInfoDTO> machineInfoProperty = new SimpleObjectProperty<>();


    public ObjectProperty<EventHandler<ActionEvent>> getRandomButtonOnActionListener() {
        return randomButton.onActionProperty();
    }

    public SimpleObjectProperty<MachineInfoDTO> getMachineInfoProperty() {
        return machineInfoProperty;

    }

    public void setParentController(BodyController controller){
        parentController = controller;
    }

    public void initialComponent(EngineDTO engineDetails){


        setReflectorPane(engineDetails.getNumOfOptionalReflectors());
        setRotorsPane(engineDetails.getEngineComponentsInfo().getOptionalRotors(),engineDetails.getNumOfUsedRotors());
        setPlugsPane(engineDetails.getEngineComponentsInfo().getABC());
    }

    private void setRotorsPane(List<CTERotor> optionalRotors, int numberOfUsedRotors){

        rotorsPane.getChildren().clear();
        for(int i=0 ; i < numberOfUsedRotors; i++){

                RotorController newController = DynamicMachineComponentFactory.createRotorOnPane(rotorsPane,this);
                newController.setRotorIdChoiceBox(IntStream.range(1,optionalRotors.size() + 1).boxed().collect(Collectors.toList()));
                rotorsChosen.add(newController);
        }
    }

    @Override
    public CTERotor getRotor(int id) {

        return parentController.getEngineDetails().getEngineComponentsInfo().getOptionalRotors().stream().
                filter(rotor -> rotor.getId() == id).findFirst().get();
    }

    public void hideSelectedRotorIdFromOthersControllers(RotorController e , Integer oldValue, Integer newValue){

        rotorsChosen.stream().filter(rotorController -> !rotorController.equals(e)).forEach(rotorController -> {
            if(oldValue != null) {
                rotorController.addRotorIdValue(oldValue);
            }
            rotorController.removeRotorIdValue(newValue);
        });
    }

    private void setReflectorPane(int numOfOptionalReflectors){

        reflectorPane.getChildren().clear();
        for(int i=0 ; i < numOfOptionalReflectors;i++) {

            RadioButton newRadioButton = new RadioButton();
            newRadioButton.setText(EngineLogic.idEncoder(i+1).name());
            availableReflectorsGroup.getToggles().add(newRadioButton);
            reflectorPane.getChildren().add(newRadioButton);
        }
    }

    @FXML
    public void manualSetCodeActionListener(ActionEvent event) {

        if(availableReflectorsGroup.getSelectedToggle() != null &&
                rotorsChosen.stream().allMatch(rotorController -> rotorController.getChosenRotorId() != null))
        {
            String reflectorId = ((RadioButton)availableReflectorsGroup.getSelectedToggle()).getText();
            List<Integer> rotorsIDs = new ArrayList<>();
            List<Character> rotorsInitialPositions = new ArrayList<>();
            rotorsChosen.forEach(rotor -> {
                        rotorsIDs.add(rotor.getChosenRotorId());
                        rotorsInitialPositions.add(rotor.getChosenInitialPosition());
                    });

            machineInfoProperty.set( new MachineInfoDTO(rotorsIDs, null, rotorsInitialPositions, reflectorId, plugBoardComponentController.createPlugsMap()));
        }
    }

    private void setPlugsPane(String ABC) {

        plugBoardComponentController.setParent(this);
        plugBoardComponentController.clear();
        ObservableList<Character> observableList = FXCollections.observableArrayList(ABC.chars().mapToObj(e->(char)e).collect(Collectors.toList()));
        leftPlugChoiceBox.setItems(observableList);
        rightPlugChoiceBox.itemsProperty().bind(leftPlugChoiceBox.itemsProperty());
    }

    @FXML
    public void connectPlugsButtonActionListener(ActionEvent event) {

        if(rightPlugChoiceBox.getValue() != null && leftPlugChoiceBox.getValue()!= null && leftPlugChoiceBox.getValue() != rightPlugChoiceBox.getValue())
        {
            plugBoardComponentController.addPlug(leftPlugChoiceBox.getValue(),rightPlugChoiceBox.getValue());
            rightPlugChoiceBox.getItems().remove(rightPlugChoiceBox.getValue());
            rightPlugChoiceBox.getItems().remove(leftPlugChoiceBox.getValue());
            leftPlugChoiceBox.setValue(null);
            rightPlugChoiceBox.setValue(null);
        }
    }

    public void onMouseClickedPlug(String chars) {

            rightPlugChoiceBox.getItems().add(chars.charAt(0));
            rightPlugChoiceBox.getItems().add(chars.charAt(1));
    }

    @Override
    public void invoke(EngineDTO updatedValue) {
        initialConfigurationLabel.setText(OutputMessages.currentMachineSpecification(updatedValue.getMachineInitialInfo()));
    }

    public void clearComponent() {
        initialConfigurationLabel.setText("");
        reflectorPane.getChildren().clear();
        rotorsPane.getChildren().clear();
        rightPlugChoiceBox.getItems().clear();
        plugBoardComponentController.clear();
    }
}
