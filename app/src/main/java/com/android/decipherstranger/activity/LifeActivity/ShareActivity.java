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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.decipherstranger.Network.NetworkService;
import com.android.decipherstranger.R;
import com.android.decipherstranger.activity.Base.BaseActivity;
import com.android.decipherstranger.adapter.ListButtonAdapter;
import com.android.decipherstranger.db.DATABASE;
import com.android.decipherstranger.db.LifeShare;
import com.android.decipherstranger.util.ChangeUtils;
import com.android.decipherstranger.util.MyStatic;
import com.android.decipherstranger.view.AutoListView;

import java.util.ArrayList;
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

    private LinearLayout layout = null;

    Bitmap photo = null;
    Bitmap portrait = null;
    private RelativeLayout topLayout = null;
    private SQLiteOpenHelper helper = null;
    private LifeShare shareList = null;
    private AutoListView listView;
    private ListButtonAdapter adapter = null;
    private ArrayList<Map<String, Object>> dataList = null;

    private int minId = 0;
    private boolean refreshFlag = false;

    private LifeShareBroadcastReceiver receiver = null;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            boolean flag = false;
            ArrayList<Map<String, Object>> result = (ArrayList<Map<String, Object>>) msg.obj;
            switch (msg.what) {
                case AutoListView.INITDATA:
                    listView.onRefreshComplete();
                    dataList.clear();
                    dataList.addAll(result);
                    flag = true;
                    System.out.println("#### 数据初始化成功");
                    break;
                case AutoListView.REFRESH:
                    listView.onRefreshComplete();
                    dataList.clear();
                    dataList.addAll(result);
                    System.out.println("#### 数据刷新成功");
                    break;
                case AutoListView.LOAD:
                    listView.onLoadComplete(result.size());
                    dataList.addAll(result);
                    System.out.println("#### 数据加载成功");
                    break;
            }
            //   listView.setResultSize(result.size());
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            layout.setClickable(flag);
            System.out.println("#### 解锁");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_share);

        this.LifeShareBroadcas();
        this.init();
        initData(0);
        this.refresh();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.refresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.unregisterReceiver(ShareActivity.this.receiver);
        topLayout.removeAllViews();
        helper.close();
        dataList.clear();
        if (photo != null && !photo.isRecycled()) {
            photo.recycle();
            photo = null;
        }
        if (portrait != null && !portrait.isRecycled()) {
            portrait.recycle();
            portrait = null;
        }
        topLayout = null;
        helper = null;
        shareList = null;
        listView = null;
        adapter = null;
        dataList = null;
    }

    /* 刷新 */
    @Override
    public void onRefresh() {
        refresh();
    }

    @Override
    public void onLoad() {
        listView.initFooter(1);
        getMinId();
        load();
    }

    private void refresh() {
        refreshFlag = true;
        layout.setClickable(true);
        System.out.println("#### 刷新锁定");
        listView.initFooter(0);
        //  TODO 向服务器发送刷新请求,获取最新的5条数据（这是一个ID为逆序的数组）
        if (NetworkService.getInstance().getIsConnected()) {
            String Msg = "type" + ":" + "24" + ":" + "requestType" + ":" + "1";
            Log.v("aaaaa", Msg);
            NetworkService.getInstance().sendUpload(Msg);
        } else {
            NetworkService.getInstance().closeConnection();
            Log.v("ShowShare", "已执行T()方法");
        }
    }

    private void load() {
        layout.setClickable(true);
        System.out.println("#### 加载锁定");
        //  TODO 向服务器发送加载数据,获取ID<count的10条数据（从count-1到count-10）
        if (NetworkService.getInstance().getIsConnected()) {
            String Msg = "type" + ":" + "24" + ":" + "requestType" + ":" + "0" + ":" + "minId" + ":" + minId;
            Log.v("### aaaaa", Msg);
            NetworkService.getInstance().sendUpload(Msg);
        } else {
            NetworkService.getInstance().closeConnection();
            Log.v("### ShowShare", "已执行T()方法");
        }
    }

    private void init() {
        this.helper = new DATABASE(this);
        /*锁定界面*/
        layout = (LinearLayout) super.findViewById(R.id.addFlag);
        layout.setClickable(true);
        System.out.println("#### 初始锁定");
        /*锁定聚焦到顶部*/
        topLayout = (RelativeLayout) super.findViewById(R.id.top);
        topLayout.setFocusable(true);
        topLayout.setFocusableInTouchMode(true);
        topLayout.requestFocus();

        this.dataList = new ArrayList<Map<String, Object>>();
        this.listView = (AutoListView) findViewById(R.id.lifeShare);
        this.adapter = new ListButtonAdapter(this,
                this.dataList,
                R.layout.list_item_share,
                new String[]{MyStatic.SHARE_PORTRAIT, MyStatic.SHARE_NAME, MyStatic.SHARE_MESSAGE,
                        MyStatic.SHARE_PHOTO, MyStatic.SHARE_TIME, MyStatic.SHARE_NUM, MyStatic.SHARE_NUM_BTN, MyStatic.SHARE_FRI_BTN},
                new int[]{R.id.sharePortrait, R.id.shareName, R.id.shareMessage, R.id.sharePhoto, R.id.shareTime, R.id.shareNum, R.id.numButton, R.id.friendButton}
        );
        /*实现ViewBinder()这个接口*/
        //    this.adapter.setViewBinder(new ViewBinderImpl());
        this.listView.setAdapter(adapter);

        listView.initFooter(0);

        this.listView.setOnRefreshListener(this);
        this.listView.setOnLoadListener(this);
    }

    private void initData(final int type) {
        this.shareList = new LifeShare(helper.getReadableDatabase());
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                if (type == 0) {
                    msg.what = AutoListView.INITDATA;
                } else {
                    msg.what = AutoListView.REFRESH;
                }
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

    public void LifeShareOnclick(View view) {
        switch (view.getId()) {
            case R.id.life_back_button:
                onBackPressed();
                break;
            case R.id.myShare:
                Intent intent = new Intent(this, ShareLifeActivity.class);
                startActivity(intent);
                break;
            default:
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
                //  TODO 将获取的数据赋值到本地
                if (intent.getStringExtra("reMatter").equals("showShare")) {
                    System.out.println("### 晒图数据接收成功");
                    int type = intent.getIntExtra("reRequestType", 1);
                    System.out.println("### result = " + intent.getStringExtra("reResult"));
                    if (intent.getStringExtra("reResult").equals("true")) {
                        //  获取返回类型为 刷新 还是 加载, 刷新为1，加载为0
                        int id = intent.getIntExtra("reId", 0);
                        String account = intent.getStringExtra("reAccount");
                        portrait = ChangeUtils.toBitmap(intent.getStringExtra("reUserPhoto"));
                        String name = intent.getStringExtra("reUserName");
                        String message = intent.getStringExtra("reSpeech");
                        photo = ChangeUtils.toBitmap(intent.getStringExtra("reSharePhoto"));
                        String time = intent.getStringExtra("reTime");
                        int number = intent.getIntExtra("reZan", 0);
                        int gender = intent.getIntExtra("re_gender", 0);
                        switch (type) {
                            case MyStatic.REFRESH:
                                if (refreshFlag) {
                                    refreshFlag = false;
                                    shareList = new LifeShare(helper.getWritableDatabase());
                                    shareList.clear();
                                }
                                shareList = new LifeShare(helper.getWritableDatabase());
                                shareList.insert(id, account, portrait, name, message, photo, time, number, gender);
                                break;
                            case MyStatic.LOAD:
                                shareList = new LifeShare(helper.getWritableDatabase());
                                shareList.insert(id, account, portrait, name, message, photo, time, number, gender);
                                break;
                        }
                    } else if (intent.getStringExtra("reResult").equals("finish")) {
                        if (intent.getIntExtra("reRequestType", 0) == 1) {
                            initData(1);
                        } else {
                            loadData();
                        }
                    } else {
                        if (intent.getIntExtra("reRequestType", 0) == 0) {
                            System.out.println("### 没有数据了");
                            listView.onLoadComplete(0);
                        } else {
                            //TODO 无数据
                        }
                    }
                } else {
                    if (intent.getStringExtra("reResult").equals("true")) {
                        //TODO 点赞成功处理
                        shareList = new LifeShare(helper.getWritableDatabase());
                        shareList.addNum(MyStatic.sharePositionId, MyStatic.sharePositionNum + 1);
                        adapter.itemDo(MyStatic.sharePosition);
                    } else {
                        //TODO 已经赞过处理
                        Toast.makeText(ShareActivity.this, "您已赞过了该分享~", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}