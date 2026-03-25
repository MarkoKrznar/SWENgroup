package Phase1.Controller;

import java.util.List;
import java.util.stream.Collectors;

import javafx.util.Pair;

import Phase1.Model.BasicFood;
import Phase1.Model.FoodComponent;
import Phase1.Model.FoodType;
import Phase1.Model.Model;
import Phase1.Model.Recipe;
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
        view.getBtnAddRecipe().setOnAction(event -> view.showRecipeAddDialog());

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

    public void handleAddRecipeDialog(Recipe recipe) {
        if (model.getFoodCollection().containsFood(recipe.getName())) {
            view.showMessage("Recipe '" + recipe.getName() + "' already exists!");
            return;
        }
        model.addRecipe(recipe);
        view.refreshTable(model.getFoodCollection().getAllFoods());
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
