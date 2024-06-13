package com.bian.nwucommunication.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bian.nwucommunication.dao.Comment;
import com.bian.nwucommunication.dto.CommentDTO;

import java.util.List;

public interface CommentService extends IService<Comment> {

    List<CommentDTO> queryComment(long id);
}
