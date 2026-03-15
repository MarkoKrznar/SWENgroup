package Phase1.Model;

public class BasicFood extends FoodComponent {

    // ── ATTRIBUTES ──────────────────────────────────────────
    private double calories;
    private double fat;
    private double carb;
    private double protein;

    // ── CONSTRUCTOR ─────────────────────────────────────────
    public BasicFood(String name, double calories,
            double fat, double carb, double protein) {
        super(name);
        this.calories = calories;
        this.fat = fat;
        this.carb = carb;
        this.protein = protein;
    }

    // ── IMPLEMENT ABSTRACT METHODS ───────────────────────────
    // Leaf just returns its own stored values — no children to sum
    @Override
    public double getCalories() {
        return calories;
    }

    @Override
    public double getFat() {
        return fat;
    }

    @Override
    public double getCarb() {
        return carb;
    }

    @Override
    public double getProtein() {
        return protein;
    }

    // ── toCSV ───────────────────────────────────────────────
    // Format: b,Hot Dog,147.0,13.6,1.1,5.1
    @Override
    public String toCSV() {
        return "b," + getName() + "," + calories + ","
                + fat + "," + carb + "," + protein;
    }
}