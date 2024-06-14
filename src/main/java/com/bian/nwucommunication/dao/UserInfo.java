package com.bian.nwucommunication.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author bian
 * @since 2024-06-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@TableName("fsahre_userinfo")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String email;

    private Integer schoolId;

    private String idCard;

    private Integer score;

    private Integer identification;

    @TableField(value = "rank_forth")
    private String username;

    @TableField(value = "rank_fifth")
    private String password;

    private Integer userId;

    private String nickName;

    private String phone;

    @TableField(value = "headimg")
    private String headImg;


}
