package com.example.hitthemole;

public class Scores {
    String name,points,life_left,mode;

    public Scores(String name, String points, String life_left, String mode) {
        this.name = name;
        this.points = points;
        this.life_left = life_left;
        this.mode = mode;
    }

    public Scores() {
    }

    public String getName() {
        return name;
    }

    public String getPoints() {
        return points;
    }

    public String getLife_left() {
        return life_left;
    }

    public String getMode() {
        return mode;
    }
}
