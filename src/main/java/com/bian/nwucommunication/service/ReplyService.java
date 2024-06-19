package com.bian.nwucommunication.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bian.nwucommunication.dao.Reply;
import com.bian.nwucommunication.dto.ReplyDTO;
import com.bian.nwucommunication.dto.req.ReplyReqDTO;

import java.util.List;

public interface ReplyService extends IService<Reply> {

    List<ReplyDTO> queryReplyByFileId(long id);

    ReplyDTO putReply(ReplyReqDTO replyReqDTO);
}
