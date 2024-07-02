package com.bian.nwucommunication.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bian.nwucommunication.common.constant.UserConstants;
import com.bian.nwucommunication.common.execption.ClientException;
import com.bian.nwucommunication.dao.Comment;
import com.bian.nwucommunication.dao.UserInfo;
import com.bian.nwucommunication.dto.CommentDTO;
import com.bian.nwucommunication.dto.ReplyDTO;
import com.bian.nwucommunication.dto.UserDTO;
import com.bian.nwucommunication.dto.req.CommentReqDTO;
import com.bian.nwucommunication.mapper.CommentMapper;
import com.bian.nwucommunication.service.CommentService;
import com.bian.nwucommunication.service.ReplyService;
import com.bian.nwucommunication.service.UserService;
import com.bian.nwucommunication.util.UserHolder;
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

    @Resource
    private ReplyService replyService;
    
    @Override
    public List<CommentDTO> queryComment(long id) {
        LambdaQueryWrapper<Comment> queryWrapper = Wrappers.lambdaQuery(Comment.class)
                .eq(Comment::getFileId, id);
        List<Comment> commentList = commentMapper.selectList(queryWrapper);
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
        comment.setGreatNum(UserConstants.FILE_INIT_GREAT_NUM);
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
