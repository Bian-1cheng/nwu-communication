package com.bian.nwucommunication.service.impl;


import com.bian.nwucommunication.common.constant.NsfwConstants;
import com.bian.nwucommunication.common.constant.OssConstants;
import com.bian.nwucommunication.dto.NsfwDTO;
import com.bian.nwucommunication.service.FileCheckService;
import com.bian.nwucommunication.util.FileUtil;
import com.bian.nwucommunication.util.VideoUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class FileCheckServiceImpl implements FileCheckService {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private FileUtil fileUtil;


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

    @Override
    public Double checkImgNsfw(String fileUrl,int retryTimes) {
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
                log.error("文件路径不存在{}",fileUrl);
            return nsfwDTO.getScore();
        } catch (RestClientException e) {
            log.error("调用鉴黄服务失败{}",e.getMessage());
            checkImgNsfw(fileUrl,retryTimes-1);
        }
        return Double.MIN_VALUE;
    }

    @Override
    public Boolean checkVideoNsfw(File file, int retryTimes) {
        List<String> videoPics = VideoUtil.randomGrabberFFmpegImage(file, 5);
        if (videoPics == null || videoPics.isEmpty()) {
            log.error("未能从视频中提取图片");
            return false;
        }
        Double score = 0.0;
        String OssFilePath;
        for(String item : videoPics){
            File itemFile = new File(item);
            try {
                OssFilePath = fileUtil.upload(new FileInputStream(itemFile),itemFile.getName(), OssConstants.FILE_TEMP_ADDRESS);
            } catch (IOException e) {
                log.error("上传Oss失败",e);
                return false;
            }
            Double itemScore = checkImg(OssFilePath,NsfwConstants.INTERFACE_RETRY_TIMES);
            score += itemScore;
        }
        if (score / videoPics.size() > NsfwConstants.YELLOW_IMG_SCORE)
            return false;
        return true;
    }
}
