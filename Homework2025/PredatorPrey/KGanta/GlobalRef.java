package Homework2025.PredatorPrey.KGanta;

import genDevs.modeling.*;
import java.util.Random;

/**
 * This class defines a GlobalRef that makes it easy to find a cell and its
 * information, such as current state of the cell, and the cell's reference
 *
 * @author  Xiaolin Hu
 * @Date: Sept. 2007
 */
public class GlobalRef {
    protected static int xDim;
    protected static int yDim;
    protected static GlobalRef _instance = null;

    public String[][] state;
    public IODevs[][] cell_ref;
    public Random rand;

    public double grassReproduceT = 1.4;
    public double sheepMoveT = 1.5;

    // The Clock updates this, and the Cells read this.
    // Dont change manually during the sim as the clock will do it
    public double currentGrassReproduceT;

    //SCENARIO 1: Basline
//    public double sheepReproduceT = 9.0; // Slow reproduction
//    public double summerGrowthT = 1.4;   // Moderate Summer
//    public double winterGrowthT = 5.0;   // Moderate Winter
//    public double seasonLength = 60.0;   // Fast Seasons
//    public double sheepLifeT = 3.0;      // moderate life

     /*SCENARIO 2: OVERPOPULATION TRAP
       Shows that better summer conditions actually destroy the ecosystem.
    */
// public double sheepReproduceT = 4.0; // FAST reproduction
//     public double summerGrowthT = 0.5;   // SUPER FAST Summer
//     public double winterGrowthT = 5.0;   // Moderate Winter
//     public double seasonLength = 60.0;   // Frequent seasons
//     public double sheepLifeT = 3.0;      // Moderate life

    /*SCENARIO 3: TIPPING POINT
       Goal: Show total extinction caused by environmental severity.
    */
 public double sheepReproduceT = 9.0; // Reset to slow
     public double summerGrowthT = 1.4;   // Reset Summer to normal
     public double winterGrowthT = 30.0;  // strong winter
     public double seasonLength = 150.0;  // Long Duration
     public double sheepLifeT = 3.0;      // Moderate life


    private GlobalRef() {
        rand = new Random(12345);
        // Initialize the dynamic variable to start in Summer
        currentGrassReproduceT = summerGrowthT;
    }

    public static GlobalRef getInstance() {
        if (_instance != null) return _instance;
        else {
            _instance = new GlobalRef();
            return _instance;
        }
    }

    public void setDim(int x, int y) {
        xDim = x;
        yDim = y;
        state = new String[xDim][yDim];
        cell_ref = new IODevs[xDim][yDim];
    }
}
