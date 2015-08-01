package com.android.decipherstranger.activity.LifeActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.android.decipherstranger.R;
import com.android.decipherstranger.activity.Base.BaseActivity;
import com.android.decipherstranger.activity.Base.MyApplication;
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

    private String account = null;
    private Intent intent = null;
    private LinearLayout layout = null;
    private MyApplication application = null;
    private LifeDetailsBroadcastReceiver receiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_details);
        application = (MyApplication) getApplication();
    //    this.init();
        this.LifeDetailsBroadcast();
        this.getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.unregisterReceiver(DetailsActivity.this.receiver);
    }

    private void init() {
        this.intent = getIntent();
        /*
         * 判断该活动的发起人与当前账号是否一致，如果一致，则隐藏最下方的两个按钮
         */
        this.layout = (LinearLayout) super.findViewById(R.id.flag);
//        if (this.account.equals(application.getAccount())) {
//            this.layout.setVisibility(View.GONE);
//        }
    }

    private void getData() {
        //  send type
        int type = intent.getIntExtra(MyStatic.LIFE_ID, 0);
        int lifeClass = intent.getIntExtra(MyStatic.LIFE_CLASS, 0);
        System.out.println("### " + type);
    }

    public void LifeDetailsOnClick(View view) {
        switch (view.getId()) {
            /*返回*/
            case R.id.life_back_button:
                onBackPressed();
                break;
            /*参团*/
            case R.id.partake:
                /*
                 * 检查是否已经报名
                 * 若未报名，则将申请人信息加入团队并跳入口令界面
                 * 否则跳出已报名提示
                 */
                //  发送请求，在广播执行跳转
                Intent intent = new Intent(this,PasswordActivity.class);
                intent.putExtra(MyStatic.LIFE_PASSWORD,"233333");
                startActivity(intent);
            //    Toast.makeText(this,"您已报名，无需重复报名",Toast.LENGTH_SHORT).show();
                break;
            /*加为好友*/
            case R.id.addFriend:
                break;
        }
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
                System.out.println("### AAA");
                if (intent.getBooleanExtra("reResult", true)){
                //    Intent it = new Intent(SendActivity.this,MainActivity.class);
                //    startActivity(it);
                }
            }
        }
    }
}
