package Phase1.Controller;

import java.util.List;
import java.util.stream.Collectors;

import Phase1.Model.BasicFood;
import Phase1.Model.FoodComponent;
import Phase1.Model.FoodType;
import Phase1.Model.Model;
import Phase1.View.View;

public class Controller {

    private Model model;
    private View view;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        attachEventHandlers();
    }

    private void attachEventHandlers() {
        view.getLoadFoodsButton().setOnAction(event -> handleLoadFoods());
        view.getAddFoodButton().setOnAction(event -> view.showAddFoodDialog());
        view.getShowRecipesButton().setOnAction(event -> handleShowRecipes());
    }

    private void handleLoadFoods() {
        model.loadFoods(); // Model reads CSV
        view.refreshTable(model.getFoodCollection().getAllFoods()); // View displays result as list of FoodComponents
    }

    private void handleShowRecipes() {
        List<FoodComponent> filtered = model.getFoodCollection().getAllFoods()
                .stream()
                .filter(f -> f.getType() == FoodType.RECIPE)
                .collect(Collectors.toList());
        view.refreshTable(filtered);
    }

    public void handleAddFoodDialog(BasicFood food) {
        if (model.getFoodCollection().containsFood(food.getName())) {
            view.showMessage("Food '" + food.getName() + "' already exists!");
            return;
        }

        // Add to model (memory + CSV)
        model.addFood(food);

        // Refresh table
        view.refreshTable(model.getFoodCollection().getAllFoods());

    }
}
