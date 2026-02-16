import java.util.Observable;

public class WeatherStation extends Observable implements Runnable {

    
    private final int KTOC = -27315;
    private final long PERIOD = 1000;
    private final IBarometer bar;
    private final ITempSensor sensor;
    private int currentReading;
    private double currentPressure;


    //Injecting the dependencies through the constructor
    public WeatherStation(ITempSensor sensor, IBarometer bar) {
        this.sensor = sensor;
        this.bar = bar;
        this.currentReading = sensor.getReading();
        this.currentPressure = bar.pressure();
    }
    
    public void run() {
        int reading;
        double pressure;
        while(true) {
            
            try {
                Thread.sleep(PERIOD);
            } catch (Exception e) {}

            reading = sensor.getReading();
            pressure = bar.pressure();
            synchronized(this) {
                currentReading = reading;
                currentPressure = pressure;
            }
            
            setChanged();
            notifyObservers();
        }
    }
    
    public synchronized double getCelsius() {
        return (currentReading + KTOC) / 100.0;
    }
    
    public synchronized double getKelvin() {
        return currentReading / 100.0;
    }

    public synchronized double getFahrenheit(){
        return getCelsius() * 9.0 / 5.0 + 32.0; // conversion formula from celsius to farenehit
    }
    public synchronized double getPressure(){
        return currentPressure;
    }
    public synchronized double getPressureMilli(){
        return currentPressure*33.864; //The conversion factor is 1 inch = 33.864 mbar.
    }

    // instead of making a new array in both awtui and swingui files for all temps, i created a method to get all temps
    public double[] getAllTemperatures(){
        return new double[]{getCelsius(), getKelvin(), getFahrenheit(), getPressure(), getPressureMilli()};
    }

}