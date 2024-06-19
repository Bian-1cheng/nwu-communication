package com.bian.nwucommunication.dto.req;

import lombok.Data;

@Data
public class CommentReqDTO {

    private Integer fileId;

    private String commentText;
}
