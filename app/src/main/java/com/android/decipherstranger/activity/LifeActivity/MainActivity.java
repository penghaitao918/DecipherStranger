package com.android.decipherstranger.activity.LifeActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.android.decipherstranger.Network.NetworkService;
import com.android.decipherstranger.R;
import com.android.decipherstranger.activity.Base.BaseActivity;
import com.android.decipherstranger.activity.Base.MyApplication;
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
 * @Date 2015/7/30 9:54
 * @e-mail penghaitao918@163.com
 */
public class MainActivity extends BaseActivity {

    private RelativeLayout topLayout = null;
    private ListView listView = null;
    private SimpleAdapter simpleAdapter = null;
    private ArrayList<Map<String, Object>> dataList = null;

    private Bitmap bitmap = null;
    private ImageView advertisementImage = null;
    private AnimationDrawable animationAdvertisement = null;

    private MyApplication application = null;
    private LifeMainBroadcastReceiver receiver = null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            fixListViewHeight(listView);
            listView.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
            System.out.println("### 活动列表刷新成功");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_main);
        application = MyApplication.getInstance();
        this.init();
        this.initData();
        this.getData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (animationAdvertisement.isRunning()) {
            animationAdvertisement.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.unregisterReceiver(MainActivity.this.receiver);
        topLayout.removeAllViews();
        topLayout = null;
        dataList.clear();
        dataList = null;
        listView = null;
        simpleAdapter = null;
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        advertisementImage = null;
        application = null;
        receiver = null;
        handler = null;
    }

    private void init() {
        this.dataList = new ArrayList<Map<String, Object>>();
        this.listView = (ListView) super.findViewById(R.id.listView);
        this.listView.setOnItemClickListener(new OnItemClickListenerImpl());

        this.animationAdvertisement = new AnimationDrawable();
        this.animationAdvertisement = (AnimationDrawable) getResources().getDrawable(R.drawable.animation_advertisement);
        this.advertisementImage = (ImageView) super.findViewById(R.id.advertisement);
        this.advertisementImage.setImageDrawable(this.animationAdvertisement);

        /*锁定聚焦到顶部*/
        topLayout = (RelativeLayout) super.findViewById(R.id.top);
        topLayout.setFocusable(true);
        topLayout.setFocusableInTouchMode(true);
        topLayout.requestFocus();

        this.LifeMainBroadcas();
    }

    private void initData() {
        this.simpleAdapter = new SimpleAdapter(this,
                this.dataList,
                R.layout.list_item_life,
                new String[]{MyStatic.LIFE_CLASS, MyStatic.LIFE_NAME, MyStatic.LIFE_TIME, MyStatic.LIFE_SPACE},
                new int[]{R.id.life_class, R.id.life_name, R.id.life_time, R.id.life_space}
        );
        /*实现ViewBinder()这个接口*/
        simpleAdapter.setViewBinder(new ViewBinderImpl());
        this.listView.setAdapter(simpleAdapter);
        /*动态跟新ListView*/
//        simpleAdapter.notifyDataSetChanged();
        /*动态计算ListView的高度*/
        this.fixListViewHeight(listView);

        this.animationAdvertisement.start();
    }

    private void getData() {
        /**
         * 此处提交获取服务器数据请求
         */
        if (NetworkService.getInstance().getIsConnected()) {
            String Msg = "type" + ":" + Integer.toString(GlobalMsgUtils.msgLifeMain) + ":" +
                    "account" + ":" + application.getAccount();
            Log.v("aaaaa", Msg);
            NetworkService.getInstance().sendUpload(Msg);
        } else {
            NetworkService.getInstance().closeConnection();
            Log.v("Login", "已经执行T（）方法");
        }
    }

    public void fixListViewHeight(ListView listView) {
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

    public void LifeMainOnClick(View view) {
        switch (view.getId()) {
            /*返回*/
            case R.id.life_back_button:
                onBackPressed();
                MainActivity.this.finish();
                break;
            /*广告*/
            case R.id.advertLayout:
                Uri uri = Uri.parse(MyStatic.ADVERTISEMENT);
                Intent intent0 = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent0);
                break;
            /*我的活动*/
            case R.id.mylife:
                Intent intent1 = new Intent(this, MyLifeActivity.class);
                startActivity(intent1);
                break;
            /*发起*/
            case R.id.sendLife:
                Intent intent2 = new Intent(this, SendActivity.class);
                startActivity(intent2);
                break;
            /*参团*/
            case R.id.partakeLife:
                Intent intent3 = new Intent(this, PartakeActivity.class);
                startActivity(intent3);
                break;
            /*分享*/
            case R.id.share:
                Intent intent4 = new Intent(this, ShareActivity.class);
                startActivity(intent4);
                break;
        }
    }

    private void setData(int lifeId, int type, String name, String time, String place) {
        switch (type) {
            // 美食
            case 1:
                bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.life_class_food);
                break;
            // 旅游
            case 2:
                bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.life_class_travel);
                break;
            // 休闲娱乐
            case 3:
                bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.life_class_other);
                break;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(MyStatic.LIFE_ID, lifeId);
        map.put(MyStatic.LIFE_CLASSINT, type);
        map.put(MyStatic.LIFE_CLASS, bitmap);
        map.put(MyStatic.LIFE_NAME, name);
        map.put(MyStatic.LIFE_TIME, time);
        map.put(MyStatic.LIFE_SPACE, place);
        dataList.add(map);
    }

    private void LifeMainBroadcas() {
        //动态方式注册广播接收者
        this.receiver = new LifeMainBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyStatic.LIFE_MAIN);
        this.registerReceiver(receiver, filter);
    }

    private class OnItemClickListenerImpl implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int lifeId = (int) dataList.get(position).get(MyStatic.LIFE_ID);
            int lifeType = (int) dataList.get(position).get(MyStatic.LIFE_CLASSINT);
            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            intent.putExtra(MyStatic.LIFE_ID, lifeId);
            intent.putExtra(MyStatic.LIFE_CLASSINT, lifeType);
            startActivity(intent);
        }
    }

    private class ViewBinderImpl implements SimpleAdapter.ViewBinder {
        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {
            // TODO Auto-generated method stub
            if (view instanceof ImageView && data instanceof Bitmap) {
                ImageView i = (ImageView) view;
                i.setImageBitmap((Bitmap) data);
                return true;
            }
            return false;
        }
    }

    public class LifeMainBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MyStatic.LIFE_MAIN)) {
                // TODO 将获取的数据赋值到本地
                if (intent.getStringExtra("reResult").equals("true")) {
                    int lifeId = intent.getIntExtra("reId", 0);
                    int type = intent.getIntExtra("reType", 3);
                    String name = intent.getStringExtra("reName");
                    String time = intent.getStringExtra("reTime");
                    String place = intent.getStringExtra("rePlace");
                    setData(lifeId, type, name, time, place);
                    System.out.println("### 接收了一条活动数据 " + lifeId);
                } else if (intent.getStringExtra("reResult").equals("finish")){
                    handler.sendMessage(new Message());
                    System.out.println("### 哎哟我去");
                }
            }
        }
    }
}