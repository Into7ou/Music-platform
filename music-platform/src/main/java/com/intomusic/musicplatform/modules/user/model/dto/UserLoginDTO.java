package com.intomusic.musicplatform.modules.user.model.dto;
import lombok.Data;

@Data
public class UserLoginDTO {
    private String username;
    private String password;
}