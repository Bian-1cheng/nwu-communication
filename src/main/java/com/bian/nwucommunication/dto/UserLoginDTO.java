package com.bian.nwucommunication.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class UserLoginDTO implements Serializable {

    private String code;

    private String email;
}
