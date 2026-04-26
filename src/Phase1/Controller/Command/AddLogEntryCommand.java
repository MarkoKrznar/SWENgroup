package Phase1.Controller.Command;

import Phase1.Model.Model;

public class AddLogEntryCommand implements Command {

    private final Model model;
    private final String foodName;
    private final double servings;

    public AddLogEntryCommand(Model model, String foodName, double servings) {
        this.model = model;
        this.foodName = foodName;
        this.servings = servings;
    }

    @Override
    public void execute() {
        model.addLogEntry(foodName, servings);
    }
}