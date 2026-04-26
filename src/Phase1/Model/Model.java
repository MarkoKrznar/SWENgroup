package Phase1.Model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Model implements Subject {
    private final FoodCollection foodCollection = new FoodCollection();
    private final ExerciseCollection exerciseCollection = new ExerciseCollection();
    private final Map<LocalDate, DailyLog> logs = new LinkedHashMap<>();
    private final List<Observer> observers = new ArrayList<>();
    private LocalDate selectedDate = LocalDate.now();
    private final FoodFactoryRegistry factoryRegistry = new FoodFactoryRegistry();

    public Model() {
        factoryRegistry.register(new BasicFoodFactory());
        factoryRegistry.register(new RecipeFactory());
    }

    public FoodCollection getFoodCollection() {
        return foodCollection;
    }

    public ExerciseCollection getExerciseCollection() {
        return exerciseCollection;
    }

    public DailyLog getOrCreateLog(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        DailyLog existing = logs.get(date);
        if (existing != null) {
            return existing;
        }

        double inheritedWeight = 150.0;
        double inheritedLimit = 2000.0;
        LocalDate latestPrevious = null;

        for (DailyLog log : logs.values()) {
            LocalDate logDate = log.getDate();
            if (logDate.isBefore(date) &&
                    (latestPrevious == null || logDate.isAfter(latestPrevious))) {
                latestPrevious = logDate;
                inheritedWeight = log.getWeight();
                inheritedLimit = log.getCalorieLimit();
            }
        }

        DailyLog created = new DailyLog(date, inheritedWeight, inheritedLimit);
        logs.put(date, created);
        return created;
    }

    public DailyLog getSelectedLog() {
        return getOrCreateLog(selectedDate);
    }

    public void setSelectedDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        selectedDate = date;
        getOrCreateLog(selectedDate);
        notifyObservers();
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void loadFoods() {
        foodCollection.getAllFoods().clear(); // no afecta realmente por copia, pero lo dejo fuera abajo
        loadFoodsFromCsv();
        notifyObservers();
    }

    // after implementing factory this method is using registry to create foods and
    // recipes
    // instead of creating methods here it just grabs the right factory and lets the
    // factory handle obj creation
    private void loadFoodsFromCsv() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        getClass().getResourceAsStream("/foods.csv"),
                        java.nio.charset.StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                String typeCode = parts[0];
                FoodFactory factory = factoryRegistry.getFactory(typeCode); // get factory
                if (factory != null) {
                    FoodComponent food = factory.create(parts, foodCollection);
                    String name = food.getName();
                    // only add food that is not already in collection
                    if (!foodCollection.containsFood(name)) {
                        foodCollection.addFood(food);
                    }
                } else {
                    System.err.println("Unknown food type code: " + typeCode);
                }
            }
        } catch (IOException e) {
            System.err.println("Could not load foods.csv: " + e.getMessage());
        }
    }

    // Load exercises from exercise.csv and add them to the exercise collection
    public void loadExerciseFromCsv() {
        java.io.InputStream stream = getClass().getResourceAsStream("/exercise.csv");
        if (stream == null) {
            System.err.println("Could not find exercise.csv in resources.");
            return;
        }
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(stream, java.nio.charset.StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                String name = parts[1];
                double calories = Double.parseDouble(parts[2]);
                double caloriesPerHour = Double.parseDouble(parts[3]);
                Exercise exercise = new Exercise(name, calories, caloriesPerHour);
                if (!exerciseCollection.containsExercise(name)) {
                    exerciseCollection.addExercise(exercise);
                }
            }
            notifyObservers();
        } catch (IOException e) {
            System.err.println("Could not load exercise.csv: " + e.getMessage());
        }
    }

    public void saveExercisesToFile() throws IOException {
        Path path = resolveDataFilePath("exercise.csv");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path.toFile()))) {
            for (Exercise exercise : exerciseCollection.getAllExercises()) {
                bw.write(exercise.toCSV() + "\n");
            }
        }
    }

    // Method to get all exercises
    public List<Exercise> getExercises() {
        return exerciseCollection.getAllExercises();
    }

    public void addFood(FoodComponent food) {
        foodCollection.addFood(food);
        notifyObservers();
    }

    public void addRecipe(Recipe recipe) {
        foodCollection.addFood(recipe);
        notifyObservers();
    }

    public void addLogEntry(String foodName, double servings) {
        FoodComponent food = foodCollection.getFood(foodName);
        if (food == null) {
            throw new IllegalArgumentException("Food not found: " + foodName);
        }
        getSelectedLog().addEntry(food, servings);
        notifyObservers();
    }

    public void addExerciseEntry(String exerciseName, double minutes) {
        Exercise exercise = exerciseCollection.getExercise(exerciseName);
        if (exercise == null) {
            throw new IllegalArgumentException("Exercise not found: " + exerciseName);
        }
        getSelectedLog().addExercise(exercise, minutes);
        notifyObservers();
    }

    public boolean deleteLogEntry(int index) {
        boolean removed = getSelectedLog().removeEntry(index);
        if (removed) {
            notifyObservers();
        }
        return removed;
    }

    public void updateWeight(double weight) {
        getSelectedLog().setWeight(weight);
        notifyObservers();
    }

    public void updateCalorieLimit(double limit) {
        getSelectedLog().setCalorieLimit(limit);
        notifyObservers();
    }

    public List<LogEntry> getSelectedLogEntries() {
        return getSelectedLog().getEntries();
    }

    public double getSelectedLogTotalCalories() {
        return getSelectedLog().getTotalCalories();
    }

    public int getSelectedLogFatPercent() {
        return getSelectedLog().getFatPercent();
    }

    public int getSelectedLogCarbPercent() {
        return getSelectedLog().getCarbPercent();
    }

    public int getSelectedLogProteinPercent() {
        return getSelectedLog().getProteinPercent();
    }

    public double getSelectedLogWeight() {
        return getSelectedLog().getWeight();
    }

    public double getSelectedLogCalorieLimit() {
        return getSelectedLog().getCalorieLimit();
    }

    public boolean selectedDayIsOverLimit() {
        return getSelectedLog().isOverLimit();
    }

    public void saveData() {
        Path logPath = resolveDataFilePath("log.csv");

        try {
            if (logPath.getParent() != null) {
                Files.createDirectories(logPath.getParent());
            }

            List<DailyLog> orderedLogs = new ArrayList<>(logs.values());
            orderedLogs.sort(Comparator.comparing(DailyLog::getDate));

            try (BufferedWriter writer = Files.newBufferedWriter(
                    logPath,
                    java.nio.charset.StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING)) {

                for (DailyLog dailyLog : orderedLogs) {
                    LocalDate date = dailyLog.getDate();

                    for (LogEntry entry : dailyLog.getEntries()) {
                        writer.write(String.format(
                                "%04d,%02d,%02d,f,%s,%s",
                                date.getYear(),
                                date.getMonthValue(),
                                date.getDayOfMonth(),
                                entry.getFood().getName(),
                                Double.toString(entry.getServings())));
                        writer.newLine();
                    }

                    writer.write(String.format(
                            "%04d,%02d,%02d,w,%s",
                            date.getYear(),
                            date.getMonthValue(),
                            date.getDayOfMonth(),
                            Double.toString(dailyLog.getWeight())));
                    writer.newLine();

                    writer.write(String.format(
                            "%04d,%02d,%02d,c,%s",
                            date.getYear(),
                            date.getMonthValue(),
                            date.getDayOfMonth(),
                            Double.toString(dailyLog.getCalorieLimit())));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving log.csv: " + e.getMessage());
        }
    }

    public void loadData() {
        Path logPath = resolveDataFilePath("log.csv");
        if (!Files.exists(logPath)) {
            return;
        }

        logs.clear();

        try (BufferedReader reader = Files.newBufferedReader(logPath, java.nio.charset.StandardCharsets.UTF_8)) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length < 5) {
                    continue;
                }

                LocalDate date = LocalDate.of(
                        Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));

                DailyLog dailyLog = getOrCreateLog(date);
                String type = parts[3];

                if ("f".equals(type) && parts.length >= 6) {
                    FoodComponent food = foodCollection.getFood(parts[4]);
                    if (food != null) {
                        dailyLog.addEntry(food, Double.parseDouble(parts[5]));
                    }
                } else if ("w".equals(type)) {
                    dailyLog.setWeight(Double.parseDouble(parts[4]));
                } else if ("c".equals(type)) {
                    dailyLog.setCalorieLimit(Double.parseDouble(parts[4]));
                }
            }
            notifyObservers();
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading log.csv: " + e.getMessage());
        }
    }

    private Path resolveDataFilePath(String fileName) {
        Path current = Paths.get(System.getProperty("user.dir")).toAbsolutePath();

        while (current != null) {
            Path candidate = current.resolve("bin").resolve(fileName);
            if (Files.exists(candidate)) {
                return candidate;
            }
            current = current.getParent();
        }

        return Paths.get(System.getProperty("user.dir"), "bin", fileName).toAbsolutePath();
    }

    public void addExercise(Exercise exercise) {
        exerciseCollection.addExercise(exercise);
        notifyObservers();
    }

    @Override
    public void attach(Observer o) {
        if (o != null && !observers.contains(o)) {
            observers.add(o);
        }
    }

    @Override
    public void detach(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    // Kept to match the class diagram naming.
    public void updateData() {
        notifyObservers();
    }
}