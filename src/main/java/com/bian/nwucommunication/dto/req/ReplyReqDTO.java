package com.bian.nwucommunication.dto.req;

import lombok.Data;

@Data
public class ReplyReqDTO {

    private Integer commentId;

    private String replyText;

    private String toUserNickName;

    private String toUserHeadImg;

}
