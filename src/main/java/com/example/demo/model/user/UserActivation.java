package com.example.demo.model.user;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "user_activations")
public class UserActivation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private Integer lifetime;

    public UserActivation(Integer id, User user, Date date, int lifetime) {
        this.id = id;
        this.user = user;
        this.date = date;
        this.lifetime = lifetime;
    }

    //region Getters and Setters


    public UserActivation() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getLifetime() {
        return lifetime;
    }

    public void setLifetime(Integer lifetime) {
        this.lifetime = lifetime;
    }

    //endregion
}