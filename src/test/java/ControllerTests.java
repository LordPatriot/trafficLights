import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;

/*
* These tests simulate traffic controller work and checks if states are switching in time
 */
public class ControllerTests {
    private static List<Intersection> intersections;
    private static TrafficController controller;

    @BeforeClass
    public static void initAndSimulateOneIntersection() {
        intersections = new ArrayList<Intersection>();
        intersections.add(new Intersection());

        // Choose 10ms to speed up the simulation in hundred times
        int updateStep = 10;
        controller = new TrafficController(intersections, updateStep, false);

        try {
            sleep(updateStep * (TrafficController.SYSTEM_LIFETIME + 1));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBlockedToEnabledStateSwitches() {
        assertEquals(1, controller.getBlockedToEnabledCounter());
    }

    @Test
    public void testEnabledToWarningStateSwitches() {
        assertEquals(TrafficController.SYSTEM_LIFETIME/TrafficController.ENABLED_PERIOD,
                controller.getEnabledToWarningCounter());
    }

    @Test
    /*
    * This test will and should fail because of the time lag of the system
    * TODO: make the test work by accounting for non-zero controller execution time
     */
    public void testWarningToEnabledStateSwitches() {
        assertEquals(TrafficController.SYSTEM_LIFETIME/TrafficController.ENABLED_PERIOD,
                controller.getWarningToEnabledCounter());
    }
}