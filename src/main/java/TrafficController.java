import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TrafficController {

    // Reference to the list of intersections system has
    private List<Intersection> intersections;

    // Stores modeled time of the system, in number of ticks
    private long systemTicks;

    // Period after which Warning (yellow) should be switched to red
    public static int WARNING_PERIOD = 30;

    // Period after which Enabled (green) should be switched to Warning (yellow)
    public static int ENABLED_PERIOD = 270;

    /*
    * Scheduled thread pool of size 1 allows us to run time controller logic in a separate thread
     */
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public TrafficController(List<Intersection> intersections, int updateStep, boolean isDebugMode) {
        this.intersections = intersections;
        systemTicks = 0;
        init(updateStep, isDebugMode);
    }

    private void init(int updateStep, final boolean isDebugMode) {
        final Runnable ticker = new Runnable() {
            public void run() {
                // Increase the ticks counter, system time increments by one
                systemTicks++;
                // Go through all the intersections in the system
                for(Intersection current : intersections) {
                    if(current.isInBlockedState()) {
                        // Enable immediately if traffic light is blocked
                        current.turnToEnabled();
                    } else if( current.isInEnabledState() && current.getTicksCounter() >= ENABLED_PERIOD ) {
                        // Switch to Warning state if traffic light was in Enabled state for at least ENABLED_PERIOD
                        current.switchToWarning();
                    } else if( current.isInWarningState() && current.getTicksCounter() >= WARNING_PERIOD ) {
                        // Flip to Enabled state if traffic light was in Warning state for at least WARNING_PERIOD
                        current.flipToEnabled();
                    } else {
                        // Increment traffic light's counter, it has been in its state for one more tick
                        current.incrementTimeCounter();
                    }
                }

                // As per requirements, printing states if in debug mode
                // (assuming there is no need to do it on production)
                if(isDebugMode) {
                    System.out.println("System tick: " + systemTicks);
                    for(Intersection current: intersections) {
                        System.out.println("Intersection time: " + current.getTicksCounter());
                        System.out.println(current.getStatesDescription());
                    }
                }
            }
        };

        // Schedule to run in a thread every <updateStep> seconds
        scheduler.scheduleAtFixedRate(ticker, 0, updateStep, TimeUnit.SECONDS);
    }
}
