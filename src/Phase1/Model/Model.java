package Phase1.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Model {
    // Hold a list of foods or other data
    private FoodCollection foodCollection = new FoodCollection();

    public Model() {
    }

    public FoodCollection getFoodCollection() {
        return foodCollection;
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

        java.io.File file = new java.io.File(System.getProperty("user.dir") + "\\bin\\foods.csv");

        try (java.io.FileWriter fw = new java.io.FileWriter(file, true);
                java.io.BufferedWriter bw = new java.io.BufferedWriter(fw)) {

            bw.newLine(); // ← go to next line
            bw.write(food.toCSV()); // ← write only the new food

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Pending ..... Dialog as well
    public void addRecipe(Recipe recipe) {
        foodCollection.addFood(recipe);
        java.io.File file = new java.io.File(System.getProperty("user.dir") + "\\bin\\foods.csv");
        try (java.io.FileWriter fw = new java.io.FileWriter(file, true);
                java.io.BufferedWriter bw = new java.io.BufferedWriter(fw)) {

            bw.newLine(); // ← go to next line
            bw.write(recipe.toCSV()); // ← write only the new recipe

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * calculates macro percentage split by calories
     * fat is 9 kcal/g, carb is 4 kcal/g, protein is 4 kcal/g.
     */
    public MacroBreakdown calculateMacroBreakdown(double fatGrams, double carbGrams, double proteinGrams) {

        double fatCalories = fatGrams * 9.0;
        double carbCalories = carbGrams * 4.0;
        double proteinCalories = proteinGrams * 4.0;

        double totalMacroCalories = fatCalories + carbCalories + proteinCalories;
        if (totalMacroCalories == 0) {
            return new MacroBreakdown(0, 0, 0);
        }

        // calculating percenteges
        double fatPercent = (fatCalories / totalMacroCalories) * 100.0;
        double carbPercent = (carbCalories / totalMacroCalories) * 100.0;
        double proteinPercent = (proteinCalories / totalMacroCalories) * 100.0;

        return new MacroBreakdown(fatPercent, carbPercent, proteinPercent);
    }

    /**
     * to calculate percentages if given a list of foods
     */
    public MacroBreakdown calculateMacroBreakdown(List<IngredientEntry> entries) {
        double totalFatGrams = 0;
        double totalCarbGrams = 0;
        double totalProteinGrams = 0;

        for (IngredientEntry entry : entries) {
            totalFatGrams += entry.getFood().getFat() * entry.getServings();
            totalCarbGrams += entry.getFood().getCarb() * entry.getServings();
            totalProteinGrams += entry.getFood().getProtein() * entry.getServings();
        }

        return calculateMacroBreakdown(totalFatGrams, totalCarbGrams, totalProteinGrams); 
    }

}
