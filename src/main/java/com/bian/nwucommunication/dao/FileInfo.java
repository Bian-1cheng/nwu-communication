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
@TableName("fsahre_fileinfo")
public class FileInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String path;

    private String title;

    @TableField("greatNum")
    private Integer greatNum;

    private Boolean isScore;

    private LocalDate pushDate;

    private String keyWord;

    // 0 --待审核  1--审核通过 3-- 审核不通过
    private Integer isPass;

    @TableField("downNum")
    private Integer downNum;

    @TableField("rank_first")
    private String intro;

    private String rankThird;

    private String rankForth;

    private String rankFifth;

    @TableField("school_id_id")
    private Integer schoolId;

    @TableField("userId_id")
    private Integer userId;

    private String schoolName;


}
