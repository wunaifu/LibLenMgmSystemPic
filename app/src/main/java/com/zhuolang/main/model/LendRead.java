package com.zhuolang.main.model;

/**
 * Created by Administrator on 2016/11/25.
 */
public class LendRead {

    private String lendId;
    private String userId;
    private String bookId;
    private String loadTime;
    private String returnTime;
    private String number;
    private String days;

    public String getLendId() {
        return lendId;
    }

    public void setLendId(String lendId) {
        this.lendId = lendId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(String loadTime) {
        this.loadTime = loadTime;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    @Override
    public String toString() {
        return "LendRead{" +
                "lendId='" + lendId + '\'' +
                ", userId='" + userId + '\'' +
                ", bookId='" + bookId + '\'' +
                ", loadTime='" + loadTime + '\'' +
                ", returnTime='" + returnTime + '\'' +
                ", number='" + number + '\'' +
                ", days='" + days + '\'' +
                '}';
    }
}
