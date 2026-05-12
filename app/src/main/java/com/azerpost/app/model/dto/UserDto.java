package com.azerpost.app.model.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private boolean status;
    private String createdAt;
}
