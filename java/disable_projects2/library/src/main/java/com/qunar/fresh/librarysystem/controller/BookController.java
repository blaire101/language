package com.qunar.fresh.librarysystem.controller;

import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.google.common.base.CharMatcher;
import com.google.common.base.Strings;
import com.google.common.math.IntMath;

import com.qunar.fresh.librarysystem.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.qunar.fresh.librarysystem.resulttype.JsonResult;
import com.qunar.fresh.librarysystem.model.Book;
import com.qunar.fresh.librarysystem.model.Category;
import com.qunar.fresh.librarysystem.search.book.BookSearchResult;
import com.qunar.fresh.librarysystem.service.BookService;
import com.qunar.fresh.librarysystem.service.BookService.BookWrapper;
import com.qunar.fresh.librarysystem.utils.UserUtils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created with IntelliJ IDEA. User: jinglv Date: 14-3-28 Time: 下午5:39 To change
 *
 * @author hang.gao
 * @author jing.lv
 * @author libin.chen
 */
@Controller
public class BookController {

    private static final int BOOK_ID_LENGTH = 10;
    private static final int BOOK_NAME_AUTHOR_PRESS_TITLE_LENGTH = 64;
    private static final int BOOK_TYPE_LENGTH = 32;
    private static final int MAX_PAGE_SIZE = 60;
    private static final int MIN_PAGE_SIZE = 15;
    private static final int DEFAULT_PAGE_NUM = 1;

    private Logger logger = LoggerFactory.getLogger("controllerlogger");

    @Resource
    private BookService bookService;

    @Resource
    private UserService userService;

    /**
     * 返回某类型下的图书的top n时，n的值
     */
    private static final int TOP_N = 10;

    @RequestMapping(value = "/admin/addbookload.do")
    @ResponseBody
    public Map<String, Object> addbookload(@RequestParam String title) {
        checkNotNull(title);
        checkArgument(title.length() <= BOOK_NAME_AUTHOR_PRESS_TITLE_LENGTH);
        title = CharMatcher.WHITESPACE.removeFrom(title);
        Set<Category> set = bookService.addBookLoad(title);
        Set<String> resultSet = Sets.newHashSet();
        if ("".equals(title)) {
            for (Category category : set) {
                resultSet.add(category.getTitle());
            }
        } else {
            for (Category category : set) {
                resultSet.add(category.getBookType());
            }
        }
        JsonResult jsonResult = new JsonResult(true);
        jsonResult.put("resultList", resultSet);
        return JsonResult.statusJson(0, "", jsonResult.getJsonDataMap());
    }

