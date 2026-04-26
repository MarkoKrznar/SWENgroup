package Phase1.Controller.Command;

import Phase1.Model.FoodComponent;
import Phase1.Model.Model;

public class AddFoodCommand implements Command {

    private final Model model;
    private final FoodComponent food;

    public AddFoodCommand(Model model, FoodComponent food) {
        this.model = model;
        this.food = food;
    }

    @Override
    public void execute() {
        model.addFood(food);
    }
}
