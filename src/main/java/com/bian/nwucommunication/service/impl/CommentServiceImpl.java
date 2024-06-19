package com.bian.nwucommunication.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bian.nwucommunication.common.execption.ClientException;
import com.bian.nwucommunication.dao.Comment;
import com.bian.nwucommunication.dao.FileInfo;
import com.bian.nwucommunication.dao.Reply;
import com.bian.nwucommunication.dao.UserInfo;
import com.bian.nwucommunication.dto.CommentDTO;
import com.bian.nwucommunication.dto.ReplyDTO;
import com.bian.nwucommunication.dto.UserDTO;
import com.bian.nwucommunication.dto.req.CommentReqDTO;
import com.bian.nwucommunication.mapper.CommentMapper;
import com.bian.nwucommunication.mapper.UserMapper;
import com.bian.nwucommunication.service.CommentService;
import com.bian.nwucommunication.service.FileInfoService;
import com.bian.nwucommunication.service.ReplyService;
import com.bian.nwucommunication.service.UserService;
import com.bian.nwucommunication.util.UserHolder;
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
            List<ReplyDTO> replyDTO = replyService.queryReplyByFileId(item.getId());

            BeanUtil.copyProperties(item,itemCommentDTO,true);
            itemCommentDTO.setHeadImg(itemUser.getHeadImg());
            itemCommentDTO.setNickName(itemUser.getNickName());
            itemCommentDTO.setReply(replyDTO);

            commentDTO.add(itemCommentDTO);
        }
        return commentDTO;
    }

    @Override
    public CommentDTO putComment(CommentReqDTO commentReqDTO) {
        UserDTO user = UserHolder.getUser();
        Comment comment = BeanUtil.toBean(commentReqDTO, Comment.class);
        comment.setTextDate(LocalDateTimeUtil.parseDate(DateUtil.today()));
        comment.setUserId(user.getId());
        comment.setIsShd(false);
        comment.setGreatNum(0);
        try {
            commentMapper.insert(comment);
        } catch (Exception e) {
            throw new ClientException("评论失败");
        }
        CommentDTO commentDTO = BeanUtil.toBean(comment, CommentDTO.class);
        commentDTO.setNickName(user.getNickName());
        commentDTO.setHeadImg(user.getHeadImg());
        return commentDTO;
    }
}
