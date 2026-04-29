package Phase1.Controller.Command;

import Phase1.Model.Model;

public class AddLogEntryCommand implements Command {

    private final Model model;
    private final String foodName;
    private final double servings;
    private int addedIndex = -1;

    public AddLogEntryCommand(Model model, String foodName, double servings) {
        this.model = model;
        this.foodName = foodName;
        this.servings = servings;
    }

    @Override
    public void execute() {
        model.addLogEntry(foodName, servings);
        addedIndex = model.getSelectedLogEntries().size() - 1;
    }

    @Override
    public void undo() {
        if (addedIndex >= 0) {
            model.deleteLogEntry(addedIndex);
        }
    }
}