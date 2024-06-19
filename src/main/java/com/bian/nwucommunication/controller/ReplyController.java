package com.bian.nwucommunication.controller;

import com.bian.nwucommunication.common.result.Result;
import com.bian.nwucommunication.common.result.Results;
import com.bian.nwucommunication.dto.req.ReplyReqDTO;
import com.bian.nwucommunication.service.ReplyService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fs")
public class ReplyController {

    @Resource
    private ReplyService replyService;

    @PostMapping("/reply")
    private Result<?> putReply(@RequestBody ReplyReqDTO replyReqDTO){
        return Results.success(replyService.putReply(replyReqDTO),"回复成功");
    }
}
