package com.bian.nwucommunication.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bian.nwucommunication.common.constant.UserConstants;
import com.bian.nwucommunication.dao.Comment;
import com.bian.nwucommunication.dao.FileInfo;
import com.bian.nwucommunication.dao.Reply;
import com.bian.nwucommunication.dao.UserInfo;
import com.bian.nwucommunication.dto.CommentDTO;
import com.bian.nwucommunication.dto.ReplyDTO;
import com.bian.nwucommunication.dto.req.ReplyReqDTO;
import com.bian.nwucommunication.mapper.CommentMapper;
import com.bian.nwucommunication.mapper.ReplyMapper;
import com.bian.nwucommunication.service.CommentService;
import com.bian.nwucommunication.service.ReplyService;
import com.bian.nwucommunication.service.UserService;
import com.bian.nwucommunication.util.UserHolder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ReplyServiceImpl extends ServiceImpl<ReplyMapper, Reply> implements ReplyService {

    @Resource
    private ReplyMapper replyMapper;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private UserService userService;

    @Override
    public List<ReplyDTO> queryReplyByFileId(long id) {
        return replyMapper.queryReplyById(id);
    }

    /**
     *
     * @param replyReqDTO
     * @return 对于评论的回复，分为两种情况，回复comment，区别于reply
     */
    @Override
    public ReplyDTO putReply(ReplyReqDTO replyReqDTO) {
        Comment comment = commentMapper.selectById(replyReqDTO.getCommentId());

        Reply reply = new Reply();
        reply.setReplyText(replyReqDTO.getReplyText());
        reply.setDate(LocalDateTimeUtil.parseDate(DateUtil.today()));
        reply.setCommentId(comment.getId());
        reply.setFormUserId(UserHolder.getUser().getId());
        if (replyReqDTO.getToUserNickName() != null && replyReqDTO.getToUserHeadImg() != null){
            LambdaQueryWrapper<UserInfo> queryWrapper = Wrappers.lambdaQuery(UserInfo.class)
                    .eq(UserInfo::getNickName, replyReqDTO.getToUserNickName());
            UserInfo userInfo = userService.getBaseMapper().selectOne(queryWrapper);
            reply.setToUserId(userInfo.getId());
        }else{
            reply.setToUserId(comment.getUserId());
        }

        int id = 0;
        try {
            id = replyMapper.insert(reply);
        } catch (Exception e) {
            log.error("插入失败{}",e.getMessage());
            throw new RuntimeException(e);
        }
        ReplyDTO replyDTO = BeanUtil.toBean(reply, ReplyDTO.class);
        if (replyReqDTO.getToUserNickName() != null && replyReqDTO.getToUserHeadImg() != null){
            replyDTO.setToUserHeadImg(replyReqDTO.getToUserHeadImg());
            replyDTO.setToUserNickName(replyReqDTO.getToUserNickName());
        }else{
            UserInfo toUserInfo = userService.getById(comment.getUserId());
            replyDTO.setToUserHeadImg(toUserInfo.getHeadImg());
            replyDTO.setToUserNickName(toUserInfo.getNickName());
        }
        replyDTO.setId(id);
        replyDTO.setFormUserNickName(UserHolder.getUser().getNickName());
        replyDTO.setFormUserHeadImg(UserHolder.getUser().getHeadImg());

        return replyDTO;
    }
}
