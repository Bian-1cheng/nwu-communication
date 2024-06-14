package com.bian.nwucommunication.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

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
@TableName("fsahre_filecomment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("sendUser")
    private Integer sendUser;

    @TableField("Comment_text")
    private String commentText;

    private LocalDate textDate;

    private Boolean isShd;

    @TableField("greatNum")
    private Integer greatNum;

    private String rankThird;

    private String rankForth;

    private String rankFifth;

    @TableField("fileid_id")
    private Integer fileId;

    @TableField("userId_id")
    private Integer userId;

    private String rankFirst;

    private String rankSecond;

}
