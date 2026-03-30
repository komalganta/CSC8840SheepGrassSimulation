package Homework2025.PredatorPrey.KGanta;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import GenCol.entity;
import genDevs.modeling.message;
import genDevs.plots.newCellGridView;
import twoDCellSpace.TwoDimCell;

public class SheepGrassCell extends TwoDimCell {

    private String state;
    private newCellGridView view;
    private final GlobalRef globalRef;

    //timers for the sheep
    private double toMoveTime;
    private double toDieTime;
    private double toReproduceTime;

    //output message
    private entity contentToSend = null;
    private String portToSendOn = "";

    public SheepGrassCell(int x, int y) {
        super(new GenCol.Pair(x, y));
        this.globalRef = GlobalRef.getInstance();
    }

    @Override
    public void initialize() {
        super.initialize();
        this.view = ((SheepGrassCellSpace) getParent()).plot.getCellGridView();

        if (state == null) {
            becomeEmpty();
        } else if (state.equals("grass")) {
            becomeGrass();
        } else if (state.equals("sheep")) {
            becomeSheep(globalRef.sheepLifeT, globalRef.sheepReproduceT);
        }
    }

    @Override
    public void deltext(double e, message x) {
        Continue(e);

        //update the timers if this cell is already sheep
        if (state.equals("sheep")) {
            updateSheepTimers(e);
        }

        //process incoming messages from neighbors
        for (int i = 0; i < x.getLength(); i++) {
            if (isInputFromNeighbor(x, i)) {
                entity val = getNeighborInputValue(x, i);
                if (val != null) {
                    if (val.getName().equals("becomeGrass") && state.equals("empty")) {
                        becomeGrass();
                    } else if (val instanceof SheepEntity) {
                        SheepEntity sheep = (SheepEntity) val;
                        if (state.equals("grass")) {
                            //sheep moves onto grass and eats it
                            becomeSheep(sheep.toDieTime + globalRef.sheepLifeT, sheep.toReproduceTime);
                        } else if (state.equals("empty")) {
                            //sheep moves/born into an empty cell
                            becomeSheep(sheep.toDieTime, sheep.toReproduceTime);
                        }
                    }
                }
            }
        }
        if (state.equals("sheep")) {
            scheduleNextSheepEvent();
        }
    }


    @Override
    public void deltint() {
        //handles state transition after a message has been sent out
        if (phaseIs("outputting")) {
            if (contentToSend instanceof SheepEntity) {
                if (contentToSend.getName().equals("sheep")) {
                    //sheep moved away so this cell becomes empty

                    becomeEmpty();
                } else if (contentToSend.getName().equals("newSheep")) {
                    //sheep has reproduced and reset its own reproduction timer and schedule its next event
                    toReproduceTime = globalRef.sheepReproduceT;
                    scheduleNextSheepEvent();
                }
            } else if (contentToSend != null && contentToSend.getName().equals("becomeGrass")) {
                //grass cell has reproduced and for next reproduction cycle
                // new
                holdIn("grass", globalRef.currentGrassReproduceT);
            }
            return;
        }

        //handles the internal event that was scheduled to happen now
        if (state.equals("grass")) {
            int[] neighbor = findRandomEmptyNeighbor();
            if (neighbor != null) {
                prepareMessage(new entity("becomeGrass"), neighbor[0], neighbor[1]);
                holdIn("outputting", 0);
            } else {
                //no empty neighbors so try again
                //new
                holdIn("grass", globalRef.currentGrassReproduceT);
            }
        } else if (state.equals("sheep")) {
            // sigma time passed so update the timers to reflect this
            updateSheepTimers(sigma);

            double epsilon = 1E-6; //floating point comparison

            //check which event occurred based on which timer is at/below zero
            if (toDieTime <= epsilon) {
                becomeEmpty();
            } else if (toReproduceTime <= epsilon) {
                int[] neighbor = findRandomEmptyNeighbor();
                if (neighbor != null) {
                    prepareMessage(new SheepEntity("newSheep", globalRef.sheepLifeT, globalRef.sheepReproduceT),
                            neighbor[0], neighbor[1]);
                    holdIn("outputting", 0);
                } else {
                    //cant reproduce now so reset timer and schedule next event
                    toReproduceTime = globalRef.sheepReproduceT;
                    scheduleNextSheepEvent();
                }
            } else if (toMoveTime <= epsilon) {
                int[] target = findMoveTarget();
                if (target != null) {
                    prepareMessage(new SheepEntity("sheep", toDieTime, toReproduceTime), target[0], target[1]);
                    holdIn("outputting", 0);
                } else {
                    //cant move now so reset timer and schedule the next event
                    toMoveTime = globalRef.sheepMoveT;
                    scheduleNextSheepEvent();
                }
            }
        }
    }

