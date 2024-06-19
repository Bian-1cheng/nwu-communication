package com.bian.nwucommunication.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReplyDTO {

    private Integer id;

    private LocalDate date;

    private String replyText;

    private String formUserNickName;

    private String formUserHeadImg;

    private String toUserNickName;

    private String toUserHeadImg;
}
