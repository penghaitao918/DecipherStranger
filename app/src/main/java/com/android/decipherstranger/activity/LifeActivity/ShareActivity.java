package com.android.decipherstranger.activity.LifeActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.android.decipherstranger.R;
import com.android.decipherstranger.activity.Base.BaseActivity;
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

    private RelativeLayout topLayout = null;
    private ListView dataList = null;
    private SimpleAdapter simpleAdapter = null;
    private ArrayList<Map<String, Object>> list = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_share);
        this.init();
        this.setData();
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

    private void setData() {
        this.list.addAll(this.selectAll());
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

    private ArrayList<Map<String, Object>> selectAll (){
        Bitmap  bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ds_icon);
        ArrayList<Map<String, Object>> all = new ArrayList< Map<String, Object> >();
        for (int i = 0; i < 5; ++ i) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(MyStatic.SHARE_PORTRAIT, bitmap);
            map.put(MyStatic.SHARE_NAME, "我是小涛啊");
            map.put(MyStatic.SHARE_MESSAGE, "今天风好大，把我吹成了傻逼-。-凑点字数看能不能出现第二行QAQ");
            map.put(MyStatic.SHARE_PHOTO, bitmap);
            map.put(MyStatic.SHARE_TIME, "2015/07/28 18:14");
            all.add(map);
        }
        return all;
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

}
