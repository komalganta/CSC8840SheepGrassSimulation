package Homework2025.PredatorPrey.KGanta;

import genDevs.modeling.*;
import genDevs.simulation.*;

public class Clock extends atomic {
    private GlobalRef globalRef;

    public Clock(String name) {
        super(name);
        globalRef = GlobalRef.getInstance();
    }

    @Override
    public void initialize() {
        // Start simulation in Summer
        // set the phase to "summer" and hold for the season duration
        holdIn("summer", globalRef.seasonLength);

        // Set the global variable to the fast summer rate
        globalRef.currentGrassReproduceT = globalRef.summerGrowthT;

        System.out.println(">>> SEASON START: SUMMER (Grass grows fast: " + globalRef.summerGrowthT + ")");
        super.initialize();
    }

    @Override
    public void deltint() {
        if (phaseIs("summer")) {
            // Summer is over, so switch to Winter
            holdIn("winter", globalRef.seasonLength);
            globalRef.currentGrassReproduceT = globalRef.winterGrowthT;
            System.out.println(">>> SEASON CHANGE: WINTER (Grass grows slow: " + globalRef.winterGrowthT + ")");
        } else {
            // Winter is over, so switch to Summer
            holdIn("summer", globalRef.seasonLength);
            globalRef.currentGrassReproduceT = globalRef.summerGrowthT;
            System.out.println(">>> SEASON CHANGE: SUMMER (Grass grows fast: " + globalRef.summerGrowthT + ")");
        }
    }

    @Override
    public void deltext(double e, message x) {
        Continue(e);
    }

    @Override
    public message out() {
        return new message();
    }
}