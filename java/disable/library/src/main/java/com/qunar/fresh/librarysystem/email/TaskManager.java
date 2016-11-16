package com.qunar.fresh.librarysystem.email;

import com.google.common.collect.Maps;
import com.qunar.fresh.librarysystem.model.ReminderInfo;
import com.qunar.fresh.librarysystem.service.ExpiresService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: libin.chen Date: 14-4-7 Time: 下午1:40 To change this template use File | Settings |
 * File Templates.
 */
@Component
public class TaskManager implements InitializingBean {

    @Resource
    private ExpiresService expiresService;

    public final static String mailSuffix = "@qunar.com";

    private MailSenderInfo mailInfo;

    @Override
    public void afterPropertiesSet() throws Exception {
        mailInfo = new MailSenderInfo();
        LoadMailConfig mailConfig = LoadMailConfig.getInstance();
        mailInfo.setMailServerHost(mailConfig.getMailConfigBykey("mailServerHost"));
        mailInfo.setMailServerPort(mailConfig.getMailConfigBykey("mailServerPort"));
        mailInfo.setValidate(true);
        mailInfo.setUserName(mailConfig.getMailConfigBykey("userName"));
        mailInfo.setPassword(mailConfig.getMailConfigBykey("password"));// 您的邮箱密码
        mailInfo.setFromAddress(mailConfig.getMailConfigBykey("fromAddress"));
        mailInfo.setSubject("去哪儿图书管理系统");
    }

    public void dealReminderExpiresUser() throws Exception {

        List<ReminderInfo> reminderExpiresInfoList = expiresService.getReminderExpiresInfo();

        if (reminderExpiresInfoList == null) {
            return;
        }
        this.integratingPersonalEmailContent(reminderExpiresInfoList, EmailType.Expires); // 标志位 代表是催还或过期提醒
    }

    private void sendMail(EmailType flag, Map<String, StringBuilder> mapUserToContent) {
        StringBuilder startContent = new StringBuilder();

        switch (flag) {
        case Expires:
            startContent.append("\n尊敬的读者，您借出的以下图书即将到期或已经到期，请尽快来馆归还到期图书或登录图书馆主页续借!\n\n\n");
            break;
        case Reserve:
            startContent.append("\n尊敬的读者，您预约的以下图书到了，并且非常枪手，请尽快到相应的图书馆借阅!\n\n\n");
            break;
        }

        // 这个类主要来发送邮件
        StringBuilder endContent = new StringBuilder();

        endContent.append("\n该邮件为系统自动发出，请勿回复，谢谢\n\nAll Rights Reserved Qunar Books Management System (QBMS)\n").append(
                "     Any problem, Please Contact SuperAdministrator");

        SimpleMailSender sms = new SimpleMailSender();

        for (Map.Entry<String, StringBuilder> entry : mapUserToContent.entrySet()) {
            mailInfo.setToAddress(entry.getKey());
            StringBuilder mailContent = new StringBuilder();
            mailInfo.setContent(mailContent.append(startContent).append(entry.getValue()).append(endContent).toString());
            sms.sendTextMail(mailInfo);// 发送文体格式
        }
    }

    public void integratingPersonalEmailContent(List<ReminderInfo> reminderInfos, EmailType flag) {
        Map<String, StringBuilder> mapUserToContent = Maps.newHashMap();
        for (ReminderInfo reminderInfo : reminderInfos) { // 遍历 读者 RTX
            // 设置邮箱内容
            StringBuilder mailMainContent;
            switch (flag) {
            case Expires:
                mailMainContent = generateExpiresInfo(reminderInfo);
                break;
            case Reserve:
                mailMainContent = generateReserveInfo(reminderInfo);
                break;
            default:
                mailMainContent = new StringBuilder();
            }
            String userEmail = reminderInfo.getUserRtx() + mailSuffix;
            if (!mapUserToContent.containsKey(userEmail)) {
                mapUserToContent.put(userEmail, mailMainContent);
            } else {
                StringBuilder personalEmailContent = mapUserToContent.get(userEmail);
                mapUserToContent.put(userEmail, personalEmailContent.append(mailMainContent));
            }
        }
        this.sendMail(flag, mapUserToContent);
    }

    private StringBuilder generateReserveInfo(ReminderInfo reminderInfo) {
        StringBuilder mainContent = new StringBuilder();
        mainContent.append("<<").append(reminderInfo.getBookName()).append(">>  馆藏地点 :  ")
                .append(reminderInfo.getLibDept()).append("   ").append(reminderInfo.getLibName()).append("\n\n");
        return mainContent;
    }

    private StringBuilder generateExpiresInfo(ReminderInfo reminderInfo) {
        StringBuilder mainContent = new StringBuilder();
        mainContent.append("<<").append(reminderInfo.getBookName()).append(">>").append(" ( ISBN : ")
                .append(reminderInfo.getBookId()).append(") ").append(getExpiresDayContent(reminderInfo)).append(".  ")
                .append("馆藏地点 :  ").append(reminderInfo.getLibDept()).append("   ").append(reminderInfo.getLibName())
                .append("  \n\n");
        return mainContent;
    }

    /**
     * 得到读者借阅书的天数内容，判断他这本书是过期，快到到期，还是刚好到期 的内容。
     * 
     * @param reminderInfo
     * @return
     */
    private StringBuilder getExpiresDayContent(ReminderInfo reminderInfo) {
        StringBuilder remindExpiresDayContent = new StringBuilder();
        if (reminderInfo.getExpiresDay() > 0) {
            remindExpiresDayContent.append("还有 ").append(reminderInfo.getExpiresDay()).append(" 天到期");
        } else if (reminderInfo.getExpiresDay() == 0) {
            remindExpiresDayContent.append("已到期");
        } else {
            remindExpiresDayContent.append("已过期 ").append(reminderInfo.getExpiresDay() * (-1)).append(" 天");
        }
        return remindExpiresDayContent;
    }

    public static enum EmailType {
        Expires, Reserve
    }
}
