package com.zhuolang.main.model;

/**
 * Created by Administrator on 2016/11/27.
 */
public class UserNowLend {
    private LendRead lendRead;
    private String userName;
    private String bookName;

    public LendRead getLendRead() {
        return lendRead;
    }

    public void setLendRead(LendRead lendRead) {
        this.lendRead = lendRead;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @Override
    public String toString() {
        return "UserNowLend{" +
                "lendRead=" + lendRead +
                ", userName='" + userName + '\'' +
                ", bookName='" + bookName + '\'' +
                '}';
    }
}
