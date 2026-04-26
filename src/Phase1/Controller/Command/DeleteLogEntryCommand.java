package Phase1.Controller.Command;

import Phase1.Model.Model;

public class DeleteLogEntryCommand implements Command {

    private final Model model;
    private final int index;

    public DeleteLogEntryCommand(Model model, int index) {
        this.model = model;
        this.index = index;
    }

    @Override
    public void execute() {
        model.deleteLogEntry(index);
    }
}