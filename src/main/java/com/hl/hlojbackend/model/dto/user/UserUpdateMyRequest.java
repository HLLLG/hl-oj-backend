package com.hl.hlojbackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserUpdateMyRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userName;

    private String userAvatar;

    private String userProfile;
}
