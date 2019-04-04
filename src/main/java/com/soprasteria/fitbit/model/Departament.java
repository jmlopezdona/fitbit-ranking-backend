package com.soprasteria.fitbit.model;

public class Departament {

    private String name;
    private int position;
    private long currentSteps;
    private long previusSteps;
    private String trend;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public long getCurrentSteps() {
        return currentSteps;
    }

    public void setCurrentSteps(long currentSteps) {
        this.currentSteps = currentSteps;
    }

    public long getPreviusSteps() {
        return previusSteps;
    }

    public void setPreviusSteps(long previusSteps) {
        this.previusSteps = previusSteps;
    }

    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }

    public Departament(String name, long currentSteps) {
        this.name = name;
        this.currentSteps = currentSteps;
    }
}
