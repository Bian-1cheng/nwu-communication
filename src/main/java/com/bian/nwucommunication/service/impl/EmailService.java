package com.bian.nwucommunication.service.impl;

import cn.hutool.extra.mail.MailUtil;
import com.bian.nwucommunication.common.constant.EmailConstants;
import com.bian.nwucommunication.common.errorcode.BaseErrorCode;
import com.bian.nwucommunication.common.execption.ClientException;
import com.bian.nwucommunication.config.EMailConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

import java.io.File;

/**
 * 邮件服务
 *
 * @author bianCheng
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final EMailConfig eMailConfig;
    private final TemplateEngine templateEngine;

    /**
     * 发送邮件
     *
     * @param to      目标邮箱
     * @param subject 标题
     * @param content 内容
     */
    public void send(String to, String subject, String content) {
        MailUtil.send(eMailConfig.getAccount(), to, subject, content, false);
    }

    /**
     * 发送邮件(带附件)
     *
     * @param to      目标邮箱
     * @param subject 标题
     * @param content 内容
     * @param files   附件（可选）
     */
    public void send(String to, String subject, String content, File... files) {
        MailUtil.send(eMailConfig.getAccount(), to, subject, content, false, files);
    }

    /**
     * 发送邮件-读取自定义模板
     *
     * @param to   收件人
     * @param code 验证码
     */
    public void sendCode(String to, String code) {
        Context context = new Context();
        context.setVariable(EmailConstants.CODE_EMAIL_PRE, code);
        String template = templateEngine.process(EmailConstants.CODE_EMAIL_TEMPLATE, context);
        try {
            MailUtil.send(eMailConfig.getAccount(), to, EmailConstants.CODE_EMAIL_SUBJECT, template, true);
        } catch (Exception e) {
            log.error("邮件发送失败：{}",to);
            throw new ClientException(BaseErrorCode.EMAIL_NOT_EXIST);
        }
    }

    /**
     * 发送邮件-读取自定义模板
     *
     * @param to   收件人
     * @param keyWord 关键词
     */
    public void sendRequirement(String to, String keyWord) {
        Context context = new Context();
        context.setVariable(EmailConstants.Require_EMAIL_PRE, keyWord);
        String template = templateEngine.process(EmailConstants.Require_EMAIL_TEMPLATE, context);
        try {
            MailUtil.send(eMailConfig.getAccount(), to, EmailConstants.CODE_EMAIL_SUBJECT, template, true);
        } catch (Exception e) {
            log.error("邮件发送失败：{}",to);
            throw new ClientException(BaseErrorCode.EMAIL_NOT_EXIST);
        }
    }
}
