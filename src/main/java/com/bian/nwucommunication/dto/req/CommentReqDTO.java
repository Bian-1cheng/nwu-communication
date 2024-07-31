package com.bian.nwucommunication.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentReqDTO {

    private Integer fileId;

    private String commentText;
}
