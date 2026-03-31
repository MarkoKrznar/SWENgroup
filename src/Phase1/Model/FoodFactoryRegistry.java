package Phase1.Model;

import java.util.HashMap;
import java.util.Map;

// registry for all food factories
public class FoodFactoryRegistry {
    // holds all the factories mapped by their type code (b for basic food and r for recipe factory)
    private Map<String, FoodFactory> registry = new HashMap<>();

    // register a new factory
    public void register(FoodFactory factory) {
        registry.put(factory.typeCode(), factory);
    }

    // get a factory by its type code
    public FoodFactory getFactory(String typeCode) {
        return registry.get(typeCode);
    }
}
