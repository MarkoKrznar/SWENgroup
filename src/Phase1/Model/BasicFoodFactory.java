package Phase1.Model;

// This is the factory for making basic food 
public class BasicFoodFactory implements FoodFactory {

    @Override
    public FoodComponent create(String[] parts, FoodCollection foodCollection) {
        // checking if parts has everything
        if (parts == null || parts.length < 6) {
            throw new IllegalArgumentException("Invalid basic food row");
        }

        // grab the name and all the nutrition numbers from the array
        String name = parts[1];
        double calories = Double.parseDouble(parts[2]);
        double fat = Double.parseDouble(parts[3]);
        double carb = Double.parseDouble(parts[4]);
        double protein = Double.parseDouble(parts[5]);

        // make basicfood
        return new BasicFood(name, calories, fat, carb, protein);
    }

    @Override
    public String typeCode() {
        return "b";
    }
}
