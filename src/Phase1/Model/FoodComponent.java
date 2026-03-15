package Phase1.Model;

public abstract class FoodComponent {

    // ── ATTRIBUTES ──────────────────────────────────────────
    private String name;
    private FoodType type;

    // ── CONSTRUCTOR ─────────────────────────────────────────
    public FoodComponent(String name) {
        this.name = name;
    }

    // ── GETTERS ─────────────────────────────────────────────
    public String getName() {
        return name;
    }

    public FoodType getType() {
        return type;
    }

    // ── ABSTRACT METHODS ────────────────────────────────────
    // Every subclass MUST implement these
    // BasicFood returns its own stored values
    // Recipe sums up its children's values

    public abstract double getCalories();

    public abstract double getFat();

    public abstract double getCarb();

    public abstract double getProtein();

    // ── toCSV ───────────────────────────────────────────────
    // Each subclass knows how to serialize itself
    // BasicFood → "b,name,cal,fat,carb,protein"
    // Recipe → "r,name,f1,count1,f2,count2,..."
    public abstract String toCSV();

    // ── toString ────────────────────────────────────────────
    // Used by ComboBox in AddFoodDialog to display food names
    @Override
    public String toString() {
        return name;
    }
}