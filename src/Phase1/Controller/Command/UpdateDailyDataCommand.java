package Phase1.Controller.Command;

import Phase1.Model.Model;

public class UpdateDailyDataCommand implements Command {

    private final Model model;
    private final double newWeight;
    private final double newCalorieLimit;

    private double oldWeight;
    private double oldCalorieLimit;

    public UpdateDailyDataCommand(Model model, double weight, double calorieLimit) {
        this.model = model;
        this.newWeight = weight;
        this.newCalorieLimit = calorieLimit;
    }

    @Override
    public void execute() {
        oldWeight = model.getSelectedLogWeight();
        oldCalorieLimit = model.getSelectedLogCalorieLimit();

        model.updateWeight(newWeight);
        model.updateCalorieLimit(newCalorieLimit);
    }

    @Override
    public void undo() {
        model.updateWeight(oldWeight);
        model.updateCalorieLimit(oldCalorieLimit);
    }
}