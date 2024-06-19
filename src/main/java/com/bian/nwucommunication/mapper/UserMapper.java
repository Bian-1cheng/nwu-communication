package com.bian.nwucommunication.mapper;



import com.bian.nwucommunication.dao.UserInfo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bian.nwucommunication.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface UserMapper extends BaseMapper<UserInfo> {

    UserDTO queryUserId(@Param("id") long Long);
}
