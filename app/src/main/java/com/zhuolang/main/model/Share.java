package com.zhuolang.main.model;

import android.widget.ImageView;

/**
 * Created by Administrator on 2016/11/28.
 */
public class Share {
    private String shareId;
    private String userId;
    private String userName;
    private String shareTime;
    private String shareLikes;
    private String shareContent;
    private boolean imageShareLike;

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getShareTime() {
        return shareTime;
    }

    public void setShareTime(String shareTime) {
        this.shareTime = shareTime;
    }

    public String getShareLikes() {
        return shareLikes;
    }

    public void setShareLikes(String shareLikes) {
        this.shareLikes = shareLikes;
    }

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public boolean isImageShareLike() {
        return imageShareLike;
    }

    public void setImageShareLike(boolean imageShareLike) {
        this.imageShareLike = imageShareLike;
    }

    @Override
    public String toString() {
        return "Share{" +
                "shareId='" + shareId + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", shareTime='" + shareTime + '\'' +
                ", shareLikes='" + shareLikes + '\'' +
                ", shareContent='" + shareContent + '\'' +
                ", imageShareLike=" + imageShareLike +
                '}';
    }
}
