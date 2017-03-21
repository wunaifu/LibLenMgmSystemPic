package com.zhuolang.main.model;

/**
 * Created by Administrator on 2016/11/25.
 */
public class User {
    private String userId;
    private String userName;
    private String userAge;
    private String userPhone;
    private String usrClass;
    private String userSex;
    private String userAdress;

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

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUsrClass() {
        return usrClass;
    }

    public void setUsrClass(String usrClass) {
        this.usrClass = usrClass;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserAdress() {
        return userAdress;
    }

    public void setUserAdress(String userAdress) {
        this.userAdress = userAdress;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userAge='" + userAge + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", usrClass='" + usrClass + '\'' +
                ", userSex='" + userSex + '\'' +
                ", userAdress='" + userAdress + '\'' +
                '}';
    }
}
