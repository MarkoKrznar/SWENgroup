package Phase1.Controller.Command;

import Phase1.Model.Model;

public class UpdateDailyDataCommand implements Command {

    private final Model model;
    private final double weight;
    private final double calorieLimit;

    public UpdateDailyDataCommand(Model model, double weight, double calorieLimit) {
        this.model = model;
        this.weight = weight;
        this.calorieLimit = calorieLimit;
    }

    @Override
    public void execute() {
        model.updateWeight(weight);
        model.updateCalorieLimit(calorieLimit);
    }
}