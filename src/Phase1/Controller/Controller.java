package Phase1.Controller;

import Phase1.Model.BasicFood;
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
    }

    private void handleLoadFoods() {
        model.loadFoods(); // Model reads CSV
        view.refreshTable(model.getFoodCollection().getAllFoods()); // View displays result
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
