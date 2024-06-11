package com.bian.nwucommunication.mapper;



import com.bian.nwucommunication.dao.UserInfo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMapper extends BaseMapper<UserInfo> {
}
