package com.zhuolang.main.model;

/**
 * Created by Administrator on 2016/11/25.
 */
public class Book {

    private String lendId;
    private String bookId;
    private String bookName;
    private String bookType;
    private String bookAuthor;
    private String bookPublisher;
    private String bookPublyear;
    private String bookPrice;
    private String bookAddress;
    private String bookNumber;
    private String bookLoanable;
    private String bookConten;

    public String getLendId() {
        return lendId;
    }

    public void setLendId(String lendId) {
        this.lendId = lendId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookPublisher() {
        return bookPublisher;
    }

    public void setBookPublisher(String bookPublisher) {
        this.bookPublisher = bookPublisher;
    }

    public String getBookPublyear() {
        return bookPublyear;
    }

    public void setBookPublyear(String bookPublyear) {
        this.bookPublyear = bookPublyear;
    }

    public String getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(String bookPrice) {
        this.bookPrice = bookPrice;
    }

    public String getBookAddress() {
        return bookAddress;
    }

    public void setBookAddress(String bookAddress) {
        this.bookAddress = bookAddress;
    }

    public String getBookNumber() {
        return bookNumber;
    }

    public void setBookNumber(String bookNumber) {
        this.bookNumber = bookNumber;
    }

    public String getBookLoanable() {
        return bookLoanable;
    }

    public void setBookLoanable(String bookLoanable) {
        this.bookLoanable = bookLoanable;
    }

    public String getBookConten() {
        return bookConten;
    }

    public void setBookConten(String bookConten) {
        this.bookConten = bookConten;
    }

    @Override
    public String toString() {
        return "Book{" +
                "lendId='" + lendId + '\'' +
                ", bookId='" + bookId + '\'' +
                ", bookName='" + bookName + '\'' +
                ", bookType='" + bookType + '\'' +
                ", bookAuthor='" + bookAuthor + '\'' +
                ", bookPublisher='" + bookPublisher + '\'' +
                ", bookPublyear='" + bookPublyear + '\'' +
                ", bookPrice='" + bookPrice + '\'' +
                ", bookAddress='" + bookAddress + '\'' +
                ", bookNumber='" + bookNumber + '\'' +
                ", bookLoanable='" + bookLoanable + '\'' +
                ", bookConten='" + bookConten + '\'' +
                '}';
    }
}
