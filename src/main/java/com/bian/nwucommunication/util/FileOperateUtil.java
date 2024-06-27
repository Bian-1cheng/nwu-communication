package com.bian.nwucommunication.util;

import cn.hutool.core.lang.UUID;
import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import com.bian.nwucommunication.config.OSSConfig;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;


import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
public class FileOperateUtil {

    @Resource
    private OSSConfig ossConfig;

    public String upload(InputStream inputStream,String originalFilename, String folderPrefix) throws IOException {

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
                return "https://"+bucketName+"."+endPoint+"/"+uploadFileName;
            }
        } finally {
            //OSS关闭服务，不然会造成OOM
            inputStream.close();
            ossClient.shutdown();
        }
        return null;
    }

//    private void fileToZip(InputStream inputStream){
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        try (ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream)) {
//
//            ZipEntry zipEntry = new ZipEntry("compressed.dat"); // 可以指定压缩后的文件名
//            zipOut.putNextEntry(zipEntry);
//
//            byte[] buffer = new byte[1024];
//            int len;
//            while ((len = inputStream.read(buffer)) > 0) {
//                zipOut.write(buffer, 0, len);
//            }
//
//            zipOut.closeEntry();
//            // 完成所有条目的写入后，流会自动刷新和关闭
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//    }

    /**
     * 获取随机字符串
     * @return
     */
    private String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }
}
