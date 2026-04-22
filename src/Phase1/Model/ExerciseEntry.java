package Phase1.Model;

public class ExerciseEntry {

    private Exercise exercise;
    private double minutes;

    public ExerciseEntry(Exercise exercise, double minutes) {
        this.exercise = exercise;
        this.minutes = minutes;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public double getMinutes() {
        return minutes;
    }

    public double getCaloriesExpended(double weight) {
        return exercise.getCaloriesExpended(weight, minutes);
    }
}