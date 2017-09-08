import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ModeledTimeController {

    /*
    * Reference to the variable that defines time in ticks within our system
     */
    private final AtomicLong modeledTime;

    /*
    * Discrete time step in modeled environment (in seconds), defines system's granularity,
    * modeledTimeStep = X means that one tick of modelled system is X seconds in real world.
    * This gives us the granularity we want (instead of system milliseconds that can be an overkill)
     */
    private final long modeledTimeStep;

    /*
    * Scheduled thread pool of size 1 allows us to run time controller logic in a separate thread
     */
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public ModeledTimeController(AtomicLong modeledTime, int modeledTimeStep) {
        this.modeledTime = modeledTime;
        this.modeledTimeStep = modeledTimeStep;
        initTimeTicking();
    }

    /*
    * Starts up a thread that is responsible for modeling time in distinct ticks,
    * This thread will be updating <modeledTime> counter every <modeledTimeStep> seconds
     */
    private void initTimeTicking() {
        final Runnable ticker = new Runnable() {
            public void run() {
                // The only job of this runnable is to increment time counter
                modeledTime.getAndIncrement();
            }
        };

        scheduler.scheduleAtFixedRate(ticker, 0, modeledTimeStep, TimeUnit.SECONDS);
    }
}
