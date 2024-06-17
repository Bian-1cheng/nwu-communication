package com.bian.nwucommunication.controller;



import cn.hutool.extra.mail.MailUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/fs")
public class MailController {

    @RequestMapping("/code")
    public String sendMail() {
//        String to, String subject, String content, boolean isHtml, File... files
//        MailUtil.send("bian_c324@163.com","11","1234",false,null);
        MailUtil.send("bian_c324@163.com", "测试", "邮件来自Hutool测试", false);
        return "简单邮件发送成功！";
    }
}