package logic;

public abstract class SubComponent implements UIComponentController {

    private UIComponentController parentController;

    public void setParentController(UIComponentController parentController) {
        this.parentController = parentController;
    }

    public UIComponentController getParentController() {
        return parentController;
    }
}
