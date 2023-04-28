package com.example.demo.model.user;

import jakarta.persistence.*;
import java.util.Date;

@Entity(name = "password_resets")
public class PasswordReset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer code;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private Date expiresIn;

    public PasswordReset(User user) {
        this.user = user;
        Date now = new Date();
        long fiveMinutesInMilliseconds = 5 * 60 * 1000;
        this.expiresIn = new Date(now.getTime() + fiveMinutesInMilliseconds);
    }

    public PasswordReset() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Date expiresIn) {
        this.expiresIn = expiresIn;
    }
}

