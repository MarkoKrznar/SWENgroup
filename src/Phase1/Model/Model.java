package Phase1.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Model {
    // Hold a list of foods or other data
    private Map<String, FoodComponent> foods = new LinkedHashMap<>();

    public Model() {
    }

    // Add getters/setters or methods as needed
    public FoodComponent getFoods(String name) {
        return foods.get(name);
    }

    public List<FoodComponent> getAllFoods() {
        return new ArrayList<>(foods.values());
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

                    foods.put(name, new BasicFood(
                            name,
                            calories, fat, carb, protein));
                }
            }
        } catch (IOException e) {
            System.err.println("Could not load foods.csv: " + e.getMessage());
        }
    }

}
