package Phase1.Controller.Command;

import Phase1.Model.Model;

public class AddExerciseEntryCommand implements Command {

    private final Model model;
    private final String exerciseName;
    private final double minutes;
    private int addedIndex = -1;

    public AddExerciseEntryCommand(Model model, String exerciseName, double minutes) {
        this.model = model;
        this.exerciseName = exerciseName;
        this.minutes = minutes;
    }

    @Override
    public void execute() {
        model.addExerciseEntry(exerciseName, minutes);
        addedIndex = model.getSelectedLog().getExerciseEntries().size() - 1;
    }

    @Override
    public void undo() {
        if (addedIndex >= 0) {
            model.deleteExerciseEntry(addedIndex);
        }
    }
}