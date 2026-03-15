package Phase1.Model;

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

    public Recipe(String name) {
        super(name);

    }

    @Override
    public double getCalories() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCalories'");
    }

    @Override
    public double getFat() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFat'");
    }

    @Override
    public double getCarb() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCarb'");
    }

    @Override
    public double getProtein() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProtein'");
    }

    @Override
    public String toCSV() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toCSV'");
    }

}