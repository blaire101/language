package com.qunar.fresh.librarysystem.controller;

import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.qunar.fresh.librarysystem.resulttype.JsonResult;
import com.qunar.fresh.librarysystem.model.Book;
import com.qunar.fresh.librarysystem.model.BorrowedInfo;
import com.qunar.fresh.librarysystem.model.User;
import com.qunar.fresh.librarysystem.service.BookRedecResult;
import com.qunar.fresh.librarysystem.service.BorrowingService;
import com.qunar.fresh.librarysystem.utils.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: feiyan.shan Date: 14-3-31 Time: 上午10:08
 *
 * @author hang.gao
 * @author feiyan.shan
 */
@Controller
public class BorrowingBookController {
    private static final int BOOK_ID_LENGTH = 10;

    private static final int MAX_PAGESIZE = 60;

    private static final int DEFAULT_GOPAGENUM = 1;

    @Resource
    private BorrowingService borrowingService;

    /**
     * 根据书籍编号，查询书籍的详细信息
     *
     * @param request
     * @param bookId
     * @return 如果数据库中存在该书，且管理员有权限，则返回书的详细信息。否则返回null。
     */
    @RequestMapping(value = "/admin/borrow/query/book.do", method = RequestMethod.GET)
    @ResponseBody
    public Object getBookInfo(HttpServletRequest request, @RequestParam("bookId") String bookId) {
        String managerRtx = UserUtils.getUserRtx(request);
        if (managerRtx == null) {
            return JsonResult.errInfo("取得管理员rtx失败");
        }

        bookId = CharMatcher.WHITESPACE.removeFrom(bookId);
        if (bookId.length() != BOOK_ID_LENGTH) {
            return JsonResult.errInfo("书籍编号输入长度错误，必须为10位");
        }

        Book book = borrowingService.getBookInfo(managerRtx, bookId);
        if (book != null) {
            JsonResult jsonResult = new JsonResult(true);
            jsonResult.put("bookInfo", book);
            return JsonResult.statusJson(0, "", jsonResult.getJsonDataMap());
        } else {
            return JsonResult.errInfo("书籍已经被借出 或 书籍信息输入错误");
        }
    }

    @RequestMapping(value = "/admin/borrow/query/reader.do", method = RequestMethod.GET)
    @ResponseBody
    public Object getUserSimpleInfo(HttpServletRequest request, @RequestParam("userRtx") String userRtx)
            throws IOException {
        User user = UserUtils.getUserInfo(userRtx);
        if (user != null) {
            String managerRtx = UserUtils.getUserRtx(request);
            if (managerRtx == null) {
                return JsonResult.errInfo("管理员rtx获取失败");
            }
            user = borrowingService.getUserInfo(managerRtx, user);
            if (user == null) { // 数据库user表中尚未有该用户的信息，在用户借书后，需要插入用户的信息
                return JsonResult.errInfo("管理员无效");
            }
            JsonResult jsonResult = new JsonResult(true);
            jsonResult.put("userInfo", user);
            return JsonResult.statusJson(0, "", jsonResult.getJsonDataMap());
        } else {
            return JsonResult.errInfo("用户名无效，请检查是否拼写错误");
        }
    }

    /**
     * 管理员借阅书籍，传递书籍id列表后，进入该函数
     *
     * @param request
     * @param bookIds
     * @param userRtx
     * @return
     */
    @RequestMapping(value = "/admin/borrow/browbook.do", method = RequestMethod.GET)
    @ResponseBody
    public Object borrowBook(HttpServletRequest request, @RequestParam("bookIdList") String[] bookIds,
                             @RequestParam("userRtx") String userRtx) throws IOException {
        List<String> bookIdList = Lists.newArrayList(bookIds);
        if (bookIdList.size() == 0) {
            return JsonResult.errInfo("请选择书籍");
        }
        User user = UserUtils.getUserInfo(userRtx);
        if (user != null) {
            String managerRtx = UserUtils.getUserRtx(request);
            if (managerRtx == null) {
                return JsonResult.errInfo("管理员rtx获取失败");
            }
            List<Book> failedBookInfo = borrowingService.borrowAllBook(bookIdList, managerRtx, user);
            if (failedBookInfo == null) {
                String errInfo = "借书失败：选择的书籍列表大于读者可借数量";
                return JsonResult.errInfo(errInfo);
            }
            if (failedBookInfo.size() > 0) {
                String errInfo = "以下书籍借阅失败：请检查书籍是否属于管理员所管理的图书馆";
                return JsonResult.errInfo(errInfo, "bookList", failedBookInfo);
            }
            JsonResult jsonResult = new JsonResult(true);
            return JsonResult.statusJson(0, "", jsonResult.getJsonDataMap());
        } else {
            return JsonResult.errInfo("用户名无效，请检查是否拼写错误");
        }

    }

