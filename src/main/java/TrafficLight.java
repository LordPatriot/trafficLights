public class TrafficLight {

    /*
    * Current traffic light state,
    * Much more memory efficient to store in int rather than String
     */
    private int state;

    /*
    * -1 = Red, stop
    *  0 = Yellow, warning
    *  1 = Green, go
     */
    private static String[] stateDescriptions = new String[]{"Red", "Yellow", "Green"};

    public TrafficLight() {
        // Always start with RED since it is safe
        state = -1;
    }

    public void switchToGreen() { state = 1; }

    public void switchToYellow() { state = 0; }

    public void switchToRed() { state = -1; }

    public int getState() { return state; }

    public String getStateDescription() {
        return stateDescriptions[state + 1];
    }
}