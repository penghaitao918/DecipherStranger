package com.android.decipherstranger.activity.LifeActivity;

import android.content.Intent;
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
    private LinearLayout layout = null;
    private MyApplication application = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_details);
        application = (MyApplication) getApplication();
        this.init();
    }

    private void init() {
        /*
         * 判断该活动的发起人与当前账号是否一致，如果一致，则隐藏最下方的两个按钮
         */
        this.layout = (LinearLayout) super.findViewById(R.id.flag);
//        if (this.account.equals(application.getAccount())) {
//            this.layout.setVisibility(View.GONE);
//        }
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
}
