package com.example.demo.dto.user;

import com.example.demo.model.user.User;
import jakarta.validation.constraints.*;

public class UserDTO {

    @NotNull
    @Pattern(regexp = "^[a-zA-Z]*$", message = "The name must contain only letters")
    private String name;
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]*$", message = "The name must contain only letters")
    private String surname;
    @NotNull
    @Size(min = 5, max = 15)
    private String telephoneNumber;
    @NotNull
    @Email
    private String email;
    @NotNull
    @Size(min = 10, max = 50)
    private String address; //za sada ovako
    @NotNull
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9]).{8,}$", message = "Password must contain at least 8 characters," +
            "1 uppercase letter, q special character and 1 number")
    private String password;


    //region Constructors

    public UserDTO(){}

    public UserDTO(String name, String surname, String telephoneNumber, String email, String address, String password) {
        this.name = name;
        this.surname = surname;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
        this.address = address;
        this.password = password;
    }

    public UserDTO(User user) {
        this.name = user.getName();
        this.surname = user.getSurname();
        this.telephoneNumber = user.getTelephoneNumber();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.password = user.getPassword();
    }

    //endregion

    //region Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    //endregion
}
