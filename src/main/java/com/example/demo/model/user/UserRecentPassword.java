package com.example.demo.model.user;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "user_recent_passwords")
public class UserRecentPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String replacedPassword;

    @Column
    private LocalDate replaceDate;

    public UserRecentPassword(Integer id, User user, String replacedPassword, LocalDate replaceDate) {
        this.id = id;
        this.user = user;
        this.replacedPassword = replacedPassword;
        this.replaceDate = replaceDate;
    }

    public UserRecentPassword() {
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

    public String getReplacedPassword() {
        return replacedPassword;
    }

    public void setReplacedPassword(String replacedPassword) {
        this.replacedPassword = replacedPassword;
    }

    public LocalDate getReplaceDate() {
        return replaceDate;
    }

    public void setReplaceDate(LocalDate replaceDate) {
        this.replaceDate = replaceDate;
    }
}
