package com.zhuolang.main.model;

/**
 * Created by Administrator on 2016/11/25.
 */
public class NowLend {

    private Book book;
    private LendRead lendRead;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LendRead getLendRead() {
        return lendRead;
    }

    public void setLendRead(LendRead lendRead) {
        this.lendRead = lendRead;
    }

    @Override
    public String toString() {
        return "NowLend{" +
                "book=" + book +
                ", lendRead=" + lendRead +
                '}';
    }
}
