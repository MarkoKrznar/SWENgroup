import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;

public class AWTUI extends Frame implements Observer {
    private final WeatherStation station;
    private String[] tempPanels = {"Celsius","Kelvin","Fahrenheit","Inches","Millibars"};
    private static Font labelFont = new Font(Font.SERIF, Font.PLAIN, 72);   
    private Label[] tempLabels; 


    public AWTUI(WeatherStation station) {
        super("Weather Station");
        this.station = station;
        this.station.addObserver(this);
        tempLabels = new Label[tempPanels.length];
        setLayout(new GridLayout(1, 0));
        for (int i = 0; i < tempPanels.length; i++) {
            tempLabels[i] = createTemperaturePanel(tempPanels[i]);
        }
        addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent windowEvent) {
                        dispose(); 
                    }
                });

        pack();
        setVisible(true);
    }

    public void update(Observable obs, Object arg) {
        if (station != obs) {
            return;
        }
        
        double[] temps = station.getAllTemperatures();
        for (int i = 0; i < temps.length; i++) {
            tempLabels[i].setText(String.format("%6.2f", temps[i]));
        }

    }

    private Label setLabel(String title, Panel panel) {
        Label label = new Label(title);
        label.setAlignment(Label.CENTER);
        label.setFont(labelFont);
        panel.add(label);
        return label;
    }

    private Label createTemperaturePanel(String title) {
        Panel panel = new Panel(new GridLayout(2, 1));
        add(panel);
        setLabel(title, panel);
        return setLabel("", panel);
    }
}