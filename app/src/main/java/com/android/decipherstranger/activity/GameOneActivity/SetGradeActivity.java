package com.android.decipherstranger.activity.GameOneActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.decipherstranger.Network.NetworkService;
import com.android.decipherstranger.R;
import com.android.decipherstranger.activity.Base.BaseActivity;
import com.android.decipherstranger.activity.Base.MyApplication;
import com.android.decipherstranger.util.GlobalMsgUtils;

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
 * @Date 2015/3/20
 * @e-mail 785351408@qq.com
 */
public class SetGradeActivity extends BaseActivity {

    private MyApplication application = null;
    private EditText gradeEdit = null;
    private EditText sumEdit = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_rsp_set_grade);
        application = MyApplication.getInstance();
        this.init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        application = null;
        gradeEdit = null;
        sumEdit = null;
    }

    private void init() {
        this.gradeEdit = (EditText) super.findViewById(R.id.editText1);
        this.sumEdit = (EditText) super.findViewById(R.id.editText2);
        this.gradeEdit.setText("6");
        this.sumEdit.setText("20");
    }

    public void GradeSetOnClick(View view) {
        switch (view.getId()) {
            case R.id.gamelist_back_button:
                onBackPressed();
                break;
            case R.id.save_btn:
                setGradeToWeb();
                onBackPressed();
                break;
        }
    }

    private void setGradeToWeb() {
        int grade = Integer.parseInt(this.gradeEdit.getText().toString());
        int sum = Integer.parseInt(this.sumEdit.getText().toString());
        if (NetworkService.getInstance().getIsConnected()) {
            String gameUser = "type" + ":" + Integer.toString(GlobalMsgUtils.msgGameOneGrade) +
                    ":" + "account" + ":" + application.getAccount() + ":" + "grade" + ":" + grade + ":" +
                    "sum" + ":" + sum;
            NetworkService.getInstance().sendUpload(gameUser);
        } else {
            NetworkService.getInstance().closeConnection();
            Toast.makeText(SetGradeActivity.this, "服务器连接失败~(≧▽≦)~啦啦啦", Toast.LENGTH_SHORT).show();
            Log.v("Login", "已经执行T（）方法");
        }
    }
}
