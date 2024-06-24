package com.bian.nwucommunication.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bian.nwucommunication.dao.Requirement;
import com.bian.nwucommunication.dto.req.RequirementDTO;
import com.bian.nwucommunication.mapper.RequirementMapper;
import com.bian.nwucommunication.service.RequirementService;
import com.bian.nwucommunication.service.UserService;
import com.bian.nwucommunication.util.constant.UserConstants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class RequirementServiceImpl extends ServiceImpl<RequirementMapper, Requirement> implements RequirementService {


    @Resource
    private RequirementMapper requirementMapper;

    @Override
    public void insertRequirement(RequirementDTO requirementDTO) {
        Requirement requirement = BeanUtil.toBean(requirementDTO, Requirement.class);
        requirement.setIsNotice(UserConstants.NOT_NOTICE);
        requirement.setUserId(requirementDTO.getUser().getId());
        requirementMapper.insert(requirement);
    }
}
