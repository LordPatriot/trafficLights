/*
* Intersection is the minimal addressable instance of the whole traffic control system.
* As per requirements there are no complex intersections, hence intersection represents a crossing
* of two roads with 4 traffic lights.
 */
public class Intersection {

    private TrafficLight north;
    private TrafficLight south;
    private TrafficLight east;
    private TrafficLight west;
    
    // Stores number of ticks this intersection was in the last state (Enabled/Warning/Blocked)
    private long ticksCounter;

    public Intersection() {
        north = new TrafficLight();
        south = new TrafficLight();
        east = new TrafficLight();
        west = new TrafficLight();
        ticksCounter = 0;
    }

    /*
    * Check if intersection is in Warning state, follow strict definition of traffic lights states
     */
    public boolean isInWarningState() {
        return ( (north.getState() == 0 && south.getState() == 0)
                || (east.getState() == 0 && west.getState() == 0) );
    }

    /*
    * Check if intersection is in Enabled state, follow strict definition of traffic lights states
     */
    public boolean isInEnabledState() {
        boolean enabledNorthSouth = north.getState() == 1 && south.getState() == 1
                && east.getState() == -1 && west.getState() == -1;
        boolean enabledEastWest = north.getState() == -1 && south.getState() == -1
                && east.getState() == 1 && west.getState() == 1;
        return enabledEastWest || enabledNorthSouth;
    }

    /*
    * Mutate intersection state from Enabled to Warning
     */
    public void switchToWarning() {
        // reset time counter
        ticksCounter = 0;

        if(north.getState() == 1 && south.getState() == 1) {
            north.switchToYellow();
            south.switchToYellow();
        } else if(east.getState() == 1 && west.getState() == 1) {
            east.switchToYellow();
            west.switchToYellow();
        } else {
            throw new IllegalStateException();
        }
    }

    /*
    * Flip intersection state from Warning to Enabled
     */
    public void flipToEnabled() {
        ticksCounter = 0;

        if(north.getState() == 0 && south.getState() == 0) {
            north.switchToRed();
            south.switchToRed();
            east.switchToGreen();
            west.switchToGreen();
        } else if(east.getState() == 0 && west.getState() == 0) {
            east.switchToRed();
            west.switchToRed();
            north.switchToGreen();
            south.switchToGreen();
        } else {
            throw new IllegalStateException();
        }
    }

    /*
    * Mutate intersection state from initial safe blocked state to Enabled
     */
    public void turnToEnabled() {
        ticksCounter = 0;

        // Currently this logic is checked in the caller's code
        // However, we can't be sure every caller will check it
        if(isInBlockedState()) {
            north.switchToGreen();
            south.switchToGreen();
        } else {
            throw new IllegalStateException();
        }
    }

    /*
    * Checker for the initial safe state, in this state all traffic lights are blocked
     */
    public boolean isInBlockedState() {
        return ( north.getState() == -1 && south.getState() == -1
                && east.getState() == -1 && west.getState() == -1 );
    }

    public void incrementTimeCounter() {
        ticksCounter++;
    }

    public long getTicksCounter() {
        return ticksCounter;
    }

    public String getStatesDescription() {
        return String.format( "North: %s\nSouth: %s\nEast: %s\nWest: %s",
                north.getStateDescription(), south.getStateDescription(),
                east.getStateDescription(), west.getStateDescription() );
    }

    public TrafficLight[] getTrafficLights() {
        TrafficLight[] lights = new TrafficLight[4];
        lights[0] = north;
        lights[1] = south;
        lights[2] = east;
        lights[3] = west;
        return lights;
    }
}
