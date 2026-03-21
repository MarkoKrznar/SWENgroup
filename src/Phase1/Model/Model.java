package Phase1.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

                // skip blank lines and recipe lines
                if (line.isEmpty() || line.startsWith("r"))
                    continue;

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
                }
            }
        } catch (IOException e) {
            System.err.println("Could not load foods.csv: " + e.getMessage());
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

}
