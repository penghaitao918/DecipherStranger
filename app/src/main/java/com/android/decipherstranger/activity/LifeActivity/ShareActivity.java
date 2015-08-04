package com.android.decipherstranger.activity.LifeActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.decipherstranger.Network.NetworkService;
import com.android.decipherstranger.R;
import com.android.decipherstranger.activity.Base.BaseActivity;
import com.android.decipherstranger.db.DATABASE;
import com.android.decipherstranger.db.LifeShare;
import com.android.decipherstranger.util.ChangeUtils;
import com.android.decipherstranger.util.MyStatic;
import com.android.decipherstranger.view.AutoListView;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
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
public class ShareActivity extends BaseActivity implements AutoListView.OnRefreshListener, AutoListView.OnLoadListener {

    private RelativeLayout topLayout = null;
    private SQLiteOpenHelper helper = null;
    private LifeShare shareList = null;

    private AutoListView listView;
    private SimpleAdapter simpleAdapter = null;
    private ArrayList<Map<String, Object>> dataList = null;

    private int minId = 0;

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
    protected void onStart() {
        super.onStart();
        this.refresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.unregisterReceiver(ShareActivity.this.receiver);
    }

    /* 刷新 */
    @Override
    public void onRefresh() {
        refresh();
    }

    @Override
    public void onLoad() {
        getMinId();
        load();
    }

    private void refresh() {
        System.out.println("### 刷新");
        //  TODO 向服务器发送刷新请求,获取最新的20条数据（这是一个ID为逆序的数组）
        if (NetworkService.getInstance().getIsConnected()){
            String Msg = "type"+":"+"24"+":"+"requestType"+":"+"1";
            Log.v("aaaaa", Msg);
            NetworkService.getInstance().sendUpload(Msg);
        }else {
            NetworkService.getInstance().closeConnection();
            Log.v("ShowShare","已执行T()方法");
        }
    }

    private void load() {
      //send  minId;
        System.out.println("### 加载");
        //  TODO 向服务器发送加载数据,获取ID<count的10条数据（从count-1到count-10）
        if (NetworkService.getInstance().getIsConnected()){
            String Msg = "type"+":"+"24"+":"+"requestType"+":"+"0"+":"+"minId"+":"+minId;
            Log.v("aaaaa", Msg);
            NetworkService.getInstance().sendUpload(Msg);
        }else{
            NetworkService.getInstance().closeConnection();
            Log.v("ShowShare", "已执行T()方法");
        }
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
                new String[] {MyStatic.SHARE_PORTRAIT, MyStatic.SHARE_NAME, MyStatic.SHARE_MESSAGE,
                        MyStatic.SHARE_PHOTO, MyStatic.SHARE_TIME, MyStatic.SHARE_NUM, MyStatic.SHARE_NUM_BTN, MyStatic.SHARE_FRI_BTN},
                new int[] {R.id.sharePortrait, R.id.shareName, R.id.shareMessage, R.id.sharePhoto, R.id.shareTime, R.id.shareNum, R.id.numButton, R.id.friendButton}
        );
        /*实现ViewBinder()这个接口*/
        this.simpleAdapter.setViewBinder(new ViewBinderImpl());
        this.listView.setAdapter(simpleAdapter);

        this.listView.setOnRefreshListener(this);
        this.listView.setOnLoadListener(this);
    }

    private void initData() {
        this.helper = new DATABASE(this);
        this.shareList = new LifeShare(helper.getReadableDatabase());
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = AutoListView.REFRESH;
                msg.obj = shareList.refresh();
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void loadData() {
        this.shareList = new LifeShare(helper.getReadableDatabase());
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = AutoListView.LOAD;
                msg.obj = shareList.load(minId);
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void getMinId() {
        this.shareList = new LifeShare(helper.getReadableDatabase());
        this.minId = shareList.getMinId();
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
                int type = intent.getIntExtra("reRequestType", 1);
                if (intent.getBooleanExtra("reResult", true)){
                    //  获取返回类型为 刷新 还是 加载, 刷新为1，加载为0
                    int id = intent.getIntExtra("reId",0);
                    String account = intent.getStringExtra("reAccount");
                    Bitmap portrait = ChangeUtils.toBitmap(intent.getStringExtra("reUserPhoto"));
                    String name = intent.getStringExtra("reUserName");
                    String message = intent.getStringExtra("reSpeech");
                    Bitmap photo = ChangeUtils.toBitmap(intent.getStringExtra("reSharePhoto"));
                    String time = intent.getStringExtra("reTime");
                    int number = intent.getIntExtra("reZan",0);
                    switch (type) {
                        case MyStatic.REFRESH:
                            shareList = new LifeShare(helper.getWritableDatabase());
                            shareList.clear();
                            shareList = new LifeShare(helper.getWritableDatabase());
                            shareList.insert(id, account, portrait, name, message, photo, time, number);
                            break;
                        case MyStatic.LOAD:
                            shareList = new LifeShare(helper.getWritableDatabase());
                            shareList.insert(id, account, portrait, name, message, photo, time, number);
                            break;
                    }
                }else if (intent.getStringExtra("reResult").equals("finish")){
                    if (intent.getIntExtra("reRequestType",0) == 1){
                        System.out.println("++++++++++"+type);
                        initData();
                    }else {
                        loadData();
                    }
                }
                else{
                    Toast.makeText(ShareActivity.this,"数据获取失败,请检查网络!!!",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}