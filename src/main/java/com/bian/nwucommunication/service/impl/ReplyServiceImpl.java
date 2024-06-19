package com.bian.nwucommunication.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bian.nwucommunication.dao.Reply;
import com.bian.nwucommunication.dto.ReplyDTO;
import com.bian.nwucommunication.mapper.ReplyMapper;
import com.bian.nwucommunication.service.ReplyService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyServiceImpl extends ServiceImpl<ReplyMapper, Reply> implements ReplyService {

    @Resource
    private ReplyMapper replyMapper;

    @Override
    public List<ReplyDTO> queryReplyByFileId(long id) {
        return replyMapper.queryReplyById(id);
    }
}
