package Phase1.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * The Recipe class represents a recipe food item.
 * It extends the Food class and provides methods to add and remove food
 * components to the recipe.
 * It also overrides the methods to calculate the calories, proteins, carbs, and
 * fats of the recipe based on its components.
 * The recipe class also implements the print, add, remove, and getComponent
 * methods from the Food class.
 */
public class Recipe extends FoodComponent {

    private List<IngredientEntry> ingredients;

    public Recipe(String name) {
        super(name);
        this.ingredients = new ArrayList<>(); // storing the ingredients in an array
    }

    // INGREDIENT MANAGEMENT

    public void addIngredient(FoodComponent food, double servings) {
        for (IngredientEntry entry : ingredients) {
            if (entry.getFood().getName().equals(food.getName())) { // checks if the ingredient already exists
                entry.setServings(entry.getServings() + servings); // if duplicate, increase serving size
                return;
            }
        }
        ingredients.add(new IngredientEntry(food, servings)); // if not duplicate, add new ingredient entry
    }

    public void removeIngredient(String foodName) { // remove ingredient
        for (int i = 0; i < ingredients.size(); i++) {
            if (ingredients.get(i).getFood().getName().equals(foodName)) { // if it's the food we want to remove
                ingredients.remove(i);
                break;
            }
        }
    }

    public List<IngredientEntry> getIngredients() { // returns ingredients
        return ingredients;
    }

    // NUTRIENT TOTALS (sum over servings)

    @Override
    public double getCalories() {
        double total = 0;
        for (IngredientEntry entry : ingredients) {
            total += entry.getFood().getCalories() * entry.getServings(); // calories are per serving
        }
        return total;
    }

    @Override
    public double getFat() {
        double total = 0;
        for (IngredientEntry entry : ingredients) {
            total += entry.getFood().getFat() * entry.getServings();
        }
        return total;
    }

    @Override
    public double getCarb() {
        double total = 0;
        for (IngredientEntry entry : ingredients) {
            total += entry.getFood().getCarb() * entry.getServings();
        }
        return total;
    }

    @Override
    public double getProtein() {
        double total = 0;
        for (IngredientEntry entry : ingredients) {
            total += entry.getFood().getProtein() * entry.getServings();
        }
        return total;
    }

    // toCSV
    // Format: r,RecipeName,food1,count1,food2,count2,...

    @Override
    public String toCSV() {
        StringBuilder sb = new StringBuilder();
        sb.append("r,").append(getName());
        for (IngredientEntry entry : ingredients) {
            sb.append(",").append(entry.getFood().getName());
            sb.append(",").append(entry.getServings());
        }
        return sb.toString();
    }

}
