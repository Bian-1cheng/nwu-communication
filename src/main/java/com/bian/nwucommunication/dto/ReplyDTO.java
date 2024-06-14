package com.bian.nwucommunication.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReplyDTO {

    private Integer id;

    private LocalDate date;

    private String replyText;

    private Integer formUserId;

    private Integer toUserId;
}
