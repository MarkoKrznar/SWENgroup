package Phase1.View;

import java.util.Map;
import Phase1.Controller.Controller;
import Phase1.Model.BasicFood;
import Phase1.Model.FoodComponent;
import Phase1.Model.Model;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class View extends Application {

    private Button loadFoodsButton = new Button("Load Foods");
    private Button loadLogButton = new Button("Load Log");
    private Button showBasicFoodsButton = new Button("Show Basic Foods");
    private Button showRecipesButton = new Button("Show Recipes");
    private Button addFoodButton = new Button("Add Food");
    private Button btnAddRecipe = new Button("Add Recipe");

    private TableView<FoodComponent> foodsTable = new TableView<>();
    ObservableList<FoodComponent> tableData = FXCollections.observableArrayList();

    private Controller controller;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Wellness Manager");

        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(10, 10, 10, 10));

        VBox foodSection = createFoodSection();
        hBox.getChildren().addAll(foodSection);

        VBox root = new VBox();
        root.getChildren().addAll(hBox);

        Scene scene = new Scene(root, 1500, 850);
        stage.setScene(scene);
        stage.show();

        controller = new Controller(new Model(), this);
    }

    @SuppressWarnings("unchecked")
    private VBox createFoodSection() {
        VBox foodSection = new VBox(10);
        foodSection.setAlignment(Pos.CENTER);

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
        foodsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        foodsTable.setEditable(false);
        foodsTable.setPrefHeight(300);
        foodsTable.setPrefWidth(600);
        foodsTable.setItems(tableData);

        FlowPane foodControlPanel = new FlowPane(10, 10);
        foodControlPanel.setAlignment(Pos.CENTER);
        foodControlPanel.setStyle("-fx-background-color: #e3e3e3;");
        foodControlPanel.getChildren().addAll(
                loadFoodsButton, showBasicFoodsButton, showRecipesButton);

        VBox addFoodSection = new VBox(10);
        addFoodSection.setAlignment(Pos.CENTER);
        addFoodSection.setStyle("-fx-background-color: #e3e3e3;");
        addFoodSection.getChildren().addAll(addFoodButton, btnAddRecipe);

        foodSection.getChildren().addAll(foodControlPanel, foodsTable, addFoodSection);
        return foodSection;
    }

    // Adding Dialog window when the user is adding a new food!
    public void showAddFoodDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Basic Food");
        dialog.setHeaderText("Enter the details of the new food");

        // ── Build form grid ──────────────────────────────────
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

                // Step 1 — validate empty fields
                if (tfName.getText().trim().isEmpty() ||
                        tfCalories.getText().trim().isEmpty() ||
                        tfFat.getText().trim().isEmpty() ||
                        tfCarb.getText().trim().isEmpty() ||
                        tfProtein.getText().trim().isEmpty()) {
                    showMessage("Please fill in all fields!");
                    return;
                }
            }

            try {
                // send to controller to handle adding food (validation + model update + table
                // refresh)
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
        });

    }

    // Alert to fill in the field!
    public void showMessage(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Wellness Manager");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public void refreshTable(Map<String, FoodComponent> foods) {
        tableData.setAll(foods.values());
    }

    public static void main(String[] args) {
        launch();
    }

    public Button getLoadFoodsButton() {
        return loadFoodsButton;
    }

    public Button getLoadLogButton() {
        return loadLogButton;
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
}