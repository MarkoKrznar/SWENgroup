package Phase1.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Model {
    // Hold a list of foods or other data
    private FoodCollection foodCollection = new FoodCollection();
    private List<WeightEntry> weightEntries = new ArrayList<>();

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
     * Calculates macro percentage split by grams
     * Percentages are whole numbers and always sum to 100
     */
    public MacroBreakdown calculateMacroBreakdown(double fatGrams, double carbGrams, double proteinGrams) {

        double safeFat = Math.max(0.0, fatGrams);
        double safeCarb = Math.max(0.0, carbGrams);
        double safeProtein = Math.max(0.0, proteinGrams);

        double totalMacroGrams = safeFat + safeCarb + safeProtein;
        if (totalMacroGrams == 0) {
            return new MacroBreakdown(0, 0, 0);
        }

        double[] raw = new double[] {
                (safeFat / totalMacroGrams) * 100.0,
                (safeCarb / totalMacroGrams) * 100.0,
                (safeProtein / totalMacroGrams) * 100.0
        };

        int[] rounded = new int[raw.length];
        for (int i = 0; i < raw.length; i++) {
            rounded[i] = (int) raw[i];
        }

        int sum = 0;
        for (int value : rounded) {
            sum += value;
        }
        int remainder = 100 - sum;

        // distributing any remaining percentage points to the largest fractional parts.
        while (remainder > 0) {
            int bestIndex = 0; // best candidate to give remainder to get 100
            double bestFraction = (raw[0] - Math.floor(raw[0])); 

            for (int i = 1; i < raw.length; i++) { // checking remaining macros to find bigger fractions 
                double fraction = raw[i] - Math.floor(raw[i]); // get fraction
                if (fraction > bestFraction) { // compare best and current 
                    bestFraction = fraction;
                    bestIndex = i;
                }
            }

            rounded[bestIndex]++; // giving 1 percent to the biggest fraction
            raw[bestIndex] = Math.floor(raw[bestIndex]); // set fraction to 0 so it doesnt keep winning in every iteration
            remainder--;
        }

        return new MacroBreakdown(rounded[0], rounded[1], rounded[2]);
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

    public void setWeightForDate(LocalDate date, double weight) {

        // checking for potential errors

        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be greater than 0");
        }


        for (WeightEntry entry : weightEntries) {
            if (entry.getDate().equals(date)) {
                entry.setWeight(weight);
                return;
            }
        }

        weightEntries.add(new WeightEntry(date, weight));
    }
    

    public Double getWeightForDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        // 1) Exact date match
        for (WeightEntry entry : weightEntries) {
            if (entry.getDate().equals(date)) {
                return entry.getWeight();
            }
        }

        // 2) Most recent previous date
        LocalDate latestPreviousDate = null;
        double latestPreviousWeight = 150.0;

        for (WeightEntry entry : weightEntries) {
            LocalDate entryDate = entry.getDate();
            if (entryDate.isBefore(date) &&
                    (latestPreviousDate == null || entryDate.isAfter(latestPreviousDate))) {
                latestPreviousDate = entryDate;
                latestPreviousWeight = entry.getWeight();
            }
        }

        // 3) Default when no prior entries exist
        return latestPreviousDate == null ? 150.0 : latestPreviousWeight;
    }

    public boolean removeWeightForDate(LocalDate date) {
        for (int i = 0; i < weightEntries.size(); i++) {
            if (weightEntries.get(i).getDate().equals(date)) {
                weightEntries.remove(i);
                return true;
            }
        }
        return false;
    }

    public List<WeightEntry> getWeightEntries() {
        return new ArrayList<>(weightEntries);
    }

}
