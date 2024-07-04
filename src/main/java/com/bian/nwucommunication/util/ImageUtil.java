package com.bian.nwucommunication.util;

import com.bian.nwucommunication.dto.ImageResolution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class ImageUtil {

    public static ImageResolution getImageResolution(MultipartFile file) {
        try {
            // 使用ImageIO读取图像文件
            InputStream inputStream = file.getInputStream();
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
                log.error("无法解析图像文件");
                return null;
            }
            // 获取图像的宽度和高度
            int width = image.getWidth();
            int height = image.getHeight();
            ImageResolution imageResolution = new ImageResolution();
            imageResolution.setWidth(width);
            imageResolution.setHeight(height);
            return imageResolution;
        } catch (IOException e) {
            log.error("读取图像失败{}", e.getMessage());
        }
        return null;
    }

    /**
     * 检查该图片是否需要降低分辨率
     * @param file
     * @return
     */
    public static Boolean checkImageResolution(MultipartFile file){
        ImageResolution imageResolution = getImageResolution(file);
        if(imageResolution.getHeight()>100 || imageResolution.getWidth()>100)
            return false;
        return true;
    }
}