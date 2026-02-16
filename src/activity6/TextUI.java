import java.util.Observer;
import java.util.Observable;

public class TextUI implements Observer {
    private final WeatherStation station;

    public TextUI(WeatherStation station) {
        this.station = station;
        this.station.addObserver(this);
    }

    public void update(Observable obs, Object ignore) {

        if (station != obs) {
            return;
        }
        System.out.printf(
                "Reading is %6.2f degrees C, %6.2f degrees K, %6.2f degrees F, Pressure is %6.2f inches, and %6.2f millibars %n",
                station.getCelsius(), station.getKelvin(),station.getFahrenheit(),station.getPressure(),station.getPressureMilli());
    }
}
