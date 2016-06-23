package com.qunar.fresh.librarysystem.service;

import java.util.List;

import com.qunar.fresh.librarysystem.model.Book;
import com.qunar.fresh.librarysystem.model.BorrowedInfo;
import com.qunar.fresh.librarysystem.model.User;

/**
 * Created with IntelliJ IDEA. User: feiyan.shan Date: 14-4-1 Time: 下午5:43 To
 *
 * @author hang.gao
 * @author feiyan.shan
 */
public interface BorrowingService {

    /**
     * 根据书籍id查询书籍的详细信息。 使用场合：管理员进入借书界面，并且输入书籍id点确定后，下方展示书籍详细信息
     * 注意：只能返回管理员所管理的图书馆中，书籍的信息。如果该书籍不在管理员的图书馆中，则返回空对象。
     *
     * @param bookId
     * @return
     */
    public Book getBookInfo(final String managerRtx, final String bookId);

    /**
     * 根据读者rtx，返回读者信息，包括读者所属部门，已借阅数，剩余借阅数。 但不包括借阅书籍。 注意：只能返回管理员所管理图书馆中，读者的信息。
     * 使用场合：管理员进入借书界面，并且输入读者rtx后，下方展示读者的信息。
     *
     * @param managerRtx
     * @return
     */
    public User getUserInfo(final String managerRtx, User user);

    /**
     * 将所有借阅书籍列表插入到数据库中 使用场合：管理员进入借书页面，并且输入了所借书籍和读者信息后，点击确定。
     * 注意：管理员只能操作自己所管理的图书馆。
     *
     * @param allBookId 所有书籍的id
     * @return 借阅失败的书籍id。后期需要根据列表来生成不同的json。当列表长度为0时，返回true。否则，返回false，以及该列表信息
     */
    public List<Book> borrowAllBook(List<String> allBookId, String managerRtx, User userInit);

    /**
     * 根据用户rtx，查询出用户在管理员所管理的图书馆中，所借书籍。 使用场合：管理员进入还书页面，输入用户rtx后，下面展示用户所借书籍的详细信息。
     *
     * @param managerRtx
     * @return 返回用户详细信息：包括用户所借书籍列表
     */
    public User getUserBorrowedBook(String managerRtx, User userInit);

    /**
     * 实现还书功能。根据参数列表中书籍id和用户rtx更新相关表格。 使用场合：管理员进入还书页面，且选中待还书籍，点击确定后。
     *
     * @param managerRtx
     * @param bookIds
     * @param user
     * @return 没有还成功的书籍id列表：根据该列表生成不同的json。如果列表长度为0，则返回true；否则返回false，以及该列表信息
     */
    public List<Book> returnBookList(String managerRtx, List<String> bookIds, User user);

    /**
     * 续借，如果不能再续借，则返回应该还书的时间
     *
     * @param bookId 书本的条码
     * @param bookId 图书馆id
     * @return 续借的结果
     * @author hang.gao
     */
    BookRedecResult redecBook(String bookId);

    /**
     * 用户查询个人信息。分页查询。
     *
     * @param userRtx 用户Rtx
     * @return 用户已借阅书籍，包括已借阅的所有图书馆书籍。列表中每一项对应一个图书馆的书籍。
     * @author : libin.chen
     */
    public List<User> getUserBorrowedBookList(String userRtx);

    public int fetchBorrowedCountOfLib(String managerRtx);

    /**
     * 查看一个图书馆是否有被借阅的书
     *
     * @param libId
     * @return
     * @author: he.chen
     */
    public int fetchBorrowedCountOfLib(int libId);


    /**
     * 管理员查看所有借阅信息。
     *
     * @param managerRtx
     * @return 借阅表中尚未归还书籍的
     */
    public BorrowedInfo getBorrowedInfo(String managerRtx, int goPageNum, int pageSize) ;

}
