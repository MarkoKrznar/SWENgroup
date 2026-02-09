import java.awt.Font;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SwingUI extends JFrame implements Observer {
    private final WeatherStation station;
    private String[] tempText = {"Celsius","Kelvin","Fahrenheit","Inches","Millibars"};
    private JLabel[] tempJLabels;

    private static Font labelFont =
        new Font(Font.SERIF, Font.PLAIN, 72);


    public SwingUI(WeatherStation station) {
        super("Weather Station");
        tempJLabels= new JLabel[tempText.length];
        for (int i = 0; i < tempJLabels.length; i++) {
            tempJLabels[i]=createTemperaturePanel(tempText[i]);
        }
        this.station = station;
        this.station.addObserver(this);
        this.setLayout(new GridLayout(1,0));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        this.pack();
        this.setVisible(true);
    }


    public void update(Observable obs, Object arg) {
        if (station != obs) {
            return;
        }
        double[] temps = station.getAllTemperatures();
        for (int i = 0; i < temps.length; i++) {
            tempJLabels[i].setText(String.format("%6.2f", temps[i]));
        }
    }

    private JLabel setLabel(String title, JPanel panel) {
        JLabel label = new JLabel(title);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.TOP);
        label.setFont(labelFont);
        panel.add(label);
        return label;
    }

    private JLabel createTemperaturePanel(String title) {
        JPanel panel = new JPanel(new GridLayout(2,1));
        this.add(panel);
        setLabel(title, panel);
        return setLabel("", panel);
    }
}
