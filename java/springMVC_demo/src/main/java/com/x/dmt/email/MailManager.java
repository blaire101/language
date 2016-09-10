package com.x.dmt.email;

import com.x.dmt.util.CF;
import com.x.dmt.util.Constant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.UnsupportedEncodingException;
import java.util.Date;

@Component
public class MailManager {

    public static MailManager instance = new MailManager();

    public static MailManager getInstance() {
        return instance;
    }

    private static Logger logger = LoggerFactory.getLogger(MailManager.class);

//    public final static String mailSuffix = "@xkeshi.com";

    private MailSenderInfo mailInfo;

    private MailManager() {
        try {
            mailInfo = MailManager.afterPropertiesSet();
        } catch (UnsupportedEncodingException e) {
            logger.error("MailManager.afterPropertiesSet() function exception : {}", e.getMessage());
        }
    }

    private static MailSenderInfo afterPropertiesSet() throws UnsupportedEncodingException {
        MailSenderInfo mailInfo = new MailSenderInfo();
        LoadMailConfig mailConfig = LoadMailConfig.getInstance();
        mailInfo.setMailServerHost(mailConfig.getMailConfigBykey("mailServerHost"));
        mailInfo.setMailServerPort(mailConfig.getMailConfigBykey("mailServerPort"));
        mailInfo.setValidate(true);
        mailInfo.setUserName(mailConfig.getMailConfigBykey("userName"));
        mailInfo.setPassword(mailConfig.getMailConfigBykey("password"));
        mailInfo.setFromAddress(mailConfig.getMailConfigBykey("fromAddress"));
        mailInfo.setSubject(Constant.MAIL_DEFAULT_SUBJECT);
        return mailInfo;
    }

    public void sendMail(String receivers, String subject, String mailCoreContent) throws UnsupportedEncodingException {
        StringBuilder mailHeader = new StringBuilder();

        mailHeader.append(Constant.SPLIT_LINE + "\n");

        StringBuilder mailFooter = new StringBuilder();

        mailFooter.append("\n" + Constant.MAIL_NOT_NEED_REPLY + "\n\nAll Rights Reserved xkeshi Corp. System (XCS)\nAny problem, Please Contact Xkeshi-Administrator\n");

        SimpleMailSender sms = new SimpleMailSender();

        mailInfo.setSubject(subject);

        StringBuilder mailContent = new StringBuilder();

        String mailAllContent = mailContent.append(mailHeader).append(mailCoreContent).append(mailFooter).toString();

        mailInfo.setContent(mailAllContent);

        this.sendCoreMail(receivers, sms);
    }

    private void sendCoreMail(String receivers, SimpleMailSender sms) {
        if (StringUtils.isEmpty(receivers)) {
            return;
        }
        String[] users = receivers.split(",");
        for (String user : users) {
            user = user.trim();
            mailInfo.setToAddress(user);
            sms.sendTextMail(mailInfo);
        }
    }
}
