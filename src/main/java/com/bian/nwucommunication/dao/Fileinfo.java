package com.bian.nwucommunication.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class Fileinfo implements Serializable {

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

    private Integer isPass;

    @TableField("downNum")
    private Integer downNum;

    private String rankFirst;

    private String rankThird;

    private String rankForth;

    private String rankFifth;

    private Integer schoolIdId;

    @TableField("userId_id")
    private Integer useridId;

    private String schoolName;


}
