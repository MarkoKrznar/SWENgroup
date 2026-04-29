package Phase1.Controller.Command;

import Phase1.Model.LogEntry;
import Phase1.Model.Model;

public class DeleteLogEntryCommand implements Command {

    private final Model model;
    private final int index;
    private LogEntry deletedEntry;

    public DeleteLogEntryCommand(Model model, int index) {
        this.model = model;
        this.index = index;
    }

    @Override
    public void execute() {
        deletedEntry = model.getSelectedLogEntry(index);
        model.deleteLogEntry(index);
    }

    @Override
    public void undo() {
        if (deletedEntry != null) {
            model.addLogEntryObjectAt(index, deletedEntry);
        }
    }
}