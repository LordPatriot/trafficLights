import org.junit.Test;

import java.lang.reflect.*;

import static org.junit.Assert.assertTrue;

public class IntersectionsTests {

    // This method sets private variables for testing purposes solely
    // This is one of the ways to simulate broken state of intersection
    public static void unsafeSetLights(TrafficLight[] lights, int state) {
        try {
            for(TrafficLight light : lights) {
                // Really bad idea for anything other than testing trying to break things
                Field field = TrafficLight.class.getDeclaredField("state");
                field.setAccessible(true);

                // All the lights are set to <state>
                field.setInt(light, state);
            }
        } catch(NoSuchFieldException e) {
            e.printStackTrace();
        } catch(IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void IntersectionInvalidStateWhenSwitching() {
        Intersection intersection = new Intersection();
        TrafficLight[] lights = intersection.getTrafficLights();
        unsafeSetLights(lights, 1);

        // Exception when flipping
        boolean exceptionThrown = false;
        try {
            intersection.flipToEnabled();
        } catch (IllegalStateException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);

        // Exception when turning on
        exceptionThrown = false;
        try {
            intersection.turnToEnabled();
        } catch (IllegalStateException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);

        // Exception when switching to warning
        unsafeSetLights(lights, -1);

        exceptionThrown = false;
        try {
            intersection.switchToWarning();
        } catch (IllegalStateException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
    }
}