    @RequestMapping(value = "/admin/addbook.do")
    @ResponseBody
    public Map<String, Object> addBook(@RequestParam String bookId, @RequestParam String bookName,
                                       @RequestParam String bookAuthor, @RequestParam String bookPress,
                                       @RequestParam String title, @RequestParam String bookType, HttpServletRequest request) {
        bookId = CharMatcher.WHITESPACE.removeFrom(bookId);
        bookName = bookName.trim();
        bookPress = CharMatcher.WHITESPACE.removeFrom(bookPress);
        title = CharMatcher.WHITESPACE.removeFrom(title);
        bookType = CharMatcher.WHITESPACE.removeFrom(bookType);
        bookAuthor = bookAuthor.trim();
        if (Strings.isNullOrEmpty(bookId) || bookId.length() != BOOK_ID_LENGTH || Strings.isNullOrEmpty(bookName)
                || bookName.length() > BOOK_NAME_AUTHOR_PRESS_TITLE_LENGTH || bookPress.length() > BOOK_NAME_AUTHOR_PRESS_TITLE_LENGTH
                || Strings.isNullOrEmpty(bookAuthor) || bookAuthor.length() > BOOK_NAME_AUTHOR_PRESS_TITLE_LENGTH
                || Strings.isNullOrEmpty(bookType) || bookType.length() > BOOK_TYPE_LENGTH || Strings.isNullOrEmpty(title)
                || title.length() > BOOK_NAME_AUTHOR_PRESS_TITLE_LENGTH || bookPress.length() > BOOK_NAME_AUTHOR_PRESS_TITLE_LENGTH || bookPress == null) {
            JsonResult jsonResult = new JsonResult(false);
            jsonResult.put("errInfo", "参数传入不正确,请检查您的输入！");
            return JsonResult.statusJson(0, "", jsonResult.getJsonDataMap());
        }
        bookId = bookId.toUpperCase();
        logger.info("进入增加图书方法");
        String userRtx = UserUtils.getUserRtx(request);
        int bookLib = userService.getUserLibId(userRtx);
        Book book = new Book();
        book.setBookId(bookId);
        book.setBookName(bookName);
        book.setBookAuthor(bookAuthor);
        book.setBookPress(bookPress);
        book.setBookType(bookType);
        book.setTitle(title);
        book.setBookLib(bookLib);
        String errmsg = bookService.addBook(book, userRtx);
        if ("".equals(errmsg)) {
            JsonResult jsonResult = new JsonResult(true);
            jsonResult.put("errInfo", "");
            return JsonResult.statusJson(0, "", jsonResult.getJsonDataMap());
        } else {
            logger.info("errmsg:{}", errmsg);
            JsonResult jsonResult = new JsonResult(false);
            jsonResult.put("errInfo", "该书已经存在或者该条形码已经过期，不允许重复添加或使用条形码");
            return JsonResult.statusJson(0, "", jsonResult.getJsonDataMap());
        }
    }

    @RequestMapping(value = "/admin/bookmag.do")
    @ResponseBody
    public Map<String, Object> getBookList(@RequestParam int goPageNum, @RequestParam int pageSize,
                                           HttpServletRequest request) {
        if (goPageNum <= 0 || pageSize < MIN_PAGE_SIZE || pageSize > MAX_PAGE_SIZE) {
            goPageNum = DEFAULT_PAGE_NUM;
            pageSize = MIN_PAGE_SIZE;
        }
        String userRtx = UserUtils.getUserRtx(request);
        int bookLib = userService.getUserLibId(userRtx);
        int limit = pageSize;
        int bookCount = bookService.getBookCountInOneLib(bookLib);
        int totalPage = IntMath.divide(bookCount, pageSize, RoundingMode.CEILING);
        int offset = (goPageNum - 1) * pageSize;
        if (goPageNum > totalPage) {
            offset = (totalPage - 1) * pageSize;
        }
        List<Book> resultList = bookService.getBookList(limit, offset, bookLib);
        if (resultList.size() > 0) {
            JsonResult jsonResult = new JsonResult(true);
            jsonResult.put("totalCount", bookCount).put("pageSize", pageSize).put("currentPageNum", goPageNum)
                    .put("totalPage", totalPage).put("resultList", resultList);
            return JsonResult.statusJson(0, "", jsonResult.getJsonDataMap());
        } else {
            JsonResult jsonResult = new JsonResult(false);
            jsonResult.put("errInfo", "获取图书列表失败").put("totalCount", bookCount).put("pageSize", pageSize)
                    .put("currentPageNum", goPageNum).put("totalPage", totalPage).put("resultList", resultList);
            return JsonResult.statusJson(0, "", jsonResult.getJsonDataMap());
        }
    }

