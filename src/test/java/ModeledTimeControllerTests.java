import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;
import static org.junit.Assert.assertTrue;

public class ModeledTimeControllerTests {
    @Test
    /*
    * This test checks accuracy of how system calculates modeled time (under no load)
     */
    public void ticksDiscrepancyUnderNoLoadTest() {
        AtomicLong ticks = new AtomicLong(0);
        long startTime = System.currentTimeMillis();
        int modeledTimeStep = 3;
        // We allow for 2 steps discrepancy at max
        long allowedDiscrepancy = 2L * modeledTimeStep;

        ModeledTimeController controller = new ModeledTimeController(ticks, modeledTimeStep);

        for(int i = 0; i < 60; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long currentTime = System.currentTimeMillis();

            assertTrue(Math.abs((currentTime - startTime)/1000 - ticks.get() * modeledTimeStep) < allowedDiscrepancy);
        }
    }
}
