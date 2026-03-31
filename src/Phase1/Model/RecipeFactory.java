package Phase1.Model;

// this is the factory for making recipes
public class RecipeFactory implements FoodFactory {

    @Override
    public FoodComponent create(String[] parts, FoodCollection foodCollection) {
        // checking if its all there
        if (parts == null || parts.length < 2) {
            throw new IllegalArgumentException("Invalid recipe row");
        }

        // get the name of the recipe
        String name = parts[1];

        // make a new recipe
        Recipe recipe = new Recipe(name);

        // ingredients are alternating name,count pairs: name1, count1, name2, count2, ...
        for (int i = 2; i + 1 < parts.length; i += 2) { // i is 2 cause index 0 is type and index 1 is name
            String ingredientName = parts[i]; // ingredient 
            double servings = Double.parseDouble(parts[i + 1]); // count of sreving
            FoodComponent ingredient = foodCollection.getFood(ingredientName);
            if (ingredient != null) {
                recipe.addIngredient(ingredient, servings);
            }
            // ingredient not found, just skip it
        }

        return recipe;
    }

    @Override
    public String typeCode() {
        return "r";
    }
}
