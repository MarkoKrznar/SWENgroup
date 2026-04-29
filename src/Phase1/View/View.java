package Phase1.View;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Phase1.Controller.Controller;
import Phase1.Model.BasicFood;
import Phase1.Model.Exercise;
import Phase1.Model.FoodComponent;
import Phase1.Model.FoodType;
import Phase1.Model.LogEntry;
import Phase1.Model.Model;
import Phase1.Model.Observer;
import Phase1.Model.Recipe;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Pair;

public class View extends Application implements Observer {

    private Button loadFoodsButton = new Button("Load Foods");
    private Button loadLogButton = new Button("Load Log");
    private Button loadExerciseBtn = new Button("Load Exercise");
    private Button addExerciseBtn = new Button("Add Exercise");
    private Button saveLogButton = new Button("Save Log");
    private Button showBasicFoodsButton = new Button("Show Basic Foods");
    private Button showRecipesButton = new Button("Show Recipes");
    private Button addFoodButton = new Button("Add Food");
    private Button btnAddRecipe = new Button("Add Recipe");
    private Button btnUndo = new Button("Undo");
    private Button btnRedo = new Button("Redo");

    private DatePicker datePicker = new DatePicker(LocalDate.now());
    private Button addLogEntryButton = new Button("Add To Daily Log");
    private Button deleteLogEntryButton = new Button("Delete Selected Entry");
    private Button updateDailyDataButton = new Button("Update Weight/Limit");

    private ChoiceBox<String> chooseFoodForLog = new ChoiceBox<>();
    private TextField tfLogServings = new TextField();

    private ChoiceBox<String> chooseExerciseForLog = new ChoiceBox<>();
    private TextField tfExerciseMinutes = new TextField();
    private Button addExerciseEntryButton = new Button("Add Exercise To Log");

    private TextField tfWeight = new TextField();
    private TextField tfCalorieLimit = new TextField();

    private Label lblTotalCalories = new Label("Total calories: 0.0");
    private Label lblLimitStatus = new Label("Status: Within limit");
    private Label lblCaloriesExpended = new Label("Calories expended: 0.0");
    private Label lblNetCalories = new Label("Net calories: 0.0");
    private Recipe recipe;

    private PieChart macrosPieChart = new PieChart();

    private TableView<FoodComponent> foodsTable = new TableView<>();
    private TableView<LogEntry> logTable = new TableView<>();

    private List<FoodComponent> allFoods = new ArrayList<>();
    private ObservableList<FoodComponent> tableData = FXCollections.observableArrayList();
    private ObservableList<LogEntry> logTableData = FXCollections.observableArrayList();
    private ObservableList<Exercise> exerciseTableData = FXCollections.observableArrayList();

    private TableView<Exercise> exerciseTable = new TableView<>();

    private Controller controller;
    private Model model;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Wellness Manager");

        VBox leftSection = createFoodSection();
        VBox rightSection = createDailyLogSection();
        VBox exerciseSection = createExerciseSection();

        VBox leftColumn = new VBox(10, leftSection, exerciseSection);

        HBox main = new HBox(20, leftColumn, rightSection);
        main.setPadding(new Insets(15));

        Scene scene = new Scene(main, 1450, 950);
        stage.setScene(scene);
        stage.show();

