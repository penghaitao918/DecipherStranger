package com.android.decipherstranger.activity.LifeActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.decipherstranger.R;
import com.android.decipherstranger.activity.Base.BaseActivity;
import com.android.decipherstranger.db.DATABASE;
import com.android.decipherstranger.db.LifeShare;
import com.android.decipherstranger.util.MyStatic;
import com.android.decipherstranger.view.AutoListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
public class ShareActivity extends BaseActivity implements AutoListView.OnRefreshListener, AutoListView.OnLoadListener {

    private RelativeLayout topLayout = null;
    private SQLiteOpenHelper helper = null;
    private LifeShare lifeShare = null;

    private AutoListView listView;
    private SimpleAdapter simpleAdapter = null;
    private ArrayList<Map<String, Object>> dataList = null;

    //	当前最大数据条数
    private int Count = 0;

    private LifeShareBroadcastReceiver receiver = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_share);

        this.init();
        this.LifeShareBroadcas();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.unregisterReceiver(ShareActivity.this.receiver);
    }

    /* 刷新 */
    @Override
    public void onRefresh() {
        //	loadData(AutoListView.REFRESH);
        //	获取服务器前20条数据，并返回 刷新标记
        Toast.makeText(this, "刷新", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoad() {
        //	loadData(AutoListView.LOAD);
        //	获取从 count + 1 到 count + 10 的数据，  并返回加载标记
        Toast.makeText(this,"加载",Toast.LENGTH_SHORT).show();
    }

    private void init() {
        /*锁定聚焦到顶部*/
        topLayout = (RelativeLayout) super.findViewById(R.id.top);
        topLayout.setFocusable(true);
        topLayout.setFocusableInTouchMode(true);
        topLayout.requestFocus();

        this.dataList = new ArrayList<Map<String, Object>>();
        this.listView = (AutoListView) findViewById(R.id.lifeShare);
        this.simpleAdapter = new SimpleAdapter(this,
                this.dataList,
                R.layout.list_item_share,
                new String[] {MyStatic.SHARE_PORTRAIT, MyStatic.SHARE_NAME, MyStatic.SHARE_MESSAGE, MyStatic.SHARE_PHOTO, MyStatic.SHARE_TIME},
                new int[] {R.id.sharePortrait, R.id.shareName, R.id.shareMessage, R.id.sharePhoto, R.id.shareTime}
        );
        /*实现ViewBinder()这个接口*/
        this.simpleAdapter.setViewBinder(new ViewBinderImpl());
        this.listView.setAdapter(simpleAdapter);

        this.listView.setOnRefreshListener(this);
        this.listView.setOnLoadListener(this);
    }


    private void initData() {
        this.helper = new DATABASE(this);
        this.lifeShare = new LifeShare(helper.getReadableDatabase());
		/*	此处获取数据库既有数据*/
        //	this.dataList.addAll(this.selectAll());
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = AutoListView.REFRESH;
                msg.obj = lifeShare.selectAll(ShareActivity.this);
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void fixListViewHeight(AutoListView listView) {
        // 如果没有设置数据适配器，则ListView没有子项，返回。
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        if (listAdapter == null) {
            return;
        }
        for (int index = 0, len = listAdapter.getCount(); index < len; ++index) {
            View listViewItem = listAdapter.getView(index, null, listView);
            // 计算子项View 的宽高
            listViewItem.measure(0, 0);
            // 计算所有子项的高度和
            totalHeight += listViewItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // listView.getDividerHeight()获取子项间分隔符的高度
        // params.height设置ListView完全显示需要的高度
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private void loadData(final int what) {
        // 这里模拟从服务器获取数据
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = handler.obtainMessage();
                msg.what = what;
                msg.obj = getData();
                handler.sendMessage(msg);
            }
        }).start();
    }

    // 测试数据
    public ArrayList<Map<String, Object>> getData() {
        Bitmap  bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ds_icon);
        ArrayList<Map<String, Object>> all = new ArrayList<Map<String, Object>>();

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            long l = random.nextInt(10000);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(MyStatic.SHARE_PORTRAIT, bitmap);
            map.put(MyStatic.SHARE_NAME, "我是小涛啊" + l);
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

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            ArrayList<Map<String, Object>> result = (ArrayList<Map<String, Object>>) msg.obj;
            switch (msg.what) {
                case AutoListView.REFRESH:
                    listView.onRefreshComplete();
                    dataList.clear();
                    dataList.addAll(result);
                    break;
                case AutoListView.LOAD:
                    listView.onLoadComplete();
                    dataList.addAll(result);
                    break;
            }
            listView.setResultSize(result.size());
            simpleAdapter.notifyDataSetChanged();
            /*动态计算ListView的高度*/
            fixListViewHeight(listView);
        };
    };

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

    private void LifeShareBroadcas() {
        //动态方式注册广播接收者
        this.receiver = new LifeShareBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyStatic.LIFE_MAIN);
        this.registerReceiver(receiver, filter);
    }

    public class LifeShareBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MyStatic.LIFE_SHARE)) {
                // TODO 将获取的数据赋值到本地
                if (intent.getBooleanExtra("reResult", true)){

                }else{

                }
            }
        }
    }

}