    @RequestMapping(value = "/admin/bookmag/del.do")
    @ResponseBody
    public Map<String, Object> delBook(@RequestParam String bookId, @RequestParam int goPageNum,
                                       @RequestParam int pageSize, HttpServletRequest request) {
        bookId = CharMatcher.WHITESPACE.removeFrom(bookId);
        if (Strings.isNullOrEmpty(bookId)) {
            JsonResult jsonResult = new JsonResult(false);
            jsonResult.put("errInfo", "图书编号不允许为空！");
            return JsonResult.statusJson(0, "", jsonResult.getJsonDataMap());
        }
        if (goPageNum <= 0 || pageSize < MIN_PAGE_SIZE || pageSize > MAX_PAGE_SIZE) {
            pageSize = MIN_PAGE_SIZE;
            goPageNum = DEFAULT_PAGE_NUM;
        }
        logger.info("进入删除图书方法");
        String userRtx = UserUtils.getUserRtx(request);
        int bookLib = userService.getUserLibId(userRtx);
        int limit = pageSize;
        int bookCount = bookService.getBookCountInOneLib(bookLib);
        int totalPage = IntMath.divide(bookCount, pageSize, RoundingMode.CEILING);
        int offset = (goPageNum - 1) * pageSize;
        if (goPageNum > totalPage) {
            offset = (totalPage - 1) * pageSize;
        }
        Map<String, Object> resultMap = bookService.deleteBook(limit, offset, bookId, bookLib, userRtx);
        List<Book> resultList = (List<Book>) resultMap.get("resultList");
        String errmsg = (String) resultMap.get("errmsg");
        if (errmsg != "删除成功") {
            JsonResult jsonResult = new JsonResult(false);
            jsonResult.put("errInfo", errmsg).put("totalCount", bookCount).put("pageSize", pageSize)
                    .put("currentPageNum", goPageNum).put("totalPage", totalPage).put("resultList", resultList);
            return JsonResult.statusJson(0, "", jsonResult.getJsonDataMap());
        } else {
            logger.info("errmsg:{}", errmsg);
            JsonResult jsonResult = new JsonResult(true);
            jsonResult.put("totalCount", bookCount - 1).put("pageSize", pageSize).put("currentPageNum", goPageNum)
                    .put("totalPage", totalPage).put("resultList", resultList);
            return JsonResult.statusJson(0, "", jsonResult.getJsonDataMap());
        }
    }

    /**
     * 通过关键词搜索图书
     *
     * @param keywords 关键词
     * @param page     页码
     * @param pageSize 每一页中数据的长度
     * @return 搜索到的结果
     * @author hang.gao
     */
    @RequestMapping("/book/search")
    @ResponseBody
    public Map<String, Object> search(@RequestParam("queryString") String keywords,
                                      @RequestParam("goPageNum") int page, @RequestParam("pageSize") int pageSize) {
        BookSearchResult result = bookService.searchBook(keywords, page, pageSize);
        return doWithBookSearchResult(keywords, page, pageSize, result);
    }

    /**
     * 通过图书名搜索图书
     *
     * @param keywords 书名关键词
     * @param page     页码
     * @param pageSize 每一页中数据的长度
     * @return 搜索到的结果
     * @author hang.gao
     */
    @RequestMapping("/book/nameSearch")
    @ResponseBody
    public Map<String, Object> searchByBookName(@RequestParam("queryString") String keywords,
                                                @RequestParam("goPageNum") int page, @RequestParam("pageSize") int pageSize) {
        BookSearchResult result = bookService.searchBookByName(keywords, page, pageSize);
        return doWithBookSearchResult(keywords, page, pageSize, result);
    }

    private Map<String, Object> doWithBookSearchResult(String keywords, int page, int pageSize, BookSearchResult result) {
        Map<String, Object> map = Maps.newHashMap();
        List<Map<String, Object>> books = Lists.transform(result.getBooks(), new Function<Book, Map<String, Object>>() {
            public Map<String, Object> apply(Book book) {
                return book.toJsonMap();
            }
        });
        map.put("resultList", books);
        map.put("totalPage",
                (int) (result.getTotalHints() % pageSize == 0 ? result.getTotalHints() / pageSize : result
                        .getTotalHints() / pageSize + 1));
        map.put("currentPageNum", page);
        map.put("pageSize", pageSize);
        map.put("querystring", keywords);
        if (books == null || books.size() == 0) {
            map.put("errInfo", "没有查到结果");
            map.put("ret", false);
        } else {
            map.put("ret", true);
        }

        return JsonResult.statusJson(0, "", map);
    }

