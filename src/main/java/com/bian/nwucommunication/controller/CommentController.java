package com.bian.nwucommunication.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.bian.nwucommunication.common.result.Result;
import com.bian.nwucommunication.common.result.Results;
import com.bian.nwucommunication.dao.Comment;
import com.bian.nwucommunication.dto.CommentDTO;
import com.bian.nwucommunication.service.CommentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fs")
public class CommentController {

    @Resource
    private CommentService commentService;

    @GetMapping("/filecomment/{id}")
    private Result<?> queryComments(@PathVariable("id") long id){
        List<CommentDTO> comments = commentService.queryComment(id);
        return Results.success(comments);
    }

}
