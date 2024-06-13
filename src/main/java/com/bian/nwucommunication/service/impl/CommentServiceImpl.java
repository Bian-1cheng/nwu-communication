package com.bian.nwucommunication.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bian.nwucommunication.dao.Comment;
import com.bian.nwucommunication.dao.FileInfo;
import com.bian.nwucommunication.dao.UserInfo;
import com.bian.nwucommunication.dto.CommentDTO;
import com.bian.nwucommunication.mapper.CommentMapper;
import com.bian.nwucommunication.mapper.UserMapper;
import com.bian.nwucommunication.service.CommentService;
import com.bian.nwucommunication.service.FileInfoService;
import com.bian.nwucommunication.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private UserService userService;
    
    @Override
    public List<CommentDTO> queryComment(long id) {
        List<Comment> commentLsit = commentMapper.selectList(new QueryWrapper<Comment>().eq("fileId_id", id));
        List<CommentDTO> commentDTO = new ArrayList<>();
        for(Comment item: commentLsit){
            CommentDTO itemCommentDTO = new CommentDTO();
            UserInfo itemUser = userService.getById(item.getUseridId());
            itemCommentDTO.setId(item.getId());
            itemCommentDTO.setCommentText(item.getCommentText());
            itemCommentDTO.setGreatNum(item.getGreatNum());
            itemCommentDTO.setTextDate(item.getTextDate());
            itemCommentDTO.setHeadImg(itemUser.getHeadImg());
            itemCommentDTO.setNickName(itemUser.getNickName());

            commentDTO.add(itemCommentDTO);
        }
        return commentDTO;
    }
}
