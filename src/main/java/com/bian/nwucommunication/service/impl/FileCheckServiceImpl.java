package com.bian.nwucommunication.service.impl;


import com.bian.nwucommunication.common.constant.NsfwConstants;
import com.bian.nwucommunication.common.constant.OssConstants;
import com.bian.nwucommunication.common.execption.ClientException;
import com.bian.nwucommunication.dto.NsfwDTO;
import com.bian.nwucommunication.service.FileCheckService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
@Slf4j
public class FileCheckServiceImpl implements FileCheckService {

    @Resource
    private RestTemplate restTemplate;


    @Override
    public Boolean checkNFSW(String fileUrl) {
        // 获取文件的后缀（png、jpg、mp4）
        String ext = fileUrl.substring(fileUrl.lastIndexOf(".") + 1);
        Double score = null;
        if(Arrays.asList(OssConstants.ALLOW_HEAD_IMG_TYPE).contains(ext)){
            score = checkImg(fileUrl, NsfwConstants.INTERFACE_RETRY_TIMES);
        }
        if(score > NsfwConstants.YELLOW_IMG_SCORE)
            return false;
        return true;
    }

    private Double checkImg(String fileUrl,int retryTimes) {
        if(retryTimes <= 0) {
            log.error("图片鉴黄服务调用失败,文件路径：{}",fileUrl);
            return Double.MIN_VALUE;
        }
        try {
            ResponseEntity<NsfwDTO> responseEntity = restTemplate.getForEntity(
                    NsfwConstants.NSFW_INTERFACE_ADDRESS+fileUrl,
                    NsfwDTO.class);
            NsfwDTO nsfwDTO = responseEntity.getBody();
            if(nsfwDTO.getScore() == null)
                log.error("文件路径错误{}",fileUrl);
            return nsfwDTO.getScore();
        } catch (RestClientException e) {
            checkImg(fileUrl,retryTimes-1);
        }
        return Double.MIN_VALUE;
    }
}
