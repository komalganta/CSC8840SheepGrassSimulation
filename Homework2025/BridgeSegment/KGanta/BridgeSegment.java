package Homework2025.BridgeSegment.KGanta;
import BridgeSegment.AbstractBridgeSystem;
import simView.ViewableAtomic;
import genDevs.modeling.message;
import GenCol.entity;
import BridgeSegment.AbstractBridgeSystem.BridgeState;
import java.util.LinkedList;
import java.util.Queue;

//BridgeSegment:
//Cars queue and cross the bridge one by one.
//Each bridge has a direction (EAST_TO_WEST or WEST_TO_EAST).
//Direction flips every fixed traffic light duration.

public class BridgeSegment extends ViewableAtomic {

    private Queue<entity> carQueue;       //waiting cars
    private double crossingTime;          //how long a car takes to cross
    private BridgeState direction;        //current allowed direction
    private double lightDuration;         //time before light flips
    private double timeSinceLastFlip;     //track passed time

    public BridgeSegment(String name, BridgeState initialDirection, double durationTime, double crossingTime) {
        super(name);
        addInport("car_in");
        addOutport("car_out");   //WEST_TO_EAST exits
        addOutport("car_out2");  //EAST_TO_WEST exits

        this.direction = initialDirection;
        this.lightDuration = durationTime;
        this.crossingTime = crossingTime;
    }

    @Override
    public void initialize() {
        carQueue = new LinkedList<>();
        timeSinceLastFlip = 0.0;
        passivate();
    }

    @Override
    public void deltext(double e, message x) {
        Continue(e);
        timeSinceLastFlip += e;

        //this is to flip traffic light if the duration of time had passed
        if (timeSinceLastFlip >= lightDuration) {
            if (direction == BridgeState.WEST_TO_EAST) {
                direction = BridgeState.EAST_TO_WEST;
            } else {
                direction = BridgeState.WEST_TO_EAST;
            }
            timeSinceLastFlip = 0.0;
        }
        //add incoming cars
        for (int i = 0; i < x.getLength(); i++) {
            if (messageOnPort(x, "car_in", i)) {
                entity car = x.getValOnPort("car_in", i);
                carQueue.add(car);
            }
        }
        //if idle but cars are waiting, schedule next crossing
        if (phaseIs("passive") && !carQueue.isEmpty()) {
            holdIn("active", crossingTime);
        }
    }

    @Override
    public void deltint() {
        timeSinceLastFlip += sigma;

        //this is to flip traffic light if the duration of time had passed
        if (timeSinceLastFlip >= lightDuration) {
            if (direction == BridgeState.WEST_TO_EAST) {
                direction = BridgeState.EAST_TO_WEST;
            } else {
                direction = BridgeState.WEST_TO_EAST;
            }
            timeSinceLastFlip = 0.0;
        }

        //remove the car (it exits in out())
        if (!carQueue.isEmpty()) {
            carQueue.poll();
        }

        //schedule for next car if waiting
        if (!carQueue.isEmpty()) {
            holdIn("active", crossingTime);
        } else {
            passivate();
        }
    }

    @Override
    public void deltcon(double e, message x) {
        deltint();
        deltext(0, x);
    }

    @Override
    public message out() {
        message m = new message();
        if (!carQueue.isEmpty()) {
            entity car = carQueue.peek(); //peek
            if (direction == BridgeState.WEST_TO_EAST) {
                m.add(makeContent("car_out", car));
            } else {
                m.add(makeContent("car_out2", car));
            }
        }
        return m;
    }
}