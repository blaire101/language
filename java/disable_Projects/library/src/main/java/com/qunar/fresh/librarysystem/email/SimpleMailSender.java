package com.qunar.fresh.librarysystem.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA. User: libin.chen Date: 14-4-7 Time: 下午1:40 To change this template use File | Settings |
 * File Templates.
 */

/**
 * 简单邮件（不带附件的邮件）发送器
 */
public class SimpleMailSender {

    private final Logger logger = LoggerFactory.getLogger(SimpleMailSender.class);

    /**
     * 以文本格式发送邮件
     * 
     * @param mailInfo 待发送的邮件的信息
     */
    public boolean sendTextMail(MailSenderInfo mailInfo) {

        MyAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties();
        if (mailInfo.isValidate()) {
            // 如果需要身份认证，则创建一个密码验证器
            authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
        try {
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(mailInfo.getFromAddress());
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            Address to = new InternetAddress(mailInfo.getToAddress());
            mailMessage.setRecipient(Message.RecipientType.TO, to);
            // 设置邮件消息的主题
            mailMessage.setSubject(mailInfo.getSubject());
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // 设置邮件消息的主要内容
            String mailContent = mailInfo.getContent();
            mailMessage.setText(mailContent);
            // 发送邮件
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            logger.error("发送邮件失败", ex);
            return false;
        }
    }

    /**
     * Created with IntelliJ IDEA. User: libin.chen Date: 14-4-7 Time: 下午1:40 To change this template use File |
     * Settings | File Templates.
     */
    private static final class MyAuthenticator extends Authenticator {
        String userName = null;
        String password = null;

        public MyAuthenticator(String username, String password) {
            this.userName = username;
            this.password = password;
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(userName, password);
        }
    }
}