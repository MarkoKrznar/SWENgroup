package Phase1.Model;

/**
 * One consumed food item inside a DailyLog
 */

public class LogEntry {

    private final FoodComponent food;
    private double servings;

    public LogEntry(FoodComponent food, double servings) {
        this.food = food;
        this.servings = servings;
    }

    public FoodComponent getFood() {
        return food;
    }

    public double getServings() {
        return servings;
    }

    public void setServings(double servings) {
        this.servings = servings;
    }

    public double getTotalCalories() {
        return food.getCalories() * servings;
    }
}
