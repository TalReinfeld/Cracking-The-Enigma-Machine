
package components.header;

import components.main.EnigmaAppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

public class HeaderController {
    EnigmaAppController mainController;
    @FXML
    private Button loadFileButton;
    @FXML
    private Label filePathLabel;

    public void setMainController(EnigmaAppController appController) {
        this.mainController = appController;
    }

    public void initial() {
        this.filePathLabel.textProperty().bind(this.mainController.selectedFileProperty());
    }

    @FXML
    public void loadFileActionListener(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("load xml file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File chosenFile = fileChooser.showOpenDialog((Window)null);
        if (chosenFile != null) {
            this.mainController.setSelectedFile(chosenFile.getAbsolutePath());
        }
    }
}
