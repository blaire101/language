package com.qunar.fresh.librarysystem.Utils;

import com.qunar.fresh.librarysystem.model.User;
import com.qunar.fresh.librarysystem.utils.UserUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: feiyan.shan
 * Date: 14-4-10
 * Time: 上午11:08
 * To change this template use File | Settings | File Templates.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/*.xml", "classpath:extractor/*.xml"})
public class UserUtilsTest {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Test
    public void testUserName_success() throws IOException {
        User user = UserUtils.getUserInfo("feiyan.shan");
//        Iterator<String> iterator = userInfo.keySet().iterator();
//        while (iterator.hasNext()){
//            String key = iterator.next().toString();
//             logger.info("key is {} and value is {}", key,userInfo.get(key));
//        }
//        Assert.assertTrue(userInfo.size() > 0);
    }

    @Test
    public void testUserName_failed() throws IOException {
        User user  = UserUtils.getUserInfo("feiyan.shandddd");
        Assert.assertTrue(user==null);
    }


}
