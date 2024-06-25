package com.bian.nwucommunication.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bian.nwucommunication.dao.Requirement;
import com.bian.nwucommunication.dto.ReplyDTO;
import com.bian.nwucommunication.dto.resp.RequirementRespDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RequirementMapper extends BaseMapper<Requirement> {

    List<RequirementRespDTO> searchRequirementByKeyWord(@Param("keyWord") String keyWord);
}
