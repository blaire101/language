package com.qunar.fresh.librarysystem.io;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 * @author hang.gao
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:extractor/crawler.xml")
public class UrlResourceAccessTest {

	@Resource
	private UrlResourceAccess resourceAccess;
	
	@Test
	public void testLoadPrivateAndStore() {
		resourceAccess.loadPrivateAndStore("42093759724295256111397187500759.jpg", System.out);
	}
	
	@Test
	public void testLoadAndStore() {
		resourceAccess.loadAndStore("http://www.baidu.com");
	}
	
	@Test
	public void testLoadPrivateAndStore_when_local_file_is_not_exists() {
		resourceAccess.loadPrivateAndStore("42093759724295256111397187500759.jpg", System.out);
	}
}
