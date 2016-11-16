package com.qunar.fresh.librarysystem.service;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.qunar.fresh.librarysystem.model.Book;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/*.xml", "classpath:extractor/*.xml"})
public class AddBookTest {

	@Resource
	private BookService bookService;
	
	@Test
	public void test() {
		Book book = new Book();
		book.setBookId("9787111442509");
		book.setBookAuthor("（美）Cay S. Horstmann /（美）Gary Cornell ");
		book.setTitle("Computer");
		book.setBookPress("机械工业出版社");
		book.setBookType("Java");
		book.setBookName("Java核心技术（卷2）：高级特性（原书第9版）");
		bookService.addBook(book, "hang.gao");
	}
}
