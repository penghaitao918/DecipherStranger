package com.android.decipherstranger.activity.LifeActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.android.decipherstranger.Network.NetworkService;
import com.android.decipherstranger.R;
import com.android.decipherstranger.activity.Base.BaseActivity;
import com.android.decipherstranger.util.GlobalMsgUtils;
import com.android.decipherstranger.util.MyStatic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
 * @Date 2015/7/28 18:25
 * @e-mail 785351408@qq.com
 */
public class ShareActivity extends BaseActivity {

    private int count = 0;
    private RelativeLayout topLayout = null;
    private ListView dataList = null;
    private SimpleAdapter simpleAdapter = null;
    private ArrayList<Map<String, Object>> list = null;
    private LifeShareBroadcastReceiver receiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_share);
        this.init();
        this.initData();
        this.LifeShareBroadcas();
        this.getData();
    }

    private void init() {
        this.list = new ArrayList< Map<String, Object> >();
        this.dataList = (ListView) super.findViewById(R.id.lifeShare);
        //    this.dataList.setOnItemClickListener(new OnItemClickListenerImpl());

        /*锁定聚焦到顶部*/
        topLayout = (RelativeLayout) super.findViewById(R.id.top);
        topLayout.setFocusable(true);
        topLayout.setFocusableInTouchMode(true);
        topLayout.requestFocus();
    }

    private void initData() {
        //    this.list.addAll(this.selectAll());
        this.simpleAdapter = new SimpleAdapter(this,
                this.list,
                R.layout.list_item_share,
                new String[] {MyStatic.SHARE_PORTRAIT, MyStatic.SHARE_NAME, MyStatic.SHARE_MESSAGE, MyStatic.SHARE_PHOTO, MyStatic.SHARE_TIME},
                new int[] {R.id.sharePortrait, R.id.shareName, R.id.shareMessage, R.id.sharePhoto, R.id.shareTime}
        );
        /*实现ViewBinder()这个接口*/
        simpleAdapter.setViewBinder(new ViewBinderImpl());
        this.dataList.setAdapter(simpleAdapter);
        /*动态跟新ListView*/
        simpleAdapter.notifyDataSetChanged();
    }


    private void getData() {
        /**
         * 此处提交获取服务器数据请求
         */

    }

    private class ViewBinderImpl implements SimpleAdapter.ViewBinder {
        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {
            // TODO Auto-generated method stub
            if(view instanceof ImageView && data instanceof Bitmap){
                ImageView i = (ImageView)view;
                i.setImageBitmap((Bitmap) data);
                return true;
            }
            return false;
        }
    }

    private void setDataList (Bitmap portrait, String account, String message, Bitmap photo, String time){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(MyStatic.SHARE_PORTRAIT, portrait);
        map.put(MyStatic.SHARE_NAME, account);
        map.put(MyStatic.SHARE_MESSAGE, message);
        map.put(MyStatic.SHARE_PHOTO, photo);
        map.put(MyStatic.SHARE_TIME, time);
        list.add(map);
        simpleAdapter.notifyDataSetChanged();
        dataList.setAdapter(simpleAdapter);
        //    return all;
    }

    public void LifeShareOnclick(View view) {
        switch (view.getId()) {
            case R.id.life_back_button:
                onBackPressed();
                break;
            case R.id.myShare:
                Intent intent = new Intent(this,ShareLifeActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void LifeShareBroadcas() {
        //动态方式注册广播接收者
        this.receiver = new LifeShareBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyStatic.LIFE_SHARE);
        this.registerReceiver(receiver, filter);
    }

    public class LifeShareBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MyStatic.LIFE_SHARE)) {
                // TODO 将获取的数据赋值到本地
                if (intent.getBooleanExtra("reResult", true)){
                    count = 3;
                    //    setDataList();
                }else{
                    if (count < 2) {
                        count++;
                        getData();
                    }
                }
            }
        }
    }

}