    @Override
    public message out() {
        message m = new message();
        if (phaseIs("outputting") && contentToSend != null && !portToSendOn.isEmpty()) {
            m.add(makeContent(portToSendOn, contentToSend));
        }
        return m;
    }

    private void draw(Color color) {
        view.drawCell(xcoord, globalRef.yDim - 1 - ycoord, color);
    }

    private void becomeEmpty() {
        state = "empty";
        globalRef.state[xcoord][ycoord] = "empty";
        draw(Color.WHITE);
        passivate();
    }

    private void becomeGrass() {
        state = "grass";
        globalRef.state[xcoord][ycoord] = "grass";
        draw(Color.GREEN);
        holdIn("grass", globalRef.currentGrassReproduceT);
    }

    private void becomeSheep(double dieTime, double reproduceTime) {
        state = "sheep";
        globalRef.state[xcoord][ycoord] = "sheep";
        draw(Color.RED);

        toMoveTime = globalRef.sheepMoveT;
        toDieTime = dieTime;
        toReproduceTime = reproduceTime;

        scheduleNextSheepEvent();
    }

    private void scheduleNextSheepEvent() {
        if (!state.equals("sheep")) return;
        double minTime = Math.min(toMoveTime, Math.min(toDieTime, toReproduceTime));
        holdIn("sheep", minTime);
    }

    private void updateSheepTimers(double elapsedTime) {
        toMoveTime -= elapsedTime;
        toDieTime -= elapsedTime;
        toReproduceTime -= elapsedTime;
    }

    private void prepareMessage(entity content, int targetX, int targetY) {
        this.contentToSend = content;
        this.portToSendOn = ((SheepGrassCellSpace)getParent()).getPortNameFromCoords(this, targetX, targetY);
    }

    private int[] findRandomEmptyNeighbor() {
        List<int[]> emptyNeighbors = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            int[] coords = ((SheepGrassCellSpace) getParent()).getNeighborXYCoord(this, i);
            if (globalRef.state[coords[0]][coords[1]].equals("empty")) {
                emptyNeighbors.add(coords);
            }
        }
        if (emptyNeighbors.isEmpty()) return null;
        return emptyNeighbors.get(globalRef.rand.nextInt(emptyNeighbors.size()));
    }

    private int[] findMoveTarget() {
        List<int[]> grassNeighbors = new ArrayList<>();
        List<int[]> emptyNeighbors = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            int[] coords = ((SheepGrassCellSpace) getParent()).getNeighborXYCoord(this, i);
            String neighborState = globalRef.state[coords[0]][coords[1]];
            if (neighborState.equals("grass")) grassNeighbors.add(coords);
            else if (neighborState.equals("empty")) emptyNeighbors.add(coords);
        }

        if (!grassNeighbors.isEmpty())
            return grassNeighbors.get(globalRef.rand.nextInt(grassNeighbors.size()));
        if (!emptyNeighbors.isEmpty())
            return emptyNeighbors.get(globalRef.rand.nextInt(emptyNeighbors.size()));

        return null;
    }

    private boolean isInputFromNeighbor(message x, int i) {
        String[] ports = {"inN", "inNE", "inE", "inSE", "inS", "inSW", "inW", "inNW"};
        for (String port : ports) if (messageOnPort(x, port, i)) return true;
        return false;
    }

    private entity getNeighborInputValue(message x, int i) {
        String[] ports = {"inN", "inNE", "inE", "inSE", "inS", "inSW", "inW", "inNW"};
        for (String port : ports) if (messageOnPort(x, port, i)) return x.getValOnPort(port, i);
        return null;
    }

    public void setInitialState(String state) {
        this.state = state;
    }
}