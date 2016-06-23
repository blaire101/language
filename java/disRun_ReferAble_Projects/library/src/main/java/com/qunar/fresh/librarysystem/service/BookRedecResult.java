package com.qunar.fresh.librarysystem.service;

import java.util.Date;

/**
 * Created with IntelliJ IDEA. User: hang.gao Date: 14-4-9 Time: 下午12:21
 * 
 * @author hang.gao
 */
public class BookRedecResult {
    /**
     * 图书不存在的错误码
     */
    public static final int NO_SUTCH_BOOK_CODE = 1;

    /**
     * 图书不能再续借
     */
    public static final int CANNOT_REDEC_BOOK_CODE = 2;

    /**
     * 图书不能再续借
     */
    public static final int BOOK_NOT_BE_BOWRROWED_CODE = 2;

    /**
     * 借书成功
     */
    public static final int SUCCESS_CODE = 0;
    /**
     * 图书不存在
     */
    public static final BookRedecResult NO_SUTCH_BOOK = new BookRedecResult(null, NO_SUTCH_BOOK_CODE, "没有这本书");

    /**
     * 图书不能被续借
     */
    public static final BookRedecResult CANNOT_REDEC_SUTCH_BOOK = new BookRedecResult(null, CANNOT_REDEC_BOOK_CODE,
            "图书不能被续借");

    /**
     * 图书没有被借出
     */
    public static final BookRedecResult BOOK_NOT_BE_BOWRROWED = new BookRedecResult(null, BOOK_NOT_BE_BOWRROWED_CODE,
            "图书没有借出");

    /**
     * 续借后还书的时间
     */
    private Date returnDate;

    /**
     * 状态码，可以用来表示成功或者失败
     */
    private int code;

    /**
     * 信息
     */
    private String message;

    public BookRedecResult(Date returnDate, int code, String message) {
        this.returnDate = returnDate;
        this.code = code;
        this.message = message;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
