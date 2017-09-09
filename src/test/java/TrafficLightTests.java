import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TrafficLightTests {
    private static TrafficLight light;

    @BeforeClass
    public static void initTrafficLight() {
        light = new TrafficLight();
    }

    @Test
    public void testInitState() {
        light = new TrafficLight();
        assertEquals(-1, light.getState());
    }

    @Test
    public void testSwitchToGreen() {
        light.switchToGreen();
        assertEquals(1, light.getState());
    }

    @Test
    public void testSwitchToRed() {
        light.switchToRed();
        assertEquals(-1, light.getState());
    }

    @Test
    public void testSwitchToYellow() {
        light.switchToYellow();
        assertEquals(0, light.getState());
    }
}
