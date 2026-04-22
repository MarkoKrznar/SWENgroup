package Phase1.Model;

public class Exercise {

    // attributes
    private String name;
    private double calories;
    private double caloriesPerHour;

    public Exercise(String name, double calories, double caloriesPerHour) {
        this.name = name;
        this.calories = calories;
        this.caloriesPerHour = caloriesPerHour;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getCaloriesPerHour() {
        return caloriesPerHour;
    }

    public void setCaloriesPerHour(double caloriesPerHour) {
        this.caloriesPerHour = caloriesPerHour;
    }

    public double getCaloriesExpended(double weight, double minutes) {
        return caloriesPerHour * (weight / 100.0) * (minutes / 60.0);

    }

    public String toCSV() {
        return "e," + name + "," + calories + "," + caloriesPerHour;
    }

}
