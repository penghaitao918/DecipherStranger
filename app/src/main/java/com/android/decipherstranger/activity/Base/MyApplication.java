package com.android.decipherstranger.activity.Base;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;

import java.util.LinkedList;
import java.util.List;

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
 * @version V1.0
 * @Date 2015/5/8 16:34
 * @e-mail 785351408@qq.com
 */
public class MyApplication extends Application {

    private static MyApplication instance;
    private List<Activity> activityList = new LinkedList<Activity>();

//    private MyApplication(){}
    /**
     * ****全局变量**********************************************************************************************
     */

    //  用户账号
    private String account = null;
    //  用户昵称
    private String name = null;
    //  用户头像
    private Bitmap portrait = null;
    //  用户性别
    private int sex = 0;
    //  用户邮箱
    private String email = null;
    //  用户电话
    private String phone = null;
    //  用户生日
    private String birth = null;
    //  用户地址
    private String address = null;
    //  个性签名
    private String signature = null;
    //未读消息
    private int unReadMessage;
    private int invSum;
    //  震动标志
    private boolean moveFlag = true;
    //  声效标志
    private boolean musicFlag = true;

    // 单例模式中获取唯一的MyApplication实例
    public static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;
    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    // 遍历所有Activity并finish
    public void exit() {
        for (Activity activity : activityList) {
            System.out.println("tag_activity﹕ Exit" + activity.getClass().getSimpleName());
            activity.finish();
        }
        System.exit(0);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public int getUnReadMessage() {
        return unReadMessage;
    }

    public void setUnReadMessage(int unReadMessage) {
        this.unReadMessage = unReadMessage;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getPortrait() {
        return portrait;
    }

    public void setPortrait(Bitmap portrait) {
        this.portrait = portrait;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int userSex) {
        this.sex = userSex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getInvSum() {
        return invSum;
    }

    public void setInvSum(int invSum) {
        this.invSum = invSum;
    }

    public boolean isMoveFlag() {
        return moveFlag;
    }

    public void setMoveFlag(boolean moveFlag) {
        this.moveFlag = moveFlag;
    }

    public boolean isMusicFlag() {
        return musicFlag;
    }

    public void setMusicFlag(boolean musicFlag) {
        this.musicFlag = musicFlag;
    }

}
