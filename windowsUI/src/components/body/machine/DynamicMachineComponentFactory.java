package components.body.machine;

import components.body.details.ReflectorParent;
import components.body.details.RotorParent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class DynamicMachineComponentFactory {

    public static RotorController createRotorOnPane(Pane parentPane, RotorParent parentController){

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RotorController.class.getResource("rotorComponent.fxml"));
            SplitPane newRotor = loader.load();
            RotorController newController = loader.getController();

            parentPane.getChildren().add(newRotor);
            newController.setParentController(parentController);

            return newController;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ReflectorController createReflectorOnPane(Pane parentPane, ReflectorParent parentController){

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ReflectorController.class.getResource("reflectorComponent.fxml"));
            SplitPane newReflector = loader.load();
            ReflectorController newController = loader.getController();

            parentPane.getChildren().add(newReflector);
            newController.setParentController(parentController);

            return newController;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
