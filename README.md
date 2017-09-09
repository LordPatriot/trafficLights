This project provides code for an application that simulates a set of traffic lights at
an intersection. The traffic lights are designated (N, S) and (E, W) like a compass.

## Requirements

* When switching from green to red, the yellow light must be displayed for 30 seconds prior to
it switching to red
* The lights will change automatically every 5 minutes
* Provide the output for the light changes which occur during a given thirty minute period
* Provide unit tests for all the logic

## Quick start
To simulate intersection(s) and output the light changes which occur during a given thirty minute period:
* Create a List of intersections (possible containing one intersection):

```
List<Intersection> intersections = new ArrayList<Intersection>;
intersections.add(new Intersection());
```
* Create a controller that will start counting system time in ticks and controlling states of all the intersections you've supplied. _Note that for production updateStep needs to be 1000ms, that means that 1s in real life will be modelled as 1 second in modeled system time. However, for simulation purposes you can specify updateStep to be 10ms, which will mean time inside the modeled environment will be 100 times faster than in real life._
```
int updateStep = 1000;
boolean isDebugMode = false;
TrafficController controller = new TrafficController(intersections, updateStep, isDebugMode);
```