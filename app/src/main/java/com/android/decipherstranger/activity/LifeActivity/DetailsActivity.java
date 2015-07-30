package com.android.decipherstranger.activity.LifeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.decipherstranger.R;
import com.android.decipherstranger.activity.Base.BaseActivity;
import com.android.decipherstranger.util.MyStatic;

/**
 * へ　　　　　／|
 * 　　/＼7　　　 ∠＿/
 * 　 /　│　　 ／　／
 * 　│　Z ＿,＜　／　　 /`ヽ
 * 　│　　　　　ヽ　　 /　　〉
 * 　 Y　　　　　`　 /　　/
 * 　?●　?　●　　??〈　　/
 * 　()　 へ　　　　|　＼〈
 * 　　>? ?_　 ィ　 │ ／／      去吧！
 * 　 / へ　　 /　?＜| ＼＼        比卡丘~
 * 　 ヽ_?　　(_／　 │／／           消灭代码BUG
 * 　　7　　　　　　　|／
 * 　　＞―r￣￣`?―＿
 *
 * @author penghaitao
 * @version V1.0
 * @Date 2015/7/28 13:39
 * @e-mail 785351408@qq.com
 */
public class DetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_details);
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
