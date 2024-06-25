package com.bian.nwucommunication.dto.req;

import com.bian.nwucommunication.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class RequirementReqDTO {

    private String keyWord;

    private LocalDate date;

    private UserDTO user;
}
