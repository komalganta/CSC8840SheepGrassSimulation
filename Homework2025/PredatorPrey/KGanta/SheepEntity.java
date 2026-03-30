package Homework2025.PredatorPrey.KGanta;

import GenCol.entity;

public class SheepEntity extends entity {
    public double toDieTime;
    public double toReproduceTime;

    public SheepEntity(String name, double toDieTime, double toReproduceTime) {
        super(name);
        this.toDieTime = toDieTime;
        this.toReproduceTime = toReproduceTime;
    }
}