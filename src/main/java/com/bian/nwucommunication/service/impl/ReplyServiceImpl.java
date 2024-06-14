package com.bian.nwucommunication.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bian.nwucommunication.dao.Reply;
import com.bian.nwucommunication.mapper.ReplyMapper;
import com.bian.nwucommunication.service.ReplyService;
import org.springframework.stereotype.Service;

@Service
public class ReplyServiceImpl extends ServiceImpl<ReplyMapper, Reply> implements ReplyService {
}
