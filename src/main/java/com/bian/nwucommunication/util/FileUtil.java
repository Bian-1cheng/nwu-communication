package com.bian.nwucommunication.util;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import com.bian.nwucommunication.common.constant.OssConstants;
import com.bian.nwucommunication.common.constant.UserConstants;
import com.bian.nwucommunication.common.errorcode.BaseErrorCode;
import com.bian.nwucommunication.common.execption.ClientException;
import com.bian.nwucommunication.common.execption.ServiceException;
import com.bian.nwucommunication.config.OSSConfig;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.bytedeco.javacpp.Loader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.ServerException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class FileUtil {

    @Resource
    private OSSConfig ossConfig;

    public String upload(InputStream inputStream,
                         String originalFilename,
                         String folderPrefix) throws IOException {

        //获取相关配置
        String bucketName = ossConfig.getBucketName();
        String endPoint = ossConfig.getEndPoint();
        String accessKeyId = ossConfig.getAccessKeyId();
        String accessKeySecret = ossConfig.getAccessKeySecret();

        //创建OSS对象
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);

        //JDK8的日期格式
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter dft = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        //拼装OSS上存储的路径
        String folder = dft.format(time);
        String fileName = generateUUID();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        //在OSS上bucket下的文件名
        String uploadFileName = folderPrefix + folder +fileName + extension;

        try {
            PutObjectResult result = ossClient.putObject(bucketName, uploadFileName, inputStream);
            //拼装返回路径
            if (result != null) {
                return OssConstants.FILE_ADDRESS_PRE +bucketName+"."+endPoint+"/"+uploadFileName;
            }
        } catch (Exception e){
            throw new ServiceException(BaseErrorCode.SERVICE_ERROR);
        }finally {
            //OSS关闭服务，不然会造成OOM
            inputStream.close();
            ossClient.shutdown();
        }
        return null;
    }

    public static boolean isValidFileType(String fileName) {
        String fileExtension = FileNameUtil.extName(fileName).toLowerCase();
        return Arrays.stream(OssConstants.ALLOW_TYPE).anyMatch(type -> type.equals(fileExtension));
    }

    /**
     * 获取随机字符串
     * @return
     */
    private String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }

    public static String getFileType(MultipartFile file){
        return Objects.requireNonNull(FileNameUtil.extName(file.getOriginalFilename())).toLowerCase();
    }

    public static String modifyResolution(
            MultipartFile multipartFile, String outputDir, Integer width, Integer height) throws Exception {
        // 检查文件类型
        String fileName = multipartFile.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        if(!Arrays.asList(OssConstants.ALLOW_HEAD_IMG_TYPE).contains(ext)){
            throw new ClientException("不支持该格式: " + ext);
        }
        // 创建一个临时文件来保存上传的图像
        Path tempFile = Files.createTempFile("temp-image-", "." + ext);
        File tempFileFile = tempFile.toFile();

        // 将MultipartFile的内容写入临时文件
        try (InputStream inputStream = multipartFile.getInputStream()) {
            FileUtils.copyInputStreamToFile(inputStream, tempFileFile);
        }

        // 构建输出文件路径
        String resultPath = Paths.get(outputDir, IdUtil.simpleUUID() + "." + ext).toString();

        // 加载FFmpeg并执行命令
        String ffmpeg = Loader.load(org.bytedeco.ffmpeg.ffmpeg.class); // 注意：这里假设Loader和ffmpeg类是正确的
        ProcessBuilder builder = new ProcessBuilder(
                ffmpeg,
                "-i",
                tempFileFile.getAbsolutePath(),
                "-vf",
                MessageFormat.format("scale={0}:{1}", width, height),
                resultPath
        );

        Process process = builder.inheritIO().start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            log.error("降低分辨率失败");
            throw new IOException("FFmpeg 执行失败 process exited with code " + exitCode);
        }
         tempFileFile.delete();

        return resultPath;
    }

}
