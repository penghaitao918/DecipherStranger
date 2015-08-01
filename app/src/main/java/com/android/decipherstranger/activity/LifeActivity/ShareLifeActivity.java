package com.android.decipherstranger.activity.LifeActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.decipherstranger.R;
import com.android.decipherstranger.activity.Base.BaseActivity;
import com.android.decipherstranger.activity.Base.MyApplication;
import com.android.decipherstranger.util.ChangeUtils;
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
 * @Date 2015/7/30 12:13
 * @e-mail penghaitao918@163.com
 */
public class ShareLifeActivity extends BaseActivity {

    private EditText editText = null;
    private ImageButton imageButton = null;
    private MyApplication application = null;
    private ShareLifeBroadcastReceiver receiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_life_share_do);
        this.init();
        this.ShareLifeBroadcas();
    }

    private void init() {
        application = (MyApplication) getApplication();
        this.editText = (EditText) super.findViewById(R.id.editText);
        this.imageButton = (ImageButton) super.findViewById(R.id.imageButton);
    }

    private void send() {
        String account = application.getAccount();
        String message = editText.getText().toString();
        Bitmap bitmap = imageButton.getDrawingCache();
    }

    public void SendShareOnclick(View view) {
        switch (view.getId()) {
            case R.id.sendShare_back_button:
                onBackPressed();
                break;
            case R.id.send_share:
                break;
            case R.id.imageButton:
                break;
        }
    }

    private void ShareLifeBroadcas() {
        //动态方式注册广播接收者
        IntentFilter filter = new IntentFilter();
        this.receiver = new ShareLifeBroadcastReceiver();
        filter.addAction(MyStatic.LIFE_SHARE_DO);
        this.registerReceiver(receiver, filter);
    }

    public class ShareLifeBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MyStatic.LIFE_SHARE_DO)) {
                if (intent.getBooleanExtra("reResult", true)){

                }else{

                }
            }
        }
    }

}
