package com.bian.nwucommunication.dto.req;

import lombok.Data;

@Data
public class CheckFileReqDTO {

    private Integer fileId;

    private Integer newStatus;
}
