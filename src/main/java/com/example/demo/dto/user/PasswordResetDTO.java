package com.example.demo.dto.user;

import jakarta.validation.constraints.Pattern;

public class PasswordResetDTO {

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9]).{8,}$", message = "Password must contain at least 8 characters," +
            "1 uppercase letter, 1 special character and 1 number")
    private String newPassword;
    private Integer code;

    public PasswordResetDTO(String newPassword, Integer code) {
        this.newPassword = newPassword;
        this.code = code;
    }

    public PasswordResetDTO(){}

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
