package com.android.decipherstranger.entity;

/**
 * へ　　　　　／|
 * 　　/＼7　　　 ∠＿/
 * 　 /　│　　 ／　／
 * 　│　Z ＿,＜　／　　 /`ヽ
 * 　│　　　　　ヽ　　 /　　〉
 * 　 Y　　　　　`　 /　　/
 * 　ｲ●　､　●　　⊂⊃〈　　/
 * 　()　 へ　　　　|　＼〈
 * 　　>ｰ ､_　 ィ　 │ ／／      去吧！
 * 　 / へ　　 /　ﾉ＜| ＼＼        比卡丘~
 * 　 ヽ_ﾉ　　(_／　 │／／           消灭代码BUG
 * 　　7　　　　　　　|／
 * 　　＞―r￣￣`ｰ―＿
 *
 * @author penghaitao
 * @version V1.1
 * @Date 2015/7/31 11:23
 * @e-mail penghaitao918@163.com
 */
public class LifeInfo {

    private String account = null;
    private String lifeName = null;
    private int lifeType = 0;
    private String lifeDate = null;
    private String lifeSpace = null;
    private int lifePeople = 0;
    private String endTime = null;
    private String rallySpace = null;
    private String rallyTime = null;
    private String password = null;
    private double latitude = 0;
    private double longtitude = 0;

    public LifeInfo() { }
    public LifeInfo(LifeInfo info) {
        this.account = info.account;
        this.lifeName = info.lifeName;
        this.lifeType = info.lifeType;
        this.lifeDate = info.lifeDate;
        this.lifeSpace = info.lifeSpace;
        this.lifePeople = info.lifePeople;
        this.endTime = info.endTime;
        this.rallySpace = info.rallySpace;
        this.rallyTime = info.rallyTime;
        this.password = info.password;
    }

    public String getLifeName() {
        return lifeName;
    }

    public void setLifeName(String lifeName) {
        this.lifeName = lifeName;
    }

    public int getLifeType() {
        return lifeType;
    }

    public void setLifeType(int lifeType) {
        this.lifeType = lifeType;
    }

    public String getLifeDate() {
        return lifeDate;
    }

    public void setLifeDate(String lifeDate) {
        this.lifeDate = lifeDate;
    }

    public String getLifeSpace() {
        return lifeSpace;
    }

    public void setLifeSpace(String lifeSpace) {
        this.lifeSpace = lifeSpace;
    }

    public int getLifePeople() {
        return lifePeople;
    }

    public void setLifePeople(int lifePeople) {
        this.lifePeople = lifePeople;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRallySpace() {
        return rallySpace;
    }

    public void setRallySpace(String rallySpace) {
        this.rallySpace = rallySpace;
    }

    public String getRallyTime() {
        return rallyTime;
    }

    public void setRallyTime(String rallyTime) {
        this.rallyTime = rallyTime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }
}
