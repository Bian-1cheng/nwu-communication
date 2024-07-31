package com.bian.nwucommunication.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bian.nwucommunication.common.constant.*;
import com.bian.nwucommunication.common.errorcode.BaseErrorCode;
import com.bian.nwucommunication.common.execption.ClientException;
import com.bian.nwucommunication.common.execption.ServiceException;
import com.bian.nwucommunication.dao.FileInfo;
import com.bian.nwucommunication.dto.FileInfoDTO;
import com.bian.nwucommunication.dto.FileUploadDTO;
import com.bian.nwucommunication.dto.UserDTO;
import com.bian.nwucommunication.dto.req.CommentReqDTO;
import com.bian.nwucommunication.dto.req.RequirementReqDTO;
import com.bian.nwucommunication.mapper.FileInfoMapper;
import com.bian.nwucommunication.service.*;
import com.bian.nwucommunication.util.*;
import com.bian.nwucommunication.util.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;




@Service
@Slf4j
@RequiredArgsConstructor
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper,FileInfo> implements FileInfoService {

    private final  FileInfoMapper fileInfoMapper;
    private final RedisTemplate redisTemplate;
    private final FileUtil fileUtil;
    private final RequirementService requirementService;
    private final FileCheckService fileCheckService;
    private final CommentService commentService;
    private final EmailService emailService;
    private final WenXinAi wenXinAi;
    private final UserService userService;

    public static final String AI_BOT = "AI机器人";

    @Override
    public List<FileInfoDTO> queryMyFile() {
        UserDTO user = UserHolder.getUser();
        List<FileInfo> fileList = fileInfoMapper.selectList(new QueryWrapper<FileInfo>().eq("user_id", user.getId()));
        if (CollUtil.isEmpty(fileList))
            throw new ServiceException(BaseErrorCode.FILE_LIST_EMPTY);
        return BeanUtil.copyToList(fileList, FileInfoDTO.class);
    }

    @Override
    public List<FileInfoDTO> querySchool() {
        UserDTO user = UserHolder.getUser();
        LambdaQueryWrapper<FileInfo> queryWrapper = Wrappers.lambdaQuery(FileInfo.class)
                .eq(FileInfo::getSchoolId, user.getSchoolId())
                .eq(FileInfo::getIsPass, UserConstants.FILE_HAVE_PASS)
                .orderByDesc(FileInfo::getPushDate);
        List<FileInfo> fileList = fileInfoMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(fileList))
            throw new ServiceException(BaseErrorCode.FILE_LIST_EMPTY);
        return BeanUtil.copyToList(fileList, FileInfoDTO.class);
    }

    @Override
    public List<FileInfoDTO> queryAllSchool() {
        Set keys = new RedisUtil().scanKeys(redisTemplate, RedisConstants.CACHE_All_School_KEY, RedisConstants.CACHE_SCANS_COUNT);
        if (!CollUtil.isEmpty(keys)){
            List<String> fileList = redisTemplate.opsForValue().multiGet(keys);
            if (CollUtil.isEmpty(fileList))
                throw new ServiceException(BaseErrorCode.FILE_LIST_EMPTY);
            List<FileInfoDTO> fileListDTO = new ArrayList<>();
            for(String item : fileList) {
                fileListDTO.add(JSONUtil.toBean(item, FileInfoDTO.class));
            }
            return fileListDTO;
        }
        return updateRedisSchoolFile();
    }

    @Override
    public void uploadFile(FileUploadDTO fileUploadDTO, MultipartFile file) {
        UserDTO user = UserHolder.getUser();

        String sensitiveWord = SensitiveWordUtil.checkSensitiveWord(fileUploadDTO);
        if(!StrUtil.isEmpty(sensitiveWord))
            throw new ServiceException(sensitiveWord,BaseErrorCode.SENSITIVE_WORD_EXIST);
        String fileName = file.getOriginalFilename();
        File tempFile = saveToLocal(file, fileName);

        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        double score = 0.0; // 为违规图片的得分，> 0.8 为违规图片
        if(Arrays.asList(OssConstants.ALLOW_IMG_TYPE).contains(ext)){
            String filePath = uploadFile(file, tempFile);
            score = fileCheckService.checkImgNsfw(filePath,NsfwConstants.INTERFACE_RETRY_TIMES);
        }
        if (score > NsfwConstants.YELLOW_IMG_SCORE)
            throw new ClientException("上传的图片为违规图片");

        if (Arrays.asList(OssConstants.ALLOW_TEXT_TYPE).contains(ext)){
            CompletableFuture.runAsync(() ->{
                TextExtractUtil textExtractUtil = new TextExtractUtil();
                String content = textExtractUtil.fileExtractText(tempFile.getPath());
                String filePath = uploadFile(file, tempFile);
                FileInfo fileInfo = convertToFileInfo(fileUploadDTO, user, filePath);
                int id = fileInfoMapper.insert(fileInfo);

                UserDTO AI = userService.getUserByName(AI_BOT);
                if(AI == null)
                    throw new ServiceException(BaseErrorCode.SERVICE_BOT_ERROR);
                String summary = wenXinAi.getSummary(content);
                if(!StrUtil.isNullOrUndefined(summary))
                    commentService.putCommentBaseAI(new CommentReqDTO(fileInfo.getId(),summary),AI);
            });
        }

        if(Arrays.asList(OssConstants.ALLOW_VIDEO_TYPE).contains(ext)){
            CompletableFuture.runAsync(() ->{
                Boolean IsPass = fileCheckService.checkVideoNsfw(tempFile,NsfwConstants.INTERFACE_RETRY_TIMES);
                if(!IsPass){
                    emailService.sendWarning(user.getEmail(), fileUploadDTO.getKeyWord());
                    log.error("内容含有非法内容，请修改后重新上传");
                }
                String filePath = uploadFile(file, tempFile);
                try {
                    FileInfo fileInfo = convertToFileInfo(fileUploadDTO, user, filePath);
                    fileInfoMapper.insert(fileInfo);
                } catch (Exception e) {
                    log.error("保存失败{}",e.getMessage());
                }
            });
        }

    }

    private String uploadFile(MultipartFile file, File tempFile) {
        String filePath;
        try {
            filePath = fileUtil.upload(new FileInputStream(tempFile), file.getOriginalFilename(), OssConstants.FILE_ADDRESS);
        } catch (IOException e) {
            throw new ServiceException(BaseErrorCode.REMOTE_ERROR);
        }
        return filePath;
    }

    @Override
    public List<FileInfoDTO> searchFileByKeyword(String search) {
        LambdaQueryWrapper<FileInfo> queryWrapper = Wrappers.lambdaQuery(FileInfo.class)
                .eq(FileInfo::getKeyWord, search)
                .eq(FileInfo::getIsPass, UserConstants.FILE_HAVE_PASS)
                .orderByDesc(FileInfo::getPushDate);
        List<FileInfo> fileInfo = fileInfoMapper.selectList(queryWrapper);
        if(CollUtil.isEmpty(fileInfo)){
            RequirementReqDTO requirementDTO = new RequirementReqDTO(search, LocalDateTimeUtil.parseDate(DateUtil.today()), UserHolder.getUser());
            requirementService.insertRequirement(requirementDTO);
            throw new ServiceException(BaseErrorCode.FILE_LIST_EMPTY);
        }
        return BeanUtil.copyToList(fileInfo, FileInfoDTO.class);
    }


    @Override
    public List<FileInfoDTO> updateRedisSchoolFile() {
        LambdaQueryWrapper<FileInfo> queryWrapper = Wrappers.lambdaQuery(FileInfo.class)
                .eq(FileInfo::getIsPass, UserConstants.FILE_HAVE_PASS)
                .orderByDesc(FileInfo::getPushDate);
        List<FileInfo> fileList = fileInfoMapper.selectList(queryWrapper);
        List<FileInfoDTO> fileInfoDTOS = BeanUtil.copyToList(fileList, FileInfoDTO.class);
        for (FileInfoDTO item : fileInfoDTOS) {
            redisTemplate.opsForValue().set(RedisConstants.CACHE_All_School_KEY+item.getId()+":",JSONUtil.toJsonStr(item));
        }
        return fileInfoDTOS;
    }

    @NotNull
    private static FileInfo convertToFileInfo(FileUploadDTO fileUploadDTO, UserDTO user, String filePath) {
        FileInfo fileInfo = BeanUtil.toBeanIgnoreCase(fileUploadDTO, FileInfo.class, true);
        fileInfo.setPushDate(LocalDateTimeUtil.parseDate(DateUtil.today()));
        fileInfo.setUserId(user.getId());
        fileInfo.setPath(filePath);
        fileInfo.setIsPass(UserConstants.FILE_WAIT_CHECK);
        fileInfo.setSchoolId(user.getSchoolId());
        fileInfo.setSchoolName(SchoolEnum.getNameByCode(user.getSchoolId()));
        return fileInfo;
    }

    @NotNull
    private static File saveToLocal(MultipartFile file, String fileName) {
        File tempFile = new File(FileTypeConstants.VIDEO_FILE_PATH, fileName);
        tempFile.getParentFile().mkdirs();
        try (FileOutputStream fos = new FileOutputStream(tempFile);
             InputStream is = file.getInputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            log.error("保存文件失败");
        }
        return tempFile;
    }
}
