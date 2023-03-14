import components.main.EnigmaAppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.MachineLogicUI;

public class MainClass extends Application {

    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("components/main/enigmaApp.fxml"));
        Parent load = loader.load();

        EnigmaAppController appController = loader.getController();

        MachineLogicUI machine = new MachineLogicUI(appController);
        appController.setMachineUI(machine);
        appController.initial();

        primaryStage.setTitle("Cracking The Enigma");
        Scene scene = new Scene(load, 1400.0, 900.0);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}