package Phase1.Controller;

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
    }

    private void handleLoadFoods() {
        model.loadFoods(); // Model reads CSV
        view.refreshTable(model.getAllFoods()); // View displays result
    }
}
