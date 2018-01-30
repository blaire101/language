#!/usr/bin/env python
# _*_ coding: utf-8 _*_

## date: 20150929

## 邮件相关配置变量
mail_to_list = ["data_space@your_company_name.com"]
#mail_host = "smtp.exmail.qq.com:465"
mail_host = "smtp.exmail.qq.com:25"
mail_user = "data_space"
mail_password = "your_password"
mail_postfix = "your_company_name.com"
mail_from = mail_user + " <" +  mail_user + "@" + mail_postfix + ">"
mail_user_address = "data_space@your_company_name.com"

# 发送内容相关配置
subject = "default-email-subject"
mail_type = "plain"

