import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TrafficController {

    // Reference to the list of intersections system has
    private List<Intersection> intersections;

    /*
     * Stores modeled time of the system, in number of ticks. In production
     * 1 tick = 1 second. However, such system allows to flexibly define what tick is.
     * In case of simulation and testing tick can be specified as low as i.e. 10ms and
      * we will be able to simulate the system 100 times faster
      */
    private long systemTicks;

    // Period after which Warning (yellow) should be switched to red
    public static int WARNING_PERIOD = 30;

    // Period after which Enabled (green) should be switched to Warning (yellow)
    public static int ENABLED_PERIOD = 270;

    // Describes after which time system should be stopped
    public static int SYSTEM_LIFETIME = 30 * 60;

    /*
    * The following 3 counters help us to reason about the correctness of the system and
    * simplifies testing
     */
    private long warningToEnabledCounter;
    private long enabledToWarningCounter;
    private long blockedToEnabledCounter;

    /*
    * Scheduled thread pool of size 1 allows us to run time controller logic in a separate thread
     */
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public TrafficController(List<Intersection> intersections, int updateStep, boolean isDebugMode) {
        this.intersections = intersections;
        systemTicks = 0;
        warningToEnabledCounter = 0;
        enabledToWarningCounter = 0;
        blockedToEnabledCounter = 0;
        init(updateStep, isDebugMode);
    }

    private void printStateChange(Intersection current, boolean isDebugMode) {
        // As per requirements, printing states if in debug mode
        // (assuming there is no need to do it on production)
        if(isDebugMode) {
            System.out.println("System tick: " + systemTicks);
            System.out.println(current.getStatesDescription());
        }
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
                        printStateChange(current, isDebugMode);
                        blockedToEnabledCounter++;
                    } else if( current.isInEnabledState() && current.getTicksCounter() >= ENABLED_PERIOD ) {
                        // Switch to Warning state if traffic light was in Enabled state for at least ENABLED_PERIOD
                        current.switchToWarning();
                        printStateChange(current, isDebugMode);
                        enabledToWarningCounter++;
                    } else if( current.isInWarningState() && current.getTicksCounter() >= WARNING_PERIOD ) {
                        // Flip to Enabled state if traffic light was in Warning state for at least WARNING_PERIOD
                        current.flipToEnabled();
                        printStateChange(current, isDebugMode);
                        warningToEnabledCounter++;
                    } else {
                        // Increment traffic light's counter, it has been in its state for one more tick
                        current.incrementTimeCounter();
                    }
                }
            }
        };

        // Schedule to run in a thread every <updateStep> milliseconds
        scheduler.scheduleAtFixedRate(ticker, 0, updateStep, TimeUnit.MILLISECONDS);

        // Timer to kill it after 30 minutes
        Timer stopSystemTimer = new Timer();
        stopSystemTimer.schedule(new TimerTask() {
            public void run(){
                if(isDebugMode) {
                    System.out.println(String.format("Stopping system after %s ticks", systemTicks));
                }
                scheduler.shutdownNow();
            }
        }, updateStep * SYSTEM_LIFETIME);
    }

    public long getWarningToEnabledCounter() { return warningToEnabledCounter; }
    public long getBlockedToEnabledCounter() { return blockedToEnabledCounter; }
    public long getEnabledToWarningCounter() { return enabledToWarningCounter; }
}
