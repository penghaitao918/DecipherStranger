package com.android.decipherstranger.util;

import android.graphics.Bitmap;

/**
 *      へ　　　　　／|
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
 * @Date 2015/4/13 11:56
 * @e-mail 785351408@qq.com
 */
public class MyStatic {
    //  设置全局静态常量

    public static final int LOAD = 0;
    public static final int REFRESH = 1;

    public static final String FILENAME_USER = "UserInfo";
    public static final String USER_MOVE = "Move";
    public static final String USER_MUSIC = "Music";
    public static final String USER_LOGIN = "Login";
    public static final String USER_ACCOUNT = "Account";
    public static final String USER_PASSWORD = "Password";
    public static final String USER_NAME = "Name";
    public static final String USER_PORTRAIT = "Portrait";
    public static final String USER_SEX = "sex";
    public static final String USER_EMAIL = "Email";
    public static final String USER_PHONE = "Phone";
    public static final String USER_BIRTH = "Birth";
    public static final String USER_SIGNATURE = "signature";

    public static final String CONVERSATION_BOARD = "com.android.conversation.board";
    public static final String USER_BOARD = "com.android.user.board";
    public static final String LIFE_MAIN = "com.android.life.main";
    public static final String LIFE_SEND = "com.android.life.send";
    public static final String LIFE_PARTAKE = "com.android.life.partake";
    public static final String LIFE_DETAILS = "com.android.life.details";
    public static final String LIFE_SHARE = "com.android.life.share";
    public static final String LIFE_SHARE_DO = "com.android.life.share.do";
    public static final String LIFE_MY_LIFE = "com.android.life.myLife";
    public static final String LIFE_LIFE_FRIENDS = "com.android.life.friends";
    
    public static final String CONVERSATION_TYPE = "Type";
    public static final String CONVERSATION_ACCOUNT = "conversationAccount";
    public static final String CONVERSATION_PORTRAIT = "conversationPortrait";
    public static final String CONVERSATION_NAME = "conversationName";
    public static final String CONVERSATION_MESSAGE = "conversationMessage";
    public static final String CONVERSATION_TIME = "conversationTime";
    public static final String CONVERSATION_COUNT = "conversationCount";
    public static final String CONVERSATION_IMAGE = "conversationCountImage";

    public static final int INVITATION_START = 0;
    public static final int INVITATION_SUCCESS = 1;
    public static final int INVITATION_FAIL = 2;
    public static final int INVITATION_SEND = 3;
    
    public static final String FILENAME_SETTINGS = "Settings";
    public static final String KEY_BG = "gameBgMusic";
    public static final String KEY_EFFECT = "gameEffectMusic";
    public static final String resultTrue = "true";
    public static final String resultFalse = "false";

    public static final String ADVERTISEMENT = "http://baike.baidu.com/link?url=n4iB9Al0XQ5gnugKpgKXL8DlWrQq1-lwCcq3XokHZPcGf6C_jaCQK9VUpbUQ9oHA8Zohw8xM6Jl9M8zWo4_JtVwz13ReJzgoErjjtxw_ehq";

    public static final String LIFE_ID = "lifeId";
    public static final String LIFE_CLASSINT = "lifeType";
    public static final String LIFE_DISTANCE = "distance";
    public static final String LIFE_FAVORITE = "favorite";
    public static final String LIFE_CLASS = "lifeClass";
    public static final String LIFE_NAME = "lifeName";
    public static final String LIFE_TIME = "lifeTime";
    public static final String LIFE_SPACE = "lifeSpace";
    public static final String LIFE_PASSWORD = "LifePassword";
    public static final String SHARE_ID = "shareId";
    public static final String SHARE_ACCOUNT = "shareAccount";
    public static final String SHARE_PORTRAIT = "sharePortrait";
    public static final String SHARE_NAME = "shareName";
    public static final String SHARE_MESSAGE = "shareMessage";
    public static final String SHARE_PHOTO = "sharePhoto";
    public static final String SHARE_TIME = "shareTime";
    public static final String SHARE_NUM = "shareNum";
    public static final String SHARE_SEX = "shareSex";
    public static final String SHARE_NUM_BTN = "numButton";
    public static final String SHARE_FRI_BTN = "friButton";
    public static final String DETAILS_FLAG = "detailsFlag";

    //  设置全局静态变量
    public static String friendAccount = null;          //  所添加好友account
    public static String friendName = null;             //  所添加好友昵称
    public static Bitmap friendPhoto = null;
    public static String friendSex = null;

    public static boolean gameEffectMusicFlag = true;         //  游戏音效控制标志
    public static boolean gameBackgroundMusicFlag = true;     //  游戏音乐控制标志

    public static int rockInt = 10;            //  当前用户出石头的几率
    public static int scissorsInt = 10;        //  当前用户出剪刀的几率
    public static int paperInt = 10;           //  当前用户出布的几率

    public static int sharePosition = -1;
}
