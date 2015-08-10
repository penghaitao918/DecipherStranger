package com.android.decipherstranger.activity.LifeActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.decipherstranger.Network.NetworkService;
import com.android.decipherstranger.R;
import com.android.decipherstranger.activity.Base.BaseActivity;
import com.android.decipherstranger.activity.Base.MyApplication;
import com.android.decipherstranger.activity.GameOneActivity.WelcomeRspActivity;
import com.android.decipherstranger.util.ChangeUtils;
import com.android.decipherstranger.util.GlobalMsgUtils;
import com.android.decipherstranger.util.MyStatic;

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
 * @Date 2015/7/28 13:39
 * @e-mail 785351408@qq.com
 */
public class DetailsActivity extends BaseActivity {

    private String myAccount = null;
    private String sendAccount = null;
    private String sendName = null;
    private Bitmap sendPortrait = null;
    private String sendSex = null;

    private String password = null;
    private int lifeId = 0;
    private int lifeClass = 0;
    private int number = 0;
    private Intent intent = null;
    private MyApplication application = null;
    private LifeDetailsBroadcastReceiver receiver = null;

    private RelativeLayout passwordLayout = null;
    private LinearLayout buttonLayout = null;
    private TextView textView1 = null;
    private TextView textView2 = null;
    private TextView textView3 = null;
    private TextView textView4 = null;
    private TextView textView5 = null;
    private TextView textView6 = null;
    private TextView textView7 = null;
    private TextView textView8 = null;
    private TextView textView9 = null;
    private TextView textView0 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_details);
        application = MyApplication.getInstance();
        this.init();
        this.LifeDetailsBroadcast();
        this.getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.unregisterReceiver(DetailsActivity.this.receiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {// 防止连续两次返回键
            //返回处理
            sendPortrait.recycle();
            onBackPressed();
            DetailsActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void init() {
        this.intent = getIntent();
        this.passwordLayout = (RelativeLayout) super.findViewById(R.id.passwordFlag);
        this.buttonLayout = (LinearLayout) super.findViewById(R.id.buttonFlag);
        this.textView1 = (TextView) super.findViewById(R.id.textView11);
        this.textView2 = (TextView) super.findViewById(R.id.textView22);
        this.textView3 = (TextView) super.findViewById(R.id.textView33);
        this.textView4 = (TextView) super.findViewById(R.id.textView44);
        this.textView5 = (TextView) super.findViewById(R.id.textView55);
        this.textView6 = (TextView) super.findViewById(R.id.textView66);
        this.textView7 = (TextView) super.findViewById(R.id.textView77);
        this.textView8 = (TextView) super.findViewById(R.id.textView88);
        this.textView9 = (TextView) super.findViewById(R.id.textView99);
    }

    /*
    * 判断该活动的发起人与当前账号是否一致，如果一致，则隐藏最下方的两个按钮
    */
    private void initButtonLayout() {
        if (myAccount.equals(sendAccount)) {
            buttonLayout.setVisibility(View.GONE);
        } else {
            buttonLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initPasswordLayout() {
        boolean flag = intent.getBooleanExtra(MyStatic.DETAILS_FLAG, false);
        if (flag) {
            passwordLayout.setVisibility(View.VISIBLE);
            buttonLayout.setVisibility(View.GONE);
            textView0 = (TextView) super.findViewById(R.id.textView00);
            textView0.setText(password);
        } else {
            this.passwordLayout.setVisibility(View.GONE);
            buttonLayout.setVisibility(View.VISIBLE);
        }
    }

    private void getData() {
        this.myAccount = application.getAccount();
        this.lifeId = intent.getIntExtra(MyStatic.LIFE_ID, 0);
        this.lifeClass = intent.getIntExtra(MyStatic.LIFE_CLASSINT, 0);
        System.out.println("### " + lifeId);
        if (NetworkService.getInstance().getIsConnected()) {
            String Msg = "type" + ":" + GlobalMsgUtils.msgDetialsActivity + ":" +
                    "activityId" + ":" + lifeId + ":" + "activityType" + ":" + lifeClass +
                    ":" + "currentAccount" + ":" + myAccount;
            Log.v("aaaaa", Msg);
            NetworkService.getInstance().sendUpload(Msg);
        } else {
            NetworkService.getInstance().closeConnection();
            Log.v("Login", "已经执行T（）方法");
        }
    }

    public void LifeDetailsOnClick(View view) {
        switch (view.getId()) {
            /*返回*/
            case R.id.life_back_button:
                onBackPressed();
                break;
            case R.id.lifeFriends:
                Intent intent0 = new Intent(DetailsActivity.this, LifeFriendsActivity.class);
                intent0.putExtra(MyStatic.LIFE_ID, lifeId);
                startActivity(intent0);
                break;
            /*参团*/
            case R.id.partake:
                if (NetworkService.getInstance().getIsConnected()) {
                    String Msg = "type" + ":" + GlobalMsgUtils.msgAttendActivity + ":" + "activityId" + ":" +
                            lifeId + ":" + "account" + ":" + myAccount;
                    Log.v("aaaaa", Msg);
                    NetworkService.getInstance().sendUpload(Msg);
                } else {
                    NetworkService.getInstance().closeConnection();
                    Log.v("Attend", "已执行T()方法");
                }
                break;
            /*加为好友*/
            case R.id.addFriend:
                Intent intent = new Intent(DetailsActivity.this, WelcomeRspActivity.class);
                intent.putExtra("Type", "AddFriend");
                intent.putExtra("Account", sendAccount);
                intent.putExtra("Name", sendName);
                intent.putExtra("Photo", sendPortrait);
                intent.putExtra("Sex", sendSex);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void setData(String name, String typeName, String date, String place, int people, String endTime, String setPlace, String setTime) {
        textView1.setText(name);
        textView2.setText(typeName);
        textView3.setText(date);
        textView4.setText(place);
        textView5.setText(people + "");
        textView6.setText(endTime);
        textView7.setText(setPlace);
        textView8.setText(setTime);
        textView9.setText(number + "");
        initButtonLayout();
        initPasswordLayout();
    }


    private void LifeDetailsBroadcast() {
        //动态方式注册广播接收者
        this.receiver = new LifeDetailsBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyStatic.LIFE_DETAILS);
        this.registerReceiver(receiver, filter);
    }

    public class LifeDetailsBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MyStatic.LIFE_DETAILS)) {
                // TODO 将获取的数据赋值到本地
                if (intent.getStringExtra("reResult").equals("true")) {
                    if (intent.getStringExtra("re_matter").equals("details")) {
                        String name = intent.getStringExtra("re_name");
                        String time = intent.getStringExtra("re_time");
                        String place = intent.getStringExtra("re_place");
                        int people = intent.getIntExtra("re_number", 0);
                        String endTime = intent.getStringExtra("re_end_time");
                        String setPlace = intent.getStringExtra("re_set_place");
                        String setTime = intent.getStringExtra("re_set_time");
                        String activityTypeName = intent.getStringExtra("activity_type_name");
                        sendName = intent.getStringExtra("re_userName");
                        sendPortrait = ChangeUtils.toBitmap(intent.getStringExtra("re_userPhoto"));
                        sendSex = intent.getIntExtra("re_gender", 0) + "";
                        number = intent.getIntExtra("re_current_number", 0);
                        sendAccount = intent.getStringExtra("re_send_account");
                        password = intent.getStringExtra("re_activity_password");
                        setData(name, activityTypeName, time, place, people, endTime, setPlace, setTime);
                    } else {
                        //弹窗显示活动口令
                        //  报名人数+1
                        number++;
                        textView9.setText(number + "");
                        Intent intent0 = new Intent(DetailsActivity.this, PasswordActivity.class);
                        intent0.putExtra(MyStatic.LIFE_PASSWORD, password);
                        System.out.println("### password " + password);
                        startActivity(intent0);
                    }
                } else if (intent.getStringExtra("reResult").equals("full")) {
                    Toast.makeText(DetailsActivity.this, "该活动报名人数已达到上限，请选择其他活动", Toast.LENGTH_SHORT).show();
                } else {
                    if (intent.getStringExtra("re_matter").equals("details")) {
                        //活动详情数据获取失败
                        Toast.makeText(DetailsActivity.this, "服务器连接失败~(≧▽≦)~啦啦啦", Toast.LENGTH_SHORT).show();
                    } else {
                        //弹窗显示已参加过此活动
                        Toast.makeText(DetailsActivity.this, "您已报名，无需重复报名", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}
