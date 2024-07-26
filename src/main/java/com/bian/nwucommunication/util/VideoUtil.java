package com.bian.nwucommunication.util;

import cn.hutool.core.lang.UUID;
import com.bian.nwucommunication.common.constant.FileTypeConstants;
import com.bian.nwucommunication.dto.VideoInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
public class VideoUtil {

    public static VideoInfoDTO getVideoInfo(File file) {
        VideoInfoDTO videoInfo = new VideoInfoDTO();
        FFmpegFrameGrabber grabber = null;
        try {
            grabber = new FFmpegFrameGrabber(file);
            // 启动 FFmpeg
            grabber.start();

            // 读取视频帧数
            videoInfo.setLengthInFrames(grabber.getLengthInVideoFrames());

            // 读取视频帧率
            videoInfo.setFrameRate(grabber.getVideoFrameRate());

            // 读取视频秒数
            videoInfo.setDuration(grabber.getLengthInTime() / 1000000.00);

            // 读取视频宽度
            videoInfo.setWidth(grabber.getImageWidth());

            // 读取视频高度
            videoInfo.setHeight(grabber.getImageHeight());


            videoInfo.setAudioChannel(grabber.getAudioChannels());

            videoInfo.setVideoCode(grabber.getVideoCodecName());

            videoInfo.setAudioCode(grabber.getAudioCodecName());
            // String md5 = MD5Util.getMD5ByInputStream(new FileInputStream(file));

            videoInfo.setSampleRate(grabber.getSampleRate());
            return videoInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (grabber != null) {
                    // 此处代码非常重要，如果没有，可能造成 FFmpeg 无法关闭
                    grabber.stop();
                    grabber.release();
                }
            } catch (FFmpegFrameGrabber.Exception e) {
                log.error("getVideoInfo grabber.release failed 获取文件信息失败：{}", e.getMessage());
            }
        }
    }

    /**
     * 随机获取视频截图
     * @param videoFilePath 视频文件路径
     * @param count 输出截图数量
     * @return 截图列表
     * */
    public static List<String> randomGrabberFFmpegImage(String videoFilePath, int count, InputStream fileInputStream) {
        FFmpegFrameGrabber grabber = null;

        String path = FileTypeConstants.VIDEO_IMG_FILE_PATH;

        try {
            List<String> images = new ArrayList<>(count);
            File tofile = new File("file.getOriginalFilename()");
            System.out.println(fileInputStream);
            FileUtils.copyInputStreamToFile(fileInputStream,tofile);
            String absolutePath = tofile.getAbsoluteFile().getAbsolutePath();
            File file = new File(videoFilePath);
            grabber = new FFmpegFrameGrabber(fileInputStream);
            grabber.setOption("stimeout", "5000000");
            grabber.start();
            // 获取视频总帧数
            int lengthInVideoFrames = grabber.getLengthInVideoFrames();
            // 获取视频时长， / 1000000 将单位转换为秒
            long delayedTime = grabber.getLengthInTime() / 1000000;
            Java2DFrameConverter converter = new Java2DFrameConverter();
            Random random = new Random();
            for (int i = 0; i < count; i++) {
                // 跳转到响应时间
                grabber.setTimestamp((random.nextInt((int)delayedTime - 1) + 1) * 1000000L);
                Frame f = grabber.grabImage();
                BufferedImage bi = converter.getBufferedImage(f);
                String imageName = UUID.randomUUID().toString()+".jpg";
                File out = Paths.get(path, imageName).toFile();
                ImageIO.write(bi, "jpg", out);
//                FileTableEntity fileTable = FileUtils.createFileTableEntity(imageName, SUFFIX, path, f.image.length, "系统生成截图", userId, FileTypeEnum.VIDEO_PHOTO.getCode());
                images.add(path+imageName);
            }
            return images;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (grabber != null) {
                    grabber.stop();
                    grabber.release();
                }
            } catch (FFmpegFrameGrabber.Exception e) {
                log.error("getVideoInfo grabber.release failed 获取文件信息失败：{}", e.getMessage());
            }
        }
    }



    /**
     * 随机获取视频截图
     * @param videoFilePath 视频文件路径
     * @param count 输出截图数量
     * @return 截图列表
     * */
    public static List<String> randomGrabberFFmpegImage(String videoFilePath, int count) {
        FFmpegFrameGrabber grabber = null;

        String path = FileTypeConstants.VIDEO_IMG_FILE_PATH;

        try {
            List<String> images = new ArrayList<>(count);
            File file = new File(videoFilePath);
            grabber = new FFmpegFrameGrabber(file);
            grabber.setOption("stimeout", "5000000");
            grabber.start();
            // 获取视频总帧数
            int lengthInVideoFrames = grabber.getLengthInVideoFrames();
            // 获取视频时长， / 1000000 将单位转换为秒
            long delayedTime = grabber.getLengthInTime() / 1000000;
            Java2DFrameConverter converter = new Java2DFrameConverter();
            Random random = new Random();
            for (int i = 0; i < count; i++) {
                // 跳转到响应时间
                grabber.setTimestamp((random.nextInt((int)delayedTime - 1) + 1) * 1000000L);
                Frame f = grabber.grabImage();
                BufferedImage bi = converter.getBufferedImage(f);
                String imageName = UUID.randomUUID().toString()+".jpg";
                File out = Paths.get(path, imageName).toFile();
                ImageIO.write(bi, "jpg", out);
//                FileTableEntity fileTable = FileUtils.createFileTableEntity(imageName, SUFFIX, path, f.image.length, "系统生成截图", userId, FileTypeEnum.VIDEO_PHOTO.getCode());
                images.add(path+imageName);
            }
            return images;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (grabber != null) {
                    grabber.stop();
                    grabber.release();
                }
            } catch (FFmpegFrameGrabber.Exception e) {
                log.error("getVideoInfo grabber.release failed 获取文件信息失败：{}", e.getMessage());
            }
        }
    }



    public static List<String> randomGrabberFFmpegImage(MultipartFile file, int count) {
        FFmpegFrameGrabber grabber = null;

        String path = FileTypeConstants.VIDEO_IMG_FILE_PATH;

        try {
            List<String> images = new ArrayList<>(count);
//            File tofile = new File(file.getOriginalFilename());
//            FileUtils.copyInputStreamToFile(file.getInputStream(),tofile);
//            String absolutePath = tofile.getAbsoluteFile().getAbsolutePath();
            String targetDir = FileTypeConstants.VIDEO_FILE_PATH; // 确保这个目录存在且可写
            File tofile = new File(targetDir, file.getOriginalFilename());
            System.out.println(tofile);
//            System.out.println(file.getInputStream());
            System.out.println(file.getOriginalFilename());
            FileUtils.copyInputStreamToFile(file.getInputStream(), tofile);
            String absolutePath = tofile.getAbsoluteFile().getAbsolutePath();
            grabber = new FFmpegFrameGrabber(absolutePath);
            grabber.start();
            // 获取视频总帧数
            int lengthInVideoFrames = grabber.getLengthInVideoFrames();
            // 获取视频时长， / 1000000 将单位转换为秒
            long delayedTime = grabber.getLengthInTime() / 1000000;
            Java2DFrameConverter converter = new Java2DFrameConverter();
            Random random = new Random();
            for (int i = 0; i < count; i++) {
                // 跳转到响应时间
                grabber.setTimestamp((random.nextInt((int)delayedTime - 1) + 1) * 1000000L);
                Frame f = grabber.grabImage();
                BufferedImage bi = converter.getBufferedImage(f);
                String imageName = UUID.randomUUID().toString()+".jpg";
                File out = Paths.get(path, imageName).toFile();
                ImageIO.write(bi, "jpg", out);
//                FileTableEntity fileTable = FileUtils.createFileTableEntity(imageName, SUFFIX, path, f.image.length, "系统生成截图", userId, FileTypeEnum.VIDEO_PHOTO.getCode());
                images.add(path+imageName);
            }
            return images;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        } finally {
            try {
                if (grabber != null) {
                    grabber.stop();
                    grabber.release();
                }
            } catch (FFmpegFrameGrabber.Exception e) {
                log.error("getVideoInfo grabber.release failed 获取文件信息失败：{}", e.getMessage());
            }
        }
    }


    public static List<String> randomGrabberFFmpegImage(File file, int count) {
        FFmpegFrameGrabber grabber = null;

        String path = FileTypeConstants.VIDEO_IMG_FILE_PATH;

        try {
            List<String> images = new ArrayList<>(count);
//            File tofile = new File(file.getOriginalFilename());
//            FileUtils.copyInputStreamToFile(file.getInputStream(),tofile);
//            String absolutePath = tofile.getAbsoluteFile().getAbsolutePath();
            String targetDir = FileTypeConstants.VIDEO_FILE_PATH; // 确保这个目录存在且可写
            grabber = new FFmpegFrameGrabber(file.getPath());
            grabber.start();
            // 获取视频总帧数
            int lengthInVideoFrames = grabber.getLengthInVideoFrames();
            // 获取视频时长， / 1000000 将单位转换为秒
            long delayedTime = grabber.getLengthInTime() / 1000000;
            Java2DFrameConverter converter = new Java2DFrameConverter();
            Random random = new Random();
            for (int i = 0; i < count; i++) {
                // 跳转到响应时间
                grabber.setTimestamp((random.nextInt((int)delayedTime - 1) + 1) * 1000000L);
                Frame f = grabber.grabImage();
                BufferedImage bi = converter.getBufferedImage(f);
                String imageName = UUID.randomUUID().toString()+".jpg";
                File out = Paths.get(path, imageName).toFile();
                ImageIO.write(bi, "jpg", out);
//                FileTableEntity fileTable = FileUtils.createFileTableEntity(imageName, SUFFIX, path, f.image.length, "系统生成截图", userId, FileTypeEnum.VIDEO_PHOTO.getCode());
                images.add(path+imageName);
            }
            return images;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        } finally {
            try {
                if (grabber != null) {
                    grabber.stop();
                    grabber.release();
                }
            } catch (FFmpegFrameGrabber.Exception e) {
                log.error("getVideoInfo grabber.release failed 获取文件信息失败：{}", e.getMessage());
            }
        }
    }

}
