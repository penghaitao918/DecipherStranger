package com.android.decipherstranger.activity.LifeActivity;

import android.os.Bundle;
import android.view.View;

import com.android.decipherstranger.R;
import com.android.decipherstranger.activity.Base.BaseActivity;


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
 * @Date 2015/7/28 11:29
 * @e-mail 785351408@qq.com
 */
public class SendActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_send);
    }

    public void LifeSendOnClick(View view) {
        switch (view.getId()) {
            /*返回*/
            case R.id.life_back_button:
                onBackPressed();
                break;
            /*美食*/
            case R.id.food:
                break;
            /*旅行*/
            case R.id.travel:
                break;
            /*休闲娱乐*/
            case R.id.others:
                break;
            /*活动名称*/
            case R.id.lifeName:
                break;
            /*活动地点*/
            case R.id.lifeSpace:
                break;
            /*活动时间*/
            case R.id.lifeTime:
                break;
            /*活动人数*/
            case R.id.lifePeople:
                break;
            /*截止时间*/
            case R.id.timeEnd:
                break;
            /*集合地点*/
            case R.id.rallySpace:
                break;
            /*活动口令*/
            case R.id.lifePassword:
                break;
            /*发布*/
            case R.id.publish:
                break;
        }
    }
}
