package com.bian.nwucommunication.dao.user;

/**
 * Created by
 *
 * @Author:jzs
 * @Date: 2021/2/20
 * @Time: 20:54
 */
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
@Data
public class Userchat implements Serializable{
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer user_id;
    private String user_identity_no;
    private Integer send_id;
    private String send_identity_no;
    private String send_username;
    private byte[] send_head;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmt_create;
    private Integer is_deleted;
    //是否撤回
    private Integer is_withdrawn;
    private Integer is_read;
    private Integer room_id;


}
