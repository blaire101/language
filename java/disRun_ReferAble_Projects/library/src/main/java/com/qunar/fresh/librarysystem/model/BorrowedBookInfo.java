package com.qunar.fresh.librarysystem.model;

import java.util.Date;

import com.qunar.fresh.librarysystem.model.enums.Returned;

/**
 * Created with IntelliJ IDEA. User: feiyan.shan Date: 14-3-28 Time: 下午12:32 To change this template use File | Settings
 * | File Templates.
 */
public final class BorrowedBookInfo {

    private String bookId;

    private String bookLib;

    private String userRtx;

    private String bookName;

    private String bookAuthor;

    private String bookPress;

    private String imageUrl;

    public BorrowedBookInfo() {
        this.userRtx = "";
        this.bookPress = "";
        this.imageUrl = "";
    }

    private Date borrowedDate;

    private Date returnDate;

    private int redecorateNum;

    Returned isReturned;

    public void setBookInfo(Book book) {
        this.bookId = book.getBookId();
        this.bookName = book.getBookName();
        this.bookAuthor = book.getBookAuthor();
        this.bookPress = book.getBookPress();
        this.imageUrl = book.getImageUrl();
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getUserRtx() {
        return userRtx;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookPress() {
        return bookPress;
    }

    public void setBookPress(String bookPress) {
        this.bookPress = bookPress;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getBorrowedDate() {
        return borrowedDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public int getRedecorateNum() {
        return redecorateNum;
    }

    public Returned getReturned() {
        return isReturned;
    }

    public void setUserRtx(String userRtx) {
        this.userRtx = userRtx;
    }

    public void setBorrowedDate(Date borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public String getBookName() {
        return bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setRedecorateNum(int redecorateNum) {
        this.redecorateNum = redecorateNum;
    }

    public void setReturned(Returned returned) {
        isReturned = returned;
    }

    public String getBookLib() {
        return bookLib;
    }

    public void setBookLib(String bookLib) {
        this.bookLib = bookLib;
    }

}
