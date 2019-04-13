package com.soprasteria.fitbit.model;

import javax.persistence.*;

@Entity
@Table(name = "user_steps")
public class UserSteps extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    @Column(name = "current_ranking_steps", nullable = false, updatable = false)
    private Long currentRankingSteps;

    @Column(name = "previous_ranking_steps", nullable = false, updatable = false)
    private Long previosRankingSteps;

    @Column(name = "today_steps", nullable = false, updatable = false)
    private Long todaySteps;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getCurrentRankingSteps() {
        return currentRankingSteps;
    }

    public void setCurrentRankingSteps(Long currentRankingSteps) {
        this.currentRankingSteps = currentRankingSteps;
    }

    public Long getPreviosRankingSteps() {
        return previosRankingSteps;
    }

    public void setPreviosRankingSteps(Long previosRankingSteps) {
        this.previosRankingSteps = previosRankingSteps;
    }

    public Long getTodaySteps() {
        return todaySteps;
    }

    public void setTodaySteps(Long todaySteps) {
        this.todaySteps = todaySteps;
    }
}