        controller = new Controller(new Model(), this);
    }

    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public void update() {
        if (model == null) {
            return;
        }

        LocalDate modelDate = model.getSelectedDate();
        if (datePicker.getValue() == null || !datePicker.getValue().equals(modelDate)) {
            datePicker.setValue(modelDate);
        }

        List<FoodComponent> foods = model.getFoodCollection().getAllFoods();
        refreshTable(foods);
        refreshFoodSelector(foods);

        List<Exercise> exercises = model.getExerciseCollection().getAllExercises();
        refreshExerciseTable(exercises);
        refreshExerciseSelector(exercises);

        refreshLogTable(model.getSelectedLogEntries());
        setDailyDataFields(
                model.getSelectedLogWeight(),
                model.getSelectedLogCalorieLimit());

        updateDailySummary(
                model.getSelectedLogTotalCalories(),
                model.getSelectedLogCalorieLimit(),
                model.selectedDayIsOverLimit(),
                model.getSelectedLogFatPercent(),
                model.getSelectedLogCarbPercent(),
                model.getSelectedLogProteinPercent(),
                model.getSelectedLog().getTotalCaloriesExpended(),
                model.getSelectedLog().getNetCalories());
    }

    // Food Section
    @SuppressWarnings("unchecked")
    private VBox createFoodSection() {
        VBox foodSection = new VBox(10);
        foodSection.setAlignment(Pos.TOP_CENTER);

        TableColumn<FoodComponent, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<FoodComponent, Number> caloriesColumn = new TableColumn<>("Calories");
        caloriesColumn.setCellValueFactory(new PropertyValueFactory<>("calories"));

        TableColumn<FoodComponent, Number> fatsColumn = new TableColumn<>("Fats");
        fatsColumn.setCellValueFactory(new PropertyValueFactory<>("fat"));

        TableColumn<FoodComponent, Number> carbsColumn = new TableColumn<>("Carbs");
        carbsColumn.setCellValueFactory(new PropertyValueFactory<>("carb"));

        TableColumn<FoodComponent, Number> proteinsColumn = new TableColumn<>("Proteins");
        proteinsColumn.setCellValueFactory(new PropertyValueFactory<>("protein"));

        foodsTable.getColumns().addAll(
                nameColumn, caloriesColumn, fatsColumn, carbsColumn, proteinsColumn);
        foodsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // this
        foodsTable.setEditable(false);
        foodsTable.setPrefHeight(330);
        foodsTable.setPrefWidth(650);
        foodsTable.setItems(tableData);

        FlowPane foodControlPanel = new FlowPane(10, 10);
        foodControlPanel.setAlignment(Pos.CENTER);
        foodControlPanel.setStyle("-fx-background-color: #e3e3e3;");
        foodControlPanel.setPadding(new Insets(10));
        foodControlPanel.getChildren().addAll(
                loadFoodsButton, loadLogButton, saveLogButton,
                showBasicFoodsButton, showRecipesButton);

        VBox addFoodSection = new VBox(10);
        addFoodSection.setAlignment(Pos.CENTER);
        addFoodSection.setStyle("-fx-background-color: #e3e3e3;");
        addFoodSection.setPadding(new Insets(10));
        addFoodSection.getChildren().addAll(addFoodButton, btnAddRecipe);

        foodSection.getChildren().addAll(
                new Label("Foods / Recipes"),
                foodControlPanel,
                foodsTable,
                addFoodSection);

        return foodSection;
    }

    // Daily Dialog
    @SuppressWarnings("unchecked")
    private VBox createDailyLogSection() {
        VBox logSection = new VBox(12);
        logSection.setAlignment(Pos.TOP_CENTER);

        HBox dateRow = new HBox(10, new Label("Selected date:"), datePicker);
        dateRow.setAlignment(Pos.CENTER_LEFT);

        chooseFoodForLog.setPrefWidth(220);
        tfLogServings.setPromptText("Servings");

        HBox addEntryRow = new HBox(10,
                new Label("Food:"), chooseFoodForLog,
                new Label("Servings:"), tfLogServings,
                addLogEntryButton);
        addEntryRow.setAlignment(Pos.CENTER_LEFT);

        tfWeight.setPromptText("Weight");
        tfCalorieLimit.setPromptText("Calorie limit");

        HBox dailyDataRow = new HBox(10,
                new Label("Weight:"), tfWeight,
                new Label("Calorie limit:"), tfCalorieLimit,
                updateDailyDataButton);
        dailyDataRow.setAlignment(Pos.CENTER_LEFT);

        chooseExerciseForLog.setPrefWidth(220);
        tfExerciseMinutes.setPromptText("Minutes");

        HBox addExerciseRow = new HBox(10,
                new Label("Exercise:"), chooseExerciseForLog,
                new Label("Minutes:"), tfExerciseMinutes,
                addExerciseEntryButton);
        addExerciseRow.setAlignment(Pos.CENTER_LEFT);

        TableColumn<LogEntry, String> logFoodCol = new TableColumn<>("Food");
        logFoodCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFood().getName()));

        TableColumn<LogEntry, Number> logServingsCol = new TableColumn<>("Servings");
        logServingsCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getServings()));

        TableColumn<LogEntry, Number> logCaloriesCol = new TableColumn<>("Total Calories");
        logCaloriesCol
                .setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotalCalories()));

        logTable.getColumns().addAll(logFoodCol, logServingsCol, logCaloriesCol);
        logTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        logTable.setPrefHeight(700);
        logTable.setPrefWidth(650);
        logTable.setItems(logTableData);

        macrosPieChart.setPrefSize(400, 350);
        macrosPieChart.setMinSize(400, 350);
        macrosPieChart.setMaxSize(400, 350);
        macrosPieChart.setLegendVisible(true);
        macrosPieChart.setLabelsVisible(true);

        VBox summaryBox = new VBox(8,
                lblTotalCalories,
                lblCaloriesExpended,
                lblNetCalories,
                lblLimitStatus,
                macrosPieChart);
        summaryBox.setPadding(new Insets(10));
        summaryBox.setStyle("-fx-background-color: #e3e3e3;");

        logSection.getChildren().addAll(
                new Label("Daily Log"),
                dateRow,
                addEntryRow,
                addExerciseRow,
                dailyDataRow,
                logTable,
                deleteLogEntryButton,
                btnUndo,
                btnRedo,
                summaryBox);

        return logSection;
    }

    // FoodDialog
    public void showAddFoodDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Basic Food");
        dialog.setHeaderText("Enter the details of the new food");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField tfName = new TextField();
        TextField tfCalories = new TextField();
        TextField tfFat = new TextField();
        TextField tfCarb = new TextField();
        TextField tfProtein = new TextField();

        tfName.setPromptText("Name");
        tfCalories.setPromptText("Calories");
        tfFat.setPromptText("Fats");
        tfCarb.setPromptText("Carbs");
        tfProtein.setPromptText("Proteins");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(tfName, 1, 0);
        grid.add(new Label("Calories:"), 0, 1);
        grid.add(tfCalories, 1, 1);
        grid.add(new Label("Fats:"), 0, 2);
        grid.add(tfFat, 1, 2);
        grid.add(new Label("Carbs:"), 0, 3);
        grid.add(tfCarb, 1, 3);
        grid.add(new Label("Proteins:"), 0, 4);
        grid.add(tfProtein, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        javafx.application.Platform.runLater(() -> tfName.requestFocus());

        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                if (tfName.getText().trim().isEmpty() ||
                        tfCalories.getText().trim().isEmpty() ||
                        tfFat.getText().trim().isEmpty() ||
                        tfCarb.getText().trim().isEmpty() ||
                        tfProtein.getText().trim().isEmpty()) {
                    showMessage("Please fill in all fields!");
                    return;
                }

                try {
                    BasicFood newFood = new BasicFood(
                            tfName.getText().trim(),
                            Double.parseDouble(tfCalories.getText().trim()),
                            Double.parseDouble(tfFat.getText().trim()),
                            Double.parseDouble(tfCarb.getText().trim()),
                            Double.parseDouble(tfProtein.getText().trim()));

                    controller.handleAddFoodDialog(newFood);

                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Input Error");
                    alert.setHeaderText("Invalid number format");
                    alert.setContentText("Please enter valid numbers for calories, fats, carbs, and proteins.");
                    alert.showAndWait();
                }
            }
        });
    }

    // Recipe Dialog
    public Pair<Boolean, Recipe> showRecipeAddDialog() {
        recipe = null;
        Dialog<Pair<Boolean, Recipe>> dialog = new Dialog<>();
        dialog.setTitle("Add Recipe");
        dialog.setHeaderText("Enter Recipe Details");

        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField recipeName = new TextField();
        recipeName.setPromptText("Recipe Name");

        ChoiceBox<String> chooseFoods = new ChoiceBox<>();
        for (FoodComponent food : allFoods) {
            chooseFoods.getItems().add(food.getName());
        }

        TextField servings = new TextField();
        servings.setPromptText("Servings");

        TextField ingredients = new TextField();
        ingredients.setEditable(false);

        Button btnAdd = new Button("Add ingredient");

        btnAdd.setOnAction(event -> {
            if (!recipeName.getText().trim().isEmpty()
                    && chooseFoods.getValue() != null
                    && !servings.getText().trim().isEmpty()) {

                String foodName = chooseFoods.getValue();
                FoodComponent selectedFood = null;

                for (FoodComponent food : allFoods) {
                    if (food.getName().equals(foodName)) {
                        selectedFood = food;
                        break;
                    }
                }

                if (selectedFood != null) {
                    try {
                        double qty = Double.parseDouble(servings.getText().trim());

                        if (qty <= 0) {
                            showMessage("Servings must be greater than 0!");
                            return;
                        }

                        if (recipe == null) {
                            recipe = new Recipe(recipeName.getText().trim());
                        }

                        if (selectedFood.getName().equals(recipeName.getText().trim())) {
                            showMessage("A recipe cannot contain itself.");
                            return;
                        }

                        if (!ingredients.getText().isEmpty()) {
                            ingredients.appendText(", ");
                        }

                        ingredients.appendText(selectedFood.getName() + " x" + qty);
                        recipe.addIngredient(selectedFood, qty);
                        servings.clear();

                    } catch (NumberFormatException e) {
                        showMessage("Servings must be a valid number!");
                    }
                } else {
                    showMessage("Selected food not found in the list!");
                }
            } else {
                showMessage("Please fill in all inputs!");
            }
        });

        grid.add(new Label("Recipe Name:"), 0, 0);
        grid.add(recipeName, 1, 0);
        grid.add(new Label("Choose Food/Recipe:"), 0, 1);
        grid.add(chooseFoods, 1, 1);
        grid.add(new Label("Ingredients:"), 0, 2);
        grid.add(ingredients, 1, 2);
        grid.add(new Label("Servings:"), 0, 3);
        grid.add(servings, 1, 3);
        grid.add(btnAdd, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                return new Pair<>(true, recipe);
            }
            return new Pair<>(false, null);
        });

        Pair<Boolean, Recipe> result = dialog.showAndWait().orElse(new Pair<>(false, null));

        if (result.getKey() && result.getValue() != null) {
            controller.handleAddRecipeDialog(result.getValue());
        }

        return result;
    }

    // Exercise section
    @SuppressWarnings("unchecked")
    private VBox createExerciseSection() {
        VBox exerciseSection = new VBox(10);
        exerciseSection.setAlignment(Pos.CENTER);

        exerciseTable = new TableView<>();
        TableColumn<Exercise, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Exercise, Double> caloriesColumn = new TableColumn<>("Calories");
        caloriesColumn.setCellValueFactory(new PropertyValueFactory<>("calories"));

        TableColumn<Exercise, Double> caloriesPerHourColumn = new TableColumn<>("Cal/Hour");
        caloriesPerHourColumn.setCellValueFactory(new PropertyValueFactory<>("caloriesPerHour"));

        // TODO: Log button

        // Adding all the columns to the table's list of columns
        exerciseTable.getColumns().addAll(nameColumn, caloriesColumn, caloriesPerHourColumn);
        exerciseTable.setEditable(false);
        exerciseTable.setPrefHeight(330);
        exerciseTable.setPrefWidth(650);
        exerciseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        exerciseTable.setItems(exerciseTableData);

        FlowPane exerciseControlPanel = new FlowPane(10, 10);
        exerciseControlPanel.setAlignment(Pos.CENTER);
        exerciseControlPanel.setStyle("-fx-background-color: #e3e3e3;");
        exerciseControlPanel.getChildren().add(loadExerciseBtn);

        VBox addExerciseSection = new VBox(10);
        addExerciseSection.setAlignment(Pos.CENTER);
        addExerciseSection.setStyle("-fx-background-color: #e3e3e3;");
        addExerciseSection.getChildren().add(addExerciseBtn);

        exerciseSection.getChildren().addAll(exerciseControlPanel, exerciseTable, addExerciseSection);
        return exerciseSection;

    }

    // Exercise Dialog
    public Pair<Boolean, Exercise> showAddExerciseDialog() {
        Dialog<Pair<Boolean, Exercise>> dialog = new Dialog<>();
        dialog.setTitle("Add Exercise");
        dialog.setHeaderText("Enter Exercise Details");

        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField exerciseName = new TextField();
        exerciseName.setPromptText("Exercise Name");

        TextField calories = new TextField();
        calories.setPromptText("Calories");

        TextField caloriesPerHour = new TextField();
        caloriesPerHour.setPromptText("Calories per hour");

        grid.add(new Label("Exercise Name:"), 0, 0);
        grid.add(exerciseName, 1, 0);
        grid.add(new Label("Calories:"), 0, 1);
        grid.add(calories, 1, 1);

        grid.add(new Label("Calories per hour:"), 0, 2);
        grid.add(caloriesPerHour, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                try {
                    String name = exerciseName.getText();
                    double cal = Double.parseDouble(calories.getText());
                    double calPerHour = Double.parseDouble(caloriesPerHour.getText());
                    Exercise exercise = new Exercise(name, cal, calPerHour);
                    return new Pair<>(true, exercise);
                } catch (NumberFormatException e) {
                    showAlert("Please enter valid numbers for calories and calories per hour.");
                    return new Pair<>(false, null);
                }
            }
            return new Pair<>(false, null);
        });

        return dialog.showAndWait().orElse(new Pair<>(false, null));
    }

    private void showAlert(String errormsg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(errormsg);
        alert.showAndWait();
    }

    public void showMessage(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Wellness Manager");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // Exercise section methods
    public void refreshExerciseTable(List<Exercise> exercises) {
        exerciseTableData.setAll(exercises);
    }

    // Refresh exercise selector
    public void refreshExerciseSelector(List<Exercise> exercises) {
        chooseExerciseForLog.getItems().clear();
        for (Exercise exercise : exercises) {
            chooseExerciseForLog.getItems().add(exercise.getName());
        }
    }

    // Food section methods
    public void refreshTable(List<FoodComponent> foods) {
        tableData.setAll(foods);
        for (FoodComponent food : foods) {
            if (allFoods.stream().noneMatch(f -> f.getName().equals(food.getName()))) {
                allFoods.add(food);
            }
        }
    }

    public void refreshFoodSelector(List<FoodComponent> foods) {
        chooseFoodForLog.getItems().clear();
        allFoods.clear();
        allFoods.addAll(foods);

        for (FoodComponent food : foods) {
            chooseFoodForLog.getItems().add(food.getName());
        }
    }

    public void refreshLogTable(List<LogEntry> entries) {
        logTableData.setAll(entries);
    }

    public void updateDailySummary(double totalCalories, double calorieLimit,
            boolean overLimit, int fatPercent, int carbPercent, int proteinPercent,
            double caloriesExpended, double netCalories) {

        lblTotalCalories.setText(String.format(
                "Total calories consumed: %.2f / %.2f", totalCalories, calorieLimit));
        lblCaloriesExpended.setText(String.format(
                "Total calories burned: %.2f", caloriesExpended));
        lblNetCalories.setText(String.format(
                "Net calories: %.2f", netCalories));
        lblLimitStatus.setText(overLimit ? "Status: Over limit" : "Status: Within limit");

        // Hide chart if no food logged yet
        if (fatPercent == 0 && carbPercent == 0 && proteinPercent == 0) {
            macrosPieChart.setData(FXCollections.observableArrayList());
            return;
        }

        macrosPieChart.setVisible(true); // ← show it again

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Fat " + fatPercent + "%", fatPercent),
                new PieChart.Data("Carbs " + carbPercent + "%", carbPercent),
                new PieChart.Data("Protein " + proteinPercent + "%", proteinPercent));
        macrosPieChart.setData(pieData);
    }

    public void setDailyDataFields(double weight, double calorieLimit) {
        tfWeight.setText(Double.toString(weight));
        tfCalorieLimit.setText(Double.toString(calorieLimit));
    }

    public String getSelectedFoodNameForLog() {
        return chooseFoodForLog.getValue();
    }

    public double getServingsForLog() {
        return Double.parseDouble(tfLogServings.getText().trim());
    }

    public int getSelectedLogEntryIndex() {
        return logTable.getSelectionModel().getSelectedIndex();
    }

    public double getWeightInputValue() {
        return Double.parseDouble(tfWeight.getText().trim());
    }

    public double getCalorieLimitInputValue() {
        return Double.parseDouble(tfCalorieLimit.getText().trim());
    }

    public void setDate(LocalDate date) {
        datePicker.setValue(date);
    }

    public static void main(String[] args) {
        launch();
    }

    public Button getLoadFoodsButton() {
        return loadFoodsButton;
    }

    public Button getLoadExercisesButton() {
        return loadExerciseBtn;
    }

    public Button getLoadLogButton() {
        return loadLogButton;
    }

    public Button getSaveLogButton() {
        return saveLogButton;
    }

    public Button getShowBasicFoodsButton() {
        return showBasicFoodsButton;
    }

    public Button getShowRecipesButton() {
        return showRecipesButton;
    }

    public Button getAddFoodButton() {
        return addFoodButton;
    }

    public Button getBtnAddRecipe() {
        return btnAddRecipe;
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }

    public Button getAddLogEntryButton() {
        return addLogEntryButton;
    }

    public Button getDeleteLogEntryButton() {
        return deleteLogEntryButton;
    }

    public Button getUpdateDailyDataButton() {
        return updateDailyDataButton;
    }

    public Button getAddExerciseButton() {
        return addExerciseBtn;
    }

    public String getSelectedExerciseNameForLog() {
        return chooseExerciseForLog.getValue();
    }

    public double getExerciseMinutesForLog() {
        return Double.parseDouble(tfExerciseMinutes.getText().trim());
    }

    public Button getAddExerciseEntryButton() {
        return addExerciseEntryButton;
    }

    public Button getUndoButton() {
        return btnUndo;
    }

    public Button getRedoButton() {
        return btnRedo;
    }

}