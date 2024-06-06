package com.bian.nwucommunication.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class UserLoginDTO implements Serializable {

    private String username;

    private String password;

    private String code;

    private String email;
}
