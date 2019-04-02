package com.soprasteria.fitbit.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NaturalId
    @Column(name="user_id", nullable = false, unique = true)
    private String userId;

    private String name;

    private String departament;

    @Column(name="avatar_uri")
    private String avatarUri;

    @Column(name="refresh_token", nullable = false)
    private String refreshToken;

    @Column(name="current_steps")
    private long currentSteps;

    @Column(name="previus_steps")
    private long previusSteps;

    @Transient
    private int position;

    @Transient
    private String trend;

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartament() {
        return departament;
    }

    public void setDepartament(String departament) {
        this.departament = departament;
    }

    public String getAvatarUri() {
        return avatarUri;
    }

    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }
}