package Phase1.Model;
// interface for food factory
public interface FoodFactory {

    FoodComponent create(String[] parts, FoodCollection foodCollection);

    String typeCode();
}
