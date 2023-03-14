package components.body.main;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import logic.HistoryUpdatable;
import logic.events.EncryptMessageEventListener;
import logic.events.handler.MachineEventHandler;

public class EncryptController {

    private encryptParentController parentController;
    @FXML private Button processButton;
    @FXML private Button doneButton;
    @FXML private Button clearButton;
    private final ToggleGroup stateSwitchButton = new ToggleGroup();
    @FXML private ToggleButton autoStateButton;
    @FXML private ToggleButton manualStateButton;
    @FXML private Button resetButton;
    @FXML private TextField messageToEncryptTF;
    @FXML private Label encryptedMessageLabel;

    @FXML private FlowPane keyBoardFlowPane;
    @FXML private FlowPane bulbsBoardFlowPane;
    public MachineEventHandler<EncryptMessageEventListener> activateEncryptEventHandler = new MachineEventHandler<>();
    private HistoryUpdatable listener;

    public void setParentController(encryptParentController parentController) {

        this.parentController = parentController;
    }

    public void setHistoryUpdatable(HistoryUpdatable listener){
        this.listener = listener;
    }

    public void setEncryptedMessageLabel(String encryptedMessage) {

        encryptedMessageLabel.setText(encryptedMessageLabel.getText() + encryptedMessage);
    }

    public StringProperty getEncryptedMessage(){

        return encryptedMessageLabel.textProperty();
    }

    public ObjectProperty<EventHandler<ActionEvent>> getResetButtonActionProperty(){

        return resetButton.onActionProperty();
    }
    @FXML
    public void initialize(){

        doneButton.disableProperty().bind(manualStateButton.selectedProperty().not());
        processButton.disableProperty().bind(autoStateButton.selectedProperty().not());
        clearButton.disableProperty().bind(autoStateButton.selectedProperty().not());
        autoStateButton.setToggleGroup(stateSwitchButton);
        manualStateButton.setToggleGroup(stateSwitchButton);
    }

    public void setAutoStateOnly(){

        autoStateButton.setSelected(true);
        manualStateButton.setDisable(true);
        stateSwitchButton.getToggles().remove(manualStateButton);
    }

    @FXML
    void processButtonActionListener(ActionEvent event) {

        if(!encryptedMessageLabel.getText().equals("")){
            clearButtonActionListener(event);
        }
        else if(parentController instanceof BruteForceController){
            BruteForceController controller = (BruteForceController) parentController;
            String newMessage = controller.getStringWithoutSpecialChars(messageToEncryptTF.getText());
            if(controller.checkWordsInTheDictionary(newMessage)){
                activateEncryptEventHandler.fireEvent(newMessage);
                messageToEncryptTF.setEditable(false);
            }
            else new Alert(Alert.AlertType.WARNING,"Words is not in the dictionary", ButtonType.OK).show();
        }
        else {
            activateEncryptEventHandler.fireEvent(messageToEncryptTF.getText());
            messageToEncryptTF.setEditable(false);
            if (listener != null) {
                listener.updateHistory();
            }
        }
    }

    @FXML
    void clearButtonActionListener(ActionEvent event) {

        messageToEncryptTF.setText("");
        encryptedMessageLabel.setText("");
        messageToEncryptTF.setEditable(true);
        bulbsBoardFlowPane.getChildren().forEach(Button->Button.setStyle(null));
    }

    @FXML
    void doneButtonActionListener(ActionEvent event) {

        listener.updateHistory();
        clearButtonActionListener(event);
    }

    @FXML
    void onKeyTypedActionListener(KeyEvent event) {

        if(!parentController.getEngineDetails().getEngineComponentsInfo().getABC().contains(event.getCharacter().toUpperCase())){
            event.consume();
        }
        else if (manualStateButton.isSelected()) {
            activateEncryptEventHandler.fireEvent(event.getCharacter());
        }
    }

    @FXML
    void onKeyPressedActionListener(KeyEvent event) {

        if(manualStateButton.isSelected() && !event.getCode().isLetterKey()){
            event.consume();
        }
    }

    @FXML
    void stateButtonActionListener(Event event) {

        if(stateSwitchButton.getSelectedToggle() == null){
            ((ToggleButton) event.getSource()).setSelected(true);
        }else if(stateSwitchButton.getSelectedToggle() == autoStateButton) {
            doneButtonActionListener(new ActionEvent());
        }else {
            clearButtonActionListener(new ActionEvent());
        }
    }

    public void removeOldAbcFromKeyboards(){
        bulbsBoardFlowPane.getChildren().clear();
        keyBoardFlowPane.getChildren().clear();
    }

    public void createKeyboards(String abc){
        for (int i = 0; i < abc.length(); i++) {
            keyBoardFlowPane.getChildren().add(createNewButton(abc.substring(i, i + 1), true));
            bulbsBoardFlowPane.getChildren().add(createNewButton(abc.substring(i, i + 1), false));
        }
        showKeyboard();
    }

    public Button createNewButton(String note, boolean pressable){
        Button btn = new Button(note);
        btn.setDisable(true);
        if (pressable){
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    if(manualStateButton.isSelected()) {
                        messageToEncryptTF.setText(messageToEncryptTF.getText() + btn.getText());
                        activateEncryptEventHandler.fireEvent(btn.getText());

                        for (Node btUn : bulbsBoardFlowPane.getChildren()) {
                            if (((Button) btUn).getText().equals(encryptedMessageLabel.getText().substring(encryptedMessageLabel.getText().length() - 1))) {
                                btUn.setStyle("-fx-background-color: #ff0000; ");
                            } else {
                                btUn.setStyle(null);
                            }
                        }
                    }
                }
            });
        }

        return btn;
    }

    public void showKeyboard(){
        keyBoardFlowPane.getChildren().forEach(Button->Button.setDisable(false));
        bulbsBoardFlowPane.getChildren().forEach(Button->Button.setDisable(false));
    }


}
