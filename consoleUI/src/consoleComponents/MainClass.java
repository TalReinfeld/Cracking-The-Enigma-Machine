package consoleComponents;

public class MainClass {

    public static void main(String[] args) {

        MachineUI enigmaMachine = new MachineUI();
        try {
            enigmaMachine.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
