package com.bian.nwucommunication.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bian.nwucommunication.dao.Requirement;
import com.bian.nwucommunication.dto.req.RequirementReqDTO;
import com.bian.nwucommunication.dto.resp.RequirementRespDTO;
import com.bian.nwucommunication.mapper.RequirementMapper;
import com.bian.nwucommunication.service.RequirementService;
import com.bian.nwucommunication.common.constant.UserConstants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequirementServiceImpl extends ServiceImpl<RequirementMapper, Requirement> implements RequirementService {


    @Resource
    private RequirementMapper requirementMapper;

    @Override
    public void insertRequirement(RequirementReqDTO requirementDTO) {
        Requirement requirement = BeanUtil.toBean(requirementDTO, Requirement.class);
        requirement.setIsNotice(UserConstants.NOT_NOTICE);
        requirement.setUserId(requirementDTO.getUser().getId());
        requirementMapper.insert(requirement);
    }

//    @Override
//    public  List<RequirementRespDTO>  searchRequirementByKeyWord(String keyWord) {
//        LambdaQueryWrapper<Requirement> queryWrapper = Wrappers.lambdaQuery(Requirement.class)
//                .eq(Requirement::getKeyWord, keyWord)
//                .eq(Requirement::getIsNotice, UserConstants.NOT_NOTICE)
//                .orderByAsc(Requirement::getDate);
//        List<Requirement> requirementList = requirementMapper.selectList(queryWrapper);
//        List<RequirementRespDTO> requirementRespDTO = BeanUtil.copyToList(requirementList, RequirementRespDTO.class);
//        return requirementRespDTO;
//    }

        @Override
    public  List<RequirementRespDTO>  searchRequirementByKeyWord(String keyWord) {
            return requirementMapper.searchRequirementByKeyWord(keyWord);
    }
}
