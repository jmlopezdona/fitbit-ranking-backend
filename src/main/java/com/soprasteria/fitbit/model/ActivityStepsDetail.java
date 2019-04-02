package com.soprasteria.fitbit.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public class ActivityStepsDetail {

    @JsonAlias("dateTime")
    private String date;

    @JsonAlias("value")
    private long steps;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getSteps() {
        return steps;
    }

    public void setSteps(long steps) {
        this.steps = steps;
    }

}
