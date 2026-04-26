package Phase1.Controller.Command;

import Phase1.Model.Model;
import Phase1.Model.Recipe;

public class AddRecipeCommand implements Command {

    private final Model model;
    private final Recipe recipe;

    public AddRecipeCommand(Model model, Recipe recipe) {
        this.model = model;
        this.recipe = recipe;
    }

    @Override
    public void execute() {
        model.addRecipe(recipe);
    }
}