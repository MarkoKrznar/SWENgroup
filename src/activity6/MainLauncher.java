import javax.swing.SwingUtilities;

public class MainLauncher{
    public static void main(String[] args) {

        ITempSensor tempSensor = new KelvinTempSensorAdapter(new KelvinTempSensor());
        IBarometer barometer = new Barometer();

        WeatherStation ws = new WeatherStation(tempSensor, barometer);
        Thread thread = new Thread(ws);
        TextUI textUI = new TextUI(ws);

        SwingUtilities.invokeLater(() -> {
            SwingUI swingUI = new SwingUI(ws);
        });

        java.awt.EventQueue.invokeLater(() -> {
            AWTUI awtUI = new AWTUI(ws);
        });
        thread.start();
    }
}