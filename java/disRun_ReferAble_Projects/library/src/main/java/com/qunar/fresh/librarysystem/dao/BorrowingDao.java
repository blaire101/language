package com.qunar.fresh.librarysystem.dao;

import java.util.List;
import com.qunar.fresh.librarysystem.model.enums.BookStatus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import com.qunar.fresh.librarysystem.model.Book;
import com.qunar.fresh.librarysystem.model.BorrowedBookInfo;
import com.qunar.fresh.librarysystem.model.User;
import com.qunar.fresh.librarysystem.model.enums.Returned;

/**
 * Created with IntelliJ IDEA. User: feiyan.shan Date: 14-3-28 Time: 上午11:57
 * 
 * @author hang.gao
 * @author feiyan.shan
 * @author libin.chen
 */
@Repository
public interface BorrowingDao {
    /**
     * 获取指定用户借阅的书籍信息，仅限管理员所管理的图书馆 返回用户的详细信息，包括借阅书籍列表
     * 
     * @param libId
     * @param userRtx
     * @return
     */
    public User getUserBorrowedBook(@Param("libId") int libId, @Param("userRtx") String userRtx);

    /**
     * 获取指定用户的简单信息，仅限管理员所管理的图书馆 简单信息：不包括借阅书籍列表
     * 
     * @param libId
     * @param userRtx
     * @return
     */
    public User getUserInfo(@Param("libId") int libId, @Param("userRtx") String userRtx);

    /**
     * 将用户借阅书籍插入到借阅表中
     * 
     * @param borrowedBook
     * @return
     */
    public int insertBorrowedBook(BorrowedBookInfo borrowedBook);

    /**
     * 统计用户在borrowed表中的借阅数量
     * 
     * @param libId
     * @param userRtx
     * @return
     */
    public int countUserBorrowNumInBorrowedTable(@Param("libId") int libId, @Param("userRtx") String userRtx);

    /**
     * 查看用户在user表中借阅数量
     * 
     * @param libId
     * @param userRtx
     * @return
     */
    public Integer getUserHasBorrowNum(@Param("libId") int libId, @Param("userRtx") String userRtx);

    /**
     * 检查管理员是否有操作指定书籍的权限
     * 
     * @param libId 管理员所管理的图书馆Id
     * @param bookId 书籍id
     * @param bookStatus 书籍状态
     * @return 返回书籍的记录Id
     */
    public Integer checkManagerRight(@Param("libId") int libId, @Param("bookId") String bookId,
            @Param("bookStatus") BookStatus bookStatus);

    /**
     * 查看指定书籍详细信息，包括无效书籍（是否应该不包括？不包括则直接返回null） 注：必须为管理员有权限的书籍
     * 
     * @param libId 管理员图书馆ID
     * @param bookId 书籍id
     * @return 书籍对象，包括书籍详细信息
     */
    public Book selectBookBelongManager(@Param("libId") int libId, @Param("bookId") String bookId);

    /**
     * 查看指定书籍的详细信息。用户借书时，创建借书信息使用。 与 selectBookBelongManager函数不同之处在于： 该接口不用判断管理员是否有操作该书籍的权限。可以少一个表连接
     * 
     * @param bookId
     * @return
     */
    public Book selectBookById(@Param("bookId") String bookId);

    /**
     * 查看指定管理员管理的图书的所有借阅信息
     * 
     * @param returned 指定已归还，或未归还
     * @param managerRtx 管理员rtx
     * @param rowBounds 分页
     * @return 借阅书籍列表
     */
    public List<BorrowedBookInfo> getAllBorrowedBook(@Param("isReturn") Returned returned,
            @Param("managerRtx") String managerRtx, RowBounds rowBounds);

    /**
     * 更新用户信息 使用场合：管理员将用户的书籍列表归还成功后，更新用户表中的已借阅数量和还可以借阅数量
     * 
     * @param userRtx 用户rtx
     * @param borrowNum 已借阅数量
     * @param libId 图书馆id
     * @return 更新影响的行数
     */
    public int updateUserBorrowNumInfo(@Param("userRtx") String userRtx, @Param("borrowNum") int borrowNum,
            @Param("libId") int libId);

    /**
     * 查询被借阅书籍，借阅人
     * 
     * @param bookId
     * @param returned
     * @return
     */
    public String getBorrowedUser(@Param("bookId") String bookId, @Param("isReturned") Returned returned);

    /**
     * 在用户表中插入用户记录
     * 
     * @param user
     * @return
     */
    public int insertUserInfoToUserTable(User user);

    /**
     * 获得管理员管理的图书馆id，（一个管理员只能管理一个图书馆，否则需要返回list）
     * 
     * @param managerRtx 管理员rtx
     * @return 返回管理员管理的图书馆id
     */
    public Integer getManagerLibId(@Param("managerRtx") String managerRtx);

    /**
     * 还书： 将借阅表中的还书字段设置为Returned.ISRETURN。表示用户已经归还该书籍
     * 
     * @param bookId 书籍id
     * @param isReturn 是否归还
     * @return 影响行数，为0时表示没有更新成功
     */
    public int updateBorrowedBookIsReturn(@Param("bookId") String bookId, @Param("isReturn") Returned isReturn);

    /**
     * 查看指定用户的所有借阅信息，包括在每一个图书馆的借阅信息。
     * 
     * @param userRtx 用户rtx 分页
     * @return 返回用户列表。一个图书馆一个列表
     */
    public List<User> getUserBorrowedBookList(@Param("userRtx") String userRtx); // libin.chen个人信息查询

    /**
     * @param userRtx
     * @return
     * @author : libin.chen
     */
    public List<User> getUserBorrowedOtherInfo(@Param("userRtx") String userRtx);

    /**
     * 查询书已经续借的次数
     * 
     * @param bookId 书的条码
     * @return 已经续借的次数
     * @author hang.gao
     */
    Integer selectRedecoratedCount(@Param("book") String bookId);

    /**
     * 将书的可借阅次数减小1
     * 
     * @param bookId 书的条码
     * @param newReturnDate 新的还书时间
     * @return 受影响的行数
     * @author hang.gao
     */
    int increaseRedecorateCount(@Param("book") String bookId, @Param("returnDate") java.sql.Date newReturnDate);

    /**
     * 查询应还书时间
     * 
     * @param bookId 书的id
     * @return 应还书的时候
     * @author hang.gao
     */
    java.sql.Date selectBorrowedBookReturnDate(@Param("book") String bookId);

    /**
     * 统计管理员所管理的图书馆中已经借阅的书籍总数。 使用场合： 1.管理员查看借阅时，分页查询用到该函数 2.删除图书馆时，判断图书馆内是否有借阅书籍
     * 
     * @param managerRtx
     * @return
     */
    public int fetchBorrowedCountOfLib(@Param("managerRtx") String managerRtx, @Param("libId") Integer libId);
}
