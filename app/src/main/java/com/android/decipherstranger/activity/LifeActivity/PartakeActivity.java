package com.android.decipherstranger.activity.LifeActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.decipherstranger.Network.NetworkService;
import com.android.decipherstranger.R;
import com.android.decipherstranger.activity.Base.BaseActivity;
import com.android.decipherstranger.util.GlobalMsgUtils;
import com.android.decipherstranger.util.MyComparator;
import com.android.decipherstranger.util.MyStatic;
import com.android.decipherstranger.view.MyScrollView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;

import java.util.ArrayList;
import java.util.Collections;
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
 * @Date 2015/7/27 17:55
 * @e-mail 785351408@qq.com
 */
public class PartakeActivity extends BaseActivity implements MyScrollView.OnScrollListener {

    /**
     * 悬浮框View
     */
    private static View suspendView;
    /**
     * 悬浮框的参数
     */
    private static WindowManager.LayoutParams suspendLayoutParams;
    private double mLatitude;
    private double mLongtitude;
    private LocationClient mLocationClient;
    private MyLocationListener mLocationListener;
    private Bitmap bitmap = null;
    private ImageView advertisementImage = null;
    private AnimationDrawable animationAdvertisement = null;
    private RelativeLayout topLayout = null;
    private TextView textView1 = null;
    private TextView textView2 = null;
    private TextView textView3 = null;
    private TextView textView4 = null;
    private ListView listView = null;
    private SimpleAdapter simpleAdapter = null;
    private ArrayList<Map<String, Object>> dataList = null;
    private LifePartakeBroadcastReceiver receiver = null;
    private MyScrollView myScrollView;
    private LinearLayout mBuyLayout;
    private WindowManager mWindowManager;
    /* 手机屏幕宽度 */
    private int screenWidth;
    /**
     * 排序布局的高度
     */
    private int buyLayoutHeight;
    /**
     * myScrollView与其父类布局的顶部距离
     */
    private int myScrollViewTop;
    /**
     * 排序布局与其父类布局的顶部距离
     */
    private int buyLayoutTop;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            listView.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
            fixListViewHeight(listView);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_life_partake);

        this.init();
        this.initData();
        this.LifePartakeBroadcas();
        System.out.println("### 啦啦啦我进来了。。。");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeSuspend();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.stop();
        animationAdvertisement.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.unregisterReceiver(PartakeActivity.this.receiver);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        topLayout.removeAllViews();
        dataList.clear();
        myScrollView.removeAllViews();
        mBuyLayout.removeAllViews();
        receiver = null;
        topLayout = null;
        dataList = null;
        listView = null;
        myScrollView = null;
        mBuyLayout = null;
    }

    private void init() {
        initLocation();

        this.animationAdvertisement = new AnimationDrawable();
        this.animationAdvertisement = (AnimationDrawable) getResources().getDrawable(R.drawable.animation_advertisement);
        this.advertisementImage = (ImageView) super.findViewById(R.id.advertisement);
        this.advertisementImage.setImageDrawable(this.animationAdvertisement);

        myScrollView = (MyScrollView) findViewById(R.id.view);
        mBuyLayout = (LinearLayout) findViewById(R.id.sort);

        myScrollView.setOnScrollListener(this);
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        screenWidth = mWindowManager.getDefaultDisplay().getWidth();

        this.dataList = new ArrayList<Map<String, Object>>();
        this.listView = (ListView) super.findViewById(R.id.listView);
        this.listView.setOnItemClickListener(new OnItemClickListenerImpl());

        this.textView1 = (TextView) super.findViewById(R.id.classText);
        this.textView2 = (TextView) super.findViewById(R.id.distanceText);
        this.textView3 = (TextView) super.findViewById(R.id.timeText);
        this.textView4 = (TextView) super.findViewById(R.id.favoriteText);

        /*锁定聚焦到顶部*/
        topLayout = (RelativeLayout) super.findViewById(R.id.top);
        topLayout.setFocusable(true);
        topLayout.setFocusableInTouchMode(true);
        topLayout.requestFocus();
    }

    private void initData() {
        this.simpleAdapter = new SimpleAdapter(this,
                this.dataList,
                R.layout.list_item_sort,
                new String[]{MyStatic.LIFE_CLASS, MyStatic.LIFE_NAME, MyStatic.LIFE_TIME, MyStatic.LIFE_SPACE, MyStatic.LIFE_DISTANCE},
                new int[]{R.id.life_class, R.id.life_name, R.id.life_time, R.id.life_space, R.id.life_distance}
        );
        /*实现ViewBinder()这个接口*/
        simpleAdapter.setViewBinder(new ViewBinderImpl());
        this.listView.setAdapter(simpleAdapter);
        /*动态跟新ListView*/
        simpleAdapter.notifyDataSetChanged();
        /*动态计算ListView的高度*/
        this.fixListViewHeight(listView);

        this.animationAdvertisement.start();
    }

    private void fixListViewHeight(ListView listView) {
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

    /**
     * 窗口有焦点的时候，即所有的布局绘制完毕的时候，我们来获取购买布局的高度和myScrollView距离父类布局的顶部位置
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            buyLayoutHeight = mBuyLayout.getHeight();
            buyLayoutTop = mBuyLayout.getTop();

            myScrollViewTop = myScrollView.getTop();
        }
    }

    /**
     * 滚动的回调方法，当滚动的Y距离大于或者等于 购买布局距离父类布局顶部的位置，就显示购买的悬浮框
     * 当滚动的Y的距离小于 购买布局距离父类布局顶部的位置加上购买布局的高度就移除购买的悬浮框
     */
    @Override
    public void onScroll(int scrollY) {
        if (scrollY >= buyLayoutTop) {
            if (suspendView == null) {
                showSuspend();
            }
        } else if (scrollY <= buyLayoutTop + buyLayoutHeight) {
            if (suspendView != null) {
                removeSuspend();
            }
        }
    }

    /**
     * 显示购买的悬浮框
     */
    private void showSuspend() {
        if (suspendView == null) {
            suspendView = LayoutInflater.from(this).inflate(R.layout.winpop_life_sort, null);
            if (suspendLayoutParams == null) {
                suspendLayoutParams = new WindowManager.LayoutParams();
                suspendLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                suspendLayoutParams.format = PixelFormat.RGBA_8888;
                suspendLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                suspendLayoutParams.gravity = Gravity.TOP;
                suspendLayoutParams.width = screenWidth;
                suspendLayoutParams.height = buyLayoutHeight;
                suspendLayoutParams.x = 0;
                suspendLayoutParams.y = myScrollViewTop;
            }
        }

        mWindowManager.addView(suspendView, suspendLayoutParams);
    }

    /**
     * 移除购买的悬浮框
     */
    private void removeSuspend() {
        if (suspendView != null) {
            mWindowManager.removeView(suspendView);
            suspendView = null;
        }
    }

    private void getData() {
        /**
         * 此处提交获取服务器数据请求
         */
        if (NetworkService.getInstance().getIsConnected()) {
            String Msg = "type" + ":" + GlobalMsgUtils.msgShowAllActivity + ":" + "latitude" + ":" + mLatitude
                    + ":" + "longtitude" + ":" + mLongtitude;
            NetworkService.getInstance().sendUpload(Msg);
            Log.v("aaaaa", Msg);
        } else {
            NetworkService.getInstance().closeConnection();
            Log.v("ShowAllActivity", "已执行T()方法");
        }
    }

    private void initLocation() {
        mLocationClient = new LocationClient(this);
        mLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mLocationListener);

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        mLocationClient.setLocOption(option);
    }

    //    lifeList.setData(id, name, type, distance, favorite, space, date);
    private void setData(int lifeId, String name, int type, double distance, int favorite, String space, String date) {
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
        map.put(MyStatic.LIFE_DISTANCE, (int) distance);
        map.put(MyStatic.LIFE_FAVORITE, favorite);
        map.put(MyStatic.LIFE_TIME, date);
        map.put(MyStatic.LIFE_CLASS, bitmap);
        map.put(MyStatic.LIFE_NAME, name);
        map.put(MyStatic.LIFE_SPACE, space);
        dataList.add(map);
    }

    private void sort(int flag) {
        MyComparator comp = new MyComparator(flag);
        Collections.sort(dataList, comp);
        simpleAdapter.notifyDataSetChanged();
    }

    public void LifePartakeOnClick(View view) {
        switch (view.getId()) {
            /*返回*/
            case R.id.life_back_button:
                onBackPressed();
                PartakeActivity.this.finish();
                break;
            /*广告*/
            case R.id.advertLayout:
                Uri uri = Uri.parse(MyStatic.ADVERTISEMENT);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            /*删除广告*/
/*            case R.id.exitButton:
                RelativeLayout layout = (RelativeLayout) super.findViewById(R.id.advertLayout);
                layout.setVisibility(View.GONE);
                break;*/
            /*类别*/
            case R.id.lifeClass:
                sort(1);
                textView1.setTextColor(getResources().getColor(R.color.red));
                textView2.setTextColor(getResources().getColor(R.color.black_grey));
                textView3.setTextColor(getResources().getColor(R.color.black_grey));
                textView4.setTextColor(getResources().getColor(R.color.black_grey));
                break;
            /*距离*/
            case R.id.lifeDistance:
                sort(2);
                textView1.setTextColor(getResources().getColor(R.color.black_grey));
                textView2.setTextColor(getResources().getColor(R.color.red));
                textView3.setTextColor(getResources().getColor(R.color.black_grey));
                textView4.setTextColor(getResources().getColor(R.color.black_grey));
                break;
            /*时间*/
            case R.id.lifeTime:
                sort(3);
                textView1.setTextColor(getResources().getColor(R.color.black_grey));
                textView2.setTextColor(getResources().getColor(R.color.black_grey));
                textView3.setTextColor(getResources().getColor(R.color.red));
                textView4.setTextColor(getResources().getColor(R.color.black_grey));
                break;
            /*欢迎度*/
            case R.id.lifeFavorite:
                sort(4);
                textView1.setTextColor(getResources().getColor(R.color.black_grey));
                textView2.setTextColor(getResources().getColor(R.color.black_grey));
                textView3.setTextColor(getResources().getColor(R.color.black_grey));
                textView4.setTextColor(getResources().getColor(R.color.red));
                break;
        }
    }

    private void LifePartakeBroadcas() {
        //动态方式注册广播接收者
        this.receiver = new LifePartakeBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyStatic.LIFE_PARTAKE);
        this.registerReceiver(receiver, filter);
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

    private class OnItemClickListenerImpl implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int lifeId = (int) dataList.get(position).get(MyStatic.LIFE_ID);
            int lifeType = (int) dataList.get(position).get(MyStatic.LIFE_CLASSINT);
            Intent intent = new Intent(PartakeActivity.this, DetailsActivity.class);
            intent.putExtra(MyStatic.LIFE_ID, lifeId);
            intent.putExtra(MyStatic.LIFE_CLASSINT, lifeType);
            startActivity(intent);
        }
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            mLatitude = location.getLatitude();
            mLongtitude = location.getLongitude();
            PartakeActivity.this.getData();

            System.out.println("### mLatitude = " + mLatitude);
            System.out.println("### mLongtitude = " + mLongtitude);
        }
    }

    public class LifePartakeBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MyStatic.LIFE_PARTAKE)) {
                if (intent.getStringExtra("reResult").equals("true")) {
                    // TODO 将数据存入List
                    int id = intent.getIntExtra("reId", 0);
                    String name = intent.getStringExtra("reName");
                    String space = intent.getStringExtra("rePlace");
                    String date = intent.getStringExtra("reTime");
                    int type = intent.getIntExtra("reType", 0);
                    double distance = intent.getDoubleExtra("reDistance", 0.0);
                    int favorite = intent.getIntExtra("reFavorite", 0);
                    setData(id, name, type, distance, favorite, space, date);
                } else if (intent.getStringExtra("reResult").equals("finish")) {
                    System.out.println("### 哎哟我去");
                    Message message = new Message();
                    handler.sendMessage(message);
                } else {
                    Toast.makeText(context, "### 没有活动=_=！！！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
