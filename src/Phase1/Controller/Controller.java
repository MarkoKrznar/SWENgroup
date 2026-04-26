package Phase1.Controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import Phase1.Model.BasicFood;
import Phase1.Model.Exercise;
import Phase1.Model.FoodComponent;
import Phase1.Model.FoodType;
import Phase1.Model.Model;
import Phase1.Model.Recipe;
import Phase1.View.View;
import javafx.util.Pair;

public class Controller {

    private final Model model;
    private final View view;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        this.view.setModel(model);
        this.model.attach(view);
        attachEventHandlers();
        initializeApp();
    }

    private void attachEventHandlers() {
        view.getLoadFoodsButton().setOnAction(event -> handleLoadFoods());
        view.getLoadLogButton().setOnAction(event -> handleLoadLog());
        view.getAddFoodButton().setOnAction(event -> view.showAddFoodDialog());
        view.getShowRecipesButton().setOnAction(event -> handleShowRecipes());
        view.getShowBasicFoodsButton().setOnAction(event -> handleShowBasicFoods());
        view.getBtnAddRecipe().setOnAction(event -> view.showRecipeAddDialog());

        view.getDatePicker().setOnAction(event -> handleDateChange());
        view.getAddLogEntryButton().setOnAction(event -> handleAddLogEntry());
        view.getDeleteLogEntryButton().setOnAction(event -> handleDeleteLogEntry());
        view.getUpdateDailyDataButton().setOnAction(event -> handleUpdateDailyData());
        view.getSaveLogButton().setOnAction(event -> handleSaveLog());
        view.getLoadExercisesButton().setOnAction(event -> handleLoadExercises());
        view.getAddExerciseButton().setOnAction(event -> handleAddExercise());
        view.getAddExerciseEntryButton().setOnAction(event -> handleAddExerciseEntry());
    }

    private void initializeApp() {
        model.setSelectedDate(LocalDate.now());
    }

    private void handleLoadFoods() {
        model.loadFoods();
    }

    private void handleLoadExercises() {
        model.loadExerciseFromCsv();
    }

    // Add exercise dialog handler
    private void handleAddExercise() {
        Pair<Boolean, Exercise> result = view.showAddExerciseDialog();
        if (result.getKey()) {
            handleAddExerciseDialogResult(result.getValue());
        }
    }

    // Handler for adding exercise to log
    private void handleAddExerciseEntry() {
        try {
            String exerciseName = view.getSelectedExerciseNameForLog();
            double minutes = view.getExerciseMinutesForLog();

            if (exerciseName == null || exerciseName.isEmpty()) {
                view.showMessage("Choose an exercise first.");
                return;
            }

            model.addExerciseEntry(exerciseName, minutes);

        } catch (NumberFormatException e) {
            view.showMessage("Minutes must be a valid number.");
        }
    }

    // Process the result from the add exercise dialog
    public void handleAddExerciseDialogResult(Exercise exercise) {
        if (exercise != null) {
            model.addExercise(exercise);
            try {
                model.saveExercisesToFile();
            } catch (IOException e) {
                view.showMessage("Error saving exercises to CSV: " + e.getMessage());
            }
        }
    }

    private void handleLoadLog() {
        model.loadData();
        view.showMessage("Log loaded correctly.");
    }

    private void handleDateChange() {
        LocalDate selected = view.getDatePicker().getValue();
        if (selected == null) {
            return;
        }

        model.setSelectedDate(selected);
    }

    private void handleAddLogEntry() {
        try {
            String foodName = view.getSelectedFoodNameForLog();
            double servings = view.getServingsForLog();

            if (foodName == null || foodName.isEmpty()) {
                view.showMessage("Choose a food first.");
                return;
            }

            model.addLogEntry(foodName, servings);

        } catch (NumberFormatException e) {
            view.showMessage("Servings must be a valid number.");
        } catch (IllegalArgumentException e) {
            view.showMessage(e.getMessage());
        }
    }

    private void handleDeleteLogEntry() {
        int selectedIndex = view.getSelectedLogEntryIndex();
        if (selectedIndex < 0) {
            view.showMessage("Select a log entry to delete.");
            return;
        }

        boolean deleted = model.deleteLogEntry(selectedIndex);
        if (!deleted) {
            view.showMessage("Could not delete the selected log entry.");
            return;
        }

    }

    private void handleUpdateDailyData() {
        try {
            double weight = view.getWeightInputValue();
            double calorieLimit = view.getCalorieLimitInputValue();

            model.updateWeight(weight);
            model.updateCalorieLimit(calorieLimit);

            view.showMessage("Daily data updated correctly.");

        } catch (NumberFormatException e) {
            view.showMessage("Weight and calorie limit must be valid numbers.");
        } catch (IllegalArgumentException e) {
            view.showMessage(e.getMessage());
        }
    }

    private void handleSaveLog() {
        model.saveData();
        view.showMessage("log.csv saved correctly.");
    }

    private void handleShowRecipes() {
        List<FoodComponent> filtered = model.getFoodCollection().getAllFoods()
                .stream()
                .filter(f -> f.getType() == FoodType.RECIPE)
                .collect(Collectors.toList());

        view.refreshTable(filtered);
    }

    private void handleShowBasicFoods() {
        List<FoodComponent> filtered = model.getFoodCollection().getAllFoods()
                .stream()
                .filter(f -> f.getType() == FoodType.BASIC_FOOD)
                .collect(Collectors.toList());

        view.refreshTable(filtered);
    }

    public void handleAddRecipeDialog(Recipe recipe) {
        if (recipe == null) {
            return;
        }

        if (model.getFoodCollection().containsFood(recipe.getName())) {
            view.showMessage("Recipe '" + recipe.getName() + "' already exists!");
            return;
        }

        model.addRecipe(recipe);
    }

    public void handleAddFoodDialog(BasicFood food) {
        if (model.getFoodCollection().containsFood(food.getName())) {
            view.showMessage("Food '" + food.getName() + "' already exists!");
            return;
        }

        model.addFood(food);
    }

}