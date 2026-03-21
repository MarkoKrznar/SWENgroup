package Phase1.Model;

public class IngredientEntry {

    private FoodComponent food;
    private double servings;

    public IngredientEntry(FoodComponent food, double servings) {
        this.food = food;
        this.servings = servings;
    }

    public FoodComponent getFood() { // returns the obj
        return food;
    }

    public double getServings() {
        return servings;
    }

    public void setServings(double servings) {
        this.servings = servings;
    }
}
