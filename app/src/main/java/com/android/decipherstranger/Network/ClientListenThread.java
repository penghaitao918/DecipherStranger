package com.android.decipherstranger.Network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.decipherstranger.activity.Base.MyApplication;
import com.android.decipherstranger.util.ChangeUtils;
import com.android.decipherstranger.util.GlobalMsgUtils;
import com.android.decipherstranger.util.MyStatic;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by Feng on 2015/04/11.
 */
public class ClientListenThread extends Thread {
    private Context clContext;
    private Socket clSocket;

    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;

    private MyApplication application = null;

    public ClientListenThread(Context context, Socket s) {
        this.clContext = context;
        this.clSocket = s;
        this.application = MyApplication.getInstance();
    }

    @Override
    public void destroy() {
        super.destroy();
        System.out.println("### 服务器进程被关闭了");
    }

    public void run() {
        super.run();
        try {
            inputStreamReader = new InputStreamReader(clSocket.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            String reMsg = new String();
            String test = new String();
            while (true) {
                System.out.println("### 服务器线程");
                while (true) {
                    test = bufferedReader.readLine();
                    if (test.contains("+++++")) {
                        reMsg += test.substring(0, test.length() - 5);
                        break;
                    } else if (test != null) {
                        reMsg += test;
                    } else {
                        break;
                    }
                }
                if (reMsg != null) {
                    Log.v("### AAA", reMsg);
                    JSONObject jsonObj = new JSONObject(reMsg);
                    Log.v(" ### 能不能接到", reMsg);
                    int msgType = jsonObj.getInt("re_type");            // type of message received
                    System.out.println("### ++++++++++++这是一条消息" + msgType);
                    switch (msgType) {
                        case GlobalMsgUtils.msgLogin:
                            Intent itLogin = new Intent("com.android.decipherstranger.LOGIN");
                            itLogin.putExtra("result", jsonObj.getString("re_message"));
                            if (jsonObj.getString("re_message").equals(MyStatic.resultTrue)) {
                                application.setName(jsonObj.getString("re_name"));
                                application.setPortrait(ChangeUtils.toBitmap(jsonObj.getString("re_photo")));
                                if (jsonObj.getInt("re_gender") == 1) {
                                    application.setSex("男");
                                } else {
                                    application.setSex("女");
                                }
                                application.setPhone(jsonObj.getString("re_phone"));
                                application.setEmail(jsonObj.getString("re_email"));
                                application.setBirth(jsonObj.getString("re_birth"));
                            }
                            clContext.sendBroadcast(itLogin);
                            break;
                        case GlobalMsgUtils.msgRegister:
                            Intent itRegister = new Intent("com.android.decipherstranger.REGISTER");
                            itRegister.putExtra("result", jsonObj.getString("re_message"));
                            clContext.sendBroadcast(itRegister);
                            break;
                        case GlobalMsgUtils.msgMessage:
                            Intent itMessage = new Intent("com.android.decipherstranger.MESSAGE");
                            itMessage.putExtra("reMessage", jsonObj.getString("re_message"));
                            itMessage.putExtra("reSender", jsonObj.getString("re_sender"));
                            itMessage.putExtra("reDate", jsonObj.getString("re_date"));
                            itMessage.putExtra("msgType", 0);
                            clContext.sendBroadcast(itMessage);
                            System.out.println("发送成功1");
                            break;
                        case GlobalMsgUtils.msgShake:
                            Intent itShake = new Intent("com.android.decipherstranger.SHAKE");
                            if (jsonObj.getString("re_message").equals(MyStatic.resultFalse)) {
                                itShake.putExtra("reResult", false);
                            } else {
                                itShake.putExtra("reResult", true);
                                itShake.putExtra("reAccount", jsonObj.getString("re_account"));
                                itShake.putExtra("rePhoto", jsonObj.getString("re_photo"));
                                itShake.putExtra("reGender", jsonObj.getInt("re_gender"));
                                itShake.putExtra("reName", jsonObj.getString("re_name"));
                            }
                            clContext.sendBroadcast(itShake);
                            break;
                        case GlobalMsgUtils.msgFriendList:
                            Intent itFriend = new Intent("com.android.decipherstranger.FRIEND");
                            if (jsonObj.getString("re_message").equals(MyStatic.resultFalse)) {
                                itFriend.putExtra("reResult", false);
                                itFriend.putExtra("isfinsh", false);
                            } else if (jsonObj.getString("re_message").equals(MyStatic.resultTrue)) {
                                itFriend.putExtra("reResult", true);
                                itFriend.putExtra("reAccount", jsonObj.getString("re_account"));
                                itFriend.putExtra("reName", jsonObj.getString("re_name"));
                                itFriend.putExtra("reGender", jsonObj.getString("re_gender"));
                                itFriend.putExtra("rePhoto", jsonObj.getString("re_photo"));
                            } else {
                                itFriend.putExtra("reResult", false);
                                itFriend.putExtra("isfinish", true);
                            }
                            clContext.sendBroadcast(itFriend);
                            Log.v("Test", "发送啦！！！");
                            break;
                        case GlobalMsgUtils.msgGameOneReceive:
                            Intent itGameRec = new Intent("com.android.decipherstranger.GAMEONE");
                            itGameRec.putExtra("reGrade", jsonObj.getInt("re_grade"));
                            itGameRec.putExtra("reSum", jsonObj.getInt("re_sum"));
                            itGameRec.putExtra("reRock", jsonObj.getInt("re_rock"));
                            itGameRec.putExtra("reScissors", jsonObj.getInt("re_scissors"));
                            itGameRec.putExtra("rePaper", jsonObj.getInt("re_paper"));
                            clContext.sendBroadcast(itGameRec);
                            break;
                        case GlobalMsgUtils.msgGameOneSend:
                            break;
                        case GlobalMsgUtils.msgAddFriend:
                            Intent itAddFriend = new Intent("com.android.decipherstranger.MESSAGE");
                            if (jsonObj.getString("re_message").equals(MyStatic.resultTrue)) {
                                itAddFriend.putExtra("Friend", "Friend");
                                itAddFriend.putExtra("reAccount", jsonObj.getString("re_account"));
                                itAddFriend.putExtra("rePhoto", jsonObj.getString("re_photo"));
                                itAddFriend.putExtra("reGender", jsonObj.getInt("re_gender"));
                                itAddFriend.putExtra("reName", jsonObj.getString("re_name"));
                                itAddFriend.putExtra("reResult", true);
                            } else {
                                itAddFriend.putExtra("Friend", "Friend");
                                itAddFriend.putExtra("reResult", false);
                            }
                            clContext.sendBroadcast(itAddFriend);
                            System.out.println("发送成功Add");
                            break;
                        case GlobalMsgUtils.msgVoice:
                            Intent itVoice = new Intent("com.android.decipherstranger.MESSAGE");
                            itVoice.putExtra("reMessage", jsonObj.getString("re_message"));
                            itVoice.putExtra("reSender", jsonObj.getString("re_sender"));
                            itVoice.putExtra("reDate", jsonObj.getString("re_date"));
                            itVoice.putExtra("reTime", jsonObj.getString("re_time"));
                            itVoice.putExtra("msgType", 1);
                            clContext.sendBroadcast(itVoice);
                            System.out.println("发送成功2");
                            break;
                        case GlobalMsgUtils.msgNearBy:
                            System.out.println("daacallll");
                            Intent itNearBy = new Intent("com.android.decipherstranger.NEARBY");
                            if (jsonObj.getString("re_message").equals(MyStatic.resultFalse)) {
                                itNearBy.putExtra("reResult", false);
                                itNearBy.putExtra("isfinsh", false);
                            } else if (jsonObj.getString("re_message").equals(MyStatic.resultTrue)) {
                                itNearBy.putExtra("reResult", true);
                                itNearBy.putExtra("reAccount", jsonObj.getString("re_account"));
                                itNearBy.putExtra("reName", jsonObj.getString("re_name"));
                                itNearBy.putExtra("rePhoto", jsonObj.getString("re_photo"));
                                itNearBy.putExtra("reGender", jsonObj.getInt("re_gender"));
                                itNearBy.putExtra("reLongtitude", jsonObj.getString("re_longtitude"));
                                itNearBy.putExtra("reLatitude", jsonObj.getString("re_latitude"));
                                itNearBy.putExtra("reDistance", jsonObj.getString("re_distance"));
                            } else {
                                itNearBy.putExtra("reResult", false);
                                itNearBy.putExtra("isfinish", true);
                            }
                            clContext.sendBroadcast(itNearBy);
                            Log.v("Test", "发送啦！！！");
                            break;
                        case GlobalMsgUtils.msgChangeInf:
                            Intent itChange = new Intent("com.android.decipherstranger.CHANGE");
                            itChange.putExtra("reResult", true);
                            clContext.sendBroadcast(itChange);
                            break;
                        case GlobalMsgUtils.msgImage:
                            Intent itImage = new Intent("com.android.decipherstranger.MESSAGE");
                            itImage.putExtra("reMessage", jsonObj.getString("re_message"));
                            itImage.putExtra("reSender", jsonObj.getString("re_sender"));
                            itImage.putExtra("reDate", jsonObj.getString("re_date"));
                            itImage.putExtra("msgType", 2);
                            clContext.sendBroadcast(itImage);
                            System.out.println("发送成功3");
                            break;
                        case GlobalMsgUtils.msgDelFri:
                            Intent itDelFri = new Intent("com.android.decipherstranger.MESSAGE");
                            itDelFri.putExtra("Friend", "Friend");
                            itDelFri.putExtra("Del", "Del");
                            itDelFri.putExtra("reAccount", jsonObj.getString("re_message"));
                            clContext.sendBroadcast(itDelFri);
                            break;
                        case GlobalMsgUtils.msgShowFri:
                            Intent itShowFri = new Intent("com.android.decipherstranger.SHOWFRI");
                            itShowFri.putExtra("reEmail", jsonObj.getString("re_email"));
                            itShowFri.putExtra("rePhone", jsonObj.getString("re_phone"));
                            itShowFri.putExtra("reBirth", jsonObj.getString("re_birth"));
                            clContext.sendBroadcast(itShowFri);
                            break;
                        case GlobalMsgUtils.msgOffMsg:
                            if (jsonObj.getString("re_message").equals(MyStatic.resultFalse)) {
                                break;
                            } else {
                                Intent itOffMsg = new Intent("com.android.decipherstranger.MESSAGE");
                                itOffMsg.putExtra("reMessage", jsonObj.getString("re_message"));
                                itOffMsg.putExtra("reSender", jsonObj.getString("re_sender"));
                                itOffMsg.putExtra("reDate", jsonObj.getString("re_date"));
                                if (jsonObj.getInt("re_msgtype") == 2) {
                                    itOffMsg.putExtra("msgType", 2);
                                } else if (jsonObj.getInt("re_msgtype") == 1) {
                                    itOffMsg.putExtra("msgType", 1);
                                    itOffMsg.putExtra("reTime", jsonObj.getString("re_time"));
                                } else {
                                    itOffMsg.putExtra("msgType", 0);
                                }
                                clContext.sendBroadcast(itOffMsg);
                                break;
                            }
                        case GlobalMsgUtils.msgSendInv:
                            break;
                        case GlobalMsgUtils.msgReceiveInv:
                            Intent itReInv = new Intent("com.android.decipherstranger.INVITATION");
                            if (jsonObj.getString("re_message").equals(MyStatic.resultTrue)) {
                                itReInv.putExtra("reResult", true);
                                itReInv.putExtra("reAccount", jsonObj.getString("re_account"));
                                itReInv.putExtra("rePhoto", jsonObj.getString("re_photo"));
                                itReInv.putExtra("reGender", jsonObj.getInt("re_gender"));
                                itReInv.putExtra("reName", jsonObj.getString("re_name"));
                            } else {
                                itReInv.putExtra("reResult", false);
                            }
                            clContext.sendBroadcast(itReInv);
                            break;
                        case GlobalMsgUtils.msgLifeMain:
                            Intent itLife = new Intent("com.android.life.main");
                            if (jsonObj.getString("re_message").equals(MyStatic.resultTrue)) {
                                itLife.putExtra("reResult", true);
                                itLife.putExtra("reId", jsonObj.getInt("re_id"));
                                itLife.putExtra("reType", jsonObj.getInt("re_activity_type"));
                                itLife.putExtra("reName", jsonObj.getString("re_name"));
                                itLife.putExtra("reTime", jsonObj.getString("re_time"));
                                itLife.putExtra("rePlace", jsonObj.getString("re_place"));
                            } else {
                                itLife.putExtra("reResult", false);
                            }
                            clContext.sendBroadcast(itLife);
                            break;
                        case GlobalMsgUtils.msgSendActivity:
                            Intent itSendActivity = new Intent(MyStatic.LIFE_SEND);
                            if (jsonObj.getString("re_message").equals(MyStatic.resultTrue)) {
                                itSendActivity.putExtra("reResult", true);
                            } else {
                                itSendActivity.putExtra("reResult", false);
                            }
                            clContext.sendBroadcast(itSendActivity);
                            break;
                        case GlobalMsgUtils.msgDetialsActivity:
                            Intent itDetials = new Intent(MyStatic.LIFE_DETAILS);
                            itDetials.putExtra("reResult", jsonObj.getString("re_message"));
                            itDetials.putExtra("re_matter", jsonObj.getString("re_matter"));
                            if (jsonObj.getString("re_message").equals(MyStatic.resultTrue)) {
                                if (jsonObj.getString("re_matter").equals("details")) {
                                    itDetials.putExtra("re_name", jsonObj.getString("re_name"));
                                    itDetials.putExtra("re_time", jsonObj.getString("re_time"));
                                    itDetials.putExtra("re_place", jsonObj.getString("re_place"));
                                    itDetials.putExtra("activity_type_name", jsonObj.getString("activity_type_name"));
                                    itDetials.putExtra("re_number", jsonObj.getInt("re_number"));
                                    itDetials.putExtra("re_end_time", jsonObj.getString("re_end_time"));
                                    itDetials.putExtra("re_set_place", jsonObj.getString("re_set_place"));
                                    itDetials.putExtra("re_set_time", jsonObj.getString("re_set_time"));
                                    itDetials.putExtra("re_current_number", jsonObj.getInt("re_current_number"));
                                    itDetials.putExtra("re_send_account", jsonObj.getString("re_send_account"));
                                    itDetials.putExtra("re_activity_password", jsonObj.getString("activity_password"));
                                    itDetials.putExtra("re_userName", jsonObj.getString("re_user_name"));
                                    itDetials.putExtra("re_userPhoto", jsonObj.getString("re_small_photo"));
                                    itDetials.putExtra("re_gender", jsonObj.getInt("re_gender"));
                                }
                            }
                            clContext.sendBroadcast(itDetials);
                            break;
                        case GlobalMsgUtils.msgShowAllActivity:
                            Intent itShowAll = new Intent(MyStatic.LIFE_PARTAKE);
                            System.out.println("### " + jsonObj.getString("re_message"));
                            if (jsonObj.getString("re_message").equals(MyStatic.resultTrue)) {
                                itShowAll.putExtra("reResult", "true");
                                itShowAll.putExtra("reId", jsonObj.getInt("re_id"));
                                itShowAll.putExtra("reName", jsonObj.getString("re_name"));
                                itShowAll.putExtra("rePlace", jsonObj.getString("re_place"));
                                itShowAll.putExtra("reTime", jsonObj.getString("re_time"));
                                itShowAll.putExtra("reType", jsonObj.getInt("re_activity_type"));
                                itShowAll.putExtra("reDistance", jsonObj.getDouble("re_distance"));
                                itShowAll.putExtra("reFavorite", jsonObj.getInt("favorite"));
                            } else if (jsonObj.getString("re_message").equals("finish")) {
                                itShowAll.putExtra("reResult", "finish");
                            } else {
                                itShowAll.putExtra("reResult", "false");
                            }
                            clContext.sendBroadcast(itShowAll);
                            break;
                        case GlobalMsgUtils.msgShareActivity:
                            Intent itShare = new Intent(MyStatic.LIFE_SHARE_DO);
                            if (jsonObj.getString("re_message").equals(MyStatic.resultTrue)) {
                                itShare.putExtra("reResult", true);
                            } else {
                                itShare.putExtra("reResult", false);
                            }
                            clContext.sendBroadcast(itShare);
                            break;
                        case GlobalMsgUtils.msgShowShare:
                            Intent itShowShare = new Intent(MyStatic.LIFE_SHARE);
                            itShowShare.putExtra("reRequestType", jsonObj.getInt("re_requestType"));
                            itShowShare.putExtra("reMatter", "showShare");
                            System.out.println("### 数据广播接收 " + jsonObj.getString("re_message"));
                            if (jsonObj.getString("re_message").equals(MyStatic.resultTrue)) {
                                itShowShare.putExtra("reResult", "true");
                                itShowShare.putExtra("reId", jsonObj.getInt("reId"));
                                itShowShare.putExtra("reAccount", jsonObj.getString("reAccount"));
                                itShowShare.putExtra("reUserPhoto", jsonObj.getString("reUserPhoto"));
                                itShowShare.putExtra("reUserName", jsonObj.getString("reUserName"));
                                itShowShare.putExtra("reSharePhoto", jsonObj.getString("reSharePhoto"));
                                itShowShare.putExtra("reSpeech", jsonObj.getString("reSpeech"));
                                itShowShare.putExtra("reTime", jsonObj.getString("reTime"));
                                itShowShare.putExtra("reZan", jsonObj.getInt("reZan"));
                                itShowShare.putExtra("re_gender", jsonObj.getInt("re_gender"));
                                System.out.println("### 数据广播 成功接收数据 ");
                            } else if (jsonObj.getString("re_message").equals("finish")) {
                                itShowShare.putExtra("reResult", "finish");
                                System.out.println("### 数据广播 接收完毕 ");
                            } else {
                                itShowShare.putExtra("reResult", "false");
                            }
                            System.out.println("### 数据广播 开始发送 ");
                            clContext.sendBroadcast(itShowShare);
                            System.out.println("### data = " + itShowShare);
                            System.out.println("### 数据广播 发送完成 ");
                            break;
                        case GlobalMsgUtils.msgClickZan:
                            Intent itClickZan = new Intent(MyStatic.LIFE_SHARE);
                            itClickZan.putExtra("reMatter", "Zan");
                            if (jsonObj.getString("re_message").equals(MyStatic.resultTrue)) {
                                itClickZan.putExtra("reResult", "true");
                            } else {
                                itClickZan.putExtra("reResult", "false");
                            }
                            clContext.sendBroadcast(itClickZan);
                            break;
                        case GlobalMsgUtils.msgPersonalCenter:
                            Intent itPersonal = new Intent(MyStatic.LIFE_MY_LIFE);
                            itPersonal.putExtra("reMatter", jsonObj.getString("reMatter"));
                            itPersonal.putExtra("reResult", jsonObj.getString("re_message"));
                            if (jsonObj.getString("re_message").equals("true")) {
                                itPersonal.putExtra("reId", jsonObj.getInt("reId"));
                                itPersonal.putExtra("reType", jsonObj.getInt("reType"));
                                itPersonal.putExtra("reName", jsonObj.getString("reName"));
                                itPersonal.putExtra("reTime", jsonObj.getString("reTime"));
                                itPersonal.putExtra("rePlace", jsonObj.getString("rePlace"));
                                itPersonal.putExtra("reNumber", jsonObj.getString("reNumber"));
                            }
                            clContext.sendBroadcast(itPersonal);
                            break;
                        case GlobalMsgUtils.msgActivityPeople:
                            Intent itAttendPeople = new Intent(MyStatic.LIFE_LIFE_FRIENDS);
                            if (jsonObj.getString("re_message").equals(MyStatic.resultTrue)) {
                                itAttendPeople.putExtra("reResult", "true");
                                itAttendPeople.putExtra("reAccount", jsonObj.getString("reAccount"));
                                itAttendPeople.putExtra("reName", jsonObj.getString("reName"));
                                itAttendPeople.putExtra("rePhoto", jsonObj.getString("rePhoto"));
                                itAttendPeople.putExtra("reGender", jsonObj.getInt("reGender"));
                            } else if (jsonObj.getString("re_message").equals("finish")) {
                                itAttendPeople.putExtra("reResult", "finish");
                            } else {
                                itAttendPeople.putExtra("reResult", "false");
                            }
                            clContext.sendBroadcast(itAttendPeople);
                            break;
                        case GlobalMsgUtils.msgRecommend:
                            Intent itRecommend = new Intent("com.android.decipherstranger.RECOMMEND");
                            itRecommend.putExtra("reResult",jsonObj.getString("re_message"));
                            if (jsonObj.getString("re_message").equals(MyStatic.resultTrue)){
                                itRecommend.putExtra("reAccount",jsonObj.getString("reAccount"));
                                itRecommend.putExtra("reName",jsonObj.getString("reName"));
                                itRecommend.putExtra("rePhoto",jsonObj.getString("rePhoto"));
                                itRecommend.putExtra("reGender",jsonObj.getInt("reGender"));
                            }
                            clContext.sendBroadcast(itRecommend);
                            break;
                        default:
                            if (NetworkService.getInstance().getIsConnected()) {
                                String testNet = "type" + ":" + "ping";
                                Log.v("aaaaa", testNet);
                                NetworkService.getInstance().sendUpload(testNet);
                            } else {
                                NetworkService.getInstance().closeConnection();
                                Toast.makeText(clContext, "服务器连接失败~(≧▽≦)~啦啦啦", Toast.LENGTH_SHORT).show();
                            }
                            break;
                    }
                    reMsg = "";
                }
            }
        } catch (Exception e) {
        }
    }

    public void closeBufferedReader() {
        try {
            bufferedReader = null;
        } catch (Exception e) {
        }
    }
}
