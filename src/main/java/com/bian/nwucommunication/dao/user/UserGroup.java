package com.bian.nwucommunication.dao.user;




import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
public class UserGroup implements Serializable{
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer user_id;
    private Integer group_id;
    //最后刷新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime refresh_time;
    private Integer is_mute;
    private Integer is_deleted;


}
