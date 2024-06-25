package com.bian.nwucommunication.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bian.nwucommunication.dao.Notice;
import com.bian.nwucommunication.mapper.NoticeMapper;
import com.bian.nwucommunication.service.NoticeService;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {
}