    /**
     * 查询用户借阅书籍。管理员进入还书页面后，输入用户rtx后，查询用户借阅的书籍
     *
     * @param request
     * @param userRtx
     * @return
     */
    @RequestMapping(value = "/admin/ret/searchretbook.do", method = RequestMethod.GET)
    @ResponseBody
    public Object getUserBorrowedBooks(HttpServletRequest request, @RequestParam("userRtx") String userRtx)
            throws IOException {


        User user = UserUtils.getUserInfo(userRtx);
        if (user != null) {
            String managerRtx = UserUtils.getUserRtx(request);
            if (managerRtx == null) {
                return JsonResult.errInfo("管理员rtx获取失败");
            }
            User tempUser = borrowingService.getUserBorrowedBook(managerRtx, user); // 在service中修改

            JsonResult jsonResult = new JsonResult(true);
            jsonResult.put("userRtx", tempUser.getUserRtx());
            jsonResult.put("userName", tempUser.getUserName());
            jsonResult.put("userDept", tempUser.getUserDept());
            jsonResult.put("borrowNum", tempUser.getBorrowNum());
            jsonResult.put("remainBrowNum", tempUser.getRemainBrowNum());
            jsonResult.put("resultList", tempUser.getResultList());
            jsonResult.put("browTotalNum", tempUser.getBrowTotalNum());
            return JsonResult.statusJson(0, "", jsonResult.getJsonDataMap());

        } else {
            return JsonResult.errInfo("用户名无效，请检查是否拼写错误");
        }


    }

    /**
     * 管理员归还用户书籍。管理员在还书页面，选中待还书籍后，点击确定提交。到达该函数 函数需要返回用户还书后的借阅信息。以便继续还书。 问题：目前service中返回的是还书失败的列表。
     *
     * @param request
     * @param userRtx
     * @param bookIds
     * @return
     */
    @RequestMapping(value = "/admin/ret/retbook.do", method = RequestMethod.GET)
    @ResponseBody
    public Object returnBooks(HttpServletRequest request, @RequestParam("userRtx") String userRtx,
                              @RequestParam("bookIdList") String[] bookIds) throws IOException {
        List<String> bookIdList = Lists.newArrayList(bookIds);
        if (bookIdList.size() == 0) {
            return JsonResult.errInfo("请选择书籍");
        }
        User user = UserUtils.getUserInfo(userRtx);
        if (user != null) {
            String managerRtx = UserUtils.getUserRtx(request);
            if (managerRtx == null) {
                return JsonResult.errInfo("管理员rtx获取失败");
            }
            borrowingService.returnBookList(managerRtx.trim(), bookIdList, user);
            user = borrowingService.getUserBorrowedBook(managerRtx, user);
            JsonResult jsonResult = new JsonResult(true);
            jsonResult.put("userRtx", user.getUserRtx());
            jsonResult.put("userName", user.getUserName());
            jsonResult.put("userDept", user.getUserDept());
            jsonResult.put("borrowNum", user.getBorrowNum());
            jsonResult.put("remainBrowNum", user.getRemainBrowNum());
            jsonResult.put("resultList", user.getResultList());
            return JsonResult.statusJson(0, "", jsonResult.getJsonDataMap());
        } else {
            return JsonResult.errInfo("用户名无效，请检查是否拼写错误");
        }
    }

    @RequestMapping(value = "/admin/viewbrow.do", method = RequestMethod.GET)
    @ResponseBody
    public Object viewAllUserBorrowedBookList(HttpServletRequest request, @RequestParam("goPageNum") int goPageNum,
                                              @RequestParam("pageSize") int pageSize) {
        if (goPageNum <= 0) {
            goPageNum = BorrowingBookController.DEFAULT_GOPAGENUM;
        }
        if (pageSize <= 0 || pageSize > 60 || pageSize < 15) {
            pageSize = BorrowingBookController.MAX_PAGESIZE;
        }
        String managerRtx = UserUtils.getUserRtx(request);
        if (managerRtx == null) {
            return JsonResult.errInfo("管理员rtx获取失败");
        }
        BorrowedInfo borrowedInfo = borrowingService.getBorrowedInfo(managerRtx, goPageNum, pageSize);
        JsonResult jsonResult = new JsonResult(true);
        jsonResult.put("totalCount", borrowedInfo.getTotalCount());
        jsonResult.put("pageSize", borrowedInfo.getPageSize());
        jsonResult.put("currentPageNum", borrowedInfo.getCurrentPageNum());// 是不是这个页面
        jsonResult.put("totalPage", borrowedInfo.getTotalPage());
        jsonResult.put("resultList", borrowedInfo.getResultList());

        return JsonResult.statusJson(0, "", jsonResult.getJsonDataMap());
    }

    /**
     * 续借
     *
     * @return 续借结果JSON
     * @author hang.gao
     */
    @RequestMapping("redec")
    @ResponseBody
    public Map<String, Object> redec(@RequestParam("bookId") String bookId) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(bookId));
        BookRedecResult redecResult = borrowingService.redecBook(bookId);
        return JsonResult.statusJson(redecResult.getCode(), redecResult.getMessage(),
                JsonResult.dataJson(true, "retTime", redecResult.getReturnDate()));
    }

    @RequestMapping(value = "/user/userinfo.do")
    @ResponseBody
    public Object getUserBorrowedBookList(HttpServletRequest request) {
        String userRtx = UserUtils.getUserRtx(request);

        List<User> user = borrowingService.getUserBorrowedBookList(userRtx);

        if (user == null) {
            return JsonResult.statusJson(0, "", JsonResult.dataJson(false, "errInfo", "个人信息页，后台数据库正在维护中..., 请您稍后再试"));
        } else if (user.isEmpty()) {
            return JsonResult.statusJson(0, "", JsonResult.dataJson(false, "errInfo", "尊敬的读者，您在去哪儿网一本书也没有借过，赶紧去借书吧！"));
        }
        return JsonResult.statusJson(0, "", JsonResult.dataJson(true, "userInfo", user));
    }

}
