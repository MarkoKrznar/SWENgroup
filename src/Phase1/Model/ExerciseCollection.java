package Phase1.Model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExerciseCollection {

    private Map<String, Exercise> exercises = new LinkedHashMap<>();

    public void addExercise(Exercise exercise) {
        exercises.put(exercise.getName(), exercise);
    }

    public Exercise getExercise(String name) {
        return exercises.get(name);
    }

    public List<Exercise> getAllExercises() {
        return new ArrayList<>(exercises.values());
    }

    public boolean containsExercise(String name) {
        return exercises.containsKey(name);
    }
}