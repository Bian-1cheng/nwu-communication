package com.bian.nwucommunication.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bian.nwucommunication.dao.Reply;
import com.bian.nwucommunication.dto.ReplyDTO;
import com.bian.nwucommunication.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReplyMapper extends BaseMapper<Reply> {

    List<ReplyDTO> queryReplyById(@Param("id") long Long);
}
