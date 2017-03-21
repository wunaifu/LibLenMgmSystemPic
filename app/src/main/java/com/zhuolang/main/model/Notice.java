package com.zhuolang.main.model;

/**
 * Created by Administrator on 2016/11/27.
 */
public class Notice {
    private String noticeId;
    private String noticeTitle;
    private String noticeTime;
    private String noticeContent;

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(String noticeTime) {
        this.noticeTime = noticeTime;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    @Override
    public String toString() {
        return "Notice{" +
                "noticeId='" + noticeId + '\'' +
                ", noticeTitle='" + noticeTitle + '\'' +
                ", noticeTime='" + noticeTime + '\'' +
                ", noticeContent='" + noticeContent + '\'' +
                '}';
    }
}
