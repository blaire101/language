package com.qunar.springtest.service;

import com.qunar.springtest.model.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Author: libin.chen@qunar.com  Date: 14-6-1 11:35
 */

//Dependency Injection
//Inverse of Control
public class UserServiceTest01 {

	@Test
	public void testAdd() throws Exception {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("testBean/testBeans.xml");

        System.out.println("haha");
		UserService service = (UserService)ctx.getBean("userService");
		UserService service2 = (UserService)ctx.getBean("userService");

		System.out.println(service == service2);


		User u = new User();
		u.setUsername("zhangsan");
		u.setPassword("zhangsan");

		service.createUser(u);

		// 08  autowire
		System.out.println(service.getUserDAO());
	}

}
