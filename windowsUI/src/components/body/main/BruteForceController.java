package components.body.main;

import components.body.details.DecryptionManagerController;
import consoleComponents.OutputMessages;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import logic.events.CodeSetEventListener;
import logic.events.EncryptMessageEventListener;
import logic.events.handler.MachineEventHandler;
import machineDtos.EngineDTO;
import manager.DecryptionManager;

public class BruteForceController implements encryptParentController, CodeSetEventListener {

    private BodyController parentController;
    @FXML private GridPane encryptComponent;
    @FXML private EncryptController encryptComponentController;
    @FXML private GridPane decryptionManagerComponent;
    @FXML private DecryptionManagerController decryptionManagerComponentController;
    @FXML private Label codeConfigurationLabel;
    private BooleanProperty encryptedWordIsAvailable = new SimpleBooleanProperty(false);


    public void setParentController(BodyController controller){
        parentController = controller;
    }

    public void initial(){

        decryptionManagerComponentController.setParentController(this);
        encryptedWordIsAvailable.bind(encryptComponentController.getEncryptedMessage().isNotEqualTo(""));

        decryptionManagerComponent.disableProperty().bind(encryptedWordIsAvailable.not());
        encryptComponentController.setAutoStateOnly();
        encryptComponentController.setParentController(this);
        encryptComponent.disableProperty().bind(parentController.getIsCodeConfigurationSetProperty().not());
    }

    public void setDecryptionManager(DecryptionManager decryptionManager) {
        decryptionManagerComponentController.setDecryptionManager(decryptionManager);
        decryptionManagerComponentController.initial();
    }

    public String getEncryptedMessage(){
        return encryptComponentController.getEncryptedMessage().getValue();
    }

    @Override
    public EngineDTO getEngineDetails() {
        return parentController.getEngineDetails();
    }

    @Override
    public StringProperty getMachineEncryptedMessageProperty() {
        return parentController.getMachineEncryptedMessageProperty();
    }

    public void setEncryptedMessageLabel(String newValue) {
        encryptComponentController.setEncryptedMessageLabel(newValue);
    }

    public MachineEventHandler<EncryptMessageEventListener> encryptMessageEventHandler()  {

        return encryptComponentController.activateEncryptEventHandler;
    }
    public String getStringWithoutSpecialChars(String message){

        return decryptionManagerComponentController.getStringWithoutSpecialChars(message);
    }
    public Boolean checkWordsInTheDictionary(String message){

        return decryptionManagerComponentController.isWordsInDictionary(message);
    }

    @Override
    public void invoke(EngineDTO updatedValue) {

        codeConfigurationLabel.setText(OutputMessages.currentMachineSpecification(updatedValue.getMachineCurrentInfo()));
    }

    public void clearComponent() {

        codeConfigurationLabel.setText("");
        encryptComponentController.clearButtonActionListener(new ActionEvent());
        decryptionManagerComponentController.clearController();

    }
}
