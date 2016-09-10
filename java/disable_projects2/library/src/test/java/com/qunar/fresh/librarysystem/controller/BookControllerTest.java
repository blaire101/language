package com.qunar.fresh.librarysystem.controller;

import java.util.Collections;

import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.qunar.fresh.librarysystem.model.Book;
import com.qunar.fresh.librarysystem.search.book.BookSearchResult;
import com.qunar.fresh.librarysystem.service.BookService;

@RunWith(EasyMockRunner.class)
public class BookControllerTest {

	@Mock
	private BookService bookService;
	
	@Test
	public void testSearch() {
		BookController bookController = new BookController();
		EasyMock.expect(bookService.searchBook("Java", 1, 10)).andReturn(new BookSearchResult(0, Collections.<Book>emptyList(), 1, 10));
		EasyMock.replay(bookService);
	}
}