    /**
     * 请求某个类型的图书借阅量的top n
     *
     * @return 结果的Map，用于生成json
     * @author hang.gao
     */
    @ResponseBody
    @RequestMapping("/book/topkind")
    public Map<String, Object> top(@RequestParam("kindType") int type) {
        List<Book> books = bookService.top(TOP_N, type);
        return JsonResult.statusJson(
                0,
                "",
                JsonResult.dataJson(true, "resultList",
                        Lists.transform(books, new Function<Book, Map<String, Object>>() {
                            public Map<String, Object> apply(Book book) {
                                return book.toJsonMap();
                            }
                        })));

    }

    /**
     * 查询某个分类下的图书
     *
     * @param navType   分类的具体类型
     * @param goPageNum 页码
     * @param pageSize  一页的大小
     * @return 查询到的数据对应的JSON
     * @author hang.gao
     */
    @RequestMapping("/book/nav")
    @ResponseBody
    public Map<String, Object> bookOfType(@RequestParam("navId") int navType, @RequestParam("goPageNum") int goPageNum,
                                          @RequestParam("pageSize") int pageSize) {
        BookWrapper bookWrapper = bookService.bookOfType(navType, goPageNum, pageSize);
        Map<String, Object> jsonMap = Maps.newHashMap();
        jsonMap.put("ret", true);
        jsonMap.put("resultList", Lists.transform(bookWrapper.getBooks(), new Function<Book, Map<String, Object>>() {
            public Map<String, Object> apply(Book book) {
                return book.toJsonMap();
            }
        }));
        jsonMap.put("totalCount", bookWrapper.getBooks().size());
        jsonMap.put("pageSize", pageSize);
        jsonMap.put("currentPageNum", goPageNum);
        int tpage = (int) (bookWrapper.getTotalCount() / pageSize);
        jsonMap.put("totalPage", tpage * pageSize < bookWrapper.getTotalCount() ? tpage + 1 : tpage);
        return JsonResult.statusJson(0, "", jsonMap);
    }

    /**
     * 管理员修改图书页
     * <p/>
     * 根据 book 表的 book_id 连表查询出 这个本书的详细信息 (包括分类等)
     *
     * @param bookId ： book 表的 book_id
     * @author libin.chen
     */
    @RequestMapping(value = "/admin/modifybookinfo")
    @ResponseBody
    public Object getBookById(@RequestParam("bookId") String bookId) { //
        Map map = bookService.getBookByBookId(bookId);
        if (map != null && map.size() > 0) {
            return JsonResult.statusJson(0, "", JsonResult.dataJson(true, "bookInfo", map));
        } else {
            return JsonResult.statusJson(0, "", JsonResult.dataJson(false, "errInfo", "查询失败， 请检查参数的正确性"));
        }
    }

    /**
     * 管理员修改图书页后，点击 “提交” 按钮，请求方式为 POST
     *
     * @author libin.chen
     */
    @RequestMapping(value = "/admin/modifybookinfoafter")
    @ResponseBody
    public Object modifyBookInfo(Book book, HttpServletRequest request) {
        book.setBookName(book.getBookName().trim());
        book.setTitle(CharMatcher.WHITESPACE.removeFrom(book.getTitle()));
        book.setBookType(CharMatcher.WHITESPACE.removeFrom(book.getBookType()));
        String userRtx = UserUtils.getUserRtx(request);
        if (0 == bookService.modifyBookInfo(book, userRtx)) {
            return JsonResult.statusJson(0, "更改失败", JsonResult.dataJson(false, "errInfo", "参数错误或未知数据库错误"));
        } else {
            return JsonResult.statusJson(0, "", JsonResult.dataJson(true, "errInfo", ""));
        }

    }
}
