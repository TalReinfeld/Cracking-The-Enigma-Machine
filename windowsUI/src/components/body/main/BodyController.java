package components.body.main;

import components.body.details.CodeCalibrationController;
import components.body.details.EngineDetailsController;
import components.body.details.MachineConfigurationController;
import components.body.details.StatisticsController;
import components.main.EnigmaAppController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import logic.EnigmaSystemEngine;
import logic.HistoryUpdatable;
import machineDtos.EngineDTO;
import machineDtos.MachineInfoDTO;
import manager.DecryptionManager;


public class BodyController implements encryptParentController {

    private EnigmaAppController mainController;
    @FXML private BorderPane codeCalibrationComponent;
    @FXML private CodeCalibrationController codeCalibrationComponentController;
    @FXML private BorderPane machineConfigurationComponent;
    @FXML private MachineConfigurationController machineConfigurationComponentController;
    @FXML private BorderPane engineDetailsComponent;
    @FXML private EngineDetailsController engineDetailsComponentController;
    @FXML private BorderPane encryptScreenMachineConfigurationComponent;
    @FXML private MachineConfigurationController encryptScreenMachineConfigurationComponentController;
    @FXML private GridPane encryptComponent;
    @FXML private EncryptController encryptComponentController;
    @FXML private ScrollPane bruteForceComponent;
    @FXML private  BruteForceController bruteForceComponentController;
    @FXML private ScrollPane statisticsComponent;
    @FXML private StatisticsController statisticsComponentController;
    @FXML private Tab encryptTab;
    private EngineDTO engineDetails;

    public void initial() {

        if (codeCalibrationComponentController != null){
            codeCalibrationComponentController.setParentController(this);
        }
        if(machineConfigurationComponentController != null){
            machineConfigurationComponentController.setParentController(this);
            machineConfigurationComponent.disableProperty().bind(machineConfigurationComponentController.getIsCodeConfigurationSetProperty().not());
            encryptScreenMachineConfigurationComponentController.bind(machineConfigurationComponentController);
            encryptComponent.disableProperty().bind(machineConfigurationComponentController.getIsCodeConfigurationSetProperty().not());
            encryptScreenMachineConfigurationComponent.disableProperty().bind(machineConfigurationComponent.disableProperty());
        }
        if(engineDetailsComponentController != null){
            engineDetailsComponentController.setParentController(this);
        }
        if(encryptComponentController != null) {
            encryptComponentController.setParentController(this);
            encryptComponentController.activateEncryptEventHandler.addListener(mainController.getEncryptMessageEventListener());
            machineConfigurationComponentController.getIsCodeConfigurationSetProperty().addListener(observable -> encryptComponentController.createKeyboards(engineDetails.getEngineComponentsInfo().getABC()));
        }
        if(encryptScreenMachineConfigurationComponentController!= null){
            encryptScreenMachineConfigurationComponentController.setParentController(this);
        }
        if(bruteForceComponentController!= null){
            bruteForceComponentController.setParentController(this);
            bruteForceComponentController.initial();
            bruteForceComponentController.encryptMessageEventHandler().addListener(mainController.getEncryptMessageEventListener());
        }

        setLogicEventsToController();
    }

    public void setMainController(EnigmaAppController appController) {

        mainController = appController;
    }

    public EngineDTO getEngineDetails(){

        return engineDetails;
    }
    public void setEngineDetails(EngineDTO details) {

        engineDetails = details;
    }

    public ObjectProperty<EventHandler<ActionEvent>> codeCalibrationRandomCodeOnAction(){

        return codeCalibrationComponentController.getRandomButtonOnActionListener();
    }

    public SimpleObjectProperty<MachineInfoDTO> getMachineInfoProperty(){
        return codeCalibrationComponentController.getMachineInfoProperty();
    }

    public void initialCodeCalibration(){

        codeCalibrationComponentController.initialComponent(engineDetails);
    }

    public void initialEngineDetails(){

        engineDetailsComponentController.initialComponent(engineDetails);
    }

    public ObjectProperty<EventHandler<ActionEvent>> encryptResetButtonActionProperty(){

        return encryptComponentController.getResetButtonActionProperty();
    }

    public void setDecryptionManager(EnigmaSystemEngine engine){
        bruteForceComponentController.setDecryptionManager(new DecryptionManager(engine));
    }

    public void setIsCodeConfigurationSet(Boolean codeSet){
        machineConfigurationComponentController.getIsCodeConfigurationSetProperty().set(codeSet);
    }

    public SimpleBooleanProperty getIsCodeConfigurationSetProperty(){
        return machineConfigurationComponentController.getIsCodeConfigurationSetProperty();
    }

    public void setListenerToHistoryUpdate(HistoryUpdatable listener){
        encryptComponentController.setHistoryUpdatable(listener);
    }

    public StringProperty getMachineEncryptedMessageProperty(){
        return mainController.getMachineEncryptedMessageProperty();
    }

    public void clearComponent() {
        codeCalibrationComponentController.clearComponent();
        machineConfigurationComponentController.clearComponent();
        engineDetailsComponentController.clearComponent();
        encryptScreenMachineConfigurationComponentController.clearComponent();
        bruteForceComponentController.clearComponent();
        statisticsComponentController.clearController();
        encryptComponentController.clearButtonActionListener(new ActionEvent());
        encryptComponentController.removeOldAbcFromKeyboards();
    }

    private void setLogicEventsToController(){

        mainController.statisticsUpdateEventHandler().addListener(statisticsComponentController);
        mainController.getMachineEncryptedMessageProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if(encryptTab.isSelected()) {
                        encryptComponentController.setEncryptedMessageLabel(newValue);
                    } else {
                        bruteForceComponentController.setEncryptedMessageLabel(newValue);
                    }
                });
        mainController.CodeSetEventHandler().addListener(bruteForceComponentController);
        mainController.CodeSetEventHandler().addListener(codeCalibrationComponentController);
        mainController.CodeSetEventHandler().addListener(machineConfigurationComponentController);

    }
}


