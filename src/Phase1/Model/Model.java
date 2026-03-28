package Phase1.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class Model {
    private final FoodCollection foodCollection = new FoodCollection();
    private final Map<LocalDate, DailyLog> logs = new LinkedHashMap<>();
    private LocalDate selectedDate = LocalDate.now();

    public Model() {
    }

    public FoodCollection getFoodCollection() {
        return foodCollection;
    }

    public DailyLog getOrCreateLog(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        return logs.computeIfAbsent(date, DailyLog::new);
    }

    public DailyLog getSelectedLog() {
        return getOrCreateLog(selectedDate);
    }

    public void setSelectedDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        selectedDate = date;
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void loadFoods() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        getClass().getResourceAsStream("/foods.csv"),
                        java.nio.charset.StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {

                // only process lines starting with "b"
                if (line.startsWith("b")) {
                    String[] parts = line.split(",");

                    String name = parts[1];
                    double calories = Double.parseDouble(parts[2]);
                    double fat = Double.parseDouble(parts[3]);
                    double carb = Double.parseDouble(parts[4]);
                    double protein = Double.parseDouble(parts[5]);

                    foodCollection.addFood(new BasicFood(
                            name, calories, fat, carb, protein));

                    // Starting point for recipe loading (not implemented yet)
                } else if (line.startsWith("r")) {

                    String[] parts = line.split(",");

                    String recipeName = parts[1];
                    Recipe recipe = new Recipe(recipeName);

                    // loop through ingredient pairs
                    for (int i = 2; i < parts.length; i += 2) {
                        String ingredientName = parts[i];
                        double servings = Double.parseDouble(parts[i + 1]);

                        // get ingredient from FoodCollection
                        // must already exist as a BasicFood (or previously loaded Recipe)
                        FoodComponent ingredient = foodCollection.getFood(ingredientName);

                        if (ingredient != null) {
                            recipe.addIngredient(ingredient, servings);
                        } else {
                            System.err.println("Ingredient not found: " + ingredientName);
                        }
                    }

                    foodCollection.addFood(recipe);
                }
            }
        } catch (IOException e) {
            System.err.println(" Could not load foods.csv: " + e.getMessage());
        }
    }

    // Adding food to the collection and displaying it in the table
    public void addFood(FoodComponent food) {
        foodCollection.addFood(food);
    }

    // Pending ..... Dialog as well
    public void addRecipe(Recipe recipe) {
        foodCollection.addFood(recipe);
    }

    public void addLogEntry(String foodName, double servings) {
        FoodComponent food = foodCollection.getFood(foodName);
        if (food == null) {
            throw new IllegalArgumentException("Food not found: " + foodName);
        }
        getSelectedLog().addEntry(food, servings);
    }

    public boolean deleteLogEntry(int index) {
        return getSelectedLog().removeEntry(index);
    }

    public void updateWeight(double weight) {
        getSelectedLog().setWeight(weight);
    }

    public void updateCalorieLimit(double limit) {
        getSelectedLog().setCalorieLimit(limit);
    }

}
