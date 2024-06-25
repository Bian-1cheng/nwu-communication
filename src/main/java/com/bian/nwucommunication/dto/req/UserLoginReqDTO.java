package com.bian.nwucommunication.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginReqDTO implements Serializable {

    private String code;

    private String email;
}
