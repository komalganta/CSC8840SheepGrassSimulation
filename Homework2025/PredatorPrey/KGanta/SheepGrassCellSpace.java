package Homework2025.PredatorPrey.KGanta;

import genDevs.simulation.*;
import genDevs.simulation.realTime.TunableCoordinator;
import genDevs.plots.newCellGridPlot;
import twoDCellSpace.TwoDimCell;
import twoDCellSpace.TwoDimCellSpace;
import javax.swing.*;


public class SheepGrassCellSpace extends TwoDimCellSpace {

    public newCellGridPlot plot;
    private int scenario = 6;
    private final GlobalRef globalRef;

    public String getPortNameFromCoords(SheepGrassCell fromCell, int targetX, int targetY) {
        int dx = targetX - fromCell.getXcoord();
        int dy = targetY - fromCell.getYcoord();

        // handle wrapping
        if (dx > 1) dx = -1;
        else if (dx < -1) dx = 1;
        if (dy > 1) dy = -1;
        else if (dy < -1) dy = 1;

        if (dx == 0 && dy == 1) return "outN";
        if (dx == 1 && dy == 1) return "outNE";
        if (dx == 1 && dy == 0) return "outE";
        if (dx == 1 && dy == -1) return "outSE";
        if (dx == 0 && dy == -1) return "outS";
        if (dx == -1 && dy == -1) return "outSW";
        if (dx == -1 && dy == 0) return "outW";
        if (dx == -1 && dy == 1) return "outNW";

        return "";
    }


    public SheepGrassCellSpace() {
        this(40, 40);
    }

