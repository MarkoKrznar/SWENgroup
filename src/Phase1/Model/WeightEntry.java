package Phase1.Model;

import java.time.LocalDate;

public class WeightEntry {

    private final LocalDate date;
    private double weight;

    public WeightEntry(LocalDate date, double weight) {
        this.date = date;
        this.weight = weight;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String toCSV() {
        return String.format("%04d,%02d,%02d,w,%s",
                date.getYear(),
                date.getMonthValue(),
                date.getDayOfMonth(),
                Double.toString(weight));
    }
}