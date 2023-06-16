package com.example.demo.model.user;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity(name = "two_step_authentication")
public class TwoStepAuthentication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private LocalDate expirationDate;

    @Column
    private Integer code;

    public  TwoStepAuthentication() {

    }

    public TwoStepAuthentication(Integer id, User user, LocalDate expiresIn, Integer code) {
        this.id = id;
        this.user = user;
        this.expirationDate = expiresIn;
        this.code = code;
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

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
