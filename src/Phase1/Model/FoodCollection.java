package Phase1.Model;

import java.util.LinkedHashMap;
import java.util.Map;

public class FoodCollection {

    private Map<String, FoodComponent> foods = new LinkedHashMap<>();

    public void addFood(FoodComponent food) {
        foods.put(food.getName(), food);
    }

    public FoodComponent getFood(String name) {
        return foods.get(name);
    }

    public Map<String, FoodComponent> getAllFoods() {
        return foods;
    }

    public boolean containsFood(String name) {
        return foods.containsKey(name);
    }

}
