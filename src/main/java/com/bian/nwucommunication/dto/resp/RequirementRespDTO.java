package com.bian.nwucommunication.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class RequirementRespDTO {

    private LocalDate date;

    private String keyWord;

    private Integer userId;

    private String nickName;

    private String email;
}
