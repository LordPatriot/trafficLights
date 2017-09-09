import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.*;

import static org.junit.Assert.assertTrue;

/*
* These tests test some of the invalid state switches for the intersection
 */
public class IntersectionsTests {
    private Intersection intersection;
    private TrafficLight[] lights;
    boolean exceptionThrown;

    @Before
    public void init() {
        intersection = new Intersection();
        lights = intersection.getTrafficLights();
        exceptionThrown = false;
    }

    @Test
    public void IntersectionInvalidStateWhenFlippingToEnabled() {
        unsafeSetLights(lights, 1);

        // Exception when flipping
        try {
            intersection.flipToEnabled();
        } catch (IllegalStateException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
    }

    @Test
    public void IntersectionInvalidStateWhenTurningToEnabled() {
        for(int i: new int[]{0, 1}) {
            unsafeSetLights(lights, i);

            // Exception when turning on
            try {
                intersection.turnToEnabled();
            } catch (IllegalStateException e) {
                exceptionThrown = true;
            }

            assertTrue(exceptionThrown);
        }
    }

    @Test
    public void IntersectionInvalidStateWhenSwitchingToWarning() {
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
}
