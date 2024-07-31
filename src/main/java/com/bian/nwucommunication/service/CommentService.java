package com.bian.nwucommunication.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bian.nwucommunication.dao.Comment;
import com.bian.nwucommunication.dto.CommentDTO;
import com.bian.nwucommunication.dto.UserDTO;
import com.bian.nwucommunication.dto.req.CommentReqDTO;

import java.util.List;

public interface CommentService extends IService<Comment> {

    List<CommentDTO> queryComment(long id);

    CommentDTO putComment(CommentReqDTO commentReqDTO);

    CommentDTO putCommentBaseAI(CommentReqDTO commentReqDTO, UserDTO userDTO);
}
