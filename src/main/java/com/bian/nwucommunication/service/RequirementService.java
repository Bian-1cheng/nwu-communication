package com.bian.nwucommunication.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bian.nwucommunication.dao.Requirement;
import com.bian.nwucommunication.dto.req.RequirementReqDTO;
import com.bian.nwucommunication.dto.resp.RequirementRespDTO;

import java.util.List;

public interface RequirementService extends IService<Requirement> {

    void insertRequirement(RequirementReqDTO requirementDTO);

    List<RequirementRespDTO> searchRequirementByKeyWord(String keyWord);

}
