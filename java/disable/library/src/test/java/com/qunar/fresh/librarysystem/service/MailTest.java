package com.qunar.fresh.librarysystem.service;

import com.google.common.collect.Lists;
import com.qunar.fresh.librarysystem.dao.ExpiresDao;
import com.qunar.fresh.librarysystem.email.TaskManager;
import com.qunar.fresh.librarysystem.model.Library;
import com.qunar.fresh.librarysystem.model.ReminderInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: libin.chen
 * Date: 14-4-07
 * Time: 19:40
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/*.xml", "classpath:extractor/*.xml"})
public class MailTest {

    @Resource
    private ExpiresService expiresService;

    @Test
    //@Ignore
    public void test_mail_TaskManager_dealReminderExpiresUser() throws Exception {
        new TaskManager().dealReminderExpiresUser();
    }

}

