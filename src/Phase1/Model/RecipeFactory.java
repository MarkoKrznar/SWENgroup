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
        /**
         * if theres ingredients we add them
         * format should be: "ingredientName:servings"
         */
        for (int i = 2; i < parts.length; i++) { // starting index 2 
            String[] ingParts = parts[i].split(":"); // split ingredientnae:serving into 2 parts
            if (ingParts.length == 2) {
                String ingredientName = ingParts[0]; // get ingredient name
                double servings = Double.parseDouble(ingParts[1]); // get serving
                FoodComponent ingredient = foodCollection.getFood(ingredientName);
                if (ingredient != null) {
                    recipe.addIngredient(ingredient, servings);
                }
                //ingredient not found, just skip it 
            }
        }

        return recipe;
    }

    @Override
    public String typeCode() {
        return "r";
    }
}
