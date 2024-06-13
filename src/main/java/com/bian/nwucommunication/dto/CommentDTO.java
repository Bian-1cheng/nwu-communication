package com.bian.nwucommunication.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CommentDTO {

    private Integer id;

    private String commentText;

    private LocalDate textDate;

    private Integer greatNum;

    private String nickName;

    private String headImg;
}
