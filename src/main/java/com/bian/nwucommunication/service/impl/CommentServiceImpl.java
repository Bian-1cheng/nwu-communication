package com.bian.nwucommunication.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bian.nwucommunication.dao.Comment;
import com.bian.nwucommunication.dao.FileInfo;
import com.bian.nwucommunication.dao.Reply;
import com.bian.nwucommunication.dao.UserInfo;
import com.bian.nwucommunication.dto.CommentDTO;
import com.bian.nwucommunication.dto.ReplyDTO;
import com.bian.nwucommunication.mapper.CommentMapper;
import com.bian.nwucommunication.mapper.UserMapper;
import com.bian.nwucommunication.service.CommentService;
import com.bian.nwucommunication.service.FileInfoService;
import com.bian.nwucommunication.service.ReplyService;
import com.bian.nwucommunication.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private UserService userService;

    @Resource
    private ReplyService replyService;
    
    @Override
    public List<CommentDTO> queryComment(long id) {
        List<Comment> commentList = commentMapper.selectList(new QueryWrapper<Comment>().eq("fileId_id", id));
        List<CommentDTO> commentDTO = new ArrayList<>();
        if(CollUtil.isEmpty(commentList))
            return commentDTO;
        for(Comment item: commentList){
            CommentDTO itemCommentDTO = new CommentDTO();

            UserInfo itemUser = userService.getById(item.getUserId());
            List<Reply> itemReply = replyService.getBaseMapper().selectList(new QueryWrapper<Reply>().eq("fileid_id", item.getId()));
            List<ReplyDTO> itemReplyDTO = BeanUtil.copyToList(itemReply, ReplyDTO.class);

            BeanUtil.copyProperties(item,itemCommentDTO,true);
            itemCommentDTO.setHeadImg(itemUser.getHeadImg());
            itemCommentDTO.setNickName(itemUser.getNickName());
            itemCommentDTO.setReply(itemReplyDTO);

            commentDTO.add(itemCommentDTO);
        }
        return commentDTO;
    }
}
