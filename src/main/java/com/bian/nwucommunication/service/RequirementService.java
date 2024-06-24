package com.bian.nwucommunication.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bian.nwucommunication.dao.Requirement;
import com.bian.nwucommunication.dto.req.RequirementDTO;

public interface RequirementService extends IService<Requirement> {

    void insertRequirement(RequirementDTO requirementDTO);
}
