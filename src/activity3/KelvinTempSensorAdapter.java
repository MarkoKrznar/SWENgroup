
//Adapter class to adapt KelvinTempSensor to ITempSensor interface
public class KelvinTempSensorAdapter implements ITempSensor {
    private final KelvinTempSensor sensor;

    public KelvinTempSensorAdapter(KelvinTempSensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public int reading() {
        return sensor.reading();
    }
}