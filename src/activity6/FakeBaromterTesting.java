
public class FakeBaromterTesting implements IBarometer {

    @Override
    public double pressure() {
        return 29.92; // Return a fixed pressure value for testing
    }

}
