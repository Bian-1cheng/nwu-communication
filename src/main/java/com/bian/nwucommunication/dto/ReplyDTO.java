package com.bian.nwucommunication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ReplyDTO {

    private Integer id;

    private LocalDate date;

    private String replyText;

    private String formUserNickName;

    private String formUserHeadImg;

    private String toUserNickName;

    private String toUserHeadImg;
}
