import java.util.Observable;

import javax.swing.SwingUtilities;

public class MainLauncher extends Observable{
    public static void main(String[] args) {
        WeatherStation ws = new WeatherStation();
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