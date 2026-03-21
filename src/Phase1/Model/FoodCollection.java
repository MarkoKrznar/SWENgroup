package Phase1.Model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FoodCollection {

    private Map<String, FoodComponent> foods = new LinkedHashMap<>();

    public void addFood(FoodComponent food) {
        foods.put(food.getName(), food);
    }

    public FoodComponent getFood(String name) {
        return foods.get(name);
    }

    // List of all foods in the collection
    public List<FoodComponent> getAllFoods() {
        return new ArrayList<>(foods.values());
    }

    // Checking if exists in the collection
    public boolean containsFood(String name) {
        return foods.containsKey(name);
    }

}