    public SheepGrassCellSpace(int xDim, int yDim) {
        super("SheepGrassCellSpace", xDim, yDim);
        this.globalRef = GlobalRef.getInstance();
        globalRef.setDim(xDim, yDim);


        //configure window and position the plot model
        plot = new newCellGridPlot("Sheep Grass Plot", 0.1, "", 600, "", 600);
        plot.setCellSize(10);
        plot.setCellGridViewLocation(570, 100);
        add(plot);

        //Add the Global Clock model
        Clock clock = new Clock("GlobalClock");
        add(clock);


        //initialize all cells and store the references
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                SheepGrassCell cell = new SheepGrassCell(i, j);
                add(cell);
                globalRef.cell_ref[i][j] = cell;
                globalRef.state[i][j] = "empty";
            }
        }



        //set up the initial state based on scenario
        switch (scenario) {
            case 1: constructScenario1(); break;
            case 2: constructScenario2(); break;
            case 3: constructScenario3(); break;
            case 4: constructScenario4(); break;
            case 5: constructScenario5(); break;
            case 6: constructScenario6(); break;
        }

        //here we add couplings after all cells are created and initialized
        doNeighborToNeighborCoupling();
        DoBoundaryToBoundaryCoupling();
    }


    private void constructScenario1() {
        //one grass with no sheep
        ((SheepGrassCell) withId(xDimCellspace / 2, yDimCellspace / 2)).setInitialState("grass");
    }

    private void constructScenario2() {
        // Multiple grass cells at different locations of the space. no sheep
        int cx = xDimCellspace / 2;
        int cy = yDimCellspace / 2;
        ((SheepGrassCell) withId(cx, cy)).setInitialState("grass");
        ((SheepGrassCell) withId(cx + 4, cy)).setInitialState("grass");
        ((SheepGrassCell) withId(cx, cy + 7)).setInitialState("grass");
    }

    private void constructScenario3() {
        // One sheep
        ((SheepGrassCell) withId(xDimCellspace / 2, yDimCellspace / 2)).setInitialState("sheep");
    }

    private void constructScenario4() {
        // Multiple sheep
        int cx = xDimCellspace / 2;
        int cy = yDimCellspace / 2;
        ((SheepGrassCell) withId(cx, cy)).setInitialState("sheep");
        ((SheepGrassCell) withId(cx + 4, cy)).setInitialState("sheep");
        ((SheepGrassCell) withId(cx, cy + 7)).setInitialState("sheep");
    }

    private void constructScenario5() {
        // Two neighboring grass cells and one sheep adjacent
        int cx = xDimCellspace / 2;
        int cy = yDimCellspace / 2;
        ((SheepGrassCell) withId(cx, cy)).setInitialState("grass");
        ((SheepGrassCell) withId(cx + 1, cy)).setInitialState("grass");
        ((SheepGrassCell) withId(cx, cy + 1)).setInitialState("sheep");
    }

    private void constructScenario6() {
        // Multiple sheep and grass
        int cx = xDimCellspace / 2;
        int cy = yDimCellspace / 2;
        int range = 5; // cluster size

        for (int i = 0; i < 20; i++) {
            int x = cx - range + globalRef.rand.nextInt(range * 2 + 1);
            int y = cy - range + globalRef.rand.nextInt(range * 2 + 1);
            ((SheepGrassCell) withId(x, y)).setInitialState("sheep");
        }

        for (int i = 0; i < 40; i++) {
            int x = cx - range + globalRef.rand.nextInt(range * 2 + 1);
            int y = cy - range + globalRef.rand.nextInt(range * 2 + 1);
            if (globalRef.state[x][y].equals("empty")) {
                ((SheepGrassCell) withId(x, y)).setInitialState("grass");
            }
        }
    }

    public static void main(String[] args) {
        TunableCoordinator r = new TunableCoordinator(new SheepGrassCellSpace());
        r.setTimeScale(0.2);
        r.initialize();
        r.simulate(10000);
    }

    private void DoBoundaryToBoundaryCoupling() {
        for( int x = 1; x < xDimCellspace-1; x++ ) {
            addCoupling(withId(x, 0), "outS", withId(x, yDimCellspace-1), "inN");
            addCoupling(withId(x, 0), "outSW", withId(x-1, yDimCellspace-1), "inNE");
            addCoupling(withId(x, 0), "outSE", withId(x+1, yDimCellspace-1), "inNW");
            addCoupling(withId(x, yDimCellspace-1), "outN", withId(x, 0), "inS");
            addCoupling(withId(x, yDimCellspace-1), "outNE", withId(x+1, 0), "inSW");
            addCoupling(withId(x, yDimCellspace-1), "outNW", withId(x-1, 0), "inSE");
        }
        for( int y = 1; y < yDimCellspace-1; y++ ) {
            addCoupling(withId(0, y), "outW", withId(xDimCellspace-1, y), "inE");
            addCoupling(withId(0, y), "outSW", withId(xDimCellspace-1, y-1), "inNE");
            addCoupling(withId(0, y), "outNW", withId(xDimCellspace-1, y+1), "inSE");
            addCoupling(withId(xDimCellspace-1, y), "outE", withId(0, y), "inW");
            addCoupling(withId(xDimCellspace-1, y), "outNE", withId(0, y+1), "inSW");
            addCoupling(withId(xDimCellspace-1, y), "outSE", withId(0, y-1), "inNW");
        }
        addCoupling(withId(0, 0), "outNW", withId(xDimCellspace-1, 1), "inSE");
        addCoupling(withId(0, 0), "outW", withId(xDimCellspace-1, 0), "inE");
        addCoupling(withId(0, 0), "outSW", withId(xDimCellspace-1, yDimCellspace-1), "inNE");
        addCoupling(withId(0, 0), "outS", withId(0, yDimCellspace-1), "inN");
        addCoupling(withId(0, 0), "outSE", withId(1, yDimCellspace-1), "inNW");
        addCoupling(withId(xDimCellspace-1, 0), "outSW", withId(xDimCellspace-2, yDimCellspace-1), "inNE");
        addCoupling(withId(xDimCellspace-1, 0), "outE", withId(0, 0), "inW");
        addCoupling(withId(xDimCellspace-1, 0), "outSE", withId(0, yDimCellspace-1), "inNW");
        addCoupling(withId(xDimCellspace-1, 0), "outS", withId(xDimCellspace-1, yDimCellspace-1), "inN");
        addCoupling(withId(xDimCellspace-1, 0), "outNE", withId(0, 1), "inSW");
        addCoupling(withId(0, yDimCellspace-1), "outSW", withId(xDimCellspace-1, yDimCellspace-2), "inNE");
        addCoupling(withId(0, yDimCellspace-1), "outW", withId(xDimCellspace-1, yDimCellspace-1), "inE");
        addCoupling(withId(0, yDimCellspace-1), "outNE", withId(1, 0), "inSW");
        addCoupling(withId(0, yDimCellspace-1), "outN", withId(0, 0), "inS");
        addCoupling(withId(0, yDimCellspace-1), "outNW", withId(xDimCellspace-1, 0), "inSE");
        addCoupling(withId(xDimCellspace-1, yDimCellspace-1), "outNW", withId(xDimCellspace-2, 0), "inSE");
        addCoupling(withId(xDimCellspace-1, yDimCellspace-1), "outE", withId(0, yDimCellspace-1), "inW");
        addCoupling(withId(xDimCellspace-1, yDimCellspace-1), "outSE", withId(0, yDimCellspace-2), "inNW");
        addCoupling(withId(xDimCellspace-1, yDimCellspace-1), "outN", withId(xDimCellspace-1, 0), "inS");
        addCoupling(withId(xDimCellspace-1, yDimCellspace-1), "outNE", withId(0, 0), "inSW");
    }
    public int[] getNeighborXYCoord(TwoDimCell myCell, int direction) {
        int[] myneighbor = new int[2];
        int tempXplus1 = myCell.getXcoord() + 1;
        int tempXminus1 = myCell.getXcoord() - 1;
        int tempYplus1 = myCell.getYcoord() + 1;
        int tempYminus1 = myCell.getYcoord() - 1;
        if (tempXplus1 >= xDimCellspace) tempXplus1 = 0;
        if (tempXminus1 < 0) tempXminus1 = xDimCellspace - 1;
        if (tempYplus1 >= yDimCellspace) tempYplus1 = 0;
        if (tempYminus1 < 0) tempYminus1 = yDimCellspace - 1;

        switch (direction) {
            case 0: myneighbor[0] = myCell.getXcoord(); myneighbor[1] = tempYplus1; break;
            case 1: myneighbor[0] = tempXplus1; myneighbor[1] = tempYplus1; break;
            case 2: myneighbor[0] = tempXplus1; myneighbor[1] = myCell.getYcoord(); break;
            case 3: myneighbor[0] = tempXplus1; myneighbor[1] = tempYminus1; break;
            case 4: myneighbor[0] = myCell.getXcoord(); myneighbor[1] = tempYminus1; break;
            case 5: myneighbor[0] = tempXminus1; myneighbor[1] = tempYminus1; break;
            case 6: myneighbor[0] = tempXminus1; myneighbor[1] = myCell.getYcoord(); break;
            case 7: myneighbor[0] = tempXminus1; myneighbor[1] = tempYplus1; break;
        }
        return myneighbor;
    }
}