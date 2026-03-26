package Phase1.Model;

/**
 * class for fat,carb and protein percentage breakdwon
 */
public class MacroBreakdown {

    private final double fatPercent;
    private final double carbPercent;
    private final double proteinPercent;

    public MacroBreakdown(double fatPercent, double carbPercent, double proteinPercent) {
        this.fatPercent = fatPercent;
        this.carbPercent = carbPercent;
        this.proteinPercent = proteinPercent;
    }

    public double getFatPercent() {
        return fatPercent;
    }

    public double getCarbPercent() {
        return carbPercent;
    }

    public double getProteinPercent() {
        return proteinPercent;
    }

    @Override
    public String toString() {
        return String.format("Fat: %.2f%%, Carb: %.2f%%, Protein: %.2f%%",
                fatPercent, carbPercent, proteinPercent);
    }
}