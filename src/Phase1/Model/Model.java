package Phase1.Model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Model {
    private final FoodCollection foodCollection = new FoodCollection();
    private final Map<LocalDate, DailyLog> logs = new LinkedHashMap<>();
    private LocalDate selectedDate = LocalDate.now();

    public Model() {
    }

    public FoodCollection getFoodCollection() {
        return foodCollection;
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
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void loadFoods() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        getClass().getResourceAsStream("/foods.csv"),
                        java.nio.charset.StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {

                // only process lines starting with "b"
                if (line.startsWith("b")) {
                    String[] parts = line.split(",");

                    String name = parts[1];
                    double calories = Double.parseDouble(parts[2]);
                    double fat = Double.parseDouble(parts[3]);
                    double carb = Double.parseDouble(parts[4]);
                    double protein = Double.parseDouble(parts[5]);

                    foodCollection.addFood(new BasicFood(
                            name, calories, fat, carb, protein));

                    // Starting point for recipe loading (not implemented yet)
                } else if (line.startsWith("r")) {

                    String[] parts = line.split(",");

                    String recipeName = parts[1];
                    Recipe recipe = new Recipe(recipeName);

                    // loop through ingredient pairs
                    for (int i = 2; i < parts.length; i += 2) {
                        String ingredientName = parts[i];
                        double servings = Double.parseDouble(parts[i + 1]);

                        // get ingredient from FoodCollection
                        // must already exist as a BasicFood (or previously loaded Recipe)
                        FoodComponent ingredient = foodCollection.getFood(ingredientName);

                        if (ingredient != null) {
                            recipe.addIngredient(ingredient, servings);
                        } else {
                            System.err.println("Ingredient not found: " + ingredientName);
                        }
                    }

                    foodCollection.addFood(recipe);
                }
            }
        } catch (IOException e) {
            System.err.println(" Could not load foods.csv: " + e.getMessage());
        }
    }

    // Adding food to the collection and displaying it in the table
    public void addFood(FoodComponent food) {
        foodCollection.addFood(food);
    }

    // Pending ..... Dialog as well
    public void addRecipe(Recipe recipe) {
        foodCollection.addFood(recipe);
    }

    public void addLogEntry(String foodName, double servings) {
        FoodComponent food = foodCollection.getFood(foodName);
        if (food == null) {
            throw new IllegalArgumentException("Food not found: " + foodName);
        }
        getSelectedLog().addEntry(food, servings);
    }

    public boolean deleteLogEntry(int index) {
        return getSelectedLog().removeEntry(index);
    }

    public void updateWeight(double weight) {
        getSelectedLog().setWeight(weight);
    }

    public void updateCalorieLimit(double limit) {
        getSelectedLog().setCalorieLimit(limit);
    }

    public void saveData() {

        // resolve log csv location and make sure the parent directory exists before writing

        Path logPath = resolveDataFilePath("log.csv");

        try {
            if (logPath.getParent() != null) {
                Files.createDirectories(logPath.getParent());
            }

            List<DailyLog> orderedLogs = new ArrayList<>(logs.values()); // copies all logs from the map into a list.
            orderedLogs.sort(Comparator.comparing(dailyLog -> dailyLog.getDate())); // sorting logs by date
            try (BufferedWriter writer = Files.newBufferedWriter(
                    logPath,
                    java.nio.charset.StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING)) {

                // Iterate through each day 
                for (DailyLog dailyLog : orderedLogs) {
                    LocalDate date = dailyLog.getDate();

                    // f lines: one line per food entry, no combining duplicates
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

                    // w line: daily weight.
                    writer.write(String.format(
                            "%04d,%02d,%02d,w,%s",
                            date.getYear(),
                            date.getMonthValue(),
                            date.getDayOfMonth(),
                            Double.toString(dailyLog.getWeight())));
                    writer.newLine();

                    // c line: daily calorie limit.
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

        //checks if the file exists and if not terminate

        Path logPath = resolveDataFilePath("log.csv");
        if (!Files.exists(logPath)) {
            return;
        }

        logs.clear(); // remove all entries from log map

        try (BufferedReader reader = Files.newBufferedReader(logPath, java.nio.charset.StandardCharsets.UTF_8)) { // try to open and read the line
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(","); // splits csv by commas
                
                // Skip rows that don't have yyyy,mm,dd,type,value fields
                if (parts.length < 5) {
                    continue;
                }

                // parse date from csv to get or create that days log
                LocalDate date = LocalDate.of(
                        Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));

                DailyLog dailyLog = getOrCreateLog(date);
                String type = parts[3];

                if ("f".equals(type) && parts.length >= 6) { //food row needs name and servings fields
                    FoodComponent food = foodCollection.getFood(parts[4]); // get food name
                    if (food != null) {
                        dailyLog.addEntry(food, Double.parseDouble(parts[5]));
                    }
                } else if ("w".equals(type)) {
                    dailyLog.setWeight(Double.parseDouble(parts[4])); // in "w" line part 4 is weight
                } else if ("c".equals(type)) {
                    dailyLog.setCalorieLimit(Double.parseDouble(parts[4])); // in c this is calorie limit
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading log.csv: " + e.getMessage()); 
        }
    }

    private Path resolveDataFilePath(String fileName) {
        Path current = Path.of(System.getProperty("user.dir")).toAbsolutePath(); // get working directorys abs path

        // go up parent directories looking for an existing bin.filename
        while (current != null) {
            Path candidate = current.resolve("bin").resolve(fileName);
            if (Files.exists(candidate)) {
                return candidate;
            }
            current = current.getParent();
        }
        return Path.of(System.getProperty("user.dir"), "bin", fileName).toAbsolutePath();  // returns abs path of file
    }

}
