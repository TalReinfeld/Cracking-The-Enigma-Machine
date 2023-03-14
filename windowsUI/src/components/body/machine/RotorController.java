package components.body.machine;

import components.body.details.CodeCalibrationController;
import components.body.details.RotorParent;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import scheme.generated.CTEPositioning;
import scheme.generated.CTERotor;

import java.util.List;

public class RotorController {

    private RotorParent parentController;
    @FXML private ChoiceBox<Integer> rotorIdChoiceBox;
    @FXML private Button previousButton;
    @FXML private Button currentButton;
    @FXML private Button nextButton;
    private CTERotor currentRotor;
    private int indexOfChosenPosition;

    @FXML
    void currentButtonActionListener(ActionEvent event) {

        if(!(this.parentController instanceof CodeCalibrationController)){

            ListView<String> rotorContent = new ListView<>();
            for(CTEPositioning positioning : currentRotor.getCTEPositioning()) {
                rotorContent.getItems().add("'" + positioning.getLeft() + "'" + " --- " + "'" + positioning.getRight() + "'");
            }
            Stage rotorStage = new Stage();
            rotorStage.setTitle("rotor's positioning");
            rotorStage.setScene(new Scene(rotorContent));
            rotorStage.setAlwaysOnTop(true);
            rotorStage.show();

        }
    }

    @FXML
    void nextButtonActionListener(ActionEvent event) {

        if(this.parentController instanceof CodeCalibrationController && !nextButton.getText().equals("")) {
            indexOfChosenPosition++;
            if(indexOfChosenPosition> currentRotor.getCTEPositioning().size()){
                indexOfChosenPosition = indexOfChosenPosition % currentRotor.getCTEPositioning().size();
            }
            previousButton.setText(currentButton.getText());
            currentButton.setText(nextButton.getText());
            nextButton.setText(currentRotor.getCTEPositioning().get((indexOfChosenPosition+1)% currentRotor.getCTEPositioning().size()).getRight());
        }
    }

    @FXML
    void previousButtonActionListener(ActionEvent event) {

        if(this.parentController instanceof CodeCalibrationController && !nextButton.getText().equals("")) {
            indexOfChosenPosition--;
            if(indexOfChosenPosition<0){
                indexOfChosenPosition = (indexOfChosenPosition + currentRotor.getCTEPositioning().size()) % currentRotor.getCTEPositioning().size();
            }
            nextButton.setText(currentButton.getText());
            currentButton.setText(previousButton.getText());
            previousButton.setText(currentRotor.getCTEPositioning().get((
                    indexOfChosenPosition -1+ currentRotor.getCTEPositioning().size()) % currentRotor.getCTEPositioning().size()).getRight());
        }
    }

    public void setParentController(RotorParent controller) {

        this.parentController = controller;
        rotorIdChoiceBox.valueProperty().addListener(((observable, oldValue, newValue) -> setCurrentRotor(this.parentController.getRotor(newValue))));
        if(this.parentController instanceof CodeCalibrationController){
            rotorIdChoiceBox.valueProperty().addListener(
                    (observable, oldValue, newValue) -> ((CodeCalibrationController)parentController).hideSelectedRotorIdFromOthersControllers(this,oldValue,newValue));
        }
    }

    public void setRotorIdChoiceBox(List<Integer> options){

        rotorIdChoiceBox.setItems(FXCollections.observableList(options));
    }

    public void setRotorIdChoiceBox(int rotorId){

        rotorIdChoiceBox.getItems().add(rotorId);
        rotorIdChoiceBox.setValue(rotorId);
    }

    public void addRotorIdValue(int id){
        rotorIdChoiceBox.getItems().add(id);
    }

    public void removeRotorIdValue(int id){
        rotorIdChoiceBox.getItems().removeAll(id);
    }

    public void setCurrentRotor(CTERotor rotor){
        currentRotor = rotor;
        indexOfChosenPosition = 0;
        currentButton.setText(currentRotor.getCTEPositioning().get(0).getRight());
        nextButton.setText(currentRotor.getCTEPositioning().get(1).getRight());
        previousButton.setText(currentRotor.getCTEPositioning().get(rotor.getCTEPositioning().size()-1).getRight());
    }

    public void setCurrentRotor(CTERotor rotor, Character initialPosition){
        currentRotor = rotor;

        for (int i=0 ;i < rotor.getCTEPositioning().size();i++){
            if(rotor.getCTEPositioning().get(i).getRight().equals(initialPosition.toString())){
                      indexOfChosenPosition = i;
            }
        }

        nextButton.setText(currentRotor.getCTEPositioning().get((indexOfChosenPosition + 1)% rotor.getCTEPositioning().size()).getRight());
        previousButton.setText(currentRotor.getCTEPositioning().get((indexOfChosenPosition - 1 + rotor.getCTEPositioning().size())% rotor.getCTEPositioning().size()).getRight());
        currentButton.setText(String.valueOf(initialPosition));
    }

    public Integer getChosenRotorId(){

        return rotorIdChoiceBox.getValue();
    }

    public Character getChosenInitialPosition(){

        return currentButton.getText() != null ? currentButton.getText().charAt(0) : null;
    }
}
