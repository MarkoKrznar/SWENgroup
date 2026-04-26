package Phase1.Controller.Command;

import Phase1.Model.Exercise;
import Phase1.Model.Model;

public class AddExerciseCommand implements Command {

    private final Model model;
    private final Exercise exercise;

    public AddExerciseCommand(Model model, Exercise exercise) {
        this.model = model;
        this.exercise = exercise;
    }

    @Override
    public void execute() {
        model.addExercise(exercise);
    }
}