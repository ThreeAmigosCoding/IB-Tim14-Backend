package com.example.demo.dto.user;

public class PasswordResetDTO {

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
