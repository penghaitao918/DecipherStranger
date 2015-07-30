package com.android.decipherstranger.activity.LifeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
 * @Date 2015/7/28 15:53
 * @e-mail 785351408@qq.com
 */
public class PasswordActivity extends BaseActivity {

    private TextView textView = null;
    private String lifePassword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.winpop_life_password);
        this.init();
    }

    private void init(){
        Intent intent = getIntent();
        this.lifePassword = intent.getStringExtra(MyStatic.LIFE_PASSWORD);
        this.textView = (TextView) super.findViewById(R.id.lifePassword);
        this.textView.setText(lifePassword);
    }

    public void LifePasswordOnclick(View view) {
        switch (view.getId()) {
            case R.id.blank:
                onBackPressed();
                break;
        }
    }
}
