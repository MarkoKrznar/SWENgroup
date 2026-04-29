package Phase1.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DailyLog {
    private final LocalDate date;
    private double weight;
    private double calorieLimit;
    private final List<LogEntry> entries;
    private final List<ExerciseEntry> exerciseEntries = new ArrayList<>();

    public DailyLog(LocalDate date) {
        this(date, 150.0, 2000.0);
    }

    public DailyLog(LocalDate date, double weight, double calorieLimit) {
        this.date = date;
        this.weight = weight;
        this.calorieLimit = calorieLimit;
        this.entries = new ArrayList<>();
    }

    public LocalDate getDate() {
        return date;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be greater than 0");
        }
        this.weight = weight;
    }

    public double getCalorieLimit() {
        return calorieLimit;
    }

    public void setCalorieLimit(double calorieLimit) {
        if (calorieLimit <= 0) {
            throw new IllegalArgumentException("Calorie limit must be greater than 0");
        }
        this.calorieLimit = calorieLimit;
    }

    public List<LogEntry> getEntries() {
        return new ArrayList<>(entries);
    }

    public void addEntry(FoodComponent food, double servings) {
        if (food == null) {
            throw new IllegalArgumentException("Food cannot be null");
        }
        if (servings <= 0) {
            throw new IllegalArgumentException("Servings must be greater than 0");
        }
        entries.add(new LogEntry(food, servings));
    }

    public void addExercise(Exercise exercise, double minutes) {
        if (exercise == null) {
            throw new IllegalArgumentException("Exercise cannot be null");
        }
        if (minutes <= 0) {
            throw new IllegalArgumentException("Minutes must be greater than 0");
        }
        exerciseEntries.add(new ExerciseEntry(exercise, minutes));
    }

    public List<ExerciseEntry> getExerciseEntries() {
        return new ArrayList<>(exerciseEntries);
    }

    public boolean removeExerciseEntry(int index) {
        if (index < 0 || index >= exerciseEntries.size()) {
            return false;
        }
        exerciseEntries.remove(index);
        return true;
    }

    public double getTotalCaloriesExpended() {
        double total = 0.0;
        for (ExerciseEntry entry : exerciseEntries) {
            total += entry.getCaloriesExpended(weight);
        }
        return total;
    }

    // Total calories from food minus calories expended from exercise
    public double getNetCalories() {
        return getTotalCalories() - getTotalCaloriesExpended();
    }

    public boolean removeEntry(int index) {
        if (index < 0 || index >= entries.size()) {
            return false;
        }
        entries.remove(index);
        return true;
    }

    public double getTotalCalories() {
        double total = 0.0;
        for (LogEntry entry : entries) {
            total += entry.getTotalCalories();
        }
        return total;
    }

    public boolean isOverLimit() {
        return getTotalCalories() > calorieLimit;
    }

    public int getFatPercent() {
        double[] totals = macroTotals();
        double totalMacros = totals[0] + totals[1] + totals[2];
        return totalMacros == 0.0 ? 0 : (int) Math.round((totals[0] / totalMacros) * 100.0);
    }

    public int getCarbPercent() {
        double[] totals = macroTotals();
        double totalMacros = totals[0] + totals[1] + totals[2];
        return totalMacros == 0.0 ? 0 : (int) Math.round((totals[1] / totalMacros) * 100.0);
    }

    public int getProteinPercent() {
        double[] totals = macroTotals();
        double totalMacros = totals[0] + totals[1] + totals[2];
        return totalMacros == 0.0 ? 0 : (int) Math.round((totals[2] / totalMacros) * 100.0);
    }

    private double[] macroTotals() {
        double totalFat = 0.0;
        double totalCarb = 0.0;
        double totalProtein = 0.0;

        for (LogEntry entry : entries) {
            totalFat += entry.getFood().getFat() * entry.getServings();
            totalCarb += entry.getFood().getCarb() * entry.getServings();
            totalProtein += entry.getFood().getProtein() * entry.getServings();
        }

        return new double[] { totalFat, totalCarb, totalProtein };
    }

    public LogEntry getEntry(int index) {
        if (index < 0 || index >= entries.size()) {
            return null;
        }
        return entries.get(index);
    }

    public void addEntry(LogEntry entry) {
        if (entry == null) {
            throw new IllegalArgumentException("Entry cannot be null");
        }
        entries.add(entry);
    }

    public void addEntryAt(int index, LogEntry entry) {
        if (entry == null) {
            throw new IllegalArgumentException("Entry cannot be null");
        }
        if (index < 0 || index > entries.size()) {
            entries.add(entry);
        } else {
            entries.add(index, entry);
        }
    }

    public ExerciseEntry getExerciseEntry(int index) {
        if (index < 0 || index >= exerciseEntries.size()) {
            return null;
        }
        return exerciseEntries.get(index);
    }

    public void addExerciseEntry(ExerciseEntry entry) {
        if (entry == null) {
            throw new IllegalArgumentException("Exercise entry cannot be null");
        }
        exerciseEntries.add(entry);
    }

    public void addExerciseEntryAt(int index, ExerciseEntry entry) {
        if (entry == null) {
            throw new IllegalArgumentException("Exercise entry cannot be null");
        }
        if (index < 0 || index > exerciseEntries.size()) {
            exerciseEntries.add(entry);
        } else {
            exerciseEntries.add(index, entry);
        }
    }
}
