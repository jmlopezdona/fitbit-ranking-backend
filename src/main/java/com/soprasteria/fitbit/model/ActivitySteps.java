package com.soprasteria.fitbit.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public class ActivitySteps {

    @JsonAlias("activities-steps")
    private List<ActivityStepsDetail> activitySteps;

    public List<ActivityStepsDetail> getActivitySteps() {
        return activitySteps;
    }

    public void setActivitySteps(List<ActivityStepsDetail> activitySteps) {
        this.activitySteps = activitySteps;
    }
}
