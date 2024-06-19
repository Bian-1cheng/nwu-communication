package com.bian.nwucommunication.controller;

import com.bian.nwucommunication.common.result.Result;
import com.bian.nwucommunication.common.result.Results;
import com.bian.nwucommunication.dto.req.ReplyReqDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fs")
public class ReplyController {

    @PostMapping("/reply")
    private Result<?> putReply(@RequestBody ReplyReqDTO replyReqDTO){
        return Results.success("回复成功");
    }
}